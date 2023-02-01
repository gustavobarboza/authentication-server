package com.gustavo.authenticationserver.user;

import com.gustavo.authenticationserver.otp.OneTimePassword;
import com.gustavo.authenticationserver.otp.OneTimePasswordRepository;
import com.gustavo.authenticationserver.otp.OneTimePasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@Transactional
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final OneTimePasswordRepository otpRepo;
    Logger logger = Logger.getLogger(UserService.class.getName());

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, OneTimePasswordRepository oneTimePasswordRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.otpRepo = oneTimePasswordRepository;
    }

    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void auth(User userToAuthenticate) {
        Optional<User> userOptional = userRepository.findUserByUsername(userToAuthenticate.getUsername());
        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();
            if (passwordEncoder.matches(userToAuthenticate.getPassword(), existingUser.getPassword())) {
                logger.info("User logged in");
                renewOtp(existingUser);
            } else {
                logger.info("Password invalid");
                throw new BadCredentialsException("Bad credentials");
            }
        } else {
            logger.info("User not found");
            throw new BadCredentialsException("Bad credentials");
        }
    }

    public boolean validateOtp(OneTimePassword otpToValidate) {
        Optional<OneTimePassword> otpOptional = otpRepo.findOneTimePasswordByUsername(otpToValidate.getUsername());
        if (otpOptional.isPresent()) {
            OneTimePassword otp = otpOptional.get();
            return otp.getCode().equals(otpToValidate.getCode());
        }

        return false;
    }

    private void renewOtp(User user) {
        String code = OneTimePasswordUtils.generateRandomOtp();

        logger.info(String.format("Otp generated: %s", code));

        Optional<OneTimePassword> otpOptional = otpRepo.findOneTimePasswordByUsername(user.getUsername());
        OneTimePassword otp;
        if (otpOptional.isPresent()) {
            otp = otpOptional.get();
            otp.setCode(code);
        } else {
            otp = new OneTimePassword(user.getUsername(), code);
        }
        otpRepo.save(otp);
    }

}
