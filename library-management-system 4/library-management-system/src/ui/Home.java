package ui;

import entity.User;
import enums.Role;
import repository.daoimpl.AuthenticationDoaImpl;
import repository.daoimpl.UserDaoImpl;
import servicesImpl.AuthenticationServicesImpl;
import ui.dashboard.DashBoardAdmin;
import ui.dashboard.DashboardUser;
import utils.Response;


import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Home extends AbstractUi {
    static Scanner sc = new Scanner(System.in);
    UserDaoImpl userDao = new UserDaoImpl();
    AuthenticationDoaImpl authenticationDoa = new AuthenticationDoaImpl(userDao);

    static {
        System.out.println("*********************************Welcome****************************************************");
    }

    public void HomeScreen() {
        boolean isExit = true;
        int choice = 0;
        while (isExit) {
            displayOption(List.of("*********************************Login**************************************************",
                    "Enter 1 for login :",
                    "Enter 2 for SignUp:", "Enter 3 for Exit:"));
            try {
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("please enter proper input");
                sc.nextLine();
                continue;
            }

            switch (choice) {
                case 1: {
                    User user = userInput(choice);
                    if (user != null) {
                        if (user.getRole().toString().equalsIgnoreCase("user")) {
                            new DashboardUser().UserScreen(user);
                        } else {
                            new DashBoardAdmin().AdminScreen(user);
                        }
                    } else {
                        System.out.println("Invalid User");
                    }
                    break;
                }
                case 2: {
                    userInput(choice);
                    break;
                }
                case 3: {
                    isExit = false;
                    break;
                }
                default: {
                    System.out.println("Invalid operation : ");
                }
            }
        }
    }

        //Method to Take user inputs
        private User userInput(int option) {
            System.out.println("************Enter a Detail****************");
            System.out.println("Enter a email : ");
            var email = sc.next();
            System.out.println("Enter a password : ");
            var password = sc.next();
            if (option == 2) {
                System.out.println("Enter a name : ");
                var name = sc.next();
              User  user = new User(name, email, password, Role.user);
                Response response =  new AuthenticationServicesImpl(authenticationDoa).userRegistration(user);
                System.out.println(response.getMessage());
                return (User)response.getResponse();
            } else {
                Response response = new AuthenticationServicesImpl(authenticationDoa).userLogin(email, password);
                System.out.println(response.getMessage());
                return (User)response.getResponse();
            }
        }
}
