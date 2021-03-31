package com.nikchat.ppmtool.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nikchat.ppmtool.domain.User;
import com.nikchat.ppmtool.payload.JWTLoginSucessResponse;
import com.nikchat.ppmtool.payload.LoginRequest;
import com.nikchat.ppmtool.security.JwtTokenProvider;
import com.nikchat.ppmtool.services.MapValidationErrorService;
import com.nikchat.ppmtool.services.UserService;
import com.nikchat.ppmtool.validator.UserValidator;
import static com.nikchat.ppmtool.security.SecurityConstants.TOKEN_PREFIX;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserValidator userValidator;
    
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result){
        // Validate passwords match
    	userValidator.validate(user,result);

        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(result);
        if(errorMap != null) return errorMap;

        User newUser = userService.saveUser(user);

        return  new ResponseEntity<User>(newUser, HttpStatus.CREATED);
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult result){
    	
        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(result);
        if(errorMap != null) return errorMap;

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = TOKEN_PREFIX +  tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTLoginSucessResponse(true, jwt));
    }
}
