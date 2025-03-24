package server.interfaces;

import model.entity.user.User;

public interface LoginAndSignUp {

    void  userRegistration();

    User userLogin();
}
