package ru.kata.pp_3_1_4_bootstrap.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.kata.pp_3_1_4_bootstrap.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "select u from User u " +
            "join fetch u.roles r where u.email = :username")
    User findByUsername(String username);
}
