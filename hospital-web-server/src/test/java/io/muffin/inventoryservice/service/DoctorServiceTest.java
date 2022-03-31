package io.muffin.inventoryservice.service;

import io.muffin.inventoryservice.model.DoctorCode;
import io.muffin.inventoryservice.model.UserDetails;
import io.muffin.inventoryservice.model.dto.DoctorProfileResponse;
import io.muffin.inventoryservice.repository.DoctorCodeRepository;
import io.muffin.inventoryservice.repository.UserDetailsRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class DoctorServiceTest {

    @Mock
    private DoctorCodeRepository doctorCodeRepository;
    @Mock
    private UserDetailsRepository userDetailsRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private DoctorService doctorService;

    @Test
    public void findDoctorByUserId() {

        when(modelMapper.map(Mockito.any(), Mockito.eq(DoctorProfileResponse.class))).thenReturn(new DoctorProfileResponse());
        when(userDetailsRepository.findByUsersId(Mockito.anyLong())).thenReturn(Optional.of(new UserDetails()));
        when(doctorCodeRepository.findByDoctorCode(Mockito.any())).thenReturn(Optional.of(new DoctorCode()));

        assertNotNull(doctorService.findDoctorByUserId("1"));
    }
}
