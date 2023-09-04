package team.exlab.ecohub.auth.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import team.exlab.ecohub.feedback.model.Feedback;
import team.exlab.ecohub.feedback.model.MessageTopic;
import team.exlab.ecohub.feedback.model.ResponseStatus;
import team.exlab.ecohub.feedback.repository.FeedbackRepository;
import team.exlab.ecohub.user.model.ERole;
import team.exlab.ecohub.user.model.User;
import team.exlab.ecohub.user.repository.RoleRepository;
import team.exlab.ecohub.user.repository.UserRepository;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class OnStartAdminsRegistrationService implements InitializingBean {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final FeedbackRepository feedbackRepository;



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
        Feedback feedback1 = new Feedback(
                3L,
                "Pavel",
                user1,
                admin,
                "pavel11sg@gmail.com",
                MessageTopic.PROBLEM,
                "Добрый день! Исправьте пожалуйста контактные данные по пункту переработки по адресу ул.Слободская 4",
                LocalDateTime.now(),
                null,
                null,
                ResponseStatus.OPEN);
        feedbackRepository.save(feedback1);
        Feedback feedback2 = new Feedback(
                4L,
                "Alex",
                user2,
                admin,
                "alex.yatsenko@gmail.com",
                MessageTopic.QUESTION,
                "Здравствуйте, подскажите можно ли где-то сдать стврую мебель. Спасибо!",
                LocalDateTime.now(),
                null,
                null,
                ResponseStatus.OPEN);
        feedbackRepository.save(feedback2);
    }
}
