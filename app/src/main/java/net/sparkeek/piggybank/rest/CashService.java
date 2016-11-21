package net.sparkeek.piggybank.rest;

import net.sparkeek.piggybank.rest.dto.DTOCash;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CashService {
    @GET("pig/engine.php")
    Call<List<DTOCash>> listCash();
}
