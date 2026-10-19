package com.example.cognit_invest.models.investments;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Investment {
    @Id
    private String id;


}
