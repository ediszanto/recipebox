package com.example.receipebox.repository;

import com.example.receipebox.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    @Query("select i from Ingredient i where i.name= :name")
    Optional<Ingredient> getByName(String name);
}
