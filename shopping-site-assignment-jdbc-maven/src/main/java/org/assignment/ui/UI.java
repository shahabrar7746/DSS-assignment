package org.assignment.ui;


import org.assignment.entities.Customer;

import java.util.List;


public abstract class UI {

    public void initAdminServices(Customer admin){}
    public void initAuthServices(){}
    public void initCustomerServices(Customer customer) {}

    public  void displayOptions(List<String> options){
        options.forEach(System.out::println);
    }
}
