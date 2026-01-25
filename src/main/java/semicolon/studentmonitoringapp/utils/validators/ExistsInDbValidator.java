package semicolon.studentmonitoringapp.utils.validators;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import semicolon.studentmonitoringapp.utils.annotations.ExistsInDb;

import java.lang.annotation.Annotation;

@Component
@RequiredArgsConstructor
public class ExistsInDbValidator implements
        ConstraintValidator<ExistsInDb, String> {

    private final EntityManager entityManager;

    private Class<?> entityClass;
    private String field;


    @Override
    public void initialize(ExistsInDb constraintAnnotation) {
        this.entityClass = constraintAnnotation.entity();
        this.field = constraintAnnotation.field();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if(value == null) return true;

        String query = String.format("SELECT COUNT(e) FROM %s e WHERE e.%s = :value",
                entityClass.getSimpleName(),
                field
        );

        Long count = entityManager.createQuery(query, Long.class)
                .setParameter("value", value)
                .getSingleResult();

        return count == 0;
    }




}
