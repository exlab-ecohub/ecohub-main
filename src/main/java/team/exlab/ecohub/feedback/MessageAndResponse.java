//package team.exlab.ecohub.feedback;
//
//import lombok.*;
//import org.springframework.format.annotation.DateTimeFormat;
//
//import javax.persistence.*;
//import java.time.LocalDateTime;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "messages")
//public class MessageAndResponse {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "message_id")
//    private Long messageId;
//
//    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
//    @JoinColumn(name = "feedback_id", nullable = false)
//    private Feedback feedback;
//
//    @Column(name = "message_content")
//    private String messageContent;
//    @Column(name = "message_time")
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm")
//    private LocalDateTime messageTime;
//    @Column (name = "response_content")
//    private String responseContent;
//    @Column(name = "response_time")
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm")
//    private LocalDateTime responseTime;
//
//}
