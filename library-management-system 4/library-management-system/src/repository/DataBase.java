package repository;

import customexception.DataDeleteException;
import customexception.DataInsertException;
import customexception.DataReadException;
import customexception.DataUpdateException;

public interface DataBase<T> {
    boolean add(T t) throws DataInsertException;

    void read() throws DataReadException;

    boolean update(T object) throws DataUpdateException;

    boolean delete(T t) throws DataDeleteException;
}
