package com.example.receipebox.service;

import com.example.receipebox.entity.Ingredient;
import com.example.receipebox.entity.dto.IngredientDto;
import com.example.receipebox.exceptions.IngredientAlreadyCreatedException;
import com.example.receipebox.exceptions.IngredientNotFoundException;
import com.example.receipebox.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final EmailService emailService;

    public Ingredient addIngredient(IngredientDto ingredientDto) {
        var existingIngredient = ingredientRepository.getByName(ingredientDto.getName());
        Ingredient ingredient = new Ingredient();
        existingIngredient.ifPresentOrElse(i -> {
                throw new IngredientAlreadyCreatedException();
            }, () -> {
                ingredient.setName(ingredientDto.getName());
                ingredient.setQuantity(ingredientDto.getQuantity());
                ingredientRepository.save(ingredient);
            }
        );

        return ingredient;
    }

    public Ingredient incrementQuantity(String name, Integer quantity) {
        var existingIngredient = getByName(name);
        Integer newQuantity = existingIngredient.getQuantity() + quantity;
        existingIngredient.setQuantity(Optional.ofNullable(newQuantity).orElse(existingIngredient.getQuantity()));
        return ingredientRepository.save(existingIngredient);
    }

    public Ingredient decrementQuantity(String name, Integer quantity) {
        var existingIngredient = getByName(name);
        int newQuantity = existingIngredient.getQuantity() - quantity;
        existingIngredient.setQuantity(Optional.ofNullable(newQuantity).orElse(existingIngredient.getQuantity()));

        if (newQuantity < 5){
            emailService.sendEmail("ediiszanto@gmail.com", "edtrading96@gmail.com", name, newQuantity);
        }
        return ingredientRepository.save(existingIngredient);
    }

    public Ingredient getByName(String name){
        return ingredientRepository.getByName(name).orElseThrow(IngredientNotFoundException::new);
    }

    public Integer getQuantityByName(String name){
        return getByName(name).getQuantity();
    }
}
