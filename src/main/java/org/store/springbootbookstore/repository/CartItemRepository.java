package org.store.springbootbookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.store.springbootbookstore.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
