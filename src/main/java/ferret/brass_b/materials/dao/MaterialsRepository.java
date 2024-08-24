package ferret.brass_b.materials.dao;

import ferret.brass_b.accouting.model.UserAccount;
import ferret.brass_b.materials.model.Material;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;

public interface MaterialsRepository extends MongoRepository<Material, String> {
   Stream<Material> findByPublisherId(String publisherId);

    Stream<Material> findByTagsInIgnoreCase(Set<String> tags);

    Stream<Material> findByDateCreatedBetween(LocalDate from, LocalDate to);
}
