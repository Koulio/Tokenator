package org.tokenator.opentokenizer;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.tokenator.opentokenizer.util.DateSerializer;

import java.util.Date;


@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Entity not found")  // 404
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Class<?> entityClass, Long id) {
        super(buildMessage(entityClass, id));
    }

    public EntityNotFoundException(Class<?> entityClass, String pan, Date expr) {
        super(buildMessage(entityClass, pan, expr));
    }

    private static String buildMessage(Class<?> entityClass, Long id) {
        StringBuilder sb = new StringBuilder(entityClass.getSimpleName());
        sb.append(" with id ");
        sb.append(id);
        sb.append(" not found");
        return sb.toString();
    }

    private static String buildMessage(Class<?> entityClass, String pan, Date expr) {
        StringBuilder sb = new StringBuilder(entityClass.getSimpleName());
        sb.append(" with pan='");
        sb.append(pan);
        sb.append(" and expr=");
        sb.append(DateSerializer.convert(expr));
        sb.append(" not found");
        return sb.toString();
    }
}