package team.exlab.ecohub.feedback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "feedbacks")
@Builder
public class Feedback1 {
    @Id
    @GeneratedValue
    @Column(name = "feedback_id")
    private Long feedbackId;
    private String email;
    @Column(name = "message_topic")
    private String messageTopic;
    @Column(name = "message_content")
    private String messageContent;
    @Column(name = "message_time")
    private LocalDateTime messageTime;
    @Column(name = "admin_id")
    private Long adminId;
    @Column(name = "response_content")
    private String responseContent;
    @Column(name = "response_time")
    private LocalDateTime responseTime;
    @Column(name = "response_status")
    private ResponseStatus responseStatus;
}
