import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;
/**
 * Created by rajesh on 26/08/17.
 */

public class FileUploadController {

    @RequestMapping(value = "/react", method = RequestMethod.POST)
    public ResponseEntity<?> handleFileUploadReact(HttpServletRequest request, @RequestParam("file") MultipartFile file,
                                                   @RequestParam("name") String fileName, @RequestParam("description") String description,
                                                   RedirectAttributes redirectAttributes) throws Exception {
        logger.info("params found in post request call for react file upload:{}", request.getParameterNames());

        logger.info("file upload request: file name found as:{}", file.getOriginalFilename());
        logger.info("file content:{}", file);
        storageService.storeZip(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return ResponseEntity.ok().body(new HashMap<>().put("Success","File uploading is successful:"+ file.getOriginalFilename()));
    }
}
