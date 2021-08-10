package com.example.receipebox.controller;

import com.example.receipebox.entity.Ingredient;
import com.example.receipebox.entity.Recipe;
import com.example.receipebox.entity.dto.IngredientDto;
import com.example.receipebox.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/ingredient")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;

    @PostMapping
    public ResponseEntity<Ingredient> addIngredient(@Validated @RequestBody IngredientDto ingredientDto){
        Ingredient response = ingredientService.addIngredient(ingredientDto);
        return new ResponseEntity<>(response, OK);
    }

    @PatchMapping
    public ResponseEntity<Ingredient> shoppIngredient(@RequestParam String name, @RequestParam Integer quantity){
        Ingredient ingredient = ingredientService.incrementQuantity(name, quantity);
        return new ResponseEntity<>(ingredient, OK);
    }

//    @GetMapping("/{name}")
//    public ResponseEntity<Integer> getQuantity(@PathVariable String name){
//        return new ResponseEntity<>(ingredientService.getQuantityByName(name), OK);
//    }
}
