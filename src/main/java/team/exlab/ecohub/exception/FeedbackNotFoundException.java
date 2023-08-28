package team.exlab.ecohub.exception;

public class FeedbackNotFoundException extends RuntimeException{
    public FeedbackNotFoundException(Long feedbackId) {
        super(String.format("Feedback with id = %s not found", feedbackId));
    }
}
