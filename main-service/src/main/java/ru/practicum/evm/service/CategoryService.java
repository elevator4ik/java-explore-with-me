package ru.practicum.evm.service;

import ru.practicum.evm.dto.category.CategoryDto;
import ru.practicum.evm.dto.category.NewCategoryDto;
import ru.practicum.evm.exception.BadRequestException;
import ru.practicum.evm.exception.IncorrectObjectException;

import java.util.List;

public interface CategoryService {
    CategoryDto addCategory(NewCategoryDto newCategoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto) throws IncorrectObjectException, BadRequestException;

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategoryById(Long catId) throws IncorrectObjectException;

    void deleteCategory(Long categoryId) throws IncorrectObjectException;
}
