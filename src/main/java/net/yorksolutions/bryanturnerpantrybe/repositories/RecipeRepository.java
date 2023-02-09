package net.yorksolutions.bryanturnerpantrybe.repositories;

import net.yorksolutions.bryanturnerpantrybe.models.Recipe;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Long> {
    Iterable<Recipe> findRecipesByAppUserId(Long appUserId);
}
