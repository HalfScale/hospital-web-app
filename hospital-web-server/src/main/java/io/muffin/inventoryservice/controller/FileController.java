package io.muffin.inventoryservice.controller;

import io.muffin.inventoryservice.model.UserDetails;
import io.muffin.inventoryservice.repository.UserDetailsRepository;
import io.muffin.inventoryservice.service.DeprecatedFileService;
import io.muffin.inventoryservice.service.FileManager;
import io.muffin.inventoryservice.utility.AuthUtil;
import io.muffin.inventoryservice.utility.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class FileController {

    private final UserDetailsRepository userDetailsRepository;
    private final AuthUtil authUtil;
    private final FileManager fileManager;

    @GetMapping(path = "/img/{identifier}/{imageHash:.+}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public byte[] getFileByIdentifier(@PathVariable String imageHash, @PathVariable String identifier) throws IOException {
        fileManager.setProperties(imageHash, identifier, null);
        return fileManager.download();
    }

    @GetMapping(path = "/user", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public byte[] getCurrentUserProfielImage() throws IOException {
        String email = authUtil.getLoggedUserEmail();
        UserDetails userDetails = userDetailsRepository.findByUsersEmail(email);

        if(userDetails.getProfileImage() != null) {
            String image = userDetails.getProfileImage();
            fileManager.setProperties(image, Constants.IMAGE_IDENTIFIER_USER, null);
            return fileManager.download();
        }

        return null;
    }
}
