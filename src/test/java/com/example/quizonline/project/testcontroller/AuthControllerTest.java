package com.example.quizonline.project.testcontroller;

import com.example.quizonline.project.entity.User;
import com.example.quizonline.project.service.EmailService;
import com.example.quizonline.project.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.security.AccessController;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(AccessController.class)
@AutoConfigureMockMvc(addFilters = false) 
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private EmailService emailService;

    private User testUser;

    @BeforeEach
    void setup() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("user@example.com");
        testUser.setPassword("encodedPass");
        testUser.setRole(User.Role.ROLE_PARTICIPANT);
    }

    @Test
    void testRegisterUserSuccess() throws Exception {
        Mockito.when(userService.registerUser(any(User.class))).thenReturn(testUser);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"user@example.com\",\"password\":\"1234\",\"role\":\"ROLE_PARTICIPANT\"}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("user@example.com"));
    }

    @Test
    void testRegisterUserFailure() throws Exception {
        Mockito.when(userService.registerUser(any(User.class)))
                .thenThrow(new RuntimeException("Duplicate email"));

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"user@example.com\",\"password\":\"1234\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Error:")));
    }

 
    @Test
    void testLoginSuccess() throws Exception {
        Mockito.when(userService.findByEmail("user@example.com"))
                .thenReturn(Optional.of(testUser));
        Mockito.when(passwordEncoder.matches("1234", "encodedPass")).thenReturn(true);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"user@example.com\",\"password\":\"1234\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Login successful")));
    }

    @Test
    void testLoginInvalidPassword() throws Exception {
        Mockito.when(userService.findByEmail("user@example.com"))
                .thenReturn(Optional.of(testUser));
        Mockito.when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"user@example.com\",\"password\":\"wrong\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Invalid credentials")));
    }

    @Test
    void testLoginUserNotFound() throws Exception {
        Mockito.when(userService.findByEmail(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"notfound@example.com\",\"password\":\"1234\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("User not found")));
    }


    @Test
    void testGetUserByEmailSuccess() throws Exception {
        Mockito.when(userService.findByEmail("user@example.com")).thenReturn(Optional.of(testUser));

        mockMvc.perform(get("/api/auth/user")
                .param("email", "user@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@example.com"));
    }

    @Test
    void testGetUserByEmailNotFound() throws Exception {
        Mockito.when(userService.findByEmail(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/auth/user")
                .param("email", "missing@example.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("User not found")));
    }

    // ==============================
    // ✅ Test: Get All users
    // ==============================
    @Test
    void testGetAllUsers() throws Exception {
        Mockito.when(userService.getAllUsers()).thenReturn(Arrays.asList(testUser));

        mockMvc.perform(get("/api/auth/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("user@example.com"));
    }
}
