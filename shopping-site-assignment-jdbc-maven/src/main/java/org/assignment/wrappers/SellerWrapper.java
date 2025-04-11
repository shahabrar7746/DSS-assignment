package org.assignment.wrappers;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SellerWrapper {
    private final long sellerId;
    private final String sellerName;
    @Override
    public String toString() {
        return String.format("| %-20s | %-30s |", sellerId, sellerName);
    }
}
