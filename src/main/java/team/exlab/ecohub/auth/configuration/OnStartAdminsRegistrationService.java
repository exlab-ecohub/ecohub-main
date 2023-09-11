package team.exlab.ecohub.auth.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
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

    @Override
    public void afterPropertiesSet() {
        //todo Как правильно создавать учетки суперадмина и админа при запуске приложения?

        User superAdmin = new User("superadmin", passwordEncoder.encode("superadmin"), null, roleRepository
                .findRoleByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error, Role USER is not found")));
        superAdmin.setRole(roleRepository.findRoleByName(ERole.ROLE_SUPERADMIN).orElseThrow());
        userRepository.save(superAdmin);

        User admin = new User("admin", passwordEncoder.encode("adminadmin"), null, roleRepository
                .findRoleByName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Error, Role ADMIN is not found")));
        admin.setRole(roleRepository.findRoleByName(ERole.ROLE_ADMIN).orElseThrow());
        userRepository.save(admin);
    }
}
