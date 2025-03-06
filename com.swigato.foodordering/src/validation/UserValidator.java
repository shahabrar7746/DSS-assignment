package validation;

import enums.UserRole;

public interface UserValidator {
    boolean isValidEmail(String email);

    boolean isValidPassword(String password);

    boolean isValidName(String name);

    UserRole getRole();
}