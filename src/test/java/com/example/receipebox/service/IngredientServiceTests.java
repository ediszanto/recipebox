package com.example.receipebox.service;

import com.example.receipebox.entity.Ingredient;
import com.example.receipebox.entity.dto.IngredientDto;
import com.example.receipebox.exceptions.IngredientAlreadyCreatedException;
import com.example.receipebox.exceptions.IngredientNotFoundException;
import com.example.receipebox.repository.IngredientRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class IngredientServiceTests {

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private IngredientService ingredientService;

    @Test
    @DisplayName("Ingredient is already added in store -> IngredientAlreadyCreatedException")
    void addIngredientTestFail() {
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setName("name");

        Ingredient ingredient = new Ingredient();

        when(ingredientRepository.getByName("name")).thenReturn(Optional.of(ingredient));
        assertThrows(IngredientAlreadyCreatedException.class, () -> ingredientService.addIngredient(ingredientDto));
    }

    @Test
    void addIngredientTestSucces(){
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setName("name");
        ingredientDto.setQuantity(10);

        Ingredient ingredient = new Ingredient();
        ingredient.setName("name");
        ingredient.setQuantity(10);

        when(ingredientRepository.getByName("name")).thenReturn(Optional.empty());
        Ingredient response = ingredientService.addIngredient(ingredientDto);

        log.info("response - " + response);
        verify(ingredientRepository).save(ingredient);
    }

    @Test
    void getByNameTestFail() {
        when(ingredientRepository.getByName("name")).thenReturn(Optional.empty());
        assertThrows(IngredientNotFoundException.class, () -> ingredientService.getByName("name"));
    }

    @Test
    void getByNameTestSuccess() {
        Ingredient ingredient = new Ingredient();
        ingredient.setName("name");

        when(ingredientRepository.getByName("name")).thenReturn(Optional.of(ingredient));
        var response = ingredientService.getByName("name");

        assertNotNull(response);
        assertEquals(ingredient, response);
    }

    @Test
    void incrementQuantityTest() {
        Ingredient ingredient = new Ingredient();
        ingredient.setName("name");
        ingredient.setQuantity(5);

        Ingredient ingredientToSave = new Ingredient();
        ingredientToSave.setName("name");
        ingredientToSave.setQuantity(6);

        when(ingredientRepository.getByName("name")).thenReturn(Optional.of(ingredient));

        var response = ingredientService.incrementQuantity("name", 1);
        log.info("response - " + response); // vine NULL
        verify(ingredientRepository).save(ingredientToSave);

    }

    @Test
    void decrementQuantityTest() {
        Ingredient ingredient = new Ingredient();
        ingredient.setName("name");
        ingredient.setQuantity(5);

        Ingredient ingredientToSave = new Ingredient();
        ingredientToSave.setName("name");
        ingredientToSave.setQuantity(4);

        when(ingredientRepository.getByName("name")).thenReturn(Optional.of(ingredient));
        var response = ingredientService.decrementQuantity("name", 1);

        verify(ingredientRepository).save(ingredientToSave);
        verify(emailService).sendEmail("ediiszanto@gmail.com", "edtrading96@gmail.com", "name", 4);
    }
}
