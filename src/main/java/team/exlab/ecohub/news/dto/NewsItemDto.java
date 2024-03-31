package team.exlab.ecohub.news.dto;

import lombok.*;
import team.exlab.ecohub.news.model.Attachment;
import team.exlab.ecohub.news.model.ENewsItemType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class NewsItemDto {
    private String header;
    private String text;
    private LocalDateTime publicationDate;
    private Set<Attachment> imageAttachment;
    private String linkToSource;
    private Set<String> keywords;
    private ENewsItemType type;
    private boolean displayed;
}
