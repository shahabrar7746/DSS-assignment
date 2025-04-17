package org.assignment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public enum Currency {
    USD('$', 1.0),
    INR('₹', 83.9),
    EUR('€', 75.3);
    private final Character symbol;
    private final double value;
}
