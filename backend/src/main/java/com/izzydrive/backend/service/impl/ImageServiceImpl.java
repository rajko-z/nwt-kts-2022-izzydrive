package com.izzydrive.backend.service.impl;

import com.izzydrive.backend.repository.ImageRepository;
import com.izzydrive.backend.service.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
}
