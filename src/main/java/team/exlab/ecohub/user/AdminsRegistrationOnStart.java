package team.exlab.ecohub.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import team.exlab.ecohub.EcohubApplication;
import team.exlab.ecohub.user.controller.AuthenticationController;
import team.exlab.ecohub.user.dto.SignupRequestDto;
import team.exlab.ecohub.user.model.ERole;

@Component
@RequiredArgsConstructor
public class AdminsRegistrationOnStart implements InitializingBean {

    private final AuthenticationController controller;

    @Override
    public void afterPropertiesSet() {
        SignupRequestDto superadmin = new SignupRequestDto();
        superadmin.setUsername("superadmin");
        //todo Как правильно создавать учетки суперадмина и админа при запуске приложения?
//        superadmin.setPassword("superadmin");

//        System.out.println("Enter superadmin password");
//        Scanner scanner = new Scanner(System.in);
//        superadmin.setPassword(scanner.next());

        superadmin.setPassword(EcohubApplication.getSuperAdminPassword());
        superadmin.setRole(ERole.ROLE_SUPERADMIN);

        SignupRequestDto admin = new SignupRequestDto();
        admin.setUsername("admin");
//        admin.setPassword("adminadmin");

//        System.out.println("Enter admin password");
//        admin.setPassword(scanner.next());

        admin.setPassword(EcohubApplication.getFirstAdminPassword());
        admin.setRole(ERole.ROLE_ADMIN);

        controller.registerUser(superadmin);
        controller.registerUser(admin);
    }
}
