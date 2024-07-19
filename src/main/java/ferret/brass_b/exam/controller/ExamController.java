package ferret.brass_b.exam.controller;

import ferret.brass_b.exam.dto.ExamCreatedGlobalDto;
import ferret.brass_b.exam.dto.ExamGlobalDto;
import ferret.brass_b.exam.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/exams")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @PostMapping("")
    public ExamGlobalDto addExam(@RequestBody ExamCreatedGlobalDto exam) {
        return examService.addExam(exam);
    }

    @GetMapping("/{examId}")
    public ExamGlobalDto findExamById(@PathVariable String examId) {
        return examService.findExamById(examId);
    }

    @DeleteMapping("/{examId}")
    public ExamGlobalDto deleteExamById(@PathVariable String examId) {
        return examService.deleteExamById(examId);
    }

    @PutMapping("/{examId}")
    public ExamGlobalDto editExamById(@PathVariable String examId, @RequestBody ExamCreatedGlobalDto exam) {
        return examService.editExamById(examId, exam);
    }

    @GetMapping("/all")
    public Iterable<ExamGlobalDto> getAllExam() {
        return examService.getAllExam();
    }

    @GetMapping("/group/{group}/years/{year}")
    public Iterable<ExamGlobalDto> findExamsByGroupAndYear(@PathVariable String group, @PathVariable String year) {
        return examService.findExamsByGroupAndYear(group, year);
    }

    @GetMapping("/years/{year}")
    public Iterable<ExamGlobalDto> findExamsByYear(@PathVariable String year) {
        return examService.findExamsByYear(year);
    }
}
