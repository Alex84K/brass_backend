package ferret.brass_b.image.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ImageDto {
    private  String id;
    private  String name;
    private String fileName;
    private Long size;
    private String contentType;
    private  byte[] bytes;
}
