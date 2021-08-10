package com.example.receipebox.service;

import com.example.receipebox.entity.*;
import com.example.receipebox.entity.dto.RecipeDto;
import com.example.receipebox.exceptions.ActionNotAllowedException;
import com.example.receipebox.exceptions.NotEnoughQuantityException;
import com.example.receipebox.exceptions.RecipeAlreadyCreatedException;
import com.example.receipebox.exceptions.RecipeNotFoundException;
import com.example.receipebox.repository.RecipeRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private IngredientService ingredientService;

    @InjectMocks
    private RecipeService recipeService;

    @Test
    @Disabled
    void testCreateRecipeSuccess(){

        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setName("test");

        when(recipeRepository.getRecipeByNameAndType(recipeDto.getName(), RecipeType.PUBLIC)).thenReturn(Optional.empty());
        var response = recipeService.create(recipeDto, RecipeType.PUBLIC);

//        TODO In metoda CREATE se face conversia de la dto la Recipe si trebuie si un user,
//        dar nu stiu cum sa fac asta
//        assertNotNull(response);
        verify(recipeService).create(recipeDto, RecipeType.PRIVATE);
    }

    @Test
    void testCreateRecipeFail(){
        Recipe recipe = new Recipe();

        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setName("test");

        when(recipeRepository.getRecipeByNameAndType(recipeDto.getName(), RecipeType.PUBLIC)).thenReturn(Optional.of(recipe));

        assertThrows(RecipeAlreadyCreatedException.class, () -> recipeService.create(recipeDto, RecipeType.PUBLIC));
    }

    @Test
    @DisplayName("Case when recipe is in DB but is private -> not allowed")
    void testDeleteFail1(){
        Recipe recipe = new Recipe();
        recipe.setType(RecipeType.PRIVATE);
        recipe.setId(1);
        Integer id = 1;

        when(recipeRepository.getRecipeByIdAndType(id, RecipeType.PUBLIC)).thenReturn(Optional.of(recipe));

        assertThrows(ActionNotAllowedException.class, () -> recipeService.delete(id));
    }

    @Test
    @DisplayName("Case when recipe is not in DB")
    void testDeleteFail2(){
        Integer id = 1;

        when(recipeRepository.getRecipeByIdAndType(id, RecipeType.PUBLIC)).thenReturn(Optional.empty());
        assertThrows(RecipeNotFoundException.class, () -> recipeService.delete(id));
    }

    @Test
    void testDeleteSucces(){
        Recipe recipe = new Recipe();
        recipe.setType(RecipeType.PUBLIC);
        recipe.setId(1);
        Integer id = 1;

        when(recipeRepository.getRecipeByIdAndType(id, RecipeType.PUBLIC)).thenReturn(Optional.of(recipe));

        recipeService.delete(id);

        verify(recipeRepository).delete(recipe);

    }

    @Test
    @DisplayName("Recipe not in DB -> RecipeNotFoundException")
    void testUpdateFail1(){
        Integer id = 1;

        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setName("test");

        when(recipeRepository.getRecipeByIdAndType(id, RecipeType.PUBLIC)).thenReturn(Optional.empty());

        assertThrows(RecipeNotFoundException.class, () -> recipeService.update(id, recipeDto, RecipeType.PUBLIC));
    }

    @Test
    @DisplayName("Failing because public user tries to update private recipe!")
    void testUpdateFail2(){
        Integer id = 1;

        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setName("test");

        Recipe recipe = new Recipe();
        recipe.setType(RecipeType.PRIVATE);

        when(recipeRepository.getRecipeByIdAndType(id, RecipeType.PUBLIC)).thenReturn(Optional.of(recipe));

        assertThrows(ActionNotAllowedException.class, () -> recipeService.update(id, recipeDto, RecipeType.PUBLIC));
    }

    @Test
    void testUpdateSucess(){
        Integer id = 1;

        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setName("test");

        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setName("apple");
        recipeIngredient.setQuantity(1);

        Recipe recipe = new Recipe();
        recipe.setName("test");
        recipe.setType(RecipeType.PUBLIC);
        recipe.setDifficulty(RecipeDifficulty.BEGINNER);
        recipe.setIngredients(List.of(recipeIngredient));
        recipe.setCookingTimeInMinutes(12);

        when(recipeRepository.getRecipeByIdAndType(id, RecipeType.PUBLIC)).thenReturn(Optional.of(recipe));

        recipeService.update(id, recipeDto, RecipeType.PUBLIC);

        verify(recipeRepository).save(recipe);
    }

    @Test
    @DisplayName("Test all variants of getRecipes method when the response is empty so RecipeNotFoundException is thrown")
    void testGetPublicRecipesFail() {

        when(recipeRepository.getRecipeByIdAndType(1, RecipeType.PUBLIC)).thenReturn(Optional.empty());
        when(recipeRepository.getAllByDifficultyAndType("ADVANCED", RecipeType.PUBLIC)).thenReturn(Optional.empty());
        when(recipeRepository.getRecipeByNameAndType("name", RecipeType.PUBLIC)).thenReturn(Optional.empty());
        when(recipeRepository.getAllByTimeAndType(15, RecipeType.PUBLIC)).thenReturn(Optional.empty());
        when(recipeRepository.getAllByDifficultyAndTime("ADVANCED", 15, RecipeType.PUBLIC)).thenReturn(Optional.empty());
        when(recipeRepository.getAllByNameAndDifficulty("name", "ADVANCED", RecipeType.PUBLIC)).thenReturn(Optional.empty());
        when(recipeRepository.getAllByNameAndDifficultyAndTime("name", "ADVANCED", 15, RecipeType.PUBLIC)).thenReturn(Optional.empty());
        when(recipeRepository.getAllByNameAndTime("name", 15, RecipeType.PUBLIC)).thenReturn(Optional.empty());

        assertThrows(RecipeNotFoundException.class, () -> recipeService.getRecipeByIdAndType(1, RecipeType.PUBLIC));
        assertThrows(RecipeNotFoundException.class, () -> recipeService.getRecipes("name", RecipeType.PUBLIC));
        assertThrows(RecipeNotFoundException.class, () -> recipeService.getRecipes(15, RecipeType.PUBLIC));
        assertThrows(RecipeNotFoundException.class, () -> recipeService.getRecipes(RecipeDifficulty.ADVANCED, RecipeType.PUBLIC));
        assertThrows(RecipeNotFoundException.class, () -> recipeService.getRecipes("name", 15, RecipeType.PUBLIC));
        assertThrows(RecipeNotFoundException.class, () -> recipeService.getRecipes("name", RecipeDifficulty.ADVANCED, RecipeType.PUBLIC));
        assertThrows(RecipeNotFoundException.class, () -> recipeService.getRecipes("name", RecipeDifficulty.ADVANCED, 15, RecipeType.PUBLIC));
        assertThrows(RecipeNotFoundException.class, () -> recipeService.getRecipes(RecipeDifficulty.ADVANCED, 15, RecipeType.PUBLIC));
    }

    @Test
    void testGetPublicRecipesSucces() {

        Recipe recipe = new Recipe();
        recipe.setName("name");

        when(recipeRepository.getRecipeByIdAndType(1, RecipeType.PUBLIC)).thenReturn(Optional.of(recipe));
        when(recipeRepository.getAllByDifficultyAndType("ADVANCED", RecipeType.PUBLIC)).thenReturn(Optional.of(List.of(recipe)));
        when(recipeRepository.getRecipeByNameAndType("name", RecipeType.PUBLIC)).thenReturn(Optional.of(recipe));
        when(recipeRepository.getAllByTimeAndType(15, RecipeType.PUBLIC)).thenReturn(Optional.of(List.of(recipe)));
        when(recipeRepository.getAllByDifficultyAndTime("ADVANCED", 15, RecipeType.PUBLIC)).thenReturn(Optional.of(List.of(recipe)));
        when(recipeRepository.getAllByNameAndDifficulty("name", "ADVANCED", RecipeType.PUBLIC)).thenReturn(Optional.of(List.of(recipe)));
        when(recipeRepository.getAllByNameAndDifficultyAndTime("name", "ADVANCED", 15, RecipeType.PUBLIC)).thenReturn(Optional.of(List.of(recipe)));
        when(recipeRepository.getAllByNameAndTime("name", 15, RecipeType.PUBLIC)).thenReturn(Optional.of(List.of(recipe)));

        var response1 = recipeService.getRecipeByIdAndType(1, RecipeType.PUBLIC);
        var response2 = recipeService.getRecipes("name", RecipeType.PUBLIC);
        var response3 = recipeService.getRecipes(15, RecipeType.PUBLIC);
        var response4 = recipeService.getRecipes(RecipeDifficulty.ADVANCED, RecipeType.PUBLIC);
        var response5 = recipeService.getRecipes("name", 15, RecipeType.PUBLIC);
        var response6 = recipeService.getRecipes("name", RecipeDifficulty.ADVANCED, RecipeType.PUBLIC);
        var response7 = recipeService.getRecipes("name", RecipeDifficulty.ADVANCED, 15, RecipeType.PUBLIC);
        var response8 = recipeService.getRecipes(RecipeDifficulty.ADVANCED, 15, RecipeType.PUBLIC);

        assertNotNull(response1);
        assertNotNull(response2);
        assertNotNull(response3);
        assertNotNull(response4);
        assertNotNull(response5);
        assertNotNull(response6);
        assertNotNull(response7);
        assertNotNull(response8);

        assertEquals("name", response1.getName());
        assertEquals("name", response2.get(0).getName());
        assertEquals("name", response3.get(0).getName());
        assertEquals("name", response4.get(0).getName());
        assertEquals("name", response5.get(0).getName());
        assertEquals("name", response6.get(0).getName());
        assertEquals("name", response7.get(0).getName());
        assertEquals("name", response8.get(0).getName());
    }

    @Test
    @DisplayName("Recipe is not found in DB -> RecipeNotFoundException")
    void testCookFail1(){
        when(recipeRepository.getByName("name")).thenReturn(Optional.empty());
        assertThrows(RecipeNotFoundException.class, () -> recipeService.cook("name"));
    }

    @Test
    @Disabled
    @DisplayName("Quantity in Store is smaller that the quantity wanted -> NotEnoughQuantityException")
    void testCookFail2(){
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setName("apple");
        recipeIngredient.setQuantity(3);

        Recipe recipe = new Recipe();
        recipe.setName("name");
        recipe.setIngredients(List.of(recipeIngredient));

        when(recipeRepository.getByName("name")).thenReturn(Optional.of(recipe));
//        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("cook@email.com");
        when(ingredientService.getQuantityByName("apple")).thenReturn(2);

        assertThrows(NotEnoughQuantityException.class, () -> recipeService.cook("name"));
    }

    @Test
    @Disabled
    @DisplayName("Recipe wanted to cook is not awailable in DB")
    void testCookFail3(){
        when(recipeRepository.getRecipeByNameAndType("name", RecipeType.PUBLIC)).thenReturn(Optional.empty());
        assertThrows(RecipeNotFoundException.class, () -> recipeService.cook("name"));
    }

    @Test
    @Disabled
    void testCookSucces(){
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setName("apple");
        recipeIngredient.setQuantity(3);

        Recipe recipe = new Recipe();
        recipe.setName("name");
        recipe.setIngredients(List.of(recipeIngredient));

        when(recipeRepository.getRecipeByNameAndType("name", RecipeType.PUBLIC)).thenReturn(Optional.of(recipe));
        when(ingredientService.getQuantityByName("apple")).thenReturn(3);

       var response = recipeService.cook("name");

        verify(ingredientService).decrementQuantity("apple", 3);
    }
}
