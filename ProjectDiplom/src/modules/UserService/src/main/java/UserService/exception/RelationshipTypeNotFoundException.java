package UserService.exception;

public class RelationshipTypeNotFoundException extends RuntimeException {
    public RelationshipTypeNotFoundException(String typeName) {
        super("Relationship type not found: " + typeName);
    }
}
