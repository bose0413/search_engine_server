package test.task.search.service.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDTO implements Serializable {
    @NonNull
    @NotNull
    @Size(min = 1)
    private String key;

    @NonNull
    @NotNull
    @Size(min = 1)
    private String content;
}
