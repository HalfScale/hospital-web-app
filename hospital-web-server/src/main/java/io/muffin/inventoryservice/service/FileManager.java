package io.muffin.inventoryservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileManager {

    @Value("${file.service.type}")
    private String fileServiceType;
    @Value("${storage.directory.path: ''}")
    private String localRootDirPath;
    private FileAdapter fileAdapter;

    public void setProperties(String fileName, String identifier, MultipartFile multipartFile) {
        fileAdapter = new FileAdapter();
        fileAdapter.setFileName(fileName);
        fileAdapter.setIdentifier(identifier);
        fileAdapter.setMultipartFile(multipartFile);

        if(fileServiceType.equalsIgnoreCase("local")) {
            fileAdapter.setFileServiceType(fileServiceType);
            fileAdapter.setLocalDirPath(localRootDirPath);
        }
    }

    public String upload() {
        return fileAdapter.upload();
    }

    public byte[] download() {
        return fileAdapter.download();
    }

    public String delete() {
        return fileAdapter.delete();
    }
}
