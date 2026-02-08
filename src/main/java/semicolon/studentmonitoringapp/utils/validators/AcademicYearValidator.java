package semicolon.studentmonitoringapp.utils.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import semicolon.studentmonitoringapp.utils.annotations.ValidAcademicYear;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AcademicYearValidator
        implements ConstraintValidator<ValidAcademicYear, String> {


    private static final Pattern SINGLE_PATTERN
            = Pattern.compile("^\\d{4}$");

    private static final Pattern RANGE_YEAR =
            Pattern.compile("^(\\d{4})/(\\d{4})$");


    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

        if(value == null || value.isBlank()) return true;

        if(SINGLE_PATTERN.matcher(value).matches()) return true;

        Matcher matcher = RANGE_YEAR.matcher(value);
        if (matcher.matches()) {
            int start = Integer.parseInt(matcher.group(1));
            int end = Integer.parseInt(matcher.group(2));

            return end == start + 1;
        }

        return false;
    }
}
