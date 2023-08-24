package team.exlab.ecohub.exception;

public class RecyclingPointNotFoundException extends RuntimeException {
    public RecyclingPointNotFoundException(Long pointId) {
        super(String.format("Recycling point with id=%s not found", pointId));
    }
}
