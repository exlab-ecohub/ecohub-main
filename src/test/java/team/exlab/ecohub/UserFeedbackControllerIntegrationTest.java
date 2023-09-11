package team.exlab.ecohub;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import team.exlab.ecohub.feedback.controller.UserFeedbackController;
import team.exlab.ecohub.feedback.repository.FeedbackRepository;

import java.nio.charset.StandardCharsets;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@AutoConfigureMockMvc
public class UserFeedbackControllerIntegrationTest {

    @MockBean
    private FeedbackRepository feedbackRepository;

    @Autowired
    UserFeedbackController userFeedbackController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenPostRequestToUsersAndValidUser_thenCorrectResponse() throws Exception {
        MediaType textPlainUtf8 = new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8);
        String feedbackUserDto = "{" +
                "\"name\": \"Pavel\"," +
                "\"email\": \"pavel11sg@gmail.com\"" +
                "\"messageContent\" : \"Hello!\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/feedbacks")
                        .content(feedbackUserDto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(textPlainUtf8));
    }


    @Test
    public void whenPostRequestToUsersAndInValidUser_thenCorrectResponse() throws Exception {
        String feedbackUserDto = "{\"name\": \"\", \"email\" : \"bob@domain.test\", \"messageContent\" : \"Hello!\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/feedbacks")
                        .content(feedbackUserDto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is("Name is mandatory")))

                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }
}