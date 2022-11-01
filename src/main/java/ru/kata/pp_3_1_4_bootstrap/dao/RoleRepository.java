package ru.kata.pp_3_1_4_bootstrap.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.kata.pp_3_1_4_bootstrap.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
