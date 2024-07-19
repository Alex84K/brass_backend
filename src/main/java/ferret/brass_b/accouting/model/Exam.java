package ferret.brass_b.accouting.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Exam {
    String examId;
    @Setter
    String exam;
    @Setter
    Integer score;
    @Setter
    LocalDate data;
    @Setter
    String teacher;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exam exam = (Exam) o;
        return Objects.equals(examId, exam.examId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(examId);
    }
}
