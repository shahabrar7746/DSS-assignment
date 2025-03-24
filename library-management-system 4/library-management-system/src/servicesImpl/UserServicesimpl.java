package servicesImpl;

import customexception.DataReadException;
import enums.ResponseStatus;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import repository.dao.UserDao;
import repository.daoimpl.UserDaoImpl;
import entity.User;
import utils.Response;

import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

public class UserServicesimpl extends UserDaoImpl {
  //  private static final Logger log = LoggerFactory.getLogger(UserServicesimpl.class);
    private final UserDao userDao;

    public UserServicesimpl(UserDao userDao) {
        this.userDao = userDao;
    }

    public Response updateUsre(User user){
        Scanner sc = new Scanner(System.in);
        Response response= new Response(null, ResponseStatus.Error,"SomeThing went wrong..");
        UserDaoImpl userDoa = new UserDaoImpl();
        System.out.println("What do u want to update");
        System.out.println("enter 1 for name update : ");
        System.out.println("enter 2 for  email update : ");
        System.out.println("enter 3 for  exit : ");
        int input=0;
        try {
           input = sc.nextInt();
        }catch (InputMismatchException e){
            System.err.println("Enter a proper input..");
            sc.nextLine();
            updateUsre(user);
        }
        switch (input) {
            case 1 -> {
                sc.nextLine();
                System.out.println("Enter a name :");
                String name = sc.nextLine().trim().toLowerCase();
                user.setName(name);
                User isUserUpdate = userDoa.add(user);
                if(Objects.nonNull(isUserUpdate)){
                    response=new Response(user,ResponseStatus.SUCCESS,"your name update successfully...");
                }
                return response;
            }
            case 2 -> {
                sc.nextLine();
                System.out.println("Enter a old email :");
                String oldEmail = sc.nextLine().trim().toLowerCase();
                System.out.println("Enter a new email :");
                String newEmail = sc.nextLine().trim().toLowerCase();
                if (user.getEmail().equalsIgnoreCase(oldEmail)) {
                    user.setEmail(newEmail);
                    User isUserUpdate = userDoa.add(user);
                    if(Objects.nonNull(isUserUpdate)){
                        response=new Response(user,ResponseStatus.SUCCESS,"your name update successfully...");
                    }
                    return response;
                } else {
                    return new Response(user,ResponseStatus.SUCCESS,"old email not match...");
                }
            }case 3-> {
              //  return; Todo need to fix returnm
            }
            default -> {
                System.out.println("Enter a proper input : ");
            }
        }
        return response;
    }
}
