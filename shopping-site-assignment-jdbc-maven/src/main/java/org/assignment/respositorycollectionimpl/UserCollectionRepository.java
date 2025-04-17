//package org.assignment.respositorycollectionimpl;
//
//import org.assignment.entities.User;
//
//import org.assignment.enums.Roles;
//import org.assignment.repository.interfaces.UserRepository;
//import org.assignment.wrappers.CustomerWrapper;
//import org.assignment.wrappers.SellerWrapper;
//
//import java.sql.SQLException;
//import java.time.LocalDateTime;
//import java.util.*;
//
//import java.util.stream.Collectors;
//
///**
// * Replicates functionality of database.
// */
//public class UserCollectionRepository implements UserRepository {
//    private List<User> users;
//
//
//
//
//    /*
//     * Used to populate List of users.
//     */
//    public UserCollectionRepository() {
//        init();
//    }
//
//    private void init() {
//        this.users = new ArrayList<>();
//        addCustomer("Abrar", "superAdmin@gmail.com", "mumbai", System.getenv("SUPER_ADMIN_PASSWORD"), Roles.SUPER_ADMIN);//change to super admin after final review
//        addCustomer("sam", "admin@gmail.com", "ambernath", "admin", Roles.ADMIN);
//        addCustomer("joe", "user@gmail.com", "ambernath", "user", Roles.CUSTOMER);
//    }
//
//
//    private void addCustomer(String name, String email, String address, String password, Roles role) {
//        long id = new Random().nextLong(0, 9000);
//        User user = User.builder()
//                .email(email)
//                .name(name)
//                .address(address)
//                .password(password)
//                .registeredOn(LocalDateTime.now())
//                .id(id)
//                .role(role)
//                .build();
//        users.add(user);
//    }
//
//
//    @Override
//    public Optional<User> fetchById(Long id) {
//        Map<Long, User> map = users.stream().collect(Collectors.toMap(User::getId, c -> c));
//
//        Optional<User> customer = map.containsKey(id) && map.get(id).getRole() == Roles.CUSTOMER ? Optional.of(map.get(id)) : Optional.empty();
//
//        return customer;
//    }
//
//    @Override
//    public Optional<User> fetchByEmail(String email) {
//        Map<String, User> map = null;
//        boolean isNull = true;
//        try {
//            map = users.stream().collect(Collectors.toConcurrentMap(User::getEmail, c -> c));
//            isNull = false;
//        } catch (IllegalStateException e) {
//            System.err.println("Duplicate entry found");
//        }
//        if (isNull) {
//            return Optional.empty();
//        }
//        return map.containsKey(email) ? Optional.of(map.get(email)) : Optional.empty();
//    }
//
//    @Override
//    public User addCustomer(User user) {
//
//        addCustomer(user.getName(), user.getEmail(), user.getAddress(), user.getPassword(), user.getRole());
//        return user;
//    }
//
//    @Override
//    public User updateCustomer(User user) {
//        users.remove(user);
//        users.add(user);
//        return user;
//    }
//
//    @Override
//    public Optional<User> fetchUserByIdAndRole(Long id, Roles role) throws SQLException {
//        return users
//                .stream()
//                .filter(u-> u.getId().equals(id) && u.getRole() == role)
//                .findFirst();
//    }
//
//    @Override
//    public List<User> fetchUserByRole(Roles role) {
//        return users
//                .stream()
//                .filter(u-> u.getRole() == role)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<SellerWrapper> fetchSellers() {
//        return List.of();
//    }
//
//    @Override
//    public List<CustomerWrapper> fetchCustomerWrappers() {
//        return List.of();
//    }
//}
