package com.shopkart.ecommerce.model;


import jakarta.persistence.Column;

import java.time.LocalDate;

public class PaymentInfo {

    @Column(name = "cardholder_name")
    private String cardholderName;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "expiraion_date")
    private LocalDate expirationDate;

    @Column(name = "cvv")
    private String cvv;

}
