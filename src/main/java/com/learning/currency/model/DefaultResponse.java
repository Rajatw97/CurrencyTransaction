package com.learning.currency.model;


import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Component
public class DefaultResponse implements  java.io.Serializable {

    private String message;
    private Block block;
}
