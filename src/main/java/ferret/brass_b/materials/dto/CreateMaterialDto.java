package ferret.brass_b.materials.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
public class CreateMaterialDto {
    private String title;
    private Set<String> tags;
    private String link;
}
