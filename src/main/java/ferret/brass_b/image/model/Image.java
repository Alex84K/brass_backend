package ferret.brass_b.image.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "images")
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Image {
    @Id
    private  String id;
    private  String name;
    private String fileName;
    private Long size;
    private String contentType;
    private  byte[] bytes;

    public Image(String name, String fileName, Long size, String contentType, byte[] bytes) {
        this.name = name;
        this.fileName = fileName;
        this.size = size;
        this.contentType = contentType;
        this.bytes = bytes;
    }
}
