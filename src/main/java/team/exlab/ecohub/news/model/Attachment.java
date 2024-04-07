package team.exlab.ecohub.news.model;

import lombok.*;

import javax.persistence.*;
import java.net.URL;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "attachments")
@Builder
public class Attachment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String extension;
	private URL attachmentPath;
}
