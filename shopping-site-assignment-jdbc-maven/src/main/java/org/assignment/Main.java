package org.assignment;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import org.assignment.entities.Product;

import java.util.stream.Stream;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        EntityManager manager = Persistence.createEntityManagerFactory("myPersistenceUnit").createEntityManager();
        manager.getTransaction().begin();
        TypedQuery<Product> query = manager.createQuery("SELECT c FROM Product c", Product.class);
       manager.getTransaction().commit();
        System.out.println(query.getResultList());

        Stream<Product> stream = query.getResultStream();
        stream.forEach(System.out::println);

    }
}