package com.dazo.hyperativa.authentication.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    UserServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Must Load User with success")
    void mustLoadUserByUsernameSuccess(){

        String username = "davison";
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .username(username)
                .password("senha")
                .roles("USER")
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

        UserDetails userDetails = userService.loadUserByUsername(username);

        assertNotNull(userDetails);
    }

    @Test
    @DisplayName("Must throw UsernameNotFoundException when not found data")
    void mustLoadUserByUsernameThrowUsernameNotFoundException(){

        String username = "davison";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(username));
    }

    @Test
    @DisplayName("Must throw UsernameNotFoundException when not pass username")
    void mustLoadUserByUsernameNullParameterThrowUsernameNotFoundException(){

        String username = "davison";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(null));
    }


}