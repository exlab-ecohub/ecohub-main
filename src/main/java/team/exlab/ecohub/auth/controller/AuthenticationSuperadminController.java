package team.exlab.ecohub.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.exlab.ecohub.auth.dto.MessageResponseDto;
import team.exlab.ecohub.auth.dto.SignupAdminRequestDto;
import team.exlab.ecohub.auth.service.AuthenticationService;
import team.exlab.ecohub.user.dto.AdminDto;
import team.exlab.ecohub.user.dto.PasswordChangeDto;
import team.exlab.ecohub.user.service.UserService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/superadmin")
@RequiredArgsConstructor
public class AuthenticationSuperadminController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/admins")
    public ResponseEntity<MessageResponseDto> registerAdmin(@Valid @RequestBody SignupAdminRequestDto signupAdminRequestDto) {
        return authenticationService.registerAdmin(signupAdminRequestDto);
    }

    @GetMapping("/admins")
    public List<AdminDto> getAllAdmins() {
        return userService.getAllAdmins();
    }

    @GetMapping("/admins/{adminId}")
    public AdminDto getAdminById(@PathVariable Long adminId) {
        return userService.getAdminById(adminId);
    }

    @PatchMapping("/admins/{adminId}")
    public void changeAdminPassword(@PathVariable Long adminId,
                                    @Valid @RequestBody PasswordChangeDto passwordDto) {
        userService.changeAdminPassword(adminId, passwordDto);
    }

    @DeleteMapping("/admins/{adminId}")
    public void deleteAdmin(@PathVariable Long adminId) {
        userService.deleteAdmin(adminId);
    }

    @PostMapping("/admins/{adminId}/block")
    public void blockAdmin(@PathVariable Long adminId) {
        userService.blockAdmin(adminId);
    }

    @PostMapping("/admins/{adminId}/unblock")
    public void unblockAdmin(@PathVariable Long adminId) {
        userService.unblockAdmin(adminId);
    }
}
