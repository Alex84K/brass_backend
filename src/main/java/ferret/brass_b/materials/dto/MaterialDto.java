package ferret.brass_b.materials.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialDto {
    private String id;
    private String title;
    private Set<String> tags;
    private String publisher;
    private String link;
    private LocalDateTime dateCreated;
}
