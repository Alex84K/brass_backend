package ferret.brass_b.exam.service;

import ferret.brass_b.accouting.dao.UserRepository;
import ferret.brass_b.accouting.dto.UserResponseDto;
import ferret.brass_b.accouting.model.Exam;
import ferret.brass_b.accouting.model.UserAccount;
import ferret.brass_b.exam.dao.ExamRepository;
import ferret.brass_b.exam.dto.ExamCreatedGlobalDto;
import ferret.brass_b.exam.dto.ExamGlobalDto;
import ferret.brass_b.exam.dto.exception.ExamNotFoundException;
import ferret.brass_b.exam.model.ExamGlobal;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService{

    private final ExamRepository examRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Override
    public ExamGlobalDto addExam(ExamCreatedGlobalDto exam) {
        ExamGlobal examGlobal = modelMapper.map(exam, ExamGlobal.class);
        examGlobal.setDataCreated(LocalDate.now());
        examRepository.save(examGlobal);
        Exam exm = new Exam(examGlobal.getId(), exam.getExamName(), 0, examGlobal.getDataCreated(), examGlobal.getTeacher());
        Iterable<UserAccount> users = userRepository.findUsersByGroup(exam.getGroup())
                .filter(userAccount -> userAccount.getGroup().equals(exam.getGroup()))
                .toList();
        users.forEach(u -> u.addExamFlags(exm));
        users.forEach(userRepository::save);
        return modelMapper.map(examGlobal, ExamGlobalDto.class);
    }

    @Override
    public ExamGlobalDto findExamById(String examId) {
        ExamGlobal examGlobal = examRepository.findById(examId).orElseThrow(ExamNotFoundException::new);
        return modelMapper.map(examGlobal, ExamGlobalDto.class);
    }

    @Override
    public ExamGlobalDto deleteExamById(String examId) {
        ExamGlobal examGlobal = examRepository.findById(examId).orElseThrow(ExamNotFoundException::new);
        Exam e = new Exam(examId, "", 0, LocalDate.now(), "");
        examRepository.deleteById(examId);

        userRepository.findAll()
                .stream()
                .peek(userAccount -> userAccount.removeExamFlags(e))
                .forEach(userRepository::save);
        return modelMapper.map(examGlobal, ExamGlobalDto.class);
    }

    @Override
    public ExamGlobalDto editExamById(String examId, ExamCreatedGlobalDto exam) {
        ExamGlobal examGlobal = examRepository.findById(examId).orElseThrow(ExamNotFoundException::new);
        examGlobal.setExamName(exam.getExamName());
        examGlobal.setGroup(exam.getGroup());
        examGlobal.setTeacher(exam.getTeacher());
        examRepository.save(examGlobal);
        //delete for user
        Exam e = new Exam(examId, "", 0, LocalDate.now(), "");
        userRepository.findAll()
                .stream()
                .peek(userAccount -> userAccount.removeExamFlags(e))
                .forEach(userRepository::save);
        //create & distribution
        Exam exm = new Exam(examGlobal.getId(), exam.getExamName(), 0, examGlobal.getDataCreated(), examGlobal.getTeacher());
        Iterable<UserAccount> users = userRepository.findUsersByGroup(exam.getGroup())
                .filter(userAccount -> userAccount.getGroup().equals(exam.getGroup()))
                .toList();
        users.forEach(u -> u.addExamFlags(exm));
        users.forEach(userRepository::save);
        return modelMapper.map(examGlobal, ExamGlobalDto.class);
    }

    @Override
    public Iterable<ExamGlobalDto> getAllExam() {
        List<ExamGlobal> exams = examRepository.findAll();
        List<ExamGlobalDto> examsDto = exams.stream().map(e -> modelMapper.map(e, ExamGlobalDto.class)).toList();
        return examsDto;
    }

    @Override
    public Iterable<ExamGlobalDto> findExamsByGroupAndYear(String group, String year) {
        return null;
    }

    @Override
    public Iterable<ExamGlobalDto> findExamsByYear(String year) {
        return null;
    }


}
