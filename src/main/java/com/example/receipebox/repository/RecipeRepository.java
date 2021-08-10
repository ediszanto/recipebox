package com.example.receipebox.repository;

import com.example.receipebox.entity.Recipe;
import com.example.receipebox.entity.RecipeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {


    @Query("select r from Recipe r where r.name= :name")
    Optional<Recipe> getByName(String name);

    @Query("select r from Recipe r where r.name= :name and r.type= :type")
    Optional<Recipe> getRecipeByNameAndType(String name, RecipeType type);

    @Query("select r from Recipe r where r.id= :id and r.type= :type")
    Optional<Recipe> getRecipeByIdAndType(Integer id, RecipeType type);

    @Query("select r from Recipe r where r.cookingTimeInMinutes= :time and r.type= :type")
    Optional<List<Recipe>> getAllByTimeAndType(Integer time, RecipeType type);

    @Query("select r from Recipe r where r.difficulty= :recipeDifficulty and r.type= :type")
    Optional<List<Recipe>> getAllByDifficultyAndType(String recipeDifficulty, RecipeType type);

    @Query("select r from Recipe r where r.name= :name and r.difficulty= :recipeDifficulty and r.cookingTimeInMinutes= :time and r.type= :type")
    Optional<List<Recipe>> getAllByNameAndDifficultyAndTime(String name, String recipeDifficulty, Integer time, RecipeType type);

    @Query("select r from Recipe r where r.name= :name and r.difficulty= :recipeDifficulty and r.type= :type")
    Optional<List<Recipe>> getAllByNameAndDifficulty(String name, String recipeDifficulty, RecipeType type);

    @Query("select r from Recipe r where r.name= :name and r.cookingTimeInMinutes= :time and r.type= :type")
    Optional<List<Recipe>> getAllByNameAndTime(String name, Integer time, RecipeType type);

    @Query("select r from Recipe r where r.difficulty= :recipeDifficulty and r.cookingTimeInMinutes= :time and r.type= :type")
    Optional<List<Recipe>> getAllByDifficultyAndTime(String recipeDifficulty, Integer time, RecipeType type);
}
