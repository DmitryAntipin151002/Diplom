package UserService.exception;

public class InvalidRelationshipTypeException extends RuntimeException {
    public InvalidRelationshipTypeException(String message) {
        super(message);
    }
}
