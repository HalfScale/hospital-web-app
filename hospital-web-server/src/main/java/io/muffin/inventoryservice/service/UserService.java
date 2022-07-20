package io.muffin.inventoryservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.inventoryservice.exception.HospitalException;
import io.muffin.inventoryservice.filehandler.FileManager;
import io.muffin.inventoryservice.model.UserDetails;
import io.muffin.inventoryservice.model.dto.UserProfileRequest;
import io.muffin.inventoryservice.repository.UserDetailsRepository;
import io.muffin.inventoryservice.utility.AuthUtil;
import io.muffin.inventoryservice.utility.Constants;
import io.muffin.inventoryservice.utility.SystemUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserDetailsRepository userDetailsRepository;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    private final AuthUtil authUtil;
    private final FileManager fileManager;

    public ResponseEntity<Object> updateUserProfile(String profileDto, MultipartFile file) throws JsonProcessingException {

        String email = authUtil.getLoggedUserEmail();
        UserDetails userDetails = userDetailsRepository.findByUsersEmail(email);
        Long userDetailsId = userDetails.getId();
        String savedProfileImage = userDetails.getProfileImage();

        UserProfileRequest profileRequest = objectMapper.readValue(profileDto, UserProfileRequest.class);
        modelMapper.map(profileRequest, userDetails);
        userDetails.setId(userDetailsId);
        userDetails.setDoctorCodeId(profileRequest.getDoctorCode());
        userDetails.setProfileImage(savedProfileImage);

        log.info("UPDATED_USER_DETAILS => [{}]", objectMapper.writeValueAsString(userDetails));
        setUserProfileImage(userDetails, file);
        UserDetails savedUserDetails = userDetailsRepository.save(userDetails);

        return ResponseEntity.ok(savedUserDetails.getUsers().getId());
    }

    private void setUserProfileImage(UserDetails userDetails, MultipartFile file) {
        if (!Objects.isNull(file) && !file.isEmpty()) {
            if(!Objects.isNull(userDetails.getProfileImage())) {
                fileManager.setProperties(userDetails.getProfileImage(), Constants.IMAGE_IDENTIFIER_USER, null);
                fileManager.delete();
            }
            fileManager.setProperties(file.getOriginalFilename(), Constants.IMAGE_IDENTIFIER_USER, file);
            String hashedFile = fileManager.upload();
            userDetails.setProfileImage(hashedFile);
        }
    }

    public ResponseEntity<Object> findUserById(String userId) {
        UserDetails userDetails = userDetailsRepository.findByUsersId(Long.valueOf(userId))
                .orElseThrow(() -> new HospitalException("User id does not exist!"));


        Map<String, String> userDetailMap = new HashMap<>();
        userDetailMap.put("name", String.format("%s %s", userDetails.getFirstName(), userDetails.getLastName()));
        userDetailMap.put("firstName", userDetails.getFirstName());
        userDetailMap.put("lastName", userDetails.getLastName());
        userDetailMap.put("email", userDetails.getUsers().getEmail());
        userDetailMap.put("mobileNo", userDetails.getMobileNo());
        userDetailMap.put("gender", String.valueOf(userDetails.getGender()));
        userDetailMap.put("address", userDetails.getAddress());

        String birthDate = null;
        if(!Objects.isNull(userDetails.getBirthDate())) {
            birthDate =  SystemUtil.formatDate(userDetails.getBirthDate(), "yyyy-MM-dd");
        }
        userDetailMap.put("birthDate", birthDate);
        userDetailMap.put("profileImg", userDetails.getProfileImage());

        return ResponseEntity.ok(userDetailMap);
    }
}
