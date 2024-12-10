package com.theplutushome.optimus.dto.resetPassword;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class PasswordResetRequest {
    @NotBlank(message = "username required")
    private String username;
    @NotBlank(message = "old password required")
    private String oldPassword;
    @NotBlank(message = "new password required")
    private String newPassword;

    public PasswordResetRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public PasswordResetRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
