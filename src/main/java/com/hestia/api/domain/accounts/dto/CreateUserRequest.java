package com.hestia.api.domain.accounts.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {

    private String name;
    private String email;
}
