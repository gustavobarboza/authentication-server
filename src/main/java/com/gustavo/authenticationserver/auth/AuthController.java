package com.gustavo.authenticationserver.auth;

import com.gustavo.authenticationserver.otp.OneTimePassword;
import com.gustavo.authenticationserver.user.User;
import com.gustavo.authenticationserver.user.UserService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("user/add")
    public void addUser(@RequestBody User newUser) {
        userService.addUser(newUser);
    }

    @PostMapping("user/auth")
    public void authenticateUser(@RequestBody User userToAuthenticate) {
        userService.auth(userToAuthenticate);
    }

    @PostMapping("otp/check")
    public ResponseEntity<Void> checkOtp(@RequestBody OneTimePassword otp) {
        boolean isValid = userService.validateOtp(otp);

        if (isValid) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
