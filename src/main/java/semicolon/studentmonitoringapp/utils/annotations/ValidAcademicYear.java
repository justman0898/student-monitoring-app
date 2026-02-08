package semicolon.studentmonitoringapp.utils.annotations;

import jakarta.validation.Payload;

public @interface ValidAcademicYear {

    String message() default "Invalid Academic year format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}
