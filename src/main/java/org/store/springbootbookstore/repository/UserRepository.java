package org.store.springbootbookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.store.springbootbookstore.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
