package UserService.exception;

public class RelationshipStatusNotFoundException extends RuntimeException {
    public RelationshipStatusNotFoundException(String statusName) {
        super("Relationship status not found: " + statusName);
    }
}