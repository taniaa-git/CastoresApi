package com.castores.api.configs;


import com.castores.api.dto.TransactionService;
import com.castores.api.utils.Utilities;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
public class ConfigBeans {

    @Bean
    Utilities getUtilities() {
        return new Utilities();
    }

    @Bean
    @RequestScope
    TransactionService getTransactionService() {
        return new TransactionService();
    }

    @Bean
    @RequestScope
    DistributedTransactions getDistributedTransactions() {
        return new DistributedTransactions();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
