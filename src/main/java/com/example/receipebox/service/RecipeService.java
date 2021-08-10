package com.example.receipebox.service;

import com.example.receipebox.entity.*;
import com.example.receipebox.entity.dto.RecipeDto;
import com.example.receipebox.exceptions.NotEnoughQuantityException;
import com.example.receipebox.exceptions.RecipeAlreadyCreatedException;
import com.example.receipebox.exceptions.RecipeNotFoundException;
import com.example.receipebox.exceptions.ActionNotAllowedException;
import com.example.receipebox.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static java.text.MessageFormat.format;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RecipeService {



    private final RecipeRepository recipeRepository;
    private final IngredientService ingredientService;

    public Recipe create(RecipeDto recipeDto, RecipeType recipeType) {
        var existingReceipe = recipeRepository.getRecipeByNameAndType(recipeDto.getName(), recipeType);
        Recipe recipe = new Recipe();
        existingReceipe.ifPresentOrElse(r -> {
            throw new RecipeAlreadyCreatedException(format("recipe - {0} - already created!", recipeDto.getName()));
        }, () -> {
            recipe.setName(recipeDto.getName());
            recipe.setType(recipeType);
            recipe.setDifficulty(recipeDto.getDifficulty());
            recipe.setCookingTimeInMinutes(recipeDto.getCookingTime());
            recipe.setIngredients(recipeDto.getIngredients());
            recipe.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
            recipeRepository.save(recipe);
        });
        return recipe;
    }

    public void update(Integer id, RecipeDto recipeDto, RecipeType recipeType) {
        Recipe recipe = getRecipeByIdAndType(id, recipeType);
        if (recipe.getType().toString().equals("PRIVATE")) {
            throw new ActionNotAllowedException("Cannot update PRIVATE recipes");
        }
        recipe.setName(Optional.ofNullable(recipeDto.getName()).orElse(recipe.getName()));
        recipe.setDifficulty(Optional.ofNullable(recipeDto.getDifficulty()).orElse(recipe.getDifficulty()));
        recipe.setIngredients(Optional.ofNullable(recipeDto.getIngredients()).orElse(recipe.getIngredients()));
        recipe.setCookingTimeInMinutes(Optional.ofNullable(recipeDto.getCookingTime()).orElse(recipe.getCookingTimeInMinutes()));
        recipe.setType(recipe.getType());
        recipe.setCreatedBy(recipe.getCreatedBy());
        recipeRepository.save(recipe);
    }

    public void delete(Integer id) {
        var recipeToDelete = recipeRepository.getRecipeByIdAndType(id, RecipeType.PUBLIC);

        recipeToDelete.ifPresentOrElse(u -> {
            if (recipeToDelete.get().getType().toString().equals("PRIVATE")) {
                throw new ActionNotAllowedException("Cannot delete PRIVATE recipes");
            }
            recipeRepository.delete(recipeToDelete.get());
        }, () -> {
            throw new RecipeNotFoundException();
        });
    }

    public Recipe getRecipeByIdAndType(Integer id, RecipeType recipeType) {
        return recipeRepository.getRecipeByIdAndType(id, recipeType).orElseThrow(RecipeNotFoundException::new);
    }

    public List<Recipe> getRecipes(String name, RecipeType recipeType) {
        var response = recipeRepository.getRecipeByNameAndType(name, RecipeType.PUBLIC);
        if (response.isEmpty()) {
            throw new RecipeNotFoundException();
        }
        return List.of(response.get());
    }

    public List<Recipe> getRecipes(String name, RecipeDifficulty recipeDifficulty, Integer time, RecipeType recipeType) {
        var response = recipeRepository.getAllByNameAndDifficultyAndTime(name, recipeDifficulty.name(), time, recipeType);
        responseCheck(response);
        return response.get();
    }

    public List<Recipe> getRecipes(String name, RecipeDifficulty recipeDifficulty, RecipeType recipeType) {
        var response = recipeRepository.getAllByNameAndDifficulty(name, recipeDifficulty.name(), recipeType);
        responseCheck(response);
        return response.get();
    }

    public List<Recipe> getRecipes(String name, Integer time, RecipeType recipeType) {
        var response = recipeRepository.getAllByNameAndTime(name, time, recipeType);
        responseCheck(response);
        return response.get();
    }

    public List<Recipe> getRecipes(RecipeDifficulty recipeDifficulty, Integer time, RecipeType recipeType) {
        var response = recipeRepository.getAllByDifficultyAndTime(recipeDifficulty.name(), time, recipeType);
        responseCheck(response);
        return response.get();
    }

    public List<Recipe> getRecipes(RecipeDifficulty recipeDifficulty, RecipeType recipeType) {
        var response = recipeRepository.getAllByDifficultyAndType(recipeDifficulty.name(), recipeType);
        responseCheck(response);
        return response.get();
    }

    public List<Recipe> getRecipes(Integer time, RecipeType recipeType) {
        var response = recipeRepository.getAllByTimeAndType(time, recipeType);
        responseCheck(response);
        return response.get();
    }

    private void responseCheck(Optional<List<Recipe>> response) {
        if (response.isEmpty()) {
            throw new RecipeNotFoundException();
        }
    }

    private Recipe getByNameAndType(String name, RecipeType recipeType) {
        var response = recipeRepository.getRecipeByNameAndType(name, recipeType);
        if (response.isEmpty()) {
            throw new RecipeNotFoundException();
        }
        return response.get();
    }


    private Recipe getByName(String name) {
        var response = recipeRepository.getByName(name);
        if(response.isEmpty()){
            throw new RecipeNotFoundException();
        }
        return response.get();
    }

    public Recipe cook(String recipeName) {
        var recipe = getByName(recipeName);

        if(recipe.getType().equals(RecipeType.PRIVATE) &&
                !recipe.getCreatedBy().equals(SecurityContextHolder.getContext().getAuthentication().getName())){
            throw new ActionNotAllowedException("You are allowed to see only PUBLIC and your PRIVATE recipes");
        }
        recipe.getIngredients().forEach(recipeIngredient -> verifyStoreQuantity(recipeIngredient));
        recipe.getIngredients().forEach(recipeIngredient ->
                ingredientService.decrementQuantity(recipeIngredient.getName(), recipeIngredient.getQuantity())
        );
        return recipe;
    }

    private void verifyStoreQuantity(RecipeIngredient desiredIngredient) {
        var dbIngredientQuantity = ingredientService.getQuantityByName(desiredIngredient.getName());
        if (dbIngredientQuantity < desiredIngredient.getQuantity()) {
            throw new NotEnoughQuantityException();
        }
    }
}
