package com.thundertaste.recipesite.core;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImageService {

    private static final String RECIPE_PHOTO_DIR = "src/main/resources/static/images/recipePhotos/";

    public String saveImage(MultipartFile imageFile) {
        if (!imageFile.isEmpty()) {
            try {
                // Ensure the directory exists
                Files.createDirectories(Paths.get(RECIPE_PHOTO_DIR));

                // You should probably do something to ensure the filename is unique
                String filename = UUID.randomUUID() + "-" + imageFile.getOriginalFilename();

                // Save the file locally in the filesystem
                Path path = Paths.get(RECIPE_PHOTO_DIR + filename);
                Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                // Return the relative path
                return "images/recipePhotos/" + filename;
            } catch (IOException e) {
                // Handle exceptions (e.g., log them and throw a runtime exception)
                throw new RuntimeException("Failed to save image", e);
            }
        } else {
            // Handle the case where the file is empty/null
            return null;
        }
    }
}
