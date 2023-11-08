package ru.practicum.evm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.evm.dto.category.CategoryDto;
import ru.practicum.evm.dto.category.NewCategoryDto;
import ru.practicum.evm.mapper.CategoryMapper;
import ru.practicum.evm.model.Category;
import ru.practicum.evm.repository.CategoryRepository;
import ru.practicum.evm.exception.BadRequestException;
import ru.practicum.evm.exception.IncorrectObjectException;
import ru.practicum.evm.managers.CategoryManager;
import ru.practicum.evm.service.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryManager categoryManager;
    private final CategoryMapper mapper;

    @Override
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        Category newCategory = mapper.toCategory(newCategoryDto);
        try {
            newCategory = categoryRepository.save(newCategory);
            return mapper.toDto(newCategory);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("Data of category exception");
        }
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        categoryManager.checkCorrectParams(from, size);
        final List<Category> categories = categoryRepository.findAll(PageRequest.of(from, size)).stream().collect(Collectors.toList());
        return mapper.toDtoList(categories);
    }

    @Override
    public CategoryDto getCategoryById(Long catId) throws IncorrectObjectException {
        categoryManager.categoryExist(catId);
        return mapper.toDto(categoryRepository.getReferenceById(catId));
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto) throws IncorrectObjectException, BadRequestException {
        categoryManager.categoryExist(categoryDto.getId());
        categoryManager.idIsNotBlank(categoryDto.getId());
        final Category updatedCategory = categoryRepository.getReferenceById(categoryDto.getId());
        updatedCategory.setName(categoryDto.getName());
        try {
            return mapper.toDto(categoryRepository.save(updatedCategory));
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Name of category exception");
        }
    }

    @Override
    public void deleteCategory(Long categoryId) throws IncorrectObjectException {
        categoryManager.categoryExist(categoryId);
        categoryRepository.deleteById(categoryId);
    }
}
