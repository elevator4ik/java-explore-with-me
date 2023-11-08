package ru.practicum.evm.managers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.evm.model.Category;
import ru.practicum.evm.repository.CategoryRepository;
import ru.practicum.evm.exception.BadRequestException;
import ru.practicum.evm.exception.IncorrectObjectException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryManager {

    private final CategoryRepository categoryRepository;

    public void categoryExist(Long catId) throws IncorrectObjectException {
        if (!categoryRepository.findAll().isEmpty()) {
            List<Long> ids = categoryRepository.findAll()
                    .stream()
                    .map(Category::getId)
                    .collect(Collectors.toList());
            if (!ids.contains(catId)) {
                throw new IncorrectObjectException("There is no category with id = " + catId);
            }
        } else {
            throw new IncorrectObjectException("There is no category with such id = " + catId);
        }
    }

    public void idIsNotBlank(Long categoryId) throws BadRequestException {
        if (categoryId == null) {
            throw new BadRequestException("Id cant be null");
        }
    }

    public void checkCorrectParams(int from, int size) {
        if (from < 0) {
            throw new IllegalArgumentException("From parameter cannot be less zero");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Size parameter cannot be less or equal zero");
        }
    }
}
