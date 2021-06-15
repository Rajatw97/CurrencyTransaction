package com.learning.currency.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Component
public class Transaction implements Serializable {

    private String sender;
    private String receiver;
    private int amount;
}
