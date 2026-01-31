package semicolon.studentmonitoringapp.utils.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import semicolon.studentmonitoringapp.utils.validators.TrimmedValidator;

import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = TrimmedValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Trimmed {

    String message() default "no trailing spaces allowed";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
