package com.castores.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
public class WarehouseLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "productId")
    @JsonIgnoreProperties({"description", "price", "quantity", "deletedAt"})
    private Product product;
    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonIgnoreProperties({"password", "role"})
    private User user;
    @Column(nullable = false)
    private Timestamp transactionDate;
    @Column(nullable = false)
    private String action;
    @Column(nullable = false)
    private int quantity;


}
