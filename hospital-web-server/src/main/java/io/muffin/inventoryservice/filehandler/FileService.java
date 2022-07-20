package io.muffin.inventoryservice.filehandler;

import io.muffin.inventoryservice.utility.Constants;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Getter
@Setter
public abstract class FileService {

    private String fileName;
    private String identifier;
    private MultipartFile multipartFile;

    public abstract String upload();
    public abstract byte[] download();
    public abstract String delete();

    public String encryptFile() {
        String encryptedFileName = null;
        String modifiedFileName = System.currentTimeMillis() + fileName;
        encryptedFileName = DigestUtils.md5DigestAsHex(modifiedFileName.getBytes());
        log.info("ENCRYPTED_FILE_NAME => [{}]", encryptedFileName);
        return encryptedFileName;
    }
    public String getPathIdentifier() {
        log.info("IDENTIFIER => [{}]", identifier);
        if(identifier.equalsIgnoreCase(Constants.IMAGE_IDENTIFIER_USER)) {
            return Constants.IMAGE_IDENTIFIER_USER;
        }else if (identifier.equalsIgnoreCase(Constants.IMAGE_IDENTIFIER_HOSPITAL_ROOM)) {
            return Constants.IMAGE_IDENTIFIER_HOSPITAL_ROOM;
        }
        throw new RuntimeException("Invalid identifier!");
    }
}
