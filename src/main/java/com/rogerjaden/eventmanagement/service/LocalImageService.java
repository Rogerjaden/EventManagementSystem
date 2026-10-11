package com.rogerjaden.eventmanagement.service;

import com.rogerjaden.eventmanagement.service.interfaces.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LocalImageService implements ImageService {
    private final Path uploadDirectory =
            Paths.get("uploads/events");

    @Override
    public String store(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Event banner is required"
            );
        }

        try {
            Files.createDirectories(uploadDirectory);

            String originalFilename =
                    file.getOriginalFilename();

            String extension = "";

            if (originalFilename != null &&
                    originalFilename.contains(".")) {

                extension = originalFilename.substring(
                        originalFilename.lastIndexOf(".")
                );
            }

            String filename =
                    UUID.randomUUID() + extension;

            Path filePath =
                    uploadDirectory.resolve(filename);

            file.transferTo(filePath);

            return "/uploads/events/" + filename;

        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to store event banner",
                    e
            );
        }
    }

    @Override
    public void delete(String imageUrl) {

        if (imageUrl == null || imageUrl.isBlank()) {
            return;
        }

        try {

            String filename =
                    imageUrl.substring(
                            imageUrl.lastIndexOf("/") + 1
                    );

            Path filePath =
                    uploadDirectory.resolve(filename);

            Files.deleteIfExists(filePath);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to delete event banner",
                    e
            );
        }
    }
}
