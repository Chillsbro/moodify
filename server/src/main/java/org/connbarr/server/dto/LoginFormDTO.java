package org.connbarr.server.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class LoginFormDTO {

    @NotNull
    @NotBlank
    private String username;

    @NotNull
    @NotBlank
    @Size(min = 2, max = 30, message = "Invalid Password, must be between 2-30 characters")
    private String password;

    public @NotNull @NotBlank String getUsername() {
        return username;
    }

    public @NotNull @NotBlank @Size(min = 2, max = 30, message = "Invalid Password, must be between 2-30 characters") String getPassword() {
        return password;
    }

    public void setUsername(@NotNull @NotBlank String username) {
        this.username = username;
    }

    public void setPassword(@NotNull @NotBlank @Size(min = 2, max = 30, message = "Invalid Password, must be between 2-30 characters") String password) {
        this.password = password;
    }
}
