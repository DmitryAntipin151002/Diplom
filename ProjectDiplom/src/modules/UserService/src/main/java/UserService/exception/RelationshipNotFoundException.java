package UserService.exception;

public class RelationshipNotFoundException extends RuntimeException {
    public RelationshipNotFoundException(Long relationshipId) {
        super("Relationship not found with ID: " + relationshipId);
    }
}