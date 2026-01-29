package semicolon.studentmonitoringapp.exceptions;

import semicolon.studentmonitoringapp.dtos.request.CreateClassRequestDto;

public class SchoolClassDuplicateException extends RuntimeException {
    public SchoolClassDuplicateException(CreateClassRequestDto createClassRequestDto) {
        super(String.format("Class with name %s and academic year %s already exists",
                createClassRequestDto.getClassName(), createClassRequestDto.getAcademicYear()));
    }
}
