package com.example.cmpshop.auth.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Permission {
    USER_CREATE("user_create"),
    USER_READ("user_read"),
    USER_UPDATE("user_update"),
    USER_DELETE("user_delete"),
    POST_CREATE("post_create"),
    POST_READ("post_read"),
    POST_UPDATE("post_update"),
    POST_DELETE("post_delete");

    private final String permissionName;

}
