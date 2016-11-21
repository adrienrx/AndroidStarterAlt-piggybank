package net.sparkeek.t2c.rest.dto;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class DTOOwner {
    @JsonField(name = "login")
    public String login;
    @JsonField(name = "id")
    public Integer id;
    @JsonField(name = "avatar_url")
    public String avatarUrl;
}
