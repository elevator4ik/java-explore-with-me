package ru.practicum.evm.mapper;

import org.springframework.stereotype.Service;
import ru.practicum.evm.dto.category.CategoryDto;
import ru.practicum.evm.dto.category.NewCategoryDto;
import ru.practicum.evm.model.Category;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryMapper {
    public Category toCategory(NewCategoryDto newCategoryDto) {
        return Category.builder()
                .name(newCategoryDto.getName())
                .build();
    }

    public CategoryDto toDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public List<CategoryDto> toDtoList(List<Category> categories) {
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        for (Category c : categories) {
            categoryDtoList.add(toDto(c));
        }
        return categoryDtoList;
    }
}
