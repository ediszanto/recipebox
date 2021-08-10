package com.example.receipebox.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngredientDto {

    @NotNull(message = "Name cannot be NULL")
    private String name;

    @NotNull(message = "Quantity cannot be NULL")
    @Positive(message = "Quantity must be a positive number")
    private Integer quantity;
}
