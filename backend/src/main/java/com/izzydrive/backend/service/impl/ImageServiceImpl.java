package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.model.Image;
import com.izzydrive.backend.repository.ImageRepository;
import com.izzydrive.backend.service.ImageService;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Override
    public Image convertImageToBase64(long id) {
        return imageRepository.findById(id).get();
    }

    @Override
    public Stream<Image> getAllPhotos() {
        return null;
    }

    @Override
    public List<Image> convertPhotosFromDTO(List<MultipartFile> photos, String email) throws IOException {
        List<Image> images = new ArrayList<Image>();
        if(photos == null) {
            return images;
        }
        for (MultipartFile photoData: photos
        ) {
            String photoName = savePhotoInFileSystem(photoData.getBytes(), email);
            Image img = new Image(photoName);
            images.add(img);
            imageRepository.save(img);
        }
        return images;
    }

    @Override
    public String savePhotoInFileSystem(byte[] bytes, String ownerEmail) throws IOException {
        String folder = "./src/main/resources/images/";
        LocalDateTime uniqueTime = LocalDateTime.now();
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
        String photoName = ownerEmail + "_" + uniqueTime.format(formater) + ".jpg";
        Path path = Paths.get(folder + photoName);
        try (FileOutputStream fos = new FileOutputStream(path.toString())) {
            fos.write(bytes);
        }
        return photoName;
    }

    @Override
    public Image convertImageFromBase64(String image, String email) throws IOException {

        byte[] bytes = DatatypeConverter.parseBase64Binary(image);
        String photoName = savePhotoInFileSystem(bytes, email);
        Image img = new Image(photoName);
        return imageRepository.save(img);

    }

    public String convertImageToBase64(Image img) throws IOException {
        String pathFile =  "./src/main/resources/images/" + img.getName();
        byte[] bytes = Files.readAllBytes(Paths.get(pathFile));
        String photoData = Base64.getEncoder().encodeToString(bytes);
        return photoData;
    }
}
