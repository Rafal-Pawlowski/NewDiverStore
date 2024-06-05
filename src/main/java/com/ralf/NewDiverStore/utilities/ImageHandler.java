package com.ralf.NewDiverStore.utilities;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageHandler {

    public static String saveFile(MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            String fileName = file.getOriginalFilename();
            Path path = Paths.get("uploads/" + fileName);
            Files.write(path, bytes);
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void deleteFile(String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
            try {
                Path path = Paths.get("uploads/" + filePath);
                if (Files.exists(path)) {
                    Files.delete(path);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
