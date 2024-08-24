package ferret.brass_b.materials.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Document(collection = "materials")
public class Material {
    String id;
    @Setter
    String title;
    @Setter
    Set<String> tags;
    @Setter
    String publisherId;
    @Setter
    String link;
    LocalDateTime dateCreated = LocalDateTime.now();;

    public Material(String id, String title, Set<String> tags, String publisherId, String link) {
        this.id = id;
        this.title = title;
        this.tags = tags;
        this.publisherId = publisherId;
        this.link = link;
    }

    public boolean addTag(String tag){
        return tags.add(tag);
    }

    public boolean removeTag(String tag){
        return tags.remove(tag);
    }
}
