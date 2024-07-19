package ferret.brass_b.exam.service;

import ferret.brass_b.exam.dto.ExamCreatedGlobalDto;
import ferret.brass_b.exam.dto.ExamGlobalDto;

public interface ExamService {
    ExamGlobalDto addExam(ExamCreatedGlobalDto exam);
    ExamGlobalDto findExamById(String examId);
    ExamGlobalDto deleteExamById(String examId);
    ExamGlobalDto editExamById(String examId, ExamCreatedGlobalDto exam);
    Iterable<ExamGlobalDto> getAllExam();
    Iterable<ExamGlobalDto> findExamsByGroupAndYear(String group, String year);
    Iterable<ExamGlobalDto> findExamsByYear(String year);
}
