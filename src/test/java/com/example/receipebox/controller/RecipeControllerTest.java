package com.example.receipebox.controller;

import com.example.receipebox.entity.Recipe;
import com.example.receipebox.entity.RecipeDifficulty;
import com.example.receipebox.entity.RecipeIngredient;
import com.example.receipebox.entity.RecipeType;
import com.example.receipebox.entity.dto.RecipeDto;
import com.example.receipebox.exceptions.RecipeAlreadyCreatedException;
import com.example.receipebox.repository.RecipeRepository;
import com.example.receipebox.service.RecipeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RecipeControllerTest {

    @MockBean
    private RecipeRepository recipeRepository;

    @MockBean
    private RecipeService recipeService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Disabled
    void testAddRecipeSucces() throws Exception {

        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setName("apple");
        recipeIngredient.setQuantity(1);

        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setName("test");
        recipeDto.setDifficulty(RecipeDifficulty.BEGINNER);
        recipeDto.setIngredients(List.of(recipeIngredient));
        recipeDto.setCookingTime(12);

        Recipe recipe = new Recipe();
        recipe.setName("test");
        recipe.setDifficulty(RecipeDifficulty.BEGINNER);
        recipe.setIngredients(List.of(recipeIngredient));
        recipe.setCookingTimeInMinutes(12);

        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(recipeDto);

//        when(recipeRepository.getRecipeByNameAndType(recipeDto.getName(), RecipeType.PUBLIC.toString())).thenReturn(Optional.empty());
        when(recipeService.create(recipeDto, RecipeType.PUBLIC)).thenReturn(recipe);

        mockMvc.perform(
                post("/recipe/public")
                        .with(user("test@test.com").password("1234").roles("ADMIN"))
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());
    }

    @Test
    @Disabled
    void testAddRecipeFail() throws Exception {
        Recipe recipe = new Recipe();

        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setName("apple");
        recipeIngredient.setQuantity(1);

        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setName("test");
        recipeDto.setDifficulty(RecipeDifficulty.BEGINNER);
        recipeDto.setIngredients(List.of(recipeIngredient));
        recipeDto.setCookingTime(12);

        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(recipeDto);

//        when(recipeRepository.getRecipeByNameAndType(recipeDto.getName(), RecipeType.PUBLIC.toString())).thenReturn(Optional.of(recipe));
        when(recipeService.create(recipeDto, RecipeType.PUBLIC)).thenThrow(RecipeAlreadyCreatedException.class);

        mockMvc.perform(
                post("/recipe/public")
                        .with(user("user").password("1234").roles("ADMIN"))
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

    @Test
    @Disabled
    void testDeleteRecipeFail() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setType(RecipeType.PRIVATE);
        recipe.setId(1);
        Integer id = 1;

        when(recipeRepository.getRecipeByIdAndType(id, RecipeType.PUBLIC)).thenReturn(Optional.empty());

        mockMvc.perform(
                delete("/recipe/1")
                    .with(user("user").password("1234").roles("ADMIN"))
        ).andExpect(status().isForbidden());
    }
}
