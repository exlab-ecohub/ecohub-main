package team.exlab.ecohub.exception;

import team.exlab.ecohub.user.model.User;

import java.time.Duration;
import java.time.LocalDateTime;

public class AdminBlockedException extends RuntimeException {

    public AdminBlockedException(User admin) {
        super(String.format("Admin with username %s is blocked %s", admin.getUsername(),
                Duration.between(LocalDateTime.now(), admin.getLockEndTime())
                .getSeconds() > 3600 ? "forever. Please contact your boss" : "for one hour"));
    }
}
