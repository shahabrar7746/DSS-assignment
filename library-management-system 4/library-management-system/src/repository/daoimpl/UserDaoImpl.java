package repository.daoimpl;

import enums.Role;
import entity.User;
import repository.dao.UserDao;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class UserDaoImpl implements UserDao {

    private Map<String, User> usersDb =new HashMap<>();
    private static final String adminEmail = "admin@gmail.com";
    private static final String adminPassword = "1234";


    public UserDaoImpl(){
        userDBinitialize();
    }

    @Override
    public User add(User user) {
      if(Objects.isNull(usersDb)){
          userDBinitialize();
      }
        return  usersDb.put(user.getEmail(),user);
    }

    private void userDBinitialize(){
        User adminUser = new User("admin", adminEmail, adminPassword, Role.admin);
        adminUser.setId("Super01");
        usersDb.put(adminEmail, adminUser);
    }

    @Override
    public Optional<User> findUserById(String id){
        return usersDb.values().stream().filter(data->data.getId().equalsIgnoreCase(id)).findFirst();
    }

    @Override
    public Optional<User> checkUserPrentOrNot(String email) {
        System.out.println(usersDb.values());
        return usersDb.values().stream().filter(data->data.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    @Override
    public void getUser() {
        try {
            usersDb.forEach((email, user) -> System.out.println(user));
        }catch (RuntimeException e){
            throw new RuntimeException("some thing happened during the reading of data");
        }
    }
}
