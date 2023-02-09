package net.yorksolutions.bryanturnerpantrybe.controllers;

import net.yorksolutions.bryanturnerpantrybe.dtos.AdjustIngoAmountsDTO;
import net.yorksolutions.bryanturnerpantrybe.dtos.IngredientDTO;
import net.yorksolutions.bryanturnerpantrybe.models.Ingredient;
import net.yorksolutions.bryanturnerpantrybe.services.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/ingredients")
@CrossOrigin
public class IngredientController {
    private final IngredientService service;

    public IngredientController(IngredientService service) {
        this.service = service;
    }

    @GetMapping
    public Iterable<Ingredient> getAllIngredients() {
        return service.getAllIngredients();
    }

    @GetMapping("/{id}")
    public Ingredient getIngredientById(@PathVariable Long id) {
        try {
            return service.getIngredientById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public void createIngredient(@RequestBody IngredientDTO ingredientRequest) {
        try {
            service.createIngredient(ingredientRequest);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED);
        }
    }

    @PutMapping
    public void adjustIngredientAmounts(@RequestBody AdjustIngoAmountsDTO adjustRequest) {
        try {
            service.adjustIngredientAmounts(adjustRequest);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public void updateIngredient(@PathVariable Long id, @RequestBody IngredientDTO ingredientRequest) {
        try {
            service.updateIngredient(id, ingredientRequest);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteIngredient(@PathVariable Long id) {
        try {
            service.deleteIngredient(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED);
        }
    }
}
