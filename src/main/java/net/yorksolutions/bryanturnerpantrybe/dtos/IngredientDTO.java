package net.yorksolutions.bryanturnerpantrybe.dtos;

import java.util.Optional;

public class IngredientDTO {
    public Optional<Long> id;
    public String ingredientName;
    public String ingredientImage;
    public String uom;
    public Double amount;
    public Integer calories;
}
