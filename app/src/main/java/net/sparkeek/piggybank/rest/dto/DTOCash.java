package net.sparkeek.piggybank.rest.dto;

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
