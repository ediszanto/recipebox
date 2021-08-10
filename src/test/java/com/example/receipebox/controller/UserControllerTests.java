package com.example.receipebox.controller;

import com.example.receipebox.entity.Authority;
import com.example.receipebox.entity.User;
import com.example.receipebox.repository.UserRepository;
import com.example.receipebox.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    void testAddUserSucces() throws Exception { // testul asta este pt |controller + service|
        Authority authority = new Authority();
        authority.setName("ADMIN");

        User user = new User();
        user.setPassword("1234");
        user.setEmail("test@test.com");
        user.setFirstName("firstname");
        user.setLastName("lastname");
        user.setAuthorities(List.of(authority));

        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(user);

        when(userRepository.findUserByUsername(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);

        mockMvc.perform(
                post("/user")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("When User already exist should return forbidden!")
    void testAddUserFail() throws Exception { // testul asta este pt |controller + service|
        User user = new User();
        user.setEmail("test@test.com");

        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(user);

        when(userRepository.findUserByUsername(user.getEmail())).thenReturn(Optional.of(user));
//        when(userRepository.save(user)).thenReturn(user);

        mockMvc.perform(
                post("/user")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("When User already exist should return forbidden!")
    void testAddUserFail1() throws Exception {
        // testul asta este pt CONTROLLER
//        TODO trebuie sa ii dau si body-ul de raspuns
        Authority authority = new Authority();
        authority.setName("ADMIN");

        User user = new User();
        user.setPassword("1234");
        user.setEmail("test@test.com");
        user.setFirstName("firstname");
        user.setLastName("lastname");
        user.setAuthorities(List.of(authority));

        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(user);

        when(userService.add(user)).thenReturn(user);
        mockMvc.perform(
                post("/user")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());
    }






































}
