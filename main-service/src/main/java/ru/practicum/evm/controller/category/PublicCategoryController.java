package ru.practicum.evm.controller.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.evm.dto.category.CategoryDto;
import ru.practicum.evm.service.CategoryService;
import ru.practicum.evm.exception.IncorrectObjectException;

import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/categories")
public class PublicCategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {
        log.info("Get categories to public");
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable @NotNull Long catId) throws IncorrectObjectException {
        log.info("Get category with id {}", catId);
        return categoryService.getCategoryById(catId);
    }
}
