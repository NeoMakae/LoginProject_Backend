package project.logininterviewproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.logininterviewproject.DTO.LoginRequest;
import project.logininterviewproject.DTO.LoginResponse;
import project.logininterviewproject.DTO.RegisterRequest;
import project.logininterviewproject.DTO.UserResponse;
import project.logininterviewproject.controllers.AuthController;
import project.logininterviewproject.service.AuthService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_Success() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("Neo");
        request.setLastName("Makae");
        request.setEmail("neo@example.com");
        request.setPassword("password123");

        when(authService.register(request)).thenReturn("User registered successfully!");

        ResponseEntity<?> response = authController.register(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User registered successfully!", response.getBody());
        verify(authService, times(1)).register(request);
    }

    @Test
    void testRegister_BadRequest() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("neo@example.com");

        when(authService.register(request)).thenThrow(new IllegalArgumentException("Username already exists!"));

        ResponseEntity<?> response = authController.register(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Username already exists!", response.getBody());
        verify(authService, times(1)).register(request);
    }

    @Test
    void testRegister_InternalServerError() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("neo@example.com");

        when(authService.register(request)).thenThrow(new RuntimeException("DB down"));

        ResponseEntity<?> response = authController.register(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Something went wrong. Please try again.", response.getBody());
        verify(authService, times(1)).register(request);
    }

    // ===== LOGIN TESTS =====

    @Test
    void testLogin_Success() {
        LoginRequest request = new LoginRequest();
        request.setEmail("neo@example.com");
        request.setPassword("password123");

        UserResponse userResponse = new UserResponse("Neo", "Makae", "neo@example.com");
        LoginResponse loginResponse = new LoginResponse("Login successful!", userResponse);

        when(authService.login(request)).thenReturn(loginResponse);

        ResponseEntity<LoginResponse> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Login successful!", response.getBody().getMessage());
        assertNotNull(response.getBody().getUser());
        verify(authService, times(1)).login(request);
    }

    @Test
    void testLogin_Unauthorized() {
        LoginRequest request = new LoginRequest();
        request.setEmail("neo@example.com");
        request.setPassword("wrongpass");

        LoginResponse loginResponse = new LoginResponse("Invalid credentials", null);
        when(authService.login(request)).thenReturn(loginResponse);

        ResponseEntity<LoginResponse> response = authController.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody().getUser());
        verify(authService, times(1)).login(request);
    }

    @Test
    void testLogin_InternalServerError() {
        LoginRequest request = new LoginRequest();
        request.setEmail("neo@example.com");

        when(authService.login(request)).thenThrow(new RuntimeException("DB down"));

        ResponseEntity<LoginResponse> response = authController.login(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Something went wrong. Please try again.", response.getBody().getMessage());
        verify(authService, times(1)).login(request);
    }

    @Test
    void testGetUser_Found() {
        String email = "neo@example.com";
        UserResponse userResponse = new UserResponse("Neo", "Makae", email);

        when(authService.getUser(email)).thenReturn(Optional.of(userResponse));

        ResponseEntity<UserResponse> response = authController.getUser(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userResponse, response.getBody());
        verify(authService, times(1)).getUser(email);
    }

    @Test
    void testGetUser_NotFound() {
        String email = "unknown@example.com";

        when(authService.getUser(email)).thenReturn(Optional.empty());

        ResponseEntity<UserResponse> response = authController.getUser(email);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(authService, times(1)).getUser(email);
    }

    @Test
    void testGetUser_InternalServerError() {
        String email = "neo@example.com";

        when(authService.getUser(email)).thenThrow(new RuntimeException("DB down"));

        ResponseEntity<UserResponse> response = authController.getUser(email);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(authService, times(1)).getUser(email);
    }
}