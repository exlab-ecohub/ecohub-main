package team.exlab.ecohub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EcohubApplication {
    private static String superAdminPassword;
    private static String firstAdminPassword;

    public static void main(String[] args) {
//        superAdminPassword = args[0];
//        firstAdminPassword = args[1];
        SpringApplication.run(EcohubApplication.class, args);
    }
//    @Bean
//    public CommandLineRunner run(FeedbackRepository feedbackRepository) throws Exception {
//        return (String[] args) -> {
//            FeedbackUserDto feedbackUserDto1 = new FeedbackUserDto(
//                    "Pavel",
//                    1,
//                    "QUESTION",
//                    MessageTopic.PROBLEM,
//                    "pavel11sg@gmail.com",
//                    LocalDateTime.now(),
//                    "Добрый день! Исправьте пожалуйста контактные данные по пункту переработки по адресу ул.Слободская 4",
//                    null,
//                    ResponseStatus.OPEN);
//            FeedbackUserDto feedbackUserDto2 = new FeedbackUserDto(
//                    "Alex",
//                    1,
//                    "alex.yatsenko@gmail.com",
//                    MessageTopic.QUESTION,
//                    "Здравствуйте, подскажите можно ли где-то сдать стврую мебель. Спасибо!",
//                    LocalDateTime.now(),
//                    null,
//                    null,
//                    ResponseStatus.OPEN);
//            feedbackRepository.save(FeedbackUserMapper.toFeedback(feedbackUserDto1));
//            feedbackRepository.save(FeedbackUserMapper.toFeedback(feedbackUserDto2));
//            feedbackRepository.findAll().forEach(System.out::println);
//        };
//    }

    public static String getSuperAdminPassword() {
        return superAdminPassword;
    }

    public static String getFirstAdminPassword() {
        return firstAdminPassword;
    }
}
