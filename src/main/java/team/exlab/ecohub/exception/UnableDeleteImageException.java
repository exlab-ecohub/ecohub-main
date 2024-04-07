package team.exlab.ecohub.exception;

public class UnableDeleteImageException extends RuntimeException {
    public UnableDeleteImageException(String filePath) {
        super("Fail to delete following file: " + filePath);
    }
}
