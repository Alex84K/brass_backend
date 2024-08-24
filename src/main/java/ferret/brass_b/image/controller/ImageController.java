package ferret.brass_b.image.controller;

import ferret.brass_b.accouting.service.UserAccountService;
import ferret.brass_b.image.dto.ImageDto;
import ferret.brass_b.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin
public class ImageController  {
    @Autowired
    private final UserAccountService userAccountService;
    private final ImageService imageService;

    @PostMapping("/{userId}/img")
    public ImageDto addImg(@PathVariable String userId, @RequestParam MultipartFile file) throws IOException {
        return imageService.addImg(userId, file);
    }

    @GetMapping("/img/{imgId}")
    public ImageDto getImg(@PathVariable String imgId) {
        return imageService.getImg(imgId);
    }
}
