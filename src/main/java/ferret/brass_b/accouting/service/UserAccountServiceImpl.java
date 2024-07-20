package ferret.brass_b.accouting.service;

import ferret.brass_b.accouting.dao.UserRepository;
import ferret.brass_b.accouting.dto.*;
import ferret.brass_b.accouting.dto.exceptions.*;
import ferret.brass_b.accouting.model.Exam;
import ferret.brass_b.accouting.model.Role;
import ferret.brass_b.accouting.model.UserAccount;
import ferret.brass_b.exam.dao.ExamRepository;
import ferret.brass_b.exam.dto.exception.ExamNotFoundException;
import ferret.brass_b.exam.model.ExamGlobal;
import ferret.brass_b.utils.PagedDataResponseDto;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Order(10)
public class UserAccountServiceImpl implements UserAccountService, CommandLineRunner {

    @Autowired
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final ExamRepository examRepository;

    @Override
    public Boolean registerTeacher(UserRegistrationRequestDto data, String pass) {
        if (userRepository.existsByUsernameIgnoreCase(data.getUsername())) {
            throw new UsernameAlreadyRegisteredException();
        } else if (userRepository.existsByEmail(data.getEmail())) {
            throw new EmailAlreadyRegisteredException();
        }
        UserAccount userAccount = modelMapper.map(data, UserAccount.class);
        userAccount.addRole(Role.TEACHER.name());
        userAccount.setCodeForEmail(pass);
        String passwd = passwordEncoder.encode(userAccount.getPassword());
        userAccount.setPassword(passwd);
        userRepository.save(userAccount);
        return true;
    }

    @Transactional
    @Override
    public String registerStudent(StudentRegisterDto data, String pass) {
        if (userRepository.existsByUsernameIgnoreCase(data.getUsername())) {
            throw new UsernameAlreadyRegisteredException();
        } else if (userRepository.existsByEmail(data.getEmail())) {
            throw new EmailAlreadyRegisteredException();
        }
        UserAccount userAccount = modelMapper.map(data, UserAccount.class);
        userAccount.addRole(Role.STUDENT.name());
        userAccount.setCodeForEmail(pass);
        String passwd = passwordEncoder.encode(userAccount.getPassword());
        userAccount.setPassword(passwd);
        userRepository.save(userAccount);
        return userAccount.getId();
    }

    @Override
    public UserResponseDto loginUser(UserLoginRequestDto data) {
        Optional<UserAccount> userAccount = userRepository.findByUsernameEquals(data.getUsername());
        if (userAccount.isPresent()) {
            try {
                UserAccount user = userAccount.get();
                boolean isPasswordsMatch = passwordEncoder.matches(data.getPassword(), user.getPassword());
                if (isPasswordsMatch) {
                    Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(),
                            passwordEncoder.encode(user.getPassword())
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    return modelMapper.map(user, UserResponseDto.class);
                } else {
                    throw new BadCredentialsOnLoginException();
                }
            } catch (BadCredentialsException e) {
                throw new BadCredentialsOnLoginException();
            }
        }
        throw new BadCredentialsOnLoginException();
    }

    @Override
    public List<Cookie> logoutUser() {
        List<Cookie> cookies = new ArrayList<>();
        cookies.add(resetCookie("access-token"));
        cookies.add(resetCookie("refresh-token"));
        return cookies;
    }

    @Override
    public UserResponseDto findStudentByNumberBook(String numberbook) {
        UserAccount userAccount = userRepository.findByNumberBook(numberbook).orElseThrow(UserNotFoundException::new);
        return modelMapper.map(userAccount, UserResponseDto.class);
    }

    @Override
    public UserResponseDto findUserByUsername(String username) {
        UserAccount userAccount = userRepository.findByUsernameIgnoreCase(username).orElseThrow(UserNotFoundException::new);
        return modelMapper.map(userAccount, UserResponseDto.class);
    }

    @Override
    public UserResponseDto findUserById(String userId) {
        UserAccount userAccount = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return modelMapper.map(userAccount, UserResponseDto.class);
    }

    @Transactional
    @Override
    public UserResponseDto deleteUserById(String userId) {
        UserAccount userAccount = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        userRepository.deleteById(userId);
        return modelMapper.map(userAccount, UserResponseDto.class);
    }

