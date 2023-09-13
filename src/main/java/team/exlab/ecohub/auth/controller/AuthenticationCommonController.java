package team.exlab.ecohub.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.exlab.ecohub.auth.dto.JwtResponseDto;
import team.exlab.ecohub.auth.dto.LoginRequestDto;
import team.exlab.ecohub.auth.dto.MessageResponseDto;
import team.exlab.ecohub.auth.dto.SignupUserRequestDto;
import team.exlab.ecohub.auth.service.AuthenticationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationCommonController {
    private final AuthenticationService authenticationService;


    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return authenticationService.authenticate(loginRequestDto);
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponseDto> registerUser(@Valid @RequestBody SignupUserRequestDto signupUserRequestDto) {
        return authenticationService.registerUser(signupUserRequestDto);
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        authenticationService.refreshToken(request, response);
    }
}
