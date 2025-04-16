package com.teya.Ledger.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table
@Getter
@Setter
public class Account {

    @Id
    @GeneratedValue
    private Integer id;
    @Column
    private BigDecimal balance;
}
