package io.muffin.inventoryservice.controller;

import io.muffin.inventoryservice.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class FileController {

    private final FileService fileService;

    @GetMapping(path = "/img/{identifier}/{imageHash:.+}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public byte[] getImageWithMediaType(@PathVariable String imageHash, @PathVariable String identifier) throws IOException {
        return fileService.getImageWithMediaType(imageHash, identifier);
    }
}
