package com.todolist.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.todolist.dto.SignUpRequest;
import com.todolist.dto.UserDto;
import com.todolist.model.User;
import com.todolist.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	
    private final UserRepository userRepository;
    private final UserServiceImpl userServiceImpl;
    
    public UserDto createUser(SignUpRequest signUpRequest) {

    	if (userRepository.findByDni(signUpRequest.getDni()).isPresent()) {
            throw new RuntimeException("El DNI '" + signUpRequest.getDni() + "' ya está registrado.");
        }
   
        if (userRepository.findByUser(signUpRequest.getUser()).isPresent()) {
            throw new RuntimeException("El nombre de usuario '" + signUpRequest.getUser() + "' ya está registrado.");
        }

        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            throw new RuntimeException("El correo electrónico '" + signUpRequest.getEmail() + "' ya está registrado.");
        }

        String nombreDeUsuario = signUpRequest.getUser();
        if (nombreDeUsuario == null || nombreDeUsuario.isEmpty()) {
            nombreDeUsuario = userServiceImpl.sugerirNombreDeUsuario(signUpRequest.getNombre(), signUpRequest.getApellido());
        }
        
        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setUser(nombreDeUsuario);  
        user.setNombre(signUpRequest.getNombre());
        user.setApellido(signUpRequest.getApellido());
        user.setDni(signUpRequest.getDni());
        user.setPassword(new BCryptPasswordEncoder().encode(signUpRequest.getPassword()));
        User createdUser = userRepository.save(user);

        return createdUser.getUserDto();
    }
}