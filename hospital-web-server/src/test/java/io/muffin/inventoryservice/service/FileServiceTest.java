package io.muffin.inventoryservice.service;

import io.muffin.inventoryservice.utility.Constants;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class FileServiceTest {

    private @InjectMocks
    FileService fileService;

    private static final String TEST_FILE_DIR = System.getProperty("user.dir");
    private static final String TEST_FILE_IDENTIFIER = "profile";
    private static final String TEST_FILE_INPUT = "709testdsdwed0994444";

    @Test
    public void testUploadToLocalFileSystem() throws IOException {
        ReflectionTestUtils.setField(fileService, "storageDirectoryPath", TEST_FILE_DIR);
        Long entityId = 1L;
        Path createdDirectory = this.createTestDirectory();

        // create a file to be uploaded in the system
        String filePath = String.format("%s%s%s", TEST_FILE_DIR, File.separator + Constants.IMAGE_IDENTIFIER_USER, File.separator + TEST_FILE_INPUT);
        File inputFile = new File(filePath);
        inputFile.createNewFile();

        //upload the file to the system
        MultipartFile multipartFile = new MockMultipartFile(TEST_FILE_INPUT, new FileInputStream(inputFile));
        fileService.setEntityId(entityId);
        fileService.setIdentifier(Constants.IMAGE_IDENTIFIER_USER);
        fileService.setFile(multipartFile);

        assertNotNull(fileService.uploadToLocalFileSystem());

        //delete all files that is used for testing
        String fileUploadedToTheSystem = DigestUtils.md5DigestAsHex(String.valueOf(entityId).getBytes());
        String uploadedFilePath = String.format("%s%s%s", TEST_FILE_DIR, File.separator + Constants.IMAGE_IDENTIFIER_USER, File.separator + fileUploadedToTheSystem);
        File uploadedFile = new File(uploadedFilePath);

        uploadedFile.delete();
        inputFile.delete();
        this.deleteTestDirectory(createdDirectory);
    }

    @Test
    public void testGetImageWithMediaType() throws IOException {
        ReflectionTestUtils.setField(fileService, "storageDirectoryPath", TEST_FILE_DIR);
        String filePath = String.format("%s%s%s", TEST_FILE_DIR, File.separator + TEST_FILE_IDENTIFIER, File.separator + TEST_FILE_INPUT);

        Path createdDirectory = this.createTestDirectory();

        File inputFile = new File(filePath);
        inputFile.createNewFile();

        assertNotNull(fileService.getImageWithMediaType(TEST_FILE_INPUT, TEST_FILE_IDENTIFIER));

        inputFile.delete();

        this.deleteTestDirectory(createdDirectory);

    }

    private Path createTestDirectory() throws IOException {
        String dirPath = String.format("%s%s", TEST_FILE_DIR, File.separator + TEST_FILE_IDENTIFIER);
        Path testDir = Paths.get(dirPath);
        Files.createDirectories(testDir);
        return testDir;
    }

    private void deleteTestDirectory(Path testDir) throws IOException {
        Files.delete(testDir);
    }

    @Test
    public void testGetIdentifierPath() {
        assertNotNull(fileService.getIdentifierPath(Constants.IMAGE_IDENTIFIER_USER));
    }

    @Test
    public void testGetIdentifierPath_ThrowError() {
        assertThrows(RuntimeException.class, () -> fileService.getIdentifierPath("INVALID_IDENTIFIER"));
    }
}
