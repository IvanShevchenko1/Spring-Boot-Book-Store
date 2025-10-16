package org.store.springbootbookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.store.springbootbookstore.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
