package io.muffin.inventoryservice.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AWSS3FileService extends FileService{

    @Value("${application.bucket.name}")
    private String bucketName;
    private final AmazonS3 s3Client;

    @Override
    public String upload() {
        File fileObj = convertMultiPartFileToFile();
        String encryptedFile = this.encryptFile();
        String filePath = buildFilePath() + encryptedFile;
        s3Client.putObject(new PutObjectRequest(bucketName, filePath, fileObj));
        fileObj.delete();
        return encryptedFile;
    }

    @Override
    public byte[] download() {
        String filePath = buildFilePath() + this.getFileName();
        S3Object s3Object = s3Client.getObject(bucketName, filePath);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String delete() {
        String filePath = buildFilePath() + this.getFileName();
        s3Client.deleteObject(bucketName, filePath);
        return "Removed " + this.getFileName();
    }

    private File convertMultiPartFileToFile() {
        File convertedFile = new File(this.getFileName());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(this.getMultipartFile().getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }

    private String buildFilePath() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getPathIdentifier());
        sb.append(File.separator);
        return sb.toString();
    }
}
