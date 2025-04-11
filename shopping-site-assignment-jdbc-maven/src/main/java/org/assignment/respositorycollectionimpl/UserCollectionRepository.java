package org.assignment.respositorycollectionimpl;

import org.assignment.entities.User;

import org.assignment.enums.Roles;
import org.assignment.repository.interfaces.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

import java.util.stream.Collectors;

/**
 * Replicates functionality of database.
 */
public class UserCollectionRepository implements UserRepository {
    private List<User> users;


    @Override
    public List<User> getCustomers()  {
        return users;
    }

    /*
     * Used to populate List of users.
     */
    public UserCollectionRepository() {
        init();
    }

    private void init() {
        this.users = new ArrayList<>();
        addCustomer("Abrar", "superAdmin@gmail.com", "mumbai", System.getenv("SUPER_ADMIN_PASSWORD"), Roles.SUPER_ADMIN);//change to super admin after final review
        addCustomer("sam", "admin@gmail.com", "ambernath", "admin", Roles.ADMIN);
        addCustomer("joe", "user@gmail.com", "ambernath", "user", Roles.CUSTOMER);
    }


    private void addCustomer(String name, String email, String address, String password, Roles role) {
        long id = new Random().nextLong(0, 9000);
        User user = User.builder()
                .email(email)
                .name(name)
                .address(address)
                .password(password)
                .registeredOn(LocalDateTime.now())
                .id(id)
                .role(Roles.CUSTOMER)
                .build();
        users.add(user);
    }


    @Override
    public Optional<User> fetchById(Long id) {
        Map<Long, User> map = users.stream().collect(Collectors.toConcurrentMap(User::getId, c -> c));

        Optional<User> customer = map.containsKey(id) && map.get(id).getRole() == Roles.CUSTOMER ? Optional.of(map.get(id)) : Optional.empty();

        return customer;
    }

    /**
     * Searches for admin based on the id.
     *
     * @param id id to be searched.
     * @return returns Optional of user if any admin with the same id is found or else false.
     */
    @Override
    public Optional<User> fetchAdminById(Long id) {
        Map<Long, User> map = users.stream().collect(Collectors.toConcurrentMap(User::getId, c -> c));
        return map.containsKey(id) && map.get(id).getRole() == Roles.ADMIN ? Optional.of(map.get(id)) : Optional.empty();
    }


    @Override
    public Optional<User> fetchByEmail(String email) {
        Map<String, User> map = null;
        boolean isNull = true;
        try {
            map = users.stream().collect(Collectors.toConcurrentMap(User::getEmail, c -> c));
            isNull = false;
        } catch (IllegalStateException e) {
            System.err.println("Duplicate entry found");
        }
        if (isNull) {
            return Optional.empty();
        }
        return map.containsKey(email) ? Optional.of(map.get(email)) : Optional.empty();
    }

    @Override
    public User addCustomer(User user) {

        addCustomer(user.getName(), user.getEmail(), user.getAddress(), user.getPassword(), user.getRole());
        return user;
    }

    @Override
    public User updateCustomer(User user) {
        users.remove(user);
        users.add(user);
        return user;
    }

    @Override
    public void removeCustomer(User user) {
        users.remove(user);
    }

    @Override
    public Optional<User> fetchSellerById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<User> fetchAllSellers() {
        return List.of();
    }
}
