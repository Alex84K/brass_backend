package ferret.brass_b.accouting.controller;


import ferret.brass_b.accouting.dao.UserRepository;
import ferret.brass_b.accouting.dto.UserResponseDto;
import ferret.brass_b.accouting.model.UserAccount;
import ferret.brass_b.accouting.service.AuthService;
import ferret.brass_b.accouting.service.MailServiceImpl;
import ferret.brass_b.accouting.service.UserAccountService;
import jakarta.mail.*;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Properties;
import java.util.Random;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class MailContrpller {

    private final UserAccountService userAccountService;
    private final AuthService authService;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @GetMapping("/{usersId}/email/{mailpass}")
    public UserResponseDto emailActivated(@PathVariable String usersId, @PathVariable String mailpass, HttpServletResponse response) {
        UserResponseDto user = userAccountService.findUserById(usersId);
        response.addCookie(authService.createAccessTokenCookie(user.getUsername()));
        response.addCookie(authService.createRefreshToken(user.getUsername()));
       return userAccountService.changeEmail(usersId, mailpass);
    }

    /*public static String generateVerificationCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000);
        return String.valueOf(code);
    }

    public static void sendEmail(String to, String code) {
        final String username = "developer.8.de@gmail.com";
        final String password = "nvfycqtlzasqlbsx";

        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.user", "artem.boiar");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.password", "nvfycqtlzasqlbsx");
        props.put("mail.debug", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPassAuth() {
                return new PasswordAuthentication(username, password);
            }
        });
session.setDebug(true);
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Verification Code");
            message.setText("Your code " + code);

            Transport.send(message);
            System.out.println("Email send");
        } catch (AddressException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String email = "user_mail";
        //String generatedCode = generateVerificationCode();
        String generatedCode = "123456";
        sendEmail(email, generatedCode);

        String userInputCode = "123456";
        if(userInputCode.equals(generatedCode)) {
            System.out.println("OK!!!");
        } else {
            System.out.println("FALL!");
        }
    }*/
}
