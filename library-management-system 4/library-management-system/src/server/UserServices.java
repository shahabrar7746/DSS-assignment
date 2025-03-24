package server;

import entity.User;

import java.util.Optional;

public interface UserServices {

     Optional<User> findUserById(String id);
     Optional<User> checkUserPrentOrNot(String email);
     void getUser();


}
