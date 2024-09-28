package com.paisa_square.paisa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiMessage {
    private String status;
    private String code;
    private String message;

}
