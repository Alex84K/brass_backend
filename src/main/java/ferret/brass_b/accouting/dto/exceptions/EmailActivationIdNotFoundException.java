package ferret.brass_b.accouting.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmailActivationIdNotFoundException extends RuntimeException {
}
