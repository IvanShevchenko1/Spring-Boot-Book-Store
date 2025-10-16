package org.store.springbootbookstore.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.store.springbootbookstore.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Page<Order> findAllByUserId(Long userId, Pageable pageable);

    List<Order> id(Long id);

    Optional<Order> findById(Long id);
}
