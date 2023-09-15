package team.exlab.ecohub.user;

import team.exlab.ecohub.user.dto.AdminDto;
import team.exlab.ecohub.user.model.User;

public class UserMapper {
    private UserMapper() {
    }

    public static AdminDto toAdminDto(User admin) {
        return new AdminDto(admin.getId(),
                admin.getUsername(),
                admin.getPassAttempts(),
                admin.isBlockedBefore(),
                admin.getLockEndTime());
    }
}
