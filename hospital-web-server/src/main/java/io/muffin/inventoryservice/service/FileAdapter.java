package io.muffin.inventoryservice.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Setter
@Component
@RequiredArgsConstructor
public class FileAdapter {

    private String fileServiceType;
    private String fileName;
    private String identifier;
    private MultipartFile multipartFile;
    private FileService fileService;
    private final LocalFileService localFileService;
    private final AWSS3FileService awss3FileService;


    public String upload() {
        setFileServiceType();
        return fileService.upload();
    }

    public byte[] download() {
        setFileServiceType();
        return fileService.download();
    }

    public String delete() {
        setFileServiceType();
        return fileService.delete();
    }

    private void setFileServiceType() {
        log.info("GET_FILE_SERVICE_TYPE => [{}]", fileServiceType);

        if(fileServiceType.equalsIgnoreCase("local")) {
            fileService = localFileService;
        }else if (fileServiceType.equalsIgnoreCase("aws-s3")) {
            fileService = awss3FileService;
        }

        fileService.setFileName(fileName);
        fileService.setIdentifier(identifier);
        fileService.setMultipartFile(multipartFile);
    }

}
