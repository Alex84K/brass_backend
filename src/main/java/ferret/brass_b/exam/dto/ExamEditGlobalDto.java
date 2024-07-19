package ferret.brass_b.exam.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ExamEditGlobalDto {
    String id;
    String examName;
    String group;
    String teacher;
}
