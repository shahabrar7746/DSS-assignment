package org.assignment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


public enum Currency {
    USD('$', 1.0),
    INR('₹', 83.9),
    EUR('€', 75.3),
    ;
    private Character symbol;
    private double value;

    public Character getSymbol() {
        return symbol;
    }

    public double getValue() {
        return value;
    }

    Currency(Character symbol, double value) {
        this.symbol = symbol;
        this.value = value;
    }
}
