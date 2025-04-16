package UserService.exception;



public class RelationshipExistsException extends RuntimeException {
    public RelationshipExistsException() {
        super("Relationship already exists");
    }
}