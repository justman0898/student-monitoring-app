package semicolon.studentmonitoringapp.utils.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import semicolon.studentmonitoringapp.utils.annotations.Trimmed;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

@Component
public class TrimmedValidator implements ConstraintValidator<Trimmed, String> {


    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(s == null) return true;

        String trimmed = s.trim();
        return trimmed.equals(s);
    }
}
