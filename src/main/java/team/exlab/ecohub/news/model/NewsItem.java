package team.exlab.ecohub.news.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "news")
@Builder
public class NewsItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String header;
	private String text;
	@ManyToMany
	@JoinTable(
			name = "news_attachments",
			joinColumns = @JoinColumn(name = "news_id"),
			inverseJoinColumns = @JoinColumn(name = "attachment_id"))
	private Set<Attachment> imageAttachment;
	private LocalDateTime publicationDate;
	private String linkToSource;
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
	@CollectionTable(
			name = "keywords",
			joinColumns = @JoinColumn(name = "news_id"))
	@Column(name = "keyword")
	private Set<String> keywords;
	@Enumerated(EnumType.STRING)
	private ENewsItemType type;
	private boolean displayed;
}
