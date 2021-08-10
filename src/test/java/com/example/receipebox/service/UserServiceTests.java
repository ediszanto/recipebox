package com.example.receipebox.service;

import com.example.receipebox.entity.User;
import com.example.receipebox.exceptions.UserAlreadyRegisteredException;
import com.example.receipebox.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;


    @Test
    @DisplayName("If User is already created should return UserAlreadyRegisteredException")
    void testIfUserIsAlreadyCreated(){
        User user = new User();
        user.setEmail("test@test.com");

        when(userRepository.findUserByUsername(user.getEmail())).thenReturn(Optional.of(user)); // prerequisites
        assertThrows(UserAlreadyRegisteredException.class, () -> userService.add(user));
    }

    @Test
    @DisplayName("If user is not created, should create one and return it")
    void testIfUserNotAlreadyCreated(){
        User user = new User();
        user.setEmail("tset@test.com");
        user.setPassword("1234");

        when(userRepository.findUserByUsername(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);

        var result = userService.add(user);
        verify(userRepository).save(user);
        assertEquals(user.getEmail(), result.getEmail());
    }

//    @Test
//    void test(){
//        Integer a = 10;
//        Integer b = 20;
//        var c = a-b;
//        System.out.println();
//    }
}