    @Override
    public PagedDataResponseDto<UserResponseDto> findAll(PageRequest pageRequest) {
        Page<UserAccount> userAccounts = userRepository.findAll(pageRequest);
        PagedDataResponseDto<UserResponseDto> pagedDataResponseDto = new PagedDataResponseDto<>();
        Page<UserResponseDto> pageMapped = userAccounts.map(u -> modelMapper.map(u, UserResponseDto.class));
        pagedDataResponseDto.setData(pageMapped.getContent());
        pagedDataResponseDto.setTotalElements(pageMapped.getTotalElements());
        pagedDataResponseDto.setTotalPages(pageMapped.getTotalPages());
        pagedDataResponseDto.setCurrentPage(pageMapped.getNumber());
        return pagedDataResponseDto;
    }

    @Override
    public Iterable<UserResponseDto> findAllAll() {
        List<UserAccount> userAccounts =  userRepository.findAll();
        List<UserResponseDto> allUsers = userAccounts.stream().map(u -> modelMapper.map(u, UserResponseDto.class)).toList();
        return allUsers;
    }

    @Transactional
    @Override
    public UserResponseDto updateUser(String userId, UserEditRequestDto data) {
        UserAccount userAccount = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if (data.getFirstName() != null && !data.getFirstName().equals(userAccount.getFirstName())) {
            userAccount.setFirstName(data.getFirstName());
        }

        if (data.getLastName() != null && !data.getLastName().equals(userAccount.getLastName())) {
            userAccount.setLastName(data.getLastName());
        }
        if (data.getCity() != null && !data.getCity().equals(userAccount.getCity())) {
            userAccount.setCity(data.getCity());
        }
        if (data.getImage() != null && !data.getImage().equals(userAccount.getImage())) {
            userAccount.setImage(data.getImage());
        }
        if (data.getTelefon() != null && !data.getTelefon().equals(userAccount.getTelefon())) {
            userAccount.setTelefon(data.getTelefon());
        }
        if (data.getGroup() != null && !data.getGroup().equals(userAccount.getGroup())) {
            userAccount.setGroup(data.getGroup());
        }
        if (data.getSpeciality() != null && !data.getSpeciality().equals(userAccount.getSpeciality())) {
            userAccount.setSpeciality(data.getSpeciality());
        }
        userRepository.save(userAccount);
        return modelMapper.map(userAccount, UserResponseDto.class);
    }

    @Override
    public RolesDto changeRoleList(String userId, String roleName, Boolean isAddRole) {
        UserAccount userAccount = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        roleName = roleName.toUpperCase();
        boolean res;
        try {
            if (isAddRole) {
                res = userAccount.addRole(roleName);
            } else {
                res = userAccount.removeRole(roleName);
            }
        } catch (Exception e) {
            throw new RoleNotFoundException();
        }
        if (res) {
            userRepository.save(userAccount);
        }
        return modelMapper.map(userAccount, RolesDto.class);
    }

    @Override
    public UserResponseDto changeGroup(String userId, String group) {
        UserAccount userAccount = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        userAccount.setGroup(group);
        userRepository.save(userAccount);
        return modelMapper.map(userAccount, UserResponseDto.class);
    }

