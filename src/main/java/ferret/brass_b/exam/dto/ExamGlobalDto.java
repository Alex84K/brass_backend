package ferret.brass_b.exam.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExamGlobalDto {
    String id;
    String examName;
    String group;
    String teacher;
    LocalDate dataCreated;
}
