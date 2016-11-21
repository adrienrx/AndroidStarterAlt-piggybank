package net.sparkeek.t2c.rest.dto;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class DTOCash {
    @JsonField(name = "ID")
    public Integer id;
    @JsonField(name = "CASH")
    public Float cash;
    @JsonField(name = "USER")
    public String user;
}
