package semicolon.studentmonitoringapp.utils.annotations;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import semicolon.studentmonitoringapp.utils.validators.ExistsInDbValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistsInDbValidator.class)
public @interface ExistsInDb {
    Class<?> entity();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String field();

    String message() default "Value already exists in DB";
}
