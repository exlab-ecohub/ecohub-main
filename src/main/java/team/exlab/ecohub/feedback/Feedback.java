package team.exlab.ecohub.feedback;

import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "feedbacks")
@Builder
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private Long feedbackId;
    @Column(name = "email")
    private String email;
    @Column(name = "message_topic")
    private String messageTopic;
    @Column(name = "message_content")
    private String messageContent;
    @Column(name = "message_time")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime messageTime;
    @Column(name = "admin_id")
    private Long adminId;
    @Column(name = "response_content")
    private String responseContent;
    @Column(name = "response_time")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime responseTime;
    @Column(name = "response_status")
    @Enumerated(EnumType.STRING)
    private ResponseStatus responseStatus;
}
