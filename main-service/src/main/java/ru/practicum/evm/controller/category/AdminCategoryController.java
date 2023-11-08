package ru.practicum.evm.controller.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.evm.dto.category.CategoryDto;
import ru.practicum.evm.dto.category.NewCategoryDto;
import ru.practicum.evm.service.CategoryService;
import ru.practicum.evm.exception.BadRequestException;
import ru.practicum.evm.exception.IncorrectObjectException;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final CategoryService service;

    @PostMapping
    public CategoryDto addCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        final CategoryDto categoryDto = service.addCategory(newCategoryDto);
        log.info("Add category {}", categoryDto);
        return categoryDto;
    }

    @PatchMapping
    public CategoryDto updateCategory(@RequestBody @Valid CategoryDto categoryDto)
            throws IncorrectObjectException, BadRequestException {
        log.info("Update category {}", categoryDto);
        return service.updateCategory(categoryDto);
    }

    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable Long catId) throws IncorrectObjectException {
        log.info("Delete category with id {}", catId);
        service.deleteCategory(catId);
    }
}
