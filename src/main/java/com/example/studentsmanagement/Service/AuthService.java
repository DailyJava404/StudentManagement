package com.example.studentsmanagement.Service;

import com.example.studentsmanagement.Entity.UserInfo;
import com.example.studentsmanagement.Enum.Role;
import com.example.studentsmanagement.Interface.IAuthService;
import com.example.studentsmanagement.Model.Request.LoginRequest;
import com.example.studentsmanagement.Model.Request.RegisterRequest;
import com.example.studentsmanagement.Model.Response.ApiResponse;
import com.example.studentsmanagement.Model.Response.AuthResponse;
import com.example.studentsmanagement.Repository.UserRepository;
import com.example.studentsmanagement.Security.JwtUtil;
import com.example.studentsmanagement.Security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {

    private final UserRepository _userRepository;
    private final PasswordEncoder _passwordEncoder;
    private final AuthenticationManager _authenticationManager;
    private final JwtUtil _jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil)
    {
        _userRepository = userRepository;
        _passwordEncoder = passwordEncoder;
        _authenticationManager = authenticationManager;
        _jwtUtil = jwtUtil;
    }

    @Override
    public ApiResponse<AuthResponse> register(RegisterRequest request) {
        if (_userRepository.existsByUsername(request.getUsername()))
        {
            logger.error("Register failed - username already exists: {}", request.getUsername());
            return ApiResponse.fail(409, "Username is already exists");
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(request.getUsername());
        userInfo.setPassword(_passwordEncoder.encode(request.getPassword()));
        userInfo.setRole(Role.Admin);

        _userRepository.save(userInfo);
        logger.debug("New user saved to DB: {}", userInfo);
        String token = _jwtUtil.generateToken(userInfo);
        logger.info("Register successful for username: {}", request.getUsername());

        return ApiResponse.success(new AuthResponse(token, userInfo.getUsername(), userInfo.getRole()));
    }

    @Override
    public ApiResponse<AuthResponse> login(LoginRequest request) {
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
            _authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            UserInfo userInfo = _userRepository.findByUsername(request.getUsername()).orElse(null);
            if (userInfo == null) {
                return ApiResponse.fail(404, "Username not found");
            }

            String token = _jwtUtil.generateToken(userInfo);
            return ApiResponse.success(new AuthResponse(token, userInfo.getUsername(), userInfo.getRole()));
        } catch (Exception e) {
            return ApiResponse.fail(401, "Invalid username or password");
        }
    }
}
