package com.izzydrive.backend.service;

import com.izzydrive.backend.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public interface ImageService {
    Image convertImageToBase64(long id);
    Stream<Image> getAllPhotos();
    List<Image> convertPhotosFromDTO(List<MultipartFile> photos, String email) throws IOException;
    String savePhotoInFileSystem(byte[] bytes, String ownerEmail) throws IOException;
    Image convertImageFromBase64(String img, String email) throws IOException;
    String convertImageToBase64(Image img) throws IOException;
}

