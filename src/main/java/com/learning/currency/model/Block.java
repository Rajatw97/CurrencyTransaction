package com.learning.currency.model;


import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Component
public class Block implements java.io.Serializable {

    private int index;
    private String timestamp;
    private int proof;
    private String previousHash;
    private String currentHash;
    private List<Transaction> transactions;

}
