package com.example.receipebox.entity.dto;


import com.example.receipebox.entity.RecipeDifficulty;
import com.example.receipebox.entity.RecipeIngredient;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDto {


    @NotNull(message = "Name is mandatory!")
    private String name;

    @NotNull
    private Integer cookingTime;

    @NotNull(message = "Difficulty should not be null!")
    private RecipeDifficulty difficulty;

    @NotNull(message = "Ingredients should not be null!")
    private List<RecipeIngredient> ingredients;
}
