package team.exlab.ecohub.news.validation;

import team.exlab.ecohub.news.dto.NewsItemDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ContainersNumberValidator implements ConstraintValidator<ContainersNumber, NewsItemDto> {
	@Override
	public boolean isValid(NewsItemDto newsItemDto, ConstraintValidatorContext constraintValidatorContext) {
		for (String keyword : newsItemDto.getKeywords()) {
			if (keyword.length() > 100) return false;
		}
		return (newsItemDto.getKeywords().size() >= 3) && (newsItemDto.getKeywords().size() <= 20);
	}
}
