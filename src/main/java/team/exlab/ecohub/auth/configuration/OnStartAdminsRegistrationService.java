package team.exlab.ecohub.auth.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import team.exlab.ecohub.user.model.ERole;
import team.exlab.ecohub.user.model.User;
import team.exlab.ecohub.user.repository.RoleRepository;
import team.exlab.ecohub.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class OnStartAdminsRegistrationService implements InitializingBean {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${app.superadmin.login}")
    private String superadminLogin;
    @Value("${app.superadmin.password}")
    private String superadminPassword;
    @Value("${app.firstadmin.login}")
    private String firstAdminLogin;
    @Value("${app.firstadmin.password}")
    private String firstAdminPassword;


    @Override
    public void afterPropertiesSet() {
        if (!userRepository.existsByUsername(superadminLogin)) {
            User superAdmin = new User(superadminLogin,
                    passwordEncoder.encode(superadminPassword),
                    null,
                    roleRepository
                            .findRoleByName(ERole.ROLE_SUPERADMIN)
                            .orElseThrow(() -> new RuntimeException("Error, Role SUPERADMIN is not found")));
            userRepository.save(superAdmin);
        }
        if (!userRepository.existsByUsername(firstAdminLogin)) {
            User admin = new User(firstAdminLogin,
                    passwordEncoder.encode(firstAdminPassword),
                    null,
                    roleRepository
                            .findRoleByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error, Role ADMIN is not found")));
            userRepository.save(admin);
        }
    }
}
