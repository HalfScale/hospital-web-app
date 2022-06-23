package io.muffin.inventoryservice.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
public class LocalFileService extends FileService {

    @Value("${storage.directory.path: ''}")
    private String storageDirectoryPath;

    @Override
    public String upload() {
        log.info("STORAGE_ROOT_DIR => [{}]", this.storageDirectoryPath);
        Path storageDirectory = Paths.get(this.buildFilePath());
        String hashedFile = null;

        if(!Files.exists(storageDirectory)) {
            try {
                Files.createDirectories(storageDirectory);
            }catch(Exception e) {
                e.printStackTrace();
            }
        }

        try {
            hashedFile = encryptFile();
            Path destination = Paths.get(storageDirectory.toString() + File.separator + hashedFile);
            Files.copy(this.getMultipartFile().getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        }catch(IOException e) {
            e.printStackTrace();
        }

        log.info("FILE_UPLOADED => [{}]", hashedFile);

        return hashedFile;
    }

    @Override
    public byte[] download() {
        log.info("GET_FILE_NAME => [{}]", this.getFileName());
        log.info("GET_IMAGE_IDENTIFIER => [{}]", this.getIdentifier());
        Path destination = Paths.get(buildFilePath() + File.separator + this.getFileName());
        byte[] result = null;
        try {
            result = IOUtils.toByteArray(destination.toUri());
        }catch (IOException ex) {
           log.info("FILE_DOWNLOAD_ERROR => [{}]", ex.getMessage());
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public String delete() {
        String status = "Unsucessful!";
        Path destination = Paths.get(buildFilePath() + File.separator + this.getFileName());
        status =  destination.toFile().delete() ? "Successfully!" : status;
        return "File: " + getFileName() + " " + status;
    }

    private String buildFilePath() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.storageDirectoryPath);
        sb.append(File.separator);
        sb.append(this.getIdentifier());
        return sb.toString();
    }

}
