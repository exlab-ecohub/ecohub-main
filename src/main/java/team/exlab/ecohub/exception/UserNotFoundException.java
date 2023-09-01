package team.exlab.ecohub.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super(String.format("User with username %s not found", username));
    }
}
