package ferret.brass_b.image.service;

import ferret.brass_b.accouting.dao.UserRepository;
import ferret.brass_b.accouting.dto.exceptions.UserNotFoundException;
import ferret.brass_b.accouting.model.Exam;
import ferret.brass_b.accouting.model.UserAccount;
import ferret.brass_b.image.dao.ImageRepository;
import ferret.brass_b.image.dto.ImageDto;
import ferret.brass_b.image.dto.exeptions.ImageNotFoundExceptions;
import ferret.brass_b.image.model.Image;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;


@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService{
    @Autowired
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    @Override
    public ImageDto addImg(String userId, MultipartFile file) throws IOException {
        UserAccount userAccount = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if(file.getSize() != 0) {
            Image img = new Image(file.getName(), file.getOriginalFilename(), file.getSize(), file.getContentType(), file.getBytes());
            imageRepository.save(img);
            /*String base64Image = Base64.getEncoder().encodeToString(img.getBytes());
            userAccount.setImage(base64Image);*/
            userAccount.setImage(img.getId());
            userRepository.save(userAccount);
            return modelMapper.map(img, ImageDto.class);
        }
        return null;
    }

    @Override
    public ImageDto getImg(String imgId) {
        Image image = imageRepository.findById(imgId).orElseThrow(ImageNotFoundExceptions::new);
        ImageDto img = modelMapper.map(image, ImageDto.class);
        return img;
    }
}
