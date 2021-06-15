package com.learning.currency.model;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Component
public class GetChainResponse implements  java.io.Serializable {

    private List<Block> chain;
    private int length;
}
