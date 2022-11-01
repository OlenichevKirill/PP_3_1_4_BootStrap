package ru.kata.pp_3_1_4_bootstrap.service;


import ru.kata.pp_3_1_4_bootstrap.model.User;

import java.util.List;

public interface UserService {
    User getUser(long id);

    void saveUser(User user);

    void removeUserById(long id);

    void updateUser(User user);

    List<User> getAllUsers();
}
