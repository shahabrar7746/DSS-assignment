package org.assignment.ui;


import jdk.swing.interop.SwingInterOpUtils;
import org.assignment.entities.CartItems;
import org.assignment.entities.Order;
import org.assignment.entities.User;
import org.assignment.enums.ProductType;
import org.assignment.util.ColorCodes;
import org.assignment.util.Response;
import org.assignment.wrappers.CustomerWrapper;
import org.assignment.wrappers.ProductWrapper;
import org.assignment.wrappers.SellerWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public abstract class UI {
    public abstract void initAdminServices(User admin);

    public abstract void initAuthServices();

    public abstract void initCustomerServices(User user);

    public void displayOptions(List<String> options) {
        options.forEach(System.out::println);
    }

    public void printResponse(Response response) {
        if (response == null) return;
        switch (response.getStatus()) {
            case SUCCESSFUL -> System.out.println(ColorCodes.GREEN + response.getData() + ColorCodes.RESET);
            case ERROR -> System.out.println(ColorCodes.RED + response.getError() + ColorCodes.RESET);
        }
    }

    public void printProducts(List<ProductWrapper> productWrappers) {
        System.out.println("Products : ");
        System.out.println(ColorCodes.PURPLE +
                "+------------------+------------+------------------+------------------+------------------+");
        System.out.println("| ProductName      | Price      | Currency         | Type             | Seller            |");
        System.out.println("+-------------------+------------+------------------+-----------------+------------------+");
        // System.out.println(ColorCodes.RESET);
        productWrappers.forEach(System.out::println);

    }

    public void printCartItems(List<CartItems> items) {
        String header = ColorCodes.BRIGHT_BLUE + "+-------------------+-------------------+--------------------+------------+\n" +
                "| Product Name      | Quantity          | Price per unit     | Total Price|\n" +
                "+-------------------+-------------------+--------------------+------------+";
        System.out.println(header);
        items.forEach(System.out::println);

    }

    public void printCustomerWrappers(List<CustomerWrapper> wrappers) {
        String header =
                ColorCodes.PURPLE + "|----------------------|--------------------------------|\n" +
                        String.format("| %-20s | %-30s |\n", "Name", "Email") +
                        "\n|----------------------|--------------------------------|";
        System.out.println(header);
        wrappers.forEach(customerWrapper -> {
            System.out.println(customerWrapper);
            System.out.println("|----------------------|--------------------------------|");
        });
        System.out.println(ColorCodes.RESET);
    }

    public void printAndDisplayOptionsForCurrencies() {
        List<String> options = new ArrayList<>();
        org.assignment.enums.Currency[] currencies = org.assignment.enums.Currency.values();
        for (int i = 0; i < currencies.length; i++) {
            options.add("PRESS " + i + " FOR " + currencies[i]);
        }
        displayOptions(options);
    }

    public void printAndDisplayOptionsForProductTypes() {
        List<String> options = new ArrayList<>();
        ProductType[] types = ProductType.values();
        for (int i = 0; i < types.length; i++) {
            options.add("PRESS " + i + " FOR " + types[i]);
        }
        displayOptions(options);
    }

    public void printSellerWrappers(List<SellerWrapper> wrappers) {
        String header =
                ColorCodes.PURPLE + "+----------------------+--------------------------------+\n" +
                        String.format("| %-20s | %-30s |\n", "Seller Id", "Seller Name") +
                        "+----------------------|--------------------------------+";
        System.out.println(header);
        wrappers.forEach(sellerWrapper -> {
            System.out.println(sellerWrapper);
            System.out.println("+----------------------|--------------------------------+");
        });
        System.out.println(ColorCodes.RESET);

    }

    public void printCustomers(List<User> customers) {
        String header = ColorCodes.BRIGHT_BLUE + "+-----------------+-------------------------+-------------------------+--------------------+-------------------------+-----------------+\n" +
                "| User ID     | Email                   | Name                    | Address            | Registered On           | Role                 |\n" +
                "+-----------------+-------------------------+-------------------------+--------------------+-------------------------+-----------------+"
                + ColorCodes.RESET;
        System.out.println(header);
        customers.forEach(customer ->
        {
            System.out.println(customer);
            System.out.println(ColorCodes.BRIGHT_BLUE +
                    "+-----------------+-------------------------+-------------------------+--------------------+-------------------------+-----------------+"
                    + ColorCodes.RESET);

        });
    }

    public void printCustomerIds(List<User> customers) {
        String header =
                "  CUSTOMER ID  \n"
                        + "----------------------";
        System.out.println(ColorCodes.BRIGHT_BLUE + header + ColorCodes.RESET);
        for (User user : customers) {
            System.out.println(ColorCodes.BRIGHT_BLUE + user.getId() + ColorCodes.RESET);
        }

    }

    public void printOrders(List<Order> orders) {
        String header = ColorCodes.YELLOW +
                "+-------------------+-------------------+-------------------+-------------------+-------------------+-------------------+-------------------+-------------------+\n" +
                "| Product Name      | Seller            | User          | Status            | Ordered On        | Price             | Currency          | Quantity          |\n" +
                "+-------------------+-------------------+-------------------+-------------------+-------------------+-------------------+-------------------+-------------------+";
        System.out.println(header);
        orders.forEach(System.out::println);
        System.out.println(ColorCodes.RESET);
    }
}
