package team.exlab.ecohub.news.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.exlab.ecohub.news.service.NewsItemService;

import javax.servlet.http.HttpServletRequest;

@RestController
//@RequestMapping("/images")
@RequiredArgsConstructor
public class AttachmentsController {
    private final NewsItemService newsItemService;
    //TODO
//    @GetMapping(value = "/E:/Git-projects/ecohub-main/files/news/images/**", produces = MediaType.IMAGE_JPEG_VALUE)
//    @GetMapping(produces = MediaType.IMAGE_JPEG_VALUE)

    /**
     * <p>This is a simple description of the method. . .
     * <a href="http://www.supermanisthegreatest.com">Superman!</a>
     * </p>
     * @param request the amount of incoming damage
     * @return the amount of health hero has after attack
     * @see <a href="http://www.link_to_jira/HERO-402">HERO-402</a>
     * @since 1.0
     */
//    @GetMapping(value = "/{fileName}",produces = MediaType.IMAGE_JPEG_VALUE)
    @GetMapping(value = "/D:/dev/Exlab Ecohub/ecohub-main/user.dir/files/news/images/**", produces = MediaType.IMAGE_JPEG_VALUE)
//    @GetMapping(value = "${resources.images-directory}" + "/**", produces = MediaType.IMAGE_JPEG_VALUE)
//    public byte[] getAttachment(@PathVariable String fileName) {
    public byte[] getAttachment(HttpServletRequest request) {
        return newsItemService.getAttachment(request.getRequestURI());
//        return newsItemService.getAttachment(fileName);
    }
}
