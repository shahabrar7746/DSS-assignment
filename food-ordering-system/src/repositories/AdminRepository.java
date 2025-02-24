package repositories;

import entities.Admin;

import java.util.ArrayList;
import java.util.List;

public final class AdminRepository {
    static List<Admin> admins;

    private AdminRepository() {
    }

    static {
        admins = new ArrayList<>();
        Admin admin = new Admin("DS", "admin", "1234");
        admins.add(admin);
    }

    public static boolean addAdmin(Admin admin) {
        admins.add(admin);
        return true;
    }

    public static List<Admin> getAdmins() {
        return admins;
    }
}