package com.example.receipebox.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recipe", schema = "public")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Enumerated(EnumType.STRING)
    private RecipeDifficulty difficulty;

    @Enumerated(EnumType.STRING)
    private RecipeType type;

    @Column(name = "cooking_time")
    private Integer cookingTimeInMinutes;

    @Column(name = "created_by")
    private String createdBy;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "recipe_recipe_ingredients",
            joinColumns = @JoinColumn(name = "recipe_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_ingredients_id", referencedColumnName = "id"))
    private List<RecipeIngredient> ingredients = new ArrayList<>();

}
