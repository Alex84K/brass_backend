package ferret.brass_b.accouting.service;

import ferret.brass_b.accouting.dto.*;
import ferret.brass_b.exam.model.ExamGlobal;
import ferret.brass_b.utils.PagedDataResponseDto;
import jakarta.servlet.http.Cookie;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserAccountService {
    Boolean registerTeacher(UserRegistrationRequestDto data, String pass);

    String  registerStudent(StudentRegisterDto data, String pass);

    UserResponseDto loginUser(UserLoginRequestDto data);

    UserResponseDto findUserByUsername(String username);

    UserResponseDto deleteUserById(String userId);

    PagedDataResponseDto<UserResponseDto> findAll(PageRequest pageRequest);
    Iterable<UserResponseDto> findAllAll();

    UserResponseDto updateUser(String userId, UserEditRequestDto data);

    RolesDto changeRoleList(String userId, String roleName, Boolean isAddRole);

    UserResponseDto changeGroup(String userId, String group);

    Iterable<UserResponseDto> findUsersByGroup(String group);

    UserResponseDto changeSpeciality(String userId, String speciality);

    Iterable<UserResponseDto> findUsersBySpeciality(String speciality);

    UserResponseDto addExam(String userId, ExamDto examDto);
    UserResponseDto removeExam(String userId, String examId);
    UserResponseDto editExam(String userId, ExamDto examDto);

    Boolean addMaterial(String userId, MaterialDto materialsDto);
    Boolean removeMaterial(String userId, MaterialDto materialsDto);
    UserResponseDto changeEmail(String userId, String password);

    //void changePassword(String userId, String password);

    void uploadAvatar(String userId, MultipartFile file);

    void removeAvatar(String userId);

    UserResponseDto findUserById(String id);

    //UserResponseDto updateUser(String login, UserEditRequestDto userEditDto);

    void changePassword(String login, String newPassword);

    Iterable<UserResponseDto> getAllUsers();

    List<Cookie> logoutUser();

    UserResponseDto findStudentByNumberBook(String numberBook);

    //
    Boolean examFlagByUser(String userId, String examId);
    UserResponseDto deleteExamFlag(String userId, String examId);
}
