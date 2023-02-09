package net.yorksolutions.bryanturnerpantrybe.repositories;

import net.yorksolutions.bryanturnerpantrybe.models.RecipeIngredient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeIngredientRepository extends CrudRepository<RecipeIngredient, Long> {
}
