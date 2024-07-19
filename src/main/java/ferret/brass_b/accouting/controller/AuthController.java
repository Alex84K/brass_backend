package ferret.brass_b.accouting.controller;

import ferret.brass_b.accouting.dto.StudentRegisterDto;
import ferret.brass_b.accouting.dto.UserLoginRequestDto;
import ferret.brass_b.accouting.dto.UserRegistrationRequestDto;
import ferret.brass_b.accouting.dto.UserResponseDto;
import ferret.brass_b.accouting.service.AuthService;
import ferret.brass_b.accouting.service.MailServiceImpl;
import ferret.brass_b.accouting.service.UserAccountService;
import ferret.brass_b.utils.MessageResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserAccountService userAccountService;
    private final AuthService authService;
    private final MailServiceImpl mailService;

    @PostMapping("/registration-teacher")
    public Boolean registerTeacher(@Valid @RequestBody UserRegistrationRequestDto data, HttpServletResponse response) {
        String code = generateVerificationCode();
        mailService.send(
                data.getEmail(),
                "Hello From Kitten",
                "Hello, my name is " + data.getFirstName() + ". Have a nice day!" + " CODE ----- " + code
        );
        return userAccountService.registerTeacher(data, code);
    }

    /*@PostMapping("/registration-student")
    public ResponseEntity<UserResponseDto> registerStudent(@Valid @RequestBody StudentRegisterDto data, HttpServletResponse response) {
        String code = generateVerificationCode();
        mailService.send(
                data.getEmail(),
                "Hello From Kitten",
                "Hello, my name is " + data.getFirstName() + ". Have a nice day!" + " CODE ----- " + code
        );

        UserResponseDto userResponseDto = userAccountService.registerStudent(data, code);
        response.addCookie(authService.createAccessTokenCookie(userResponseDto.getUsername()));
        response.addCookie(authService.createRefreshToken(userResponseDto.getUsername()));
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
    }*/

    @PostMapping("/registration-student")
    public String registerStudent(@Valid @RequestBody StudentRegisterDto data, HttpServletResponse response) {
        String code = generateVerificationCode();
        mailService.send(
                data.getEmail(),
                "Password for email",
                "Hello, CODE ----- " + code
        );
        return userAccountService.registerStudent(data, code);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> loginUser(@Valid @RequestBody UserLoginRequestDto data, HttpServletResponse response) {
        UserResponseDto userResponseDto = userAccountService.loginUser(data);
        response.addCookie(authService.createAccessTokenCookie(userResponseDto.getUsername()));
        response.addCookie(authService.createRefreshToken(userResponseDto.getUsername()));
        return ResponseEntity.ok(userResponseDto);
    }

    @GetMapping("/logout")
    public ResponseEntity<MessageResponseDto> logout(HttpServletResponse response) {
        userAccountService.logoutUser().forEach(response::addCookie);
        return ResponseEntity.ok(new MessageResponseDto("Successful logout"));
    }

    /*@GetMapping("/activation")
    public UserResponseDto activateUserEmail(@NotBlank @RequestParam String key) {
        return userAccountService.activateUserEmail(key);
    }*/

    public static String generateVerificationCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000);
        return String.valueOf(code);
    }
}
