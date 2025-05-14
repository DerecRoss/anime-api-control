package academy.devdojo.springboot2.requests;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnimePutRequestBody {
    private Long id;

    @NotEmpty(message = "The name of anime is empty")
    private String name;
}
