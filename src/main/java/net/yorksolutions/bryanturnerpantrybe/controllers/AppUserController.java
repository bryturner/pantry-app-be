package net.yorksolutions.bryanturnerpantrybe.controllers;

import net.yorksolutions.bryanturnerpantrybe.dtos.AppUserDTO;
import net.yorksolutions.bryanturnerpantrybe.dtos.AppUserNoPassDTO;
import net.yorksolutions.bryanturnerpantrybe.models.AppUser;
import net.yorksolutions.bryanturnerpantrybe.services.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@RestController
@RequestMapping("/appusers")
@CrossOrigin
public class AppUserController {
    private final AppUserService service;

    public AppUserController(AppUserService service) {
        this.service = service;
    }

    @GetMapping
    public Set<AppUserNoPassDTO> getAllAppUsers(){
        return service.getAllAppUsers();
    }

    @GetMapping("/{id}")
    public AppUser getAppUserById(@PathVariable Long id) {
        try {
            return service.getAppUserById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(params = {"username", "password"})
    public AppUser getAppUserByUsernameAndPassword(@RequestParam String username, @RequestParam String password) {
        try {
            return service.getAppUserByUsernameAndPassword(username, password);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password is incorrect");
        }
    }

    @PostMapping
    public void createAppUser(@RequestBody AppUserDTO appUserRequest) {
        try {
            service.createAppUser(appUserRequest);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }
    }

    @DeleteMapping("/{id}")
    public void deleteAppUser(@PathVariable Long id) {
        try {
            service.deleteAppUser(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public AppUser updateAppUser(@PathVariable Long id, @RequestBody AppUserDTO appUserRequest) {
        try {
            return service.updateAppUser(id, appUserRequest);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
