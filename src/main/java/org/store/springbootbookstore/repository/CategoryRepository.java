package org.store.springbootbookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.store.springbootbookstore.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
