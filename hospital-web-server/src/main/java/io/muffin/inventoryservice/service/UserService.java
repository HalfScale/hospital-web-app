package io.muffin.inventoryservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.inventoryservice.model.UserDetails;
import io.muffin.inventoryservice.model.dto.UserProfileRequest;
import io.muffin.inventoryservice.repository.UserDetailsRepository;
import io.muffin.inventoryservice.utility.AuthUtil;
import io.muffin.inventoryservice.utility.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserDetailsRepository userDetailsRepository;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    private final AuthUtil authUtil;
    private final FileService fileService;

    public ResponseEntity<Object> updateUserProfile(String profileDto, MultipartFile multipartFile) throws JsonProcessingException {

        String email = authUtil.getLoggedUserEmail();
        UserDetails userDetails = userDetailsRepository.findByUsersEmail(email);
        Long userDetailsId = userDetails.getId();
        String savedProfileImage = userDetails.getProfileImage();

        UserProfileRequest profileRequest = objectMapper.readValue(profileDto, UserProfileRequest.class);

        modelMapper.typeMap(UserProfileRequest.class, UserDetails.class)
                        .addMappings(mapper -> {
                           mapper.map(src -> src.getDoctorCode(), (target, v) -> target.setDoctorCodeId((String) v));
                        });
        modelMapper.map(profileRequest, userDetails);
        userDetails.setId(userDetailsId);

        log.info("UPDATED_USER_DETAILS => [{}]", objectMapper.writeValueAsString(userDetails));
        if (!Objects.isNull(multipartFile)) {
            fileService.setEntityId(userDetails.getUsers().getId());
            fileService.setIdentifier(fileService.getIdentifierPath(Constants.IMAGE_IDENTIFIER_USER));
            fileService.setFile(multipartFile);

            String hashedFile = fileService.uploadToLocalFileSystem();
            log.info("HASHED_FILE => [{}]", hashedFile);

            userDetails.setProfileImage(hashedFile);
        }else {
            // assign the previous profileImage
            userDetails.setProfileImage(savedProfileImage);
        }

        userDetailsRepository.save(userDetails);

        return ResponseEntity.ok().build();
    }
}
