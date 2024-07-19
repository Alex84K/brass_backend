package ferret.brass_b.exam.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "exams")
@Getter
@NoArgsConstructor
public class ExamGlobal {
    @Id
    String id;
    @Setter
    String examName;
    @Setter
    String group;
    @Setter
    String teacher;
    @Setter
    LocalDate dataCreated;

    public ExamGlobal( String examName, String group, String teacher) {
        this.examName = examName;
        this.group = group;
        this.teacher = teacher;
        this.dataCreated = LocalDate.now();
    }
}
