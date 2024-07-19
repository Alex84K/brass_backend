package ferret.brass_b.exam.dao;

import ferret.brass_b.exam.model.ExamGlobal;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExamRepository extends MongoRepository<ExamGlobal, String> {
}
