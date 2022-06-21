package io.muffin.inventoryservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.inventoryservice.service.AmazonS3Service;
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
    private final AmazonS3Service awss3Service;

    @PostMapping(path = "/upload", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> mvcPost(@RequestParam(value = "file", required = false) MultipartFile file) {
        log.info("UPLOADING_AWS_FILE => [{}]", "");
        awss3Service.uploadFile("identifier", file);
        return ResponseEntity.ok(1);
    }

    @GetMapping(path = "/download/{fileName}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public byte[] mvcGet(@PathVariable String fileName) {
        log.info("DOWNLOADING_AWS_FILE => [{}]", fileName);
        return awss3Service.downloadFile(fileName);
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