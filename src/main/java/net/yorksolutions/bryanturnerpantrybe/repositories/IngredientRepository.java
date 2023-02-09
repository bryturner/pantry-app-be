package net.yorksolutions.bryanturnerpantrybe.repositories;

import net.yorksolutions.bryanturnerpantrybe.models.Ingredient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IngredientRepository extends CrudRepository<Ingredient, Long> {
}
