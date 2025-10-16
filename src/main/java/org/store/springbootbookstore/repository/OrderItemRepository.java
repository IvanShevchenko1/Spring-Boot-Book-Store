package org.store.springbootbookstore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.store.springbootbookstore.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Page<OrderItem> findByOrderId(Long orderId, Pageable pageable);

    OrderItem findByOrderIdAndId(Long orderId, Long orderItemId);
}
