package ui.dashboard;

import ui.display.DisplayOptions;
import model.entity.user.User;

public abstract class Ui extends DisplayOptions {
    void UserScreen(User user){}
    void AdminScreen(User user){}
}
