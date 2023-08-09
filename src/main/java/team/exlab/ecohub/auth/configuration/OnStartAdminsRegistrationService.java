package team.exlab.ecohub.auth.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import team.exlab.ecohub.EcohubApplication;
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

        User superAdmin = new User(EcohubApplication.getSuperAdminLogin(),
                passwordEncoder.encode(EcohubApplication.getSuperAdminPassword()),
                null);
//        User superAdmin = new User("superadmin", passwordEncoder.encode("superadmin"), null);
        superAdmin.setRole(roleRepository.findRoleByName(ERole.ROLE_SUPERADMIN).orElseThrow());
        userRepository.save(superAdmin);

            User admin = new User(EcohubApplication.getFirstAdminLogin(),
                    passwordEncoder.encode(EcohubApplication.getFirstAdminPassword()),
                    null);
//        User admin = new User("admin", passwordEncoder.encode("adminadmin"), null);
        admin.setRole(roleRepository.findRoleByName(ERole.ROLE_ADMIN).orElseThrow());
        userRepository.save(admin);
    }
}
