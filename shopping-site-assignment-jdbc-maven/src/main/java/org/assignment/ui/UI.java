package org.assignment.ui;


import org.assignment.entities.Customer;
import org.assignment.exceptions.UnauthorizedOperationException;
import org.assignment.repository.interfaces.ProductRepository;
import org.assignment.repositoryhibernateimpl.ProductRepoHibernateImpl;
import org.assignment.repositoryjdbcimpl.ProductRepositoryImpl;
import org.assignment.services.AuthenticationService;
import org.assignment.util.ColorCodes;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public abstract class UI {

    public void initAdminServices(Customer admin){}
    public void initAuthServices(){}
    public void initCustomerServices(Customer customer) throws UnauthorizedOperationException, SQLException {}

    public static void displayOptions(List<String> options){
        options.forEach(System.out::println);
    }
}
