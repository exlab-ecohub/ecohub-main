package team.exlab.ecohub.user;

import team.exlab.ecohub.user.dto.UserDto;
import team.exlab.ecohub.user.model.User;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(),
                user.getUsername());
    }
}
