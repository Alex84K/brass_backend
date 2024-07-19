package ferret.brass_b.accouting.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;


@Document(collection = "users_b")
@AllArgsConstructor
//@Builder
@Getter
public class UserAccount implements UserDetails {

    @Id
    private String id;

    private String username;
    @Setter
    private String firstName;
    @Setter
    private String lastName;

    private String email;
    @Setter
    private String password;
    @Setter
    String numberBook;
    @Setter
    String city;
    @Setter
    private String image;
    @Setter
    private String group;
    @Setter
    private String speciality;

    private LocalDate dateRegistered;

    private Set<Role> roles;
    @Setter
    private boolean isEmailActivated;
    @Setter
    private String codeForEmail;

    private ArrayList<Exam> progres;
    private Map<String, String> materials;
    private Map<String, Boolean> examsFlags;


    public UserAccount() {
        this.isEmailActivated = false;
        this.image = "https://gravatar.com/avatar/0?d=retro";
        this.dateRegistered = LocalDate.now();
        this.roles = new HashSet<>();
        this.progres = new ArrayList<>();
        materials = new HashMap<>();
        this.examsFlags = new HashMap<>();
    }

    public UserAccount(String username, String password, String firstName, String lastName, String numberbBook, String city, String code,String group, String speciality) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.numberBook = numberbBook;
        this.group = group;
        this.speciality = speciality;
        this.city = city;
        this.dateRegistered = LocalDate.now();
        this.roles = new HashSet<>();
        this.progres = new ArrayList<>();
        materials = new HashMap<>();
        this.codeForEmail = code;
        this.isEmailActivated = false;
        examsFlags = new HashMap<>();
    }

    public boolean addRole(String role) {
        return roles.add(Role.valueOf(role));
    }

    public boolean removeRole(String role) {
        return roles.remove(Role.valueOf(role));
    }

    public boolean addProgres(Exam exam) {
        return progres.add(exam);
    }

    public boolean removeProgres(Exam exam) {
        Iterator<Exam> examIterator = progres.iterator();
        while (examIterator.hasNext()) {
            Exam nextExam = examIterator.next();
            if (nextExam.getExamId().equals(exam.examId)) {
                examIterator.remove();
                return true;
            }
        }
        return false;
    }

    public boolean editProgres(Exam exam) {
        Iterator<Exam> examIterator = progres.iterator();
        while (examIterator.hasNext()) {
            Exam nextExam = examIterator.next();
            if (nextExam.getExamId().equals(exam.examId)) {
                examIterator.remove();
            }
        }
        progres.add(exam);
        return true;
    }

    public boolean addMaterial(String materialName, String link) {
        return materials.put(materialName, link) == null;
    }

    public boolean removeMaterial(String materialName) {
        return materials.remove(materialName) == null;
    }

    public boolean addExamFlags(String examId) {
        return examsFlags.put(examId, false) == null;
    }

    public void removeExamFlags(String examId) {
        examsFlags.remove(examId);
    }

    public boolean editExamFlags(String examId, Boolean b) {
        return examsFlags.put(examId, b) == null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                .toList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setSetEmailActivated() {
        this.isEmailActivated = true;
    }
}