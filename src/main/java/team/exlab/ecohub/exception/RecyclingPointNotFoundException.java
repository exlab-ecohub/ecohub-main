package team.exlab.ecohub.exception;

public class RecyclingPointNotFoundException extends RuntimeException {
    public RecyclingPointNotFoundException(Long pointId) {
        super(String.format("Точка переработки отходов с id=%s не найдена", pointId));
    }
}
