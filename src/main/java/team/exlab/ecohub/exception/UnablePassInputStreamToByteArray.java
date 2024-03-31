package team.exlab.ecohub.exception;

public class UnablePassInputStreamToByteArray extends RuntimeException {
    public UnablePassInputStreamToByteArray(String fileToByteArray) {
        super(String.format("Unable to convert following file \"%s\" to ByteArray and send it to user", fileToByteArray));
    }
}
