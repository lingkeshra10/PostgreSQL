package com.postgresql.demo.repository;

import com.postgresql.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepo extends JpaRepository<User, Long> {

    @Query("from User u where u.username = :username")
    User retrieveByUsername(@Param("username") String username);

    @Query("select count(u)>0 from User u where u.username = :username")
    boolean findExistByUsername(@Param("username") String username);

    @Query("select count(u)>0 from User u where u.email = :email")
    boolean findExistByEmail(@Param("email") String email);

}
