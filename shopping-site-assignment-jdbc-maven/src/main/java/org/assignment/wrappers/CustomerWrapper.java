package org.assignment.wrappers;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomerWrapper {
    private final String name;
    private final String email;

    @Override
    public String toString() {
        return String.format("| %-20s | %-30s |", name, email);
    }
}
