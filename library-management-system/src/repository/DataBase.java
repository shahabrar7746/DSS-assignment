package repository;

import customexception.DataInsertException;

public interface DataBase<T> {

    boolean add(T t) throws DataInsertException;

    void read() throws RuntimeException;

    boolean update(T object) throws RuntimeException;

    boolean delete(T t) throws RuntimeException;
}
