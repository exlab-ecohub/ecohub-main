package team.exlab.ecohub.feedback;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class ResponseStatusConverter implements AttributeConverter<ResponseStatus, String> {

    @Override
    public String convertToDatabaseColumn(ResponseStatus responseStatus) {
        return responseStatus.name();
    }

    @Override
    public ResponseStatus convertToEntityAttribute(String s) {
        return Stream.of(ResponseStatus.values()).
                filter(rs -> rs.name().equals(s)).findFirst().orElseThrow();
    }
}