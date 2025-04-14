package com.castores.api.configs;

import java.util.ArrayDeque;
import java.util.Deque;

import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DistributedTransactions {
    private Deque<DefaultTransactionDefinition> defaultTransactionDefinitionList;
    private Deque<JpaTransactionManager> dataSourceTransactionManagerList;
    private Deque<TransactionStatus> transactionStatusList;

    public void createTransactionDefinition(String transactionID, String... transactionsTags) {
        defaultTransactionDefinitionList = new ArrayDeque<>();

        for (String tag : transactionsTags) {
            DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
            transactionDefinition.setName(String.format("%s_%s", tag, transactionID));
            defaultTransactionDefinitionList.add(transactionDefinition);
        }
    }

    public void beginTransactions(JpaTransactionManager... transactionsmanger) throws IllegalStateException {
        if (transactionsmanger.length != defaultTransactionDefinitionList.size()) {
            throw new IllegalStateException("Error starting transactions. The number of transactions to handle is incorrect");
        }

        transactionStatusList = new ArrayDeque<>();
        dataSourceTransactionManagerList = new ArrayDeque<>();

        for (JpaTransactionManager itemDataSource : transactionsmanger) {
            DefaultTransactionDefinition transactionDefinition = defaultTransactionDefinitionList.removeFirst();

            TransactionStatus transactionStatus = itemDataSource.getTransaction(transactionDefinition);
            transactionStatusList.push(transactionStatus);
            dataSourceTransactionManagerList.push(itemDataSource);
        }
    }

    public void commitTransactions() {
        if (transactionStatusList != null) {
            while (!transactionStatusList.isEmpty()) {
                TransactionStatus transactionStatus = transactionStatusList.removeFirst();
                JpaTransactionManager transactionManager = dataSourceTransactionManagerList.removeFirst();
                transactionManager.commit(transactionStatus);
            }
        }

        clearTransactionsList();
    }

    public void rollbackTransactions() {
        if (transactionStatusList != null) {
            while (!transactionStatusList.isEmpty()) {
                TransactionStatus transactionStatus = transactionStatusList.removeFirst();
                JpaTransactionManager transactionManager = dataSourceTransactionManagerList.removeFirst();
                transactionManager.rollback(transactionStatus);
            }
        }

        clearTransactionsList();
    }

    private void clearTransactionsList() {
        if (dataSourceTransactionManagerList != null) {
            dataSourceTransactionManagerList.clear();
        }

        if (transactionStatusList != null) {
            transactionStatusList.clear();
        }
    }

}
