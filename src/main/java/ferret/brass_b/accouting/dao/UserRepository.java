package ferret.brass_b.accouting.dao;

import ferret.brass_b.accouting.dto.UserResponseDto;
import ferret.brass_b.accouting.model.UserAccount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.stream.Stream;

public interface UserRepository extends MongoRepository<UserAccount, String> {

    Optional<UserAccount> findByUsernameEquals(String username);

    Optional<UserAccount> findById(String userId);
    Optional<UserAccount> findByNumberBook(String numberBook);

    Optional<UserAccount> findByNumberBookEquals(String numberBook);

    Optional<UserAccount> findByUsernameIgnoreCase(String username);

    Optional<UserAccount> findByEmail(String email);

    Boolean existsByUsernameIgnoreCase(String username);

    Boolean existsByEmail(String email);

    Boolean existsByNumberBook(String numberBook);

    Stream<UserAccount> findUsersByGroup(String group);

    Stream<UserAccount> findUsersBySpeciality(String speciality);

}