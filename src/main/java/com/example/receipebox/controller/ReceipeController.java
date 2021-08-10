package com.example.receipebox.controller;

import com.example.receipebox.aspect.Log;
import com.example.receipebox.entity.Recipe;
import com.example.receipebox.entity.RecipeDifficulty;
import com.example.receipebox.entity.RecipeType;
import com.example.receipebox.entity.dto.RecipeDto;
import com.example.receipebox.service.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/recipe")
@RequiredArgsConstructor
@EnableAspectJAutoProxy
@Slf4j
public class ReceipeController {

    private final RecipeService recipeService;

    @PostMapping("/public")
    @Log
    public ResponseEntity<Recipe> createPublic(@Validated @RequestBody RecipeDto recipeDto) {
        Recipe result = recipeService.create(recipeDto, RecipeType.PUBLIC);
        return new ResponseEntity<>(result, CREATED);
    }

    @Log
    @PostMapping("/private")
    public ResponseEntity<Recipe> createPrivate(@Validated @RequestBody RecipeDto recipeDto) {
        Recipe result = recipeService.create(recipeDto, RecipeType.PRIVATE);
        return new ResponseEntity<>(result, CREATED);
    }

    @Log
    @PostMapping("/update/{id}")
    public ResponseEntity<Recipe> updatePublic(@RequestBody RecipeDto recipeDto, @PathVariable Integer id) {
        recipeService.update(id, recipeDto, RecipeType.PUBLIC);
        return new ResponseEntity<>(OK);
    }

    @Log
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        recipeService.delete(id);
        return new ResponseEntity<>(OK);
    }

    @Log
    @GetMapping("/public")
    public ResponseEntity<List<Recipe>> getPublicRecipes(
                                                @RequestParam(name = "name") Optional<String> name,
                                                @RequestBody Optional<RecipeDifficulty> difficulty,
                                                @RequestParam(name = "time") Optional<Integer> time) {
//        TODO have to figure out how to pass Enum through @RequestBody
        return new ResponseEntity<>(recipeFinder(name, difficulty, time, RecipeType.PUBLIC), OK);
    }

    @Log
    @GetMapping("/private")
    public ResponseEntity<List<Recipe>> getPrivateRecipes(
                                                @RequestParam(name = "name") Optional<String> name,
                                                @RequestBody Optional<RecipeDifficulty> difficulty,
                                                @RequestParam(name = "time") Optional<Integer> time) {
        return new ResponseEntity<>(recipeFinder(name, difficulty, time, RecipeType.PRIVATE), OK);
    }

    private List<Recipe> recipeFinder(Optional<String> name, Optional<RecipeDifficulty> difficulty, Optional<Integer> time, RecipeType recipeType) {
        List<Recipe> recipes = new ArrayList<>();

        if (name.isPresent() && difficulty.isPresent() && time.isPresent()) {
            recipes = recipeService.getRecipes(name.get(), difficulty.get(), time.get(), recipeType);
        } else if (name.isPresent() && difficulty.isPresent()) {
            recipes = recipeService.getRecipes(name.get(), difficulty.get(), recipeType);
        } else if (difficulty.isPresent() && time.isPresent()) {
            recipes = recipeService.getRecipes(difficulty.get(), time.get(), recipeType);
        } else if (name.isPresent() && time.isPresent()) {
            recipes = recipeService.getRecipes(name.get(), time.get(), recipeType);
        } else if (name.isPresent()) {
            recipes = recipeService.getRecipes(name.get(), recipeType);
        } else if (difficulty.isPresent()) {
            recipes = recipeService.getRecipes(difficulty.get(), recipeType);
        } else if (time.isPresent()) {
            recipes = recipeService.getRecipes(time.get(), recipeType);
        }
        return recipes;
    }

    @Log
    @GetMapping("/{recipeName}")
    public ResponseEntity<Recipe> cook(@PathVariable String recipeName){
        return new ResponseEntity<>(recipeService.cook(recipeName), OK);
    }
}
