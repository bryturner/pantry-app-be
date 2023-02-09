package net.yorksolutions.bryanturnerpantrybe.services;

import net.yorksolutions.bryanturnerpantrybe.dtos.AppUserDTO;
import net.yorksolutions.bryanturnerpantrybe.dtos.AppUserNoPassDTO;
import net.yorksolutions.bryanturnerpantrybe.models.AppUser;
import net.yorksolutions.bryanturnerpantrybe.models.Recipe;
import net.yorksolutions.bryanturnerpantrybe.models.RecipeIngredient;
import net.yorksolutions.bryanturnerpantrybe.repositories.AppUserRepository;
import net.yorksolutions.bryanturnerpantrybe.repositories.RecipeIngredientRepository;
import net.yorksolutions.bryanturnerpantrybe.repositories.RecipeRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AppUserService {
    private final AppUserRepository appUserRepository;
    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;

    public AppUserService(AppUserRepository appUserRepository, RecipeRepository recipeRepository, RecipeIngredientRepository recipeIngredientRepository) {
        this.appUserRepository = appUserRepository;
        this.recipeRepository = recipeRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
    }

    public AppUser getAppUserById(Long id) {
        return appUserRepository.findById(id).orElse(null);
    }

    public AppUser getAppUserByUsernameAndPassword(String username, String password) throws Exception {
        Optional<AppUser> appUserOptional = appUserRepository.findAppUserByUsername(username);
        if (appUserOptional.isEmpty())
            throw new Exception();

        AppUser appUser = appUserOptional.get();

        if (!appUser.getPassword().equals(password))
            throw new Exception();

        return appUser;
    }

    public void createAppUser(AppUserDTO appUserRequest) throws Exception {
        Optional<AppUser> appUserOptional = appUserRepository.findAppUserByUsername(appUserRequest.username);
        if (appUserOptional.isPresent())
            throw new Exception();

        AppUser appUser = new AppUser();
        appUser.setUsername(appUserRequest.username);
        appUser.setPassword(appUserRequest.password);
        appUser.setFirstName(appUserRequest.firstName);
        appUser.setLastName(appUserRequest.lastName);
        appUser.setRecipes(new HashSet<>());
        appUserRepository.save(appUser);
    }

    public void deleteAppUser(Long id) {
        AppUser appUser = appUserRepository.findById(id).orElseThrow();
        for (Recipe appUserRecipe : appUser.getRecipes()) {
            Recipe recipe = recipeRepository.findById(appUserRecipe.getId()).orElseThrow();
            for (RecipeIngredient recipeIngredient : recipe.getRecipeIngredients()) {
                recipeIngredient.setIngredient(null);
                recipeIngredientRepository.save(recipeIngredient);
            }
        }
        appUserRepository.deleteById(id);
    }

    public AppUser updateAppUser(Long id, AppUserDTO appUserRequest) {
        AppUser appUser = appUserRepository.findById(id).orElseThrow();
        appUser.setUsername(appUserRequest.username);
        appUser.setPassword(appUserRequest.password);
        appUser.setFirstName(appUserRequest.firstName);
        appUser.setLastName(appUserRequest.lastName);
        AppUser savedAppUser = appUserRepository.save(appUser);
        for (Recipe userRecipe : savedAppUser.getRecipes()) {
            Recipe recipe = recipeRepository.findById(userRecipe.getId()).orElseThrow();
            recipe.setAppUserFullName(savedAppUser.getFirstName() + " " + savedAppUser.getLastName());
            recipeRepository.save(recipe);
        }
        return savedAppUser;
    }

    public Set<AppUserNoPassDTO> getAllAppUsers() {
        Iterable<AppUser> appUsers = appUserRepository.findAll();
        Set<AppUserNoPassDTO> appUserNoPassSet = new HashSet<>();
        for (AppUser appUser : appUsers) {
            AppUserNoPassDTO appUserNoPass = new AppUserNoPassDTO();
            appUserNoPass.id = appUser.getId();
            appUserNoPass.firstName = appUser.getFirstName();
            appUserNoPass.lastName = appUser.getLastName();
            appUserNoPass.username = appUser.getUsername();
            appUserNoPassSet.add(appUserNoPass);
        }

        return appUserNoPassSet;
    }
}
