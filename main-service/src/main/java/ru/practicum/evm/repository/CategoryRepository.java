package ru.practicum.evm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.evm.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
