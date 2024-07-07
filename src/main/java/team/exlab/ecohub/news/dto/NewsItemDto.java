package team.exlab.ecohub.news.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.URL;
import team.exlab.ecohub.news.model.Attachment;
import team.exlab.ecohub.news.model.ENewsItemType;
import team.exlab.ecohub.news.validation.ContainersNumber;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class NewsItemDto {
	@NotEmpty(message = "field must not be empty")
	@Size(max = 100, message = "should be less than 100 characters")
	private String header;
	@NotEmpty(message = "field must not be empty")
	@Size(max = 15000, message = "should be less than 15000 characters")
	private String text;
	private LocalDateTime publicationDate;
	@NotEmpty(message = "field must not be empty")
	private Set<Attachment> imageAttachment;
	@URL(regexp = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)", message = "Not valid URL!")
	private String linkToSource;
	@NotEmpty(message = "field must not be empty")
	@ContainersNumber
	private Set<String> keywords;
	@NotEmpty(message = "field must not be empty")
	private ENewsItemType type;
	@NotEmpty(message = "field must not be empty")
	private boolean displayed;
}
