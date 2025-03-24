package server;

import customexception.DataInsertException;
import customexception.LoginException;
import entity.User;
import utils.Response;

public interface LoginAndSignUpServices {

    Response userRegistration(User user) ;

    Response userLogin(String email,String password) ;
}
