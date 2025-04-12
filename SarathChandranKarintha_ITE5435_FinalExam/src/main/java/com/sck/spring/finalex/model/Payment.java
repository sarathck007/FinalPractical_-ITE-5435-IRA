package com.sck.spring.finalex.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;




import java.time.LocalDate;

@Document(collection = "payments")
public class Payment {
    @Id
    private String id;
    private Integer amount;
    private LocalDate date;

    public Payment() {}
    
    public Payment(Integer amount, LocalDate date) {
        this.amount = amount;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}