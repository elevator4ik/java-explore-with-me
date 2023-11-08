package dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HitDto {
    @NotNull(message = "timestamp is null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timeStamp;
    @NotBlank
    @Size(max = 1000, message = "uri is more than 1000 symbols")
    private String uri;
    @NotBlank
    @Size(max = 15, message = "ip is more than 15 symbols")
    @Pattern(regexp = "^((25[0-5]|(2[0-4]|1[0-9]|[1-9]|)[0-9])(\\.(?!$)|$)){4}$", message = "Invalid IP Address")
    private String ip;
    @NotBlank
    @Size(max = 100, message = "appName is more than 100 symbols")
    private String app;
}
