package net.yorksolutions.bryanturnerpantrybe.services;


import net.yorksolutions.bryanturnerpantrybe.dtos.RecipeDTO;
import net.yorksolutions.bryanturnerpantrybe.dtos.RecipeIngredientDTO;
import net.yorksolutions.bryanturnerpantrybe.models.AppUser;
import net.yorksolutions.bryanturnerpantrybe.models.Recipe;
import net.yorksolutions.bryanturnerpantrybe.models.RecipeIngredient;
import net.yorksolutions.bryanturnerpantrybe.repositories.AppUserRepository;
import net.yorksolutions.bryanturnerpantrybe.repositories.RecipeIngredientRepository;
import net.yorksolutions.bryanturnerpantrybe.repositories.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final AppUserRepository appUserRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeIngredientService recipeIngredientService;

    public RecipeService(RecipeRepository recipeRepository, AppUserRepository appUserRepository, RecipeIngredientRepository recipeIngredientRepository, RecipeIngredientService recipeIngredientService) {
        this.recipeRepository = recipeRepository;
        this.appUserRepository = appUserRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.recipeIngredientService = recipeIngredientService;
    }

    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id).orElse(null);
    }

    public void createRecipe(RecipeDTO recipeRequest) {
        AppUser appUser = appUserRepository.findById(recipeRequest.appUserId).orElseThrow();
        Set<RecipeIngredient> recipeIngredients = recipeIngredientService.createRecipeIngredients(recipeRequest.recipeIngredients);

        String appUserFullName = appUser.getFirstName() + " " + appUser.getLastName();
        Recipe recipe = new Recipe();
        recipe.setRecipeName(recipeRequest.recipeName);
        recipe.setRecipeImage(recipeRequest.recipeImage);
        recipe.setRecipeIngredients(recipeIngredients);
        recipe.setInstructions(recipeRequest.instructions);
        recipe.setAppUserId(recipeRequest.appUserId);
        recipe.setAppUserFullName(appUserFullName);
        recipe.setRecipeDescription(recipeRequest.recipeDescription);
        recipe.setCookTime(recipeRequest.cookTime);
        recipe.setPrepTime(recipeRequest.prepTime);
        Recipe savedRecipe = recipeRepository.save(recipe);

        appUser.getRecipes().add(savedRecipe);
        appUserRepository.save(appUser);
    }

    public void deleteRecipe(Long id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow();
        AppUser appUser = appUserRepository.findById(recipe.getAppUserId()).orElseThrow();

        for (RecipeIngredient recipeIngredient : recipe.getRecipeIngredients()) {
            recipeIngredient.setIngredient(null);
            recipeIngredientRepository.save(recipeIngredient);
        }

        appUser.getRecipes().remove(recipe);
        appUserRepository.save(appUser);
        recipeRepository.deleteById(id);
    }

    public Recipe updateRecipe(Long id, RecipeDTO recipeRequest) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow();
        Set<RecipeIngredient> currentRecipeIngredients = recipe.getRecipeIngredients();
        Set<RecipeIngredientDTO> newRecipeIngredientDTOS = new HashSet<>();
        Set<RecipeIngredient> updatedRecipeIngredients = new HashSet<>();

        for (RecipeIngredientDTO recipeIngredientDTO : recipeRequest.recipeIngredients) {
            // if dto does not have an id, it is a new recipe ingredient that has been added to recipe
            if (recipeIngredientDTO.id.isEmpty()) {
                newRecipeIngredientDTOS.add(recipeIngredientDTO);
            } else {
                // using dto id, finds remaining recipe ingredients entities and stores them in new set
                RecipeIngredient recipeIngredient = recipeIngredientRepository.findById(recipeIngredientDTO.id.get()).orElseThrow();
                updatedRecipeIngredients.add(recipeIngredient);
            }
        }

        // if updated set size is less than current set size, ingredient(s) have been removed
        Set<RecipeIngredient> removedRecipeIngredients = new HashSet<>();
        if (updatedRecipeIngredients.size() < currentRecipeIngredients.size()) {
            for (RecipeIngredient currentRecipeIngredient : currentRecipeIngredients) {
                // if updated set does not contain current recipe ingredient, it has been removed
                if (!updatedRecipeIngredients.contains(currentRecipeIngredient))
                    removedRecipeIngredients.add(currentRecipeIngredient);
            }
        }
        // remove recipe ingredient from recipe, save recipe changes, delete recipe ingredient
        for (RecipeIngredient removedRecipeIngredient : removedRecipeIngredients) {
            recipe.getRecipeIngredients().remove(removedRecipeIngredient);
            recipeRepository.save(recipe);
            removedRecipeIngredient.setIngredient(null);
            recipeIngredientRepository.delete(removedRecipeIngredient);
        }

        Set<RecipeIngredient> newRecipeIngredients = recipeIngredientService.createRecipeIngredients(newRecipeIngredientDTOS);
        currentRecipeIngredients.retainAll(updatedRecipeIngredients);
        currentRecipeIngredients.addAll(newRecipeIngredients);

        recipe.setRecipeIngredients(currentRecipeIngredients);
        recipe.setRecipeName(recipeRequest.recipeName);
        recipe.setRecipeImage(recipeRequest.recipeImage);
        recipe.setInstructions(recipeRequest.instructions);
        recipe.setRecipeDescription(recipeRequest.recipeDescription);
        recipe.setCookTime(recipeRequest.cookTime);
        recipe.setPrepTime(recipeRequest.prepTime);
        return recipeRepository.save(recipe);
    }

    public Iterable<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }
}


