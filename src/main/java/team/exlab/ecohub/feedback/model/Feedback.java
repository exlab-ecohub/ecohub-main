package team.exlab.ecohub.feedback.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import team.exlab.ecohub.user.model.User;

import javax.persistence.*;
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
    @Column(name = "id")
    private Long id;
    @Column(name = "user_feedback_count")
    private int userFeedbackCount;
    @Column(name = "name")
    private String name;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "admin_id")
    private User admin;
    @Column(name = "email")
    private String email;
    @Column(name = "message_topic")
    @Enumerated(EnumType.STRING)
    private MessageTopic messageTopic;
    @Column(name = "message_content")
    private String messageContent;
    @Column(name = "message_time")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime messageTime;
    @Column(name = "response_content")
    private String responseContent;
    @Column(name = "response_time")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime responseTime;
    @Column(name = "response_status")
    @Enumerated(EnumType.STRING)
    private ResponseStatus responseStatus;
}
