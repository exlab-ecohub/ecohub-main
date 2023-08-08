package team.exlab.ecohub.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.exlab.ecohub.auth.dto.MessageResponseDto;
import team.exlab.ecohub.auth.dto.SignupAdminRequestDto;
import team.exlab.ecohub.auth.service.AuthenticationService;

import javax.validation.Valid;


@RestController
@RequestMapping("/superadmin")
@RequiredArgsConstructor
public class AuthenticationSuperadminController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register-admin")
    public ResponseEntity<MessageResponseDto> registerAdmin(@Valid @RequestBody SignupAdminRequestDto signupAdminRequestDto) {
        return authenticationService.registerAdmin(signupAdminRequestDto);
    }
}
