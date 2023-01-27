package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.exception.InternalServerException;
import com.izzydrive.backend.model.Image;
import com.izzydrive.backend.repository.ImageRepository;
import com.izzydrive.backend.service.ImageService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.DatatypeConverter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Override
    public Stream<Image> getAllPhotos() {
        return null;
    }

    @Override
    public List<Image> convertPhotosFromDTO(List<MultipartFile> photos, String email) {
        List<Image> images = new ArrayList<>();
        if(photos == null) {
            return images;
        }
        for (MultipartFile photoData: photos) {
            String photoName;
            try {
                photoName = savePhotoInFileSystem(photoData.getBytes(), email);
            } catch (IOException e) {
                throw new InternalServerException(ExceptionMessageConstants.SOMETHING_WENT_WRONG_MESSAGE);
            }
            Image img = new Image(photoName);
            images.add(img);
            imageRepository.save(img);
        }
        return images;
    }

    @Override
    public String savePhotoInFileSystem(byte[] bytes, String ownerEmail) {
        String folder = "./src/main/resources/images/";
        LocalDateTime uniqueTime = LocalDateTime.now();
        String photoName = ownerEmail + ".jpg";
        Path path = Paths.get(folder + photoName);

        try (FileOutputStream fos = new FileOutputStream(path.toString())) {
            fos.write(bytes);
        }
        catch (IOException e) {
            throw new InternalServerException(ExceptionMessageConstants.SOMETHING_WENT_WRONG_MESSAGE);
        }
        return photoName;
    }

    @Override
    public void convertImageFromBase64(String image, String email) {
        byte[] bytes = DatatypeConverter.parseBase64Binary(image);
        savePhotoInFileSystem(bytes, email);
    }

    public String convertImageToBase64(Image img) {
        String pathFile =  "./src/main/resources/images/" + img.getName();
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(pathFile));
            return Base64.getEncoder().encodeToString(bytes);
        }
        catch (IOException e) {
            throw new InternalServerException(ExceptionMessageConstants.SOMETHING_WENT_WRONG_MESSAGE);
        }
    }

    @Override
    public void deleteImageByName(String imageName) {
        Optional<Image> image = imageRepository.findByName(imageName);
        image.ifPresent(imageRepository::delete);
    }
}
