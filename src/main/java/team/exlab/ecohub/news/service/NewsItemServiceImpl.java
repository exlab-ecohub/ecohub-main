package team.exlab.ecohub.news.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.provider.HibernateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.exlab.ecohub.exception.AttachmentUploadException;
import team.exlab.ecohub.exception.NewsItemNotFoundException;
import team.exlab.ecohub.exception.UnableCreateDirectoryException;
import team.exlab.ecohub.exception.UnablePassInputStreamToByteArray;
import team.exlab.ecohub.news.dto.NewsItemDto;
import team.exlab.ecohub.news.dto.NewsItemMapper;
import team.exlab.ecohub.news.model.Attachment;
import team.exlab.ecohub.news.model.ENewsItemType;
import team.exlab.ecohub.news.model.NewsItem;
import team.exlab.ecohub.news.repository.AttachmentRepository;
import team.exlab.ecohub.news.repository.NewsItemRepository;
import team.exlab.ecohub.pageable.OffsetLimitPageable;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsItemServiceImpl implements NewsItemService {
    private final Environment environment;
    private final NewsItemRepository newsItemRepository;
    private final AttachmentRepository attachmentRepository;

    @Override
    @Transactional
    public NewsItemDto addNewsItem(NewsItemDto newsItemDto, MultipartFile imageAttachment) {
        NewsItem newsItemToSave = NewsItem.builder()
                .header(newsItemDto.getHeader())
                .text(newsItemDto.getText())
                .publicationDate(LocalDateTime.now())
                .linkToSource(newsItemDto.getLinkToSource())
                .keywords(newsItemDto.getKeywords())
                .type(newsItemDto.getType())
                .displayed(true)
                .build();
        Optional<Attachment> attachment = saveImage(imageAttachment);
        if (attachment.isPresent()) {
            attachmentRepository.save(attachment.get());
            newsItemToSave.setImageAttachment(Collections.singleton(attachment.get()));
        }
        return NewsItemMapper.toDto(newsItemRepository.save(newsItemToSave));
    }

    @Override
    @Transactional
    public NewsItemDto editNewsItem(Long newsId, NewsItemDto newsItemDto, MultipartFile attachment) {
        NewsItem newsItemToEdit = newsItemRepository.findById(newsId).orElseThrow(() -> new NewsItemNotFoundException(newsId));
        Set<Attachment> set = new HashSet<>();
        if (!attachment.isEmpty()) {
            deleteImage(newsItemToEdit.getImageAttachment());
            Optional<Attachment> newAttachment = saveImage(attachment);
            set.add(newAttachment.get());
            attachmentRepository.save(newAttachment.get());
            newsItemToEdit.setImageAttachment(set);
        }
        newsItemToEdit.setHeader(newsItemDto.getHeader());
        newsItemToEdit.setText(newsItemDto.getText());
        newsItemToEdit.setLinkToSource(newsItemDto.getLinkToSource());
        newsItemToEdit.setType(newsItemDto.getType());
        newsItemToEdit.setDisplayed(newsItemDto.isDisplayed());
        newsItemToEdit.setKeywords(newsItemDto.getKeywords());
        return NewsItemMapper.toDto(newsItemRepository.save(newsItemToEdit));
    }

    @Override
    @Transactional
    public List<NewsItemDto> getAllNews(Integer from, Integer size, Boolean displayed, ENewsItemType type) {
        List<NewsItemDto> newsItemDtos;
        from = from >= 1 ? from - 1 : from;
        if (size == 0 && newsItemRepository.count() > from) {
            size = (int) newsItemRepository.count() - from;
        } else if (size == 0 && newsItemRepository.count() <= from) {
            size = 1;
        }
        if (displayed) {
            newsItemDtos = newsItemRepository.findAllByDisplayedAndTypeOrderById(OffsetLimitPageable.of(from, size), true, type).stream()
                    .map(NewsItemMapper::toDto).collect(Collectors.toList());
        } else {
            newsItemDtos = newsItemRepository.findAllByTypeOrderById(OffsetLimitPageable.of(from, size), type).stream()
                    .map(NewsItemMapper::toDto).collect(Collectors.toList());
        }
        return newsItemDtos;
    }

    @Override
    @Transactional
    public NewsItemDto getNewsItem(Long newsId) {
        return NewsItemMapper.toDto(newsItemRepository.findById(newsId).orElseThrow(() -> new NewsItemNotFoundException(newsId)));
    }

    @Override
    @Transactional
    public byte[] getAttachment(String attachmentURL) {
//        Path uploadPath = Paths.get(Objects.requireNonNull(environment.getProperty("resources.images-directory")), "files", "news", "images");
//        Path filePath = Paths.get(uploadPath.toString(), attachmentURL);
//
//        try (InputStream is = new FileInputStream(filePath.toUri().toURL().toString().split(":", 2)[1].replace("%20", " "))) {
//        try (InputStream is = new FileInputStream(attachmentURL)) {
        try (InputStream is = new FileInputStream(attachmentURL.replace("%20", " "))) {
            return IOUtils.toByteArray(is);
        } catch (IOException e) {
            throw new UnablePassInputStreamToByteArray(attachmentURL);
        }
    }

    @Override
    public Collection<String> getKeywordsBySubstring(String keywordSubstring) {
        return newsItemRepository.findAll().stream().map(NewsItem::getKeywords).flatMap(Collection::stream)
                .filter(s -> s.contains(keywordSubstring)).collect(Collectors.toSet());
    }

    @Override
    public Collection<NewsItemDto> getAllNewsWithSpecifiedKeywords(String[] keywords) {
        return newsItemRepository.findAll().stream().filter(n -> !Collections.disjoint(n.getKeywords(), Set.of(keywords)))
                .map(NewsItemMapper::toDto).collect(Collectors.toList());
    }

    private Optional<Attachment> saveImage(MultipartFile imageAttachment) {
        if (!imageAttachment.isEmpty()) {
            Path uploadPath = Paths.get(Objects.requireNonNull(environment.getProperty("resources.images-directory")), "files", "news", "images");
            if (!Files.exists(uploadPath)) {
                try {
                    Files.createDirectories(uploadPath);
                } catch (IOException e) {
                    throw new UnableCreateDirectoryException("Fail to create directory: " + uploadPath);
                }
            }
            String[] dotSeparatedFileName = imageAttachment.getOriginalFilename().split("\\.");
            String fileNameWithoutFormat = String.join("", Arrays.copyOfRange(dotSeparatedFileName, 0, dotSeparatedFileName.length - 1));
            String fileName = "image_" + fileNameWithoutFormat + "_" + UUID.randomUUID() + "." + dotSeparatedFileName[dotSeparatedFileName.length - 1];

//            Path filePath = Paths.get(uploadPath.toString(), fileName);
            Path filePath = Paths.get(uploadPath.toString(), fileName);
            try (InputStream imageInputStream = imageAttachment.getInputStream()) {
                URL url = new URL("https://localhost:8081/images/" + fileName);
                Files.copy(imageInputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                return Optional.of(Attachment.builder().title(fileName).extension(FilenameUtils.getExtension(fileName)).attachmentPath(filePath.toUri().toURL()).build());
//                return Optional.of(Attachment.builder().title(fileName).extension(FilenameUtils.getExtension(fileName)).attachmentPath(url).build());
            } catch (IOException e) {
                throw new AttachmentUploadException("Fail to getInputStream or saving to a file " + filePath);
            }
        }
        return Optional.empty();
    }

    private boolean deleteImage(Set<Attachment> imageAttachments) {
        Attachment attachmentForRemoval = imageAttachments.iterator().next();
        attachmentRepository.delete(attachmentForRemoval);
        File file = new File(attachmentForRemoval.getAttachmentPath().getPath());
        return file.delete();
    }
}
