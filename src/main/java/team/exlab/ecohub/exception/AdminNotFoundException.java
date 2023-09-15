package team.exlab.ecohub.exception;

public class AdminNotFoundException extends RuntimeException {
    public AdminNotFoundException(Long adminId) {
        super(String.format("Admin with id %s not found", adminId));
    }
}
