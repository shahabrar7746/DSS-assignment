package servicesImpl;

import enums.ResponseStatus;
import entity.User;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import repository.daoimpl.AuthenticationDoaImpl;
import server.AuthenticationServices;
import utils.Response;
import java.util.Objects;


public class AuthenticationServicesImpl implements AuthenticationServices {

    //private static final Logger log = LoggerFactory.getLogger(AuthenticationServicesImpl.class);
    private final AuthenticationDoaImpl authenticationDao;

    public AuthenticationServicesImpl(AuthenticationDoaImpl authenticationDao){
        this.authenticationDao=authenticationDao;
    }

    @Override
    public Response userRegistration(User user) {
        Response response = new Response(null,ResponseStatus.Error,"User Already Exist");
        try {
            if(authenticationDao.userRegistration(user)){
               response = new Response(user,ResponseStatus.SUCCESS,"Registration successful..");
            }
        } catch (Exception e) {
          //  log.error("Registration error",e);
        }
        return response;
    }

    @Override
    public Response userLogin(String email,String password) {
        Response response = new Response(null,ResponseStatus.Error,"Wrong Credentials..");
        try {
            User user = authenticationDao.userLogin(email, password);
            if(Objects.nonNull(user)) {
                response = new Response(user,ResponseStatus.SUCCESS,"Welcome "+user.getName());
            }
        } catch (Exception e) {
          // log.error("Login error",e);
        }
        return response;
    }
}
