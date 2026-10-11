package com.rogerjaden.eventmanagement.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String store(MultipartFile file);

    void delete(String imageUrl);
}
