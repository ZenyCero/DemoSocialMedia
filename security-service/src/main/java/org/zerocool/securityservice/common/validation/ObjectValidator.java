package org.zerocool.securityservice.common.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.zerocool.securityservice.common.exception.CustomException;

import java.util.Set;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class ObjectValidator {

    private final Validator validator;

    /**
     * Metodo que validara si el objeto obtenido de la peticion es igual al esperado
     * @param object
     * @return el objeto validado si no se encuentran errores.
     * @throws CustomException: si se encuentran violaciones de las restricciones de validación,
     * con un mensaje que detalla las violaciones encontradas.
     */
    @SneakyThrows
    public <T> T validate(T object){
        Set<ConstraintViolation<T>> errors = validator.validate(object);
        if (errors.isEmpty())
            return object;
        else {
            String message = errors.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", "));
            throw  new CustomException(message, HttpStatus.BAD_REQUEST);
        }
    }
}
