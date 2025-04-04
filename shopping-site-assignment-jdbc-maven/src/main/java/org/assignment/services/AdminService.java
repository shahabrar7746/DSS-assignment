package org.assignment.services;

import org.assignment.entities.Product;
import org.assignment.enums.ProductType;
import org.assignment.exceptions.*;
import org.assignment.serviceimlementation.AdminServiceImplementation;
import org.assignment.util.Response;

import javax.naming.OperationNotSupportedException;


public interface AdminService {

    Response grantAccess(Long id);


    Response revokeAccess(Long id);



}
