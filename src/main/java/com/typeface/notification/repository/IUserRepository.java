package com.typeface.notification.repository;

import com.typeface.notification.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
}
