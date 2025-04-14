package com.castores.api.controller;

import com.castores.api.dto.Data;
import com.castores.api.dto.Response;
import com.castores.api.dto.TransactionService;
import com.castores.api.dto.requests.LoginRequest;
import com.castores.api.dto.requests.RegisterRequest;
import com.castores.api.entities.User;
import com.castores.api.entities.Role;
import com.castores.api.exception.RepositoryException;
import com.castores.api.repository.RoleRepository;
import com.castores.api.repository.UserRepository;
import com.castores.api.utils.Constants;
import com.castores.api.utils.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource(name = "getTransactionService")
    private final TransactionService transactionService;

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, UserRepository userRepository, TransactionService transactionService, RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.transactionService= transactionService;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/register")
    public Response register(@Valid @RequestBody RegisterRequest request) {
        Response response = new Response();
        Data data = new Data();

        User existingUser = userRepository.findFirstByUsername(request.getUsername());
        if (existingUser != null) {
            throw new RepositoryException(Constants.CODE_ERROR_REQUEST_INVALID,Constants.MSJE_ERROR_REQUEST_INVALID,"Username already exist");
        }

        Role userRole =  roleRepository.findFirstById((long) request.getRole());
        if (userRole == null) {
            throw new RepositoryException(Constants.CODE_ERROR_REQUEST_INVALID,Constants.MSJE_ERROR_REQUEST_INVALID,"Role does not exist");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(userRole);

        userRepository.save(user);

        this.transactionService.setMeta(Constants.CODE_SUCCESS, Constants.MSJE_SAVE_SUCCESS);
        data.setMessage("User registered successfully");
        response.setData(data);
        response.setMeta(this.transactionService.getMeta());
        return response;
    }

    @PostMapping("/login")
    public Response login(@Valid @RequestBody LoginRequest request) {
        Response response = new Response();
        Data data = new Data();

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword())
            );

            String jwt = jwtUtil.generateToken(authentication);

            this.transactionService.setMeta(Constants.CODE_SUCCESS, Constants.MSJE_READ_SUCCESS);
            data.setToken(jwt);
            response.setData(data);
            response.setMeta(this.transactionService.getMeta());
            return response;

        } catch (BadCredentialsException e) {
            throw new RepositoryException(Constants.CODE_ERROR_REQUEST_INVALID,Constants.MSJE_ERROR_REQUEST_INVALID,"Credenciales inválidas");
        } catch (Exception e) {
            throw new RepositoryException(Constants.CODE_ERROR_SERVICE_INTERNAL,"Error interno: " + e.getMessage(),"");
        }
    }
}
