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
        //todo Как правильно создавать учетки суперадмина и админа при запуске приложения?

        User superAdmin = new User(superadminLogin,
                passwordEncoder.encode(superadminPassword),
                null);
//        User superAdmin = new User("superadmin", passwordEncoder.encode("superadmin"), null);
        superAdmin.setRole(roleRepository.findRoleByName(ERole.ROLE_SUPERADMIN).orElseThrow());

        User admin = new User(firstAdminLogin,
                passwordEncoder.encode(firstAdminPassword),
                null);
//        User admin = new User("admin", passwordEncoder.encode("adminadmin"), null);
        admin.setRole(roleRepository.findRoleByName(ERole.ROLE_ADMIN).orElseThrow());
//        userRepository.save(superAdmin);
//        userRepository.save(admin);
    }
}
