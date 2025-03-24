package repository.dao;

import entity.User;
import utils.Response;

public interface AuthenticationDao {
     boolean userRegistration(User user) throws Exception;
     User userLogin(String email,String password) throws Exception;
}
