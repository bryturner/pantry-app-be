package net.yorksolutions.bryanturnerpantrybe.services;

import net.yorksolutions.bryanturnerpantrybe.dtos.RecipeIngredientDTO;
import net.yorksolutions.bryanturnerpantrybe.models.Ingredient;
import net.yorksolutions.bryanturnerpantrybe.models.RecipeIngredient;
import net.yorksolutions.bryanturnerpantrybe.repositories.IngredientRepository;
import net.yorksolutions.bryanturnerpantrybe.repositories.RecipeIngredientRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class RecipeIngredientService {
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final IngredientRepository ingredientRepository;

    public RecipeIngredientService(RecipeIngredientRepository recipeIngredientRepository, IngredientRepository ingredientRepository) {
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public Set<RecipeIngredient> createRecipeIngredients(Iterable<RecipeIngredientDTO> recipeIngredientDTOS) {
        Set<RecipeIngredient> recipeIngredients = new HashSet<>();

        for (RecipeIngredientDTO recipeIngredientDTO : recipeIngredientDTOS) {
            Ingredient ingredient = ingredientRepository.findById(recipeIngredientDTO.ingredientId).orElseThrow();
            RecipeIngredient recipeIngredient = new RecipeIngredient();
            recipeIngredient.setAmountNeeded(recipeIngredientDTO.amountNeeded);
            recipeIngredient.setIngredient(ingredient);
            recipeIngredients.add(recipeIngredient);
        }
        recipeIngredientRepository.saveAll(recipeIngredients);
        return recipeIngredients;
    }
}
