package team.exlab.ecohub.auth.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import team.exlab.ecohub.auth.controller.AuthenticationController;
import team.exlab.ecohub.auth.dto.SignupRequestDto;
import team.exlab.ecohub.user.model.ERole;

@Component
@RequiredArgsConstructor
public class OnStartAdminsRegistrationService implements InitializingBean {

    private final AuthenticationController controller;

    @Override
    public void afterPropertiesSet() {
        SignupRequestDto superadmin = new SignupRequestDto();
        superadmin.setUsername("superadmin");
        //todo Как правильно создавать учетки суперадмина и админа при запуске приложения?
//        superadmin.setPassword(EcohubApplication.getSuperAdminPassword());
        superadmin.setPassword("superadmin");
        superadmin.setRole(ERole.ROLE_SUPERADMIN);

        SignupRequestDto admin = new SignupRequestDto();
        admin.setUsername("admin");
//        admin.setPassword(EcohubApplication.getFirstAdminPassword());
        admin.setPassword("adminadmin");
        admin.setRole(ERole.ROLE_ADMIN);

        controller.registerUser(superadmin);
        controller.registerUser(admin);
    }
}
