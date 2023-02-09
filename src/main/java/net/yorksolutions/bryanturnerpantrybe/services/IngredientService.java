package net.yorksolutions.bryanturnerpantrybe.services;

import net.yorksolutions.bryanturnerpantrybe.dtos.AdjustIngoAmountsDTO;
import net.yorksolutions.bryanturnerpantrybe.dtos.AdjustAmountDTO;
import net.yorksolutions.bryanturnerpantrybe.dtos.IngredientDTO;
import net.yorksolutions.bryanturnerpantrybe.models.Ingredient;
import net.yorksolutions.bryanturnerpantrybe.models.Recipe;
import net.yorksolutions.bryanturnerpantrybe.models.RecipeIngredient;
import net.yorksolutions.bryanturnerpantrybe.repositories.IngredientRepository;
import net.yorksolutions.bryanturnerpantrybe.repositories.RecipeRepository;
import org.springframework.stereotype.Service;

@Service
public class IngredientService {
    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;

    public IngredientService(IngredientRepository ingredientRepository, RecipeRepository recipeRepository) {
        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;
    }

    public Iterable<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    public Ingredient getIngredientById(Long id) {
        return ingredientRepository.findById(id).orElse(null);
    }

    public void createIngredient(IngredientDTO ingredientRequest) {
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientName(ingredientRequest.ingredientName);
        ingredient.setIngredientImage(ingredientRequest.ingredientImage);
        ingredient.setUom(ingredientRequest.uom);
        ingredient.setAmount(ingredientRequest.amount);
        ingredient.setCalories(ingredientRequest.calories);
        ingredientRepository.save(ingredient);
    }

    public void updateIngredient(Long id, IngredientDTO ingredientRequest) {
        Ingredient ingredient = ingredientRepository.findById(id).orElseThrow();
        ingredient.setIngredientName(ingredientRequest.ingredientName);
        ingredient.setIngredientImage(ingredientRequest.ingredientImage);
        ingredient.setUom(ingredientRequest.uom);
        ingredient.setAmount(ingredientRequest.amount);
        ingredient.setCalories(ingredientRequest.calories);
        ingredientRepository.save(ingredient);
    }

    public void adjustIngredientAmounts(AdjustIngoAmountsDTO adjustRequest) throws Exception {
        for (AdjustAmountDTO adjustAmountDTO : adjustRequest.adjustAmountDTOS) {
            Ingredient ingredient = ingredientRepository.findById(adjustAmountDTO.ingredientId).orElseThrow();

            if (adjustAmountDTO.isAddingAmount) {
                ingredient.setAmount(ingredient.getAmount() + adjustAmountDTO.amountToAdjust);
            } else {
                if (ingredient.getAmount() < adjustAmountDTO.amountToAdjust)
                    throw new Exception();

                ingredient.setAmount(ingredient.getAmount() - adjustAmountDTO.amountToAdjust);
            }

            ingredientRepository.save(ingredient);
        }
    }

    public void deleteIngredient(Long id) throws Exception{
        Ingredient ingredient = ingredientRepository.findById(id).orElseThrow();
        Iterable<Recipe> recipes = recipeRepository.findAll();
        for (Recipe recipe : recipes) {
            for (RecipeIngredient recipeIngredient : recipe.getRecipeIngredients()) {
                if (recipeIngredient.getIngredient().getId().equals(ingredient.getId()))
                    throw new Exception();
            }
        }
        ingredientRepository.deleteById(ingredient.getId());
    }
}
