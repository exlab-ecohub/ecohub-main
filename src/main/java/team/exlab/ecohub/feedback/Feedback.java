package team.exlab.ecohub.feedback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "feedbacks")
@Builder
public class Feedback {
    @Id
    @GeneratedValue
    private Long feedback_id;
    private String email;
    private String message_topic;
    private String message_content;
    private LocalDateTime message_time;
    private Long admin_id;
    private StringBuilder response_content;
    private LocalDateTime response_time;
    private ResponseStatus response_status;
}
