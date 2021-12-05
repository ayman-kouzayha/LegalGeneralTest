package com.example.LegalGeneralTest;

public class CoinUnit {
    private String name;
    private int value;
    private int quantity;

    public CoinUnit(String name, int value, int amount) {
        this.name = name;
        this.value = value;
        this.quantity = amount;
    }

    public CoinUnit(CoinUnit coinUnit) {
        this.name = coinUnit.name;
        this.value = coinUnit.value;
        this.quantity = coinUnit.quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
