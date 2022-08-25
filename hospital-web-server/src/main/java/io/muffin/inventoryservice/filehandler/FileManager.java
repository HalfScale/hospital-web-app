package io.muffin.inventoryservice.filehandler;

import io.muffin.inventoryservice.filehandler.FileAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileManager {

    @Value("${file.service.type}")
    private String fileServiceType;
    private final FileAdapter fileAdapter;

    public void setProperties(String fileName, String identifier, MultipartFile multipartFile) {
        fileAdapter.setFileName(fileName);
        fileAdapter.setIdentifier(identifier);
        fileAdapter.setMultipartFile(multipartFile);
        fileAdapter.setFileServiceType(fileServiceType);
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
