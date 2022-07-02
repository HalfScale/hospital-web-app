package io.muffin.inventoryservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.inventoryservice.service.DeprecatedAmazonS3Service;
import io.muffin.inventoryservice.service.FileManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/aws/s3")
public class AWSS3Controller {

    private final ObjectMapper objectMapper;
    private final DeprecatedAmazonS3Service awss3Service;
    private final FileManager fileManager;

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> mvcPost(@RequestParam("identifier") String identifier,
                                          @RequestParam(value = "file", required = false) MultipartFile file) {
        log.info("UPLOADING_AWS_FILE => [{}]", identifier);
        log.info("UPLOADING_AWS_FILE_EMPTY? => [{}]", file.isEmpty());
        fileManager.setProperties(file.getOriginalFilename(), identifier, file);
        return ResponseEntity.ok(fileManager.upload());
    }

    @PostMapping(path = "/upload/exist", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> uploadExisting(@RequestParam("identifier") String identifier,
                                                 @RequestParam("exist") String exist,
                                          @RequestParam(value = "file", required = false) MultipartFile file) {
        log.info("UPLOADING_AWS_FILE => [{}]", identifier);
        log.info("UPLOADING_AWS_FILE_EMPTY? => [{}]", file.isEmpty());
        fileManager.setProperties(exist, identifier, null);
        fileManager.delete();
        fileManager.setProperties(file.getOriginalFilename(), identifier, file);

        return ResponseEntity.ok(fileManager.upload());
    }

    @GetMapping(path = "/download/{identifier}/{fileName}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public byte[] mvcGet(@PathVariable String identifier, @PathVariable String fileName) {
        log.info("DOWNLOADING_AWS_FILE => [{}]", fileName);
        fileManager.setProperties(fileName, identifier, null);
        return fileManager.download();
    }

    @PutMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> mvcPut() {
        log.info("PUT => [{}]", "");
        return null;
    }

    @DeleteMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> mvcDelete() {
        log.info("DELETE => [{}]", "");
        return null;
    }
}