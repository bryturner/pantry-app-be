package net.yorksolutions.bryanturnerpantrybe.dtos;

import javax.persistence.Column;
import java.util.Optional;

public class RecipeDTO {
    public Optional<Long> id;
    public String recipeName;
    public String recipeImage;
    public String instructions;
    public Iterable<RecipeIngredientDTO> recipeIngredients;
    public Long appUserId;
    public Integer prepTime;
    public Integer cookTime;
    public String recipeDescription;
}