    @Override
    public Iterable<UserResponseDto> findUsersByGroup(String group) {
        return userRepository.findUsersByGroup(group)
                .map(userAccount -> modelMapper.map(userAccount, UserResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto changeSpeciality(String userId, String speciality) {
        UserAccount userAccount = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        userAccount.setSpeciality(speciality);
        userRepository.save(userAccount);
        return modelMapper.map(userAccount, UserResponseDto.class);
    }

    @Override
    public Iterable<UserResponseDto> findUsersBySpeciality(String speciality) {
        return userRepository.findUsersBySpeciality(speciality)
                .map(userAccount -> modelMapper.map(userAccount, UserResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void changePassword(String userId, String password) {
        if (!isValidPassword(password)) {
            throw new PasswordValidationException();
        }

        UserAccount userAccount = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        userAccount.setPassword(passwordEncoder.encode(password));
        userRepository.save(userAccount);
    }

    @Override
    public Iterable<UserResponseDto> getAllUsers() {
        Iterable<UserResponseDto> pageMapped = userRepository.findAll()
                .stream().map(u -> modelMapper.map(u, UserResponseDto.class))
                .collect(Collectors.toList());
        return pageMapped;
    }

    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!$#%])[A-Za-z\\d!$#%]{8,}$");
    }

    @Override
    public void uploadAvatar(String userId, MultipartFile file) {
        String fileName = file.getOriginalFilename();
    }

    @Override
    public void removeAvatar(String userId) {

    }

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByUsernameIgnoreCase("admin")) {
            String password = passwordEncoder.encode("admin");
            UserAccount userAccount = new UserAccount("admin", password, "admin", "admin", "---", "---", "---", "---", "---", "---");
            userAccount.addRole(Role.MODERATOR.toString());
            userAccount.addRole(Role.ADMINISTRATOR.toString());
            userRepository.save(userAccount);
        }
    }

    private Cookie resetCookie(String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        return cookie;
    }

    @Override
    public Boolean addMaterial(String userId, MaterialsDto material) {
        UserAccount user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if(user.getMaterials().entrySet().equals(material)) {
            return false;
        } else {
            boolean res = user.addMaterial(material.getMaterialName(), material.getLink());
            userRepository.save(user);
            return res;
        }
    }

    @Override
    public Boolean removeMaterial(String userId, MaterialsDto material) {
        UserAccount user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        boolean res = user.removeMaterial(material.getMaterialName());
        userRepository.save(user);
        return res;
    }

    @Override
    public UserResponseDto changeEmail(String userId, String password) {
        UserAccount user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if(password.equals(user.getCodeForEmail())) {
            user.setSetEmailActivated();
            userRepository.save(user);
            return modelMapper.map(user, UserResponseDto.class);
        } else {
            return null;
        }
    }

    @Override
    public UserResponseDto addExam(String userId, ExamDto exam) {
        UserAccount userAccount = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        ExamGlobal examGlobal = examRepository.findById(exam.getExamId()).orElseThrow(ExamNotFoundException::new);
        Exam exm = new Exam(exam.getExamId(), exam.getExam(), exam.getScore(), LocalDate.now(), exam.getTeacher());
        userAccount.addProgres(exm);
        userAccount.editExamFlags(exam.getExamId(), true);
        userRepository.save(userAccount);
        return modelMapper.map(userAccount, UserResponseDto.class);
    }

    @Override
    public UserResponseDto removeExam(String userId, ExamDto examDto) {
        UserAccount user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Exam exam = modelMapper.map(examDto, Exam.class);
        user.removeProgres(exam);
        user.editExamFlags(exam.getExamId(), false);
        userRepository.save(user);
        return modelMapper.map(user, UserResponseDto.class);
    }

    @Override
    public UserResponseDto editExam(String userId, ExamDto examDto) {
        UserAccount user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Exam exam = modelMapper.map(examDto, Exam.class);
        user.editProgres(exam);
        user.editExamFlags(exam.getExamId(), true);
        userRepository.save(user);
        return modelMapper.map(user, UserResponseDto.class);
    }

    @Override
    public Boolean examDistribution(String group, String examId) {
        ExamGlobal exam = examRepository.findById(examId).orElseThrow(ExamNotFoundException::new);
        Iterable<UserAccount> users = userRepository.findUsersByGroup(group)
                .filter(userAccount -> userAccount.getGroup().equals(group))
                .toList();
        users.forEach(u -> u.addExamFlags(examId));
        users.forEach(userRepository::save);
        return true;
    }

    @Override
    public Boolean examFlagByUser(String userId, String examId) {
        UserAccount user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        ExamGlobal exam = examRepository.findById(examId).orElseThrow(ExamNotFoundException::new);
        user.addExamFlags(exam.getId());
        userRepository.save(user);
        return true;
    }

    @Override
    public Boolean deleteExamFlag(String userId, String examId) {
        UserAccount user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        ExamGlobal exam = examRepository.findById(examId).orElseThrow(ExamNotFoundException::new);
        user.removeExamFlags(examId);
        userRepository.save(user);
        return true;
    }
}
