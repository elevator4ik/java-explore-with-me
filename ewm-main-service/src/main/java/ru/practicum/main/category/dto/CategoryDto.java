package ru.practicum.main.category.dto;

import lombok.*;
import ru.practicum.dto.Validator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Long id;
    @NotBlank(groups = {Validator.Create.class, Validator.Update.class})
    @Size(min = 1,
            max = 50,
            groups = {Validator.Create.class, Validator.Update.class},
            message = "name is more then 50 symbols")
    private String name;
}
