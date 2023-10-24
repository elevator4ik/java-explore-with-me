package ru.practicum.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatDto {
    @NotNull(message = "timestamp is null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @PastOrPresent
    private LocalDateTime timeStamp;
    @NotBlank
    @Size(max = 1000, message = "uri is more than 2000 symbols")
    private String uri;
    @NotBlank
    @Size(max = 15, message = "ip is more than 50 symbols")
    @Pattern(regexp = "^((25[0-5]|(2[0-4]|1[0-9]|[1-9]|)[0-9])(\\.(?!$)|$)){4}$",
            message = "Invalid IP Address")
    private String ip;
    @NotBlank
    @Size(max = 200, message = "appName is more than 200 symbols")
    private String app;
}
