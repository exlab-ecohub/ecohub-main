package team.exlab.ecohub.news.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class ImageFileValidator implements ConstraintValidator<ValidImage, MultipartFile> {

	@Override
	public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
		boolean result = true;
		if (!isSupportedContentType(multipartFile)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Only PNG, JPG, JPEG and GIF images are allowed.").addConstraintViolation();
			result = false;
		} else if (!isProvidedImageOfSupportedResolutionsOrEmpty(multipartFile)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Provided image width/height is out of aloud range!").addConstraintViolation();
			result = false;
		}
		return result;
	}

	private boolean isSupportedContentType(MultipartFile multipartFile) {
		String contentType = multipartFile.getContentType();
		return contentType != null &&
				(contentType.equals("image/png") || contentType.equals("multipartFile/jpg") || contentType.equals("image/jpeg") || contentType.equals("image/gif"));
	}

	private boolean isProvidedImageOfSupportedResolutionsOrEmpty(MultipartFile multipartFile) {
		if (!multipartFile.isEmpty()) {
			try (InputStream is = multipartFile.getInputStream()) {
				BufferedImage image = ImageIO.read(is);
				return (image.getHeight() <= 1500 && image.getHeight() >= 800) && (image.getWidth() <= 2000 && image.getWidth() >= 1200);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else return true;
	}
}
