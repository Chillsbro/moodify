package org.connerretttech.server.repositories;


import org.connerretttech.server.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsernameIgnoreCase(String username);
    User findByEmail(String email);
    boolean existsByUsername(String username);
}
//Gruphlipiscup
//Connection to our database.  Methods will be used to interact with  our database to see if a user exists, find by email, etc.
