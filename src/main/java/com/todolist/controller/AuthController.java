package com.todolist.controller;

import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.todolist.dto.AuthenticationRequest;
import com.todolist.dto.AuthenticationResponse;
import com.todolist.dto.SignUpRequest;
import com.todolist.dto.UserDto;
import com.todolist.model.User;
import com.todolist.repository.UserRepository;
import com.todolist.service.AuthService;
import com.todolist.service.UserService;
import com.todolist.service.UserServiceImpl;
import com.todolist.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api/auth") 
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final UserServiceImpl userServiceImpl;

    @PostMapping("/signup")
    public ResponseEntity<?> signUpUser(@RequestBody SignUpRequest signUpRequest) {
        try {
            UserDto createdUser = authService.createUser(signUpRequest);
            return new ResponseEntity<>(createdUser, HttpStatus.OK);
        } catch (RuntimeException e) {  
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            return new ResponseEntity<>("Ocurrió un error", HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect username or password", e);
        }

        final UserDetails userDetails = userService.userDetailsService().loadUserByUsername(authenticationRequest.getEmail());
        Optional<User> optionalUser = userRepository.findByEmail(authenticationRequest.getEmail()).or(() -> userRepository.findByUser(authenticationRequest.getEmail()));
        final String jwt = jwtUtil.generateToken(userDetails);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        if (optionalUser.isPresent()) {
            authenticationResponse.setJwt(jwt);
            authenticationResponse.setUserId(optionalUser.get().getId());
            authenticationResponse.setName(optionalUser.get().getNombre());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt); 

        return new ResponseEntity<>(authenticationResponse, headers, HttpStatus.OK);
    }
    
    
    @GetMapping("/sugerir-nombre-usuario")
    public String sugerirNombreUsuario(@RequestParam String nombre, @RequestParam String apellido) {
        return userServiceImpl.sugerirNombreDeUsuario(nombre, apellido);
    }
    
    @GetMapping("/sugerir-contraseña")
    public String generarContraseñaSegura() {
        return userServiceImpl.generarContraseñaSegura();
    }
}