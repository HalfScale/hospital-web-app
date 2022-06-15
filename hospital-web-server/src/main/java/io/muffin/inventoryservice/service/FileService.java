package io.muffin.inventoryservice.service;

import io.muffin.inventoryservice.utility.Constants;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;

@Service
@Getter
@Setter
@Slf4j
public class FileService {

    @Value("${storage.directory.path}")
    private String storageDirectoryPath;

    private Long entityId;
    private String identifier;
    private MultipartFile file;

    public String uploadToLocalFileSystem() {
        log.info("STORAGE_ROOT_DIR => [{}]", this.storageDirectoryPath);

        String hashedFile = null;


        Path storageDirectory = Paths.get(this.buildFilePath());

        if(!Files.exists(storageDirectory)) {
            try {
                Files.createDirectories(storageDirectory);
            }catch(Exception e) {
                e.printStackTrace();
            }
        }

        log.info("UPLOAD_FILE_TO_PATH => [{}]", storageDirectory.toString());

        try {
            hashedFile = this.hashFile();
            log.info("HASHED_FILE_RESULT => [{}]", hashedFile);

            Path destination = Paths.get(storageDirectory.toString() + File.separator + hashedFile);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        }catch(IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hashedFile;
    }

    private String hashFile() throws NoSuchAlgorithmException {
        StringBuilder sb = new StringBuilder();
        String modifiedFileName = sb.append(this.entityId).toString();
        log.info("MODIFIED_FILE_NAME => [{}]", modifiedFileName);
        return DigestUtils.md5DigestAsHex(modifiedFileName.getBytes());
    }

    private String buildFilePath() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.storageDirectoryPath);
        sb.append(File.separator);
        sb.append(this.identifier);
        return sb.toString();
    }

    public byte[] getImageWithMediaType(String imageHash, String identifier){
        log.info("GET_IMAGE_NAME => [{}]", imageHash);
        log.info("GET_IMAGE_IDENTIFIER => [{}]", identifier);
        Path destination = Paths.get(this.storageDirectoryPath + File.separator +
                this.getIdentifierPath(identifier) + File.separator + imageHash);

        byte[] result = null;

        try {
            result = IOUtils.toByteArray(destination.toUri());
        }catch (IOException ex) {
            result = this.getDefaultImage(identifier);
        }

        return result;
    }

    private byte[] getDefaultImage(String identifier) {
        byte[] result = null;

        try {
            Path destination = Paths.get(this.storageDirectoryPath + File.separator +
                    this.getIdentifierPath(identifier) + File.separator + "default.png");
            result = IOUtils.toByteArray(destination.toUri());
        }catch (IOException ex) {
            log.error("IO exception", ex);
            ex.printStackTrace();
        }
        return result;
    }

    public String getIdentifierPath(String identifier) {

        if(identifier.equalsIgnoreCase(Constants.IMAGE_IDENTIFIER_USER)) {
            return Constants.IMAGE_IDENTIFIER_USER;
        }else if (identifier.equalsIgnoreCase(Constants.IMAGE_IDENTIFIER_HOSPITAL_ROOM)) {
            return Constants.IMAGE_IDENTIFIER_HOSPITAL_ROOM;
        }

        throw new RuntimeException("Invalid identifier!");
    }
}
