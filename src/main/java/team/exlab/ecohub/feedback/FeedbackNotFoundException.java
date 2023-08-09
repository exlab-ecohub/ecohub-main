package team.exlab.ecohub.feedback;

public class FeedbackNotFoundException extends RuntimeException{
    public FeedbackNotFoundException(Long id) {
        super("Feedback with id = " + id + " not found");
    }
}
