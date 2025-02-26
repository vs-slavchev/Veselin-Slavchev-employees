package com.slavchev.employees.employees_service.service;

import com.slavchev.employees.employees_service.exceptions.UploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class CsvService {

    public String handleUpload(MultipartFile file) {
        if (file.isEmpty()) {
            throw new UploadException("File is empty.");
        }
        try (InputStream inputStream = file.getInputStream()) {
            return new String(inputStream.readAllBytes());
        } catch (IOException e) {
            throw new UploadException("Failed to process the file.", e);
        }
    }
}
