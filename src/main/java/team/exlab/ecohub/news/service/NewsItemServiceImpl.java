package team.exlab.ecohub.news.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.exlab.ecohub.exception.AttachmentUploadException;
import team.exlab.ecohub.exception.NewsItemNotFoundException;
import team.exlab.ecohub.exception.UnableCreateDirectoryException;
import team.exlab.ecohub.exception.UnablePassInputStreamToByteArray;
import team.exlab.ecohub.news.StorageConfigurationProperties;
import team.exlab.ecohub.news.dto.NewsItemDto;
import team.exlab.ecohub.news.dto.NewsItemMapper;
import team.exlab.ecohub.news.model.Attachment;
import team.exlab.ecohub.news.model.NewsItem;
import team.exlab.ecohub.news.repository.AttachmentRepository;
import team.exlab.ecohub.news.repository.NewsItemRepository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class NewsItemServiceImpl implements NewsItemService {
    private final StorageConfigurationProperties storageProperties;
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
        Attachment attachment = saveImage(imageAttachment);
        if (attachment != null) {
            attachmentRepository.save(attachment);
            newsItemToSave.setImageAttachment(Collections.singleton(attachment));
        }
        return NewsItemMapper.toDto(newsItemRepository.save(newsItemToSave));
    }

    @Override
    @Transactional
    public NewsItemDto editNewsItem(Long newsId, NewsItemDto newsItemDto, MultipartFile attachment) {
        NewsItem newsItemToEdit = newsItemRepository.findById(newsId).orElseThrow(() -> new NewsItemNotFoundException(newsId));
        Set<Attachment> set = newsItemToEdit.getImageAttachment();
        Attachment newAttachment = saveImage(attachment);
        if (newAttachment != null) {
            set.add(newAttachment);
            attachmentRepository.save(newAttachment);
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
    public List<NewsItemDto> getAllNews() {
        return newsItemRepository.findAll().stream().map(NewsItemMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public NewsItemDto getNewsItem(Long newsId) {
        return NewsItemMapper.toDto(newsItemRepository.findById(newsId).orElseThrow(() -> new NewsItemNotFoundException(newsId)));
    }

    @Override
    @Transactional
    public byte[] getAttachment(String attachmentURI) {
        try (InputStream is = getClass().getResourceAsStream(attachmentURI)) {
            return IOUtils.toByteArray(is);
        } catch (IOException e) {
            throw new UnablePassInputStreamToByteArray(attachmentURI);
        }
    }

    private Attachment saveImage(MultipartFile imageAttachment) {
        if (!imageAttachment.isEmpty()) {
            Path uploadPath = Paths.get(storageProperties.getImagesDirectory());
            if (!Files.exists(uploadPath)) {
                try {
                    Files.createDirectories(uploadPath);
                } catch (IOException e) {
                    throw new UnableCreateDirectoryException("Fail to create directory: " + uploadPath);
                }
            }
            String fileName = "image_" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + "_" + imageAttachment.getOriginalFilename();
            Path filePath = Paths.get(uploadPath + "/" + fileName);
            String filePathAsString = "/news/images/".concat(fileName);
            try {
                InputStream imageInputStream = imageAttachment.getInputStream();
                Files.copy(imageInputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                imageInputStream.close();
            } catch (IOException e) {
                throw new AttachmentUploadException("Fail to getInputStream or saving to a file " + filePath);
            }
            return Attachment.builder().title(fileName).extension(FilenameUtils.getExtension(fileName)).attachmentPath(filePathAsString).build();
        } else {
            return null;
        }
    }
}
