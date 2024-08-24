package ferret.brass_b.image.service;

import ferret.brass_b.image.dto.ImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    ImageDto addImg(String userId, MultipartFile file) throws IOException;
    ImageDto getImg(String imgId);
}
