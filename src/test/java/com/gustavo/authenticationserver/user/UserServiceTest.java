package com.gustavo.authenticationserver.user;

import com.gustavo.authenticationserver.otp.OneTimePasswordRepository;
import org.assertj.core.api.BDDAssumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OneTimePasswordRepository oneTimePasswordRepository;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserService(passwordEncoder, userRepository, oneTimePasswordRepository);
    }

    @Test
    public void shouldAddNewUserToDatabase() {
        // given
        User user = new User();
        user.setUsername("user_name");
        user.setPassword("user_password");

        given(passwordEncoder.encode("user_password")).willReturn("encoded_password");

        // when
        userService.addUser(user);

        // then
        verify(userRepository, times(1)).save(user);
    }
}