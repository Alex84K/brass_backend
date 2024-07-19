package ferret.brass_b.accouting.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ExamDto {
    String examId;
    @Getter
    String exam;
    @Getter
    Integer score;
    @Getter
    LocalDate data;
    @Getter
    String teacher;
}
