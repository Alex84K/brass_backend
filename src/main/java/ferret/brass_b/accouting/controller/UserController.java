package ferret.brass_b.accouting.controller;

import ferret.brass_b.accouting.dto.*;
import ferret.brass_b.accouting.dto.exceptions.UserNotFoundException;
import ferret.brass_b.accouting.dto.exceptions.UserUnauthorizedException;
import ferret.brass_b.accouting.model.UserAccount;
import ferret.brass_b.accouting.service.MailServiceImpl;
import ferret.brass_b.accouting.service.UserAccountService;
import ferret.brass_b.exam.model.ExamGlobal;
import ferret.brass_b.utils.MessageResponseDto;
import ferret.brass_b.utils.PagedDataResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserAccountService userAccountService;
    private final MailServiceImpl mailService;

    @GetMapping("")
    public PagedDataResponseDto<UserResponseDto> getAllUsers(
            @RequestParam(required = false, defaultValue = "25", name = "size") Integer size,
            @RequestParam(required = false, defaultValue = "0", name = "page") Integer page
    ) {
        return userAccountService.findAll(PageRequest.of(page, size));
    }

    @GetMapping("/me")
    public UserResponseDto getCurrentUser(@AuthenticationPrincipal UserAccount user) {
        if (user == null) {
            throw new UserUnauthorizedException();
        }
        return userAccountService.findUserById(user.getId());
    }

    @GetMapping("/{userId}")
    public UserResponseDto findUserById(@PathVariable String userId) {
        return userAccountService.findUserById(userId);
    }

    @PutMapping("/{userId}")
    public UserResponseDto updateUser(@Valid @RequestBody UserEditRequestDto data, @PathVariable String userId) {
        return userAccountService.updateUser(userId, data);
    }

    @DeleteMapping("/{userId}")
    public UserResponseDto deleteUser(@PathVariable String userId) {
        return userAccountService.deleteUserById(userId);
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<MessageResponseDto> changePassword(@PathVariable String userId,
                                                             @RequestHeader("X-Password")
                                                             String password) {
        userAccountService.changePassword(userId, password);
        return ResponseEntity.ok(new MessageResponseDto("Successfully changed password"));
    }

    @PostMapping("/{userId}/group/{group}")
    public UserResponseDto changeGroup(@PathVariable String userId, @PathVariable String group) {
        return userAccountService.changeGroup(userId, group);
    }

    @GetMapping("/group/{group}")
    public Iterable<UserResponseDto> findUsersByGroup(@PathVariable String group) {
        return userAccountService.findUsersByGroup(group);
    }

    @PostMapping("/{userId}/speciality/{speciality}")
    public UserResponseDto changeSpeciality(@PathVariable String userId, @PathVariable String speciality) {
        return userAccountService.changeSpeciality(userId, speciality);
    }

    @GetMapping("/speciality/{speciality}")
    public Iterable<UserResponseDto> findUsersBySpeciality(@PathVariable String speciality) {
        return userAccountService.findUsersBySpeciality(speciality);
    }

    @PostMapping("/{userId}/avatar")
    public ResponseEntity<Void> uploadAvatar(@RequestParam("file") MultipartFile file, @PathVariable String userId) {
        userAccountService.uploadAvatar(userId, file);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/avatar")
    public ResponseEntity<Void> removeAvatar(@PathVariable String userId) {
        userAccountService.removeAvatar(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}/role/{role}")
    public RolesDto addRole(@PathVariable String userId, @PathVariable String role) {
        try {
            return userAccountService.changeRoleList(userId, role, true);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect user ID format");
        }
    }

    @DeleteMapping("/{userId}/role/{role}")
    public RolesDto removeRole(@PathVariable String userId, @PathVariable String role) {
        try {
            return userAccountService.changeRoleList(userId, role, false);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect user ID format");
        }
    }

    @PostMapping("/{userId}/exams/scores")
    public UserResponseDto addExam(@PathVariable String userId, @RequestBody ExamDto exam) {
        try {
            return userAccountService.addExam(userId, exam);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect user ID format");
        }
    }

    @DeleteMapping("/{userId}/exams")
    public UserResponseDto removeExam(@PathVariable String userId, @RequestBody ExamDto examDto){
        return userAccountService.removeExam(userId, examDto);
    }

    @PutMapping("/{userId}/exams")
    public UserResponseDto editExam(@PathVariable String userId, @RequestBody ExamDto examDto){
        return userAccountService.editExam(userId, examDto);
    }

    @PutMapping("/materials/students/{userId}")
    public Boolean addMaterial(@PathVariable String userId, @RequestBody MaterialsDto material) {
        return userAccountService.addMaterial(userId, material);
    }

    @DeleteMapping("/materials/students/{userId}")
    public Boolean delMaterial(@PathVariable String userId, @RequestBody MaterialsDto materialsDto) {
        return userAccountService.removeMaterial(userId, materialsDto);
    }

    @GetMapping("/all")
    public Iterable<UserResponseDto> getAllAllUsers() {
        return userAccountService.findAllAll();
    }

    @GetMapping("/students/{numberBook}")
    UserResponseDto findStudentByNumberBook(@PathVariable String numberBook) {
        return userAccountService.findStudentByNumberBook(numberBook);
    }

    @GetMapping("/group/{numberGroup}/exams/{examId}")
    public Boolean examDistribution(@PathVariable String numberGroup, @PathVariable String examId){
        return userAccountService.examDistribution(numberGroup, examId);
    }

    @GetMapping("/{userId}/exams/{examId}")
    public Boolean examFlagByUser(@PathVariable String userId, @PathVariable String examId){
        return userAccountService.examFlagByUser(userId, examId);
    }

    @DeleteMapping("/{userId}/exams/{examId}")
    public Boolean deleteExamFlag(@PathVariable String userId, @PathVariable String examId){
        return userAccountService.deleteExamFlag(userId, examId);
    }
}