package com.izzydrive.backend.service;

import com.izzydrive.backend.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public interface ImageService {
    Stream<Image> getAllPhotos();
    List<Image> convertPhotosFromDTO(List<MultipartFile> photos, String email);
    String savePhotoInFileSystem(byte[] bytes, String ownerEmail);
    Image convertImageFromBase64(String img, String email);
    String convertImageToBase64(Image img);
}

