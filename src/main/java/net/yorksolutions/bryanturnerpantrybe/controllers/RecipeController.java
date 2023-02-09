package net.yorksolutions.bryanturnerpantrybe.controllers;

import net.yorksolutions.bryanturnerpantrybe.dtos.RecipeDTO;
import net.yorksolutions.bryanturnerpantrybe.models.Recipe;
import net.yorksolutions.bryanturnerpantrybe.services.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/recipes")
@CrossOrigin
public class RecipeController {
    private final RecipeService service;

    public RecipeController(RecipeService service) {
        this.service = service;
    }

    @GetMapping
    public Iterable<Recipe> getAllRecipes() {
        return service.getAllRecipes();
    }

    @GetMapping("/{id}")
    public Recipe getRecipeById(@PathVariable Long id) {
        try {
            return service.getRecipeById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public void createRecipe(@RequestBody RecipeDTO recipeRequest) {
        try {
            service.createRecipe(recipeRequest);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteRecipe(@PathVariable Long id) {
        try {
            service.deleteRecipe(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public Recipe updateRecipe(@PathVariable Long id, @RequestBody RecipeDTO recipeRequest) {
        try {
            return service.updateRecipe(id, recipeRequest);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
