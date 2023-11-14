package ru.practicum.main.comment.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateDto {
    @NotBlank
    @Size(min = 1,
            max = 1000,
            message = "text is less then 1 and more then 1000 symbols")
    private String text;
}
