package com.digicade.web.rest;

import com.digicade.domain.User;
import com.digicade.repository.UserRepository;
import com.digicade.security.jwt.JWTFilter;
import com.digicade.security.jwt.TokenProvider;
import com.digicade.service.UserService;
import com.digicade.service.dto.AdminUserDTO;
import com.digicade.web.rest.vm.LoginVM;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth")
public class OAuthController {

    private final Logger log = LoggerFactory.getLogger(OAuthController.class);

    private final UserService userService;

    private final UserRepository userRepository;

    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public OAuthController(
        UserService userService,
        UserRepository userRepository,
        TokenProvider tokenProvider,
        AuthenticationManagerBuilder authenticationManagerBuilder
    ) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/register")
    public ResponseEntity<User> oauthRegister(@Valid @RequestBody AdminUserDTO userDTO) {
        log.debug("REST request to save User : {}", userDTO);
        User user = userService.createUser(userDTO);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/login/{email}")
    public ResponseEntity<User> oAuthLoginUser(@PathVariable("email") String email) {
        log.debug("REST request to login User : {}", email);
        Optional<User> optional = userService.findUserByEmail(email);
        return new ResponseEntity<>(optional.get(), HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<UserJWTController.JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            loginVM.getUsername(),
            loginVM.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, loginVM.isRememberMe());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new UserJWTController.JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }
}
