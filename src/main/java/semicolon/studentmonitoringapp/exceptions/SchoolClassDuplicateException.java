package semicolon.studentmonitoringapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import semicolon.studentmonitoringapp.dtos.request.CreateClassRequestDto;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SchoolClassDuplicateException extends RuntimeException {
    public SchoolClassDuplicateException(CreateClassRequestDto createClassRequestDto) {
        super(String.format("Class with name %s and academic year %s already exists",
                createClassRequestDto.getClassName(), createClassRequestDto.getAcademicYear()));
    }
}
