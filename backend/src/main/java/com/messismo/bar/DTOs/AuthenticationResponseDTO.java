package com.messismo.bar.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.messismo.bar.Entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseDTO {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("username")
    private String username;

    @JsonProperty("role")
    private Role role;
}
