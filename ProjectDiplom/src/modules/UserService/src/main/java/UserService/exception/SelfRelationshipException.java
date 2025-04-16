// SelfRelationshipException.java
package UserService.exception;

public class SelfRelationshipException extends RuntimeException {
    public SelfRelationshipException() {
        super("Cannot create relationship with yourself");
    }
}