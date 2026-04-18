package zampano.ai.zampanoreminder.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String entityName, Long id) {
        super(entityName + " not found: " + id);
    }
}
