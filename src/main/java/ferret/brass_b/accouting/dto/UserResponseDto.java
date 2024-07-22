package ferret.brass_b.accouting.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ferret.brass_b.accouting.model.Exam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    String id;
    String username;
    String firstName;
    String lastName;
    String email;
    String telefon;
    String numberBook;
    String city;
    String group;
    String speciality;
    @JsonProperty("isEmailConfirmed")
    boolean emailActivated;
    String codeForEmail;
    String image;
    LocalDate dateRegistered;
    Set<String> roles;
    private ArrayList<Exam> progres;
    private Map<String, String> materials;
    private Set<ExamDto> examsFlags;
}
