package com.example.healthgenie.repository;

import com.example.healthgenie.entity.Role;
import com.example.healthgenie.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {



    public User findByRoleAndId(Role role,Long Id);

}
