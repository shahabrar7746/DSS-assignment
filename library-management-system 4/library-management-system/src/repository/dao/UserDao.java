package repository.dao;

import customexception.DataDeleteException;
import customexception.DataInsertException;
import customexception.DataReadException;
import customexception.DataUpdateException;
import entity.User;

import java.util.Optional;

public interface UserDao {

    User add(User user) throws Exception;

    void getUser() throws Exception;

    Optional<User> findUserById(String id);

    Optional<User> checkUserPrentOrNot(String email);
}
