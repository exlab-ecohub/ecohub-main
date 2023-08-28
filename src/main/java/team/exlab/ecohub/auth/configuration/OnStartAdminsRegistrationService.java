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

//        User superAdmin = new User("superadmin", passwordEncoder.encode(EcohubApplication.getSuperAdminPassword()), null)
        User superAdmin = new User("superadmin", passwordEncoder.encode("superadmin"), null);
        superAdmin.setRole(roleRepository.findRoleByName(ERole.ROLE_SUPERADMIN).orElseThrow());
        userRepository.save(superAdmin);

//            User admin = new User("superadmin", passwordEncoder.encode(EcohubApplication.getFirstAdminPassword()), null);
        User admin = new User("admin", passwordEncoder.encode("adminadmin"), null);
        admin.setRole(roleRepository.findRoleByName(ERole.ROLE_ADMIN).orElseThrow());
        userRepository.save(admin);

//      User user1 = new User("testUser", testPassword, pavel11sg@gmail.com)
        User user1 = new User("testUser", passwordEncoder.encode("testPassword"),"pavel11sg@gmail.com");
        user1.setRole(roleRepository.findRoleByName(ERole.ROLE_USER).orElseThrow());
        userRepository.save(user1);

//      User user2 = new User("testUser2", testPassword2, alex@gmail.com)
        User user2 = new User("testUser2", passwordEncoder.encode("testPassword2"),"alex@gmail.com");
        user2.setRole(roleRepository.findRoleByName(ERole.ROLE_USER).orElseThrow());
        userRepository.save(user2);
    }
}
