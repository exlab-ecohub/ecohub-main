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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
		deleteImage(newsItemToEdit.getImageAttachment());
		Set<Attachment> set = new HashSet<>();
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
	public byte[] getAttachment(String attachmentURL) {
		try (InputStream is = new FileInputStream(attachmentURL)) {
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

	private Attachment saveImage(MultipartFile imageAttachment) {
		if (!imageAttachment.isEmpty()) {
			Path uploadPath = Paths.get(System.getProperty(storageProperties.getImagesDirectory()), "files", "news", "images");
			if (!Files.exists(uploadPath)) {
				try {
					Files.createDirectories(uploadPath);
				} catch (IOException e) {
					throw new UnableCreateDirectoryException("Fail to create directory: " + uploadPath);
				}
			}
			String fileName = "image_" + UUID.randomUUID() + "_" + imageAttachment.getOriginalFilename();
			Path filePath = Paths.get(uploadPath.toString(), fileName);
			try (InputStream imageInputStream = imageAttachment.getInputStream()) {
				Files.copy(imageInputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
				return Attachment.builder().title(fileName).extension(FilenameUtils.getExtension(fileName)).attachmentPath(filePath.toUri().toURL()).build();
			} catch (IOException e) {
				throw new AttachmentUploadException("Fail to getInputStream or saving to a file " + filePath);
			}
		} else {
			return null;
		}
	}

	private boolean deleteImage(Set<Attachment> imageAttachments) {
		Attachment attachmentForRemoval = imageAttachments.iterator().next();
		attachmentRepository.delete(attachmentForRemoval);
		File file = new File(attachmentForRemoval.getAttachmentPath().getPath());
		return file.delete();
	}
}
