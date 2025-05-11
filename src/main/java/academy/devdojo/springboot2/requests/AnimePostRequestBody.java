package academy.devdojo.springboot2.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AnimePostRequestBody {
    @NotEmpty(message = "The name of anime is empty")
    private String name;
}
