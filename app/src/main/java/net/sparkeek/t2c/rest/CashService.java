package net.sparkeek.t2c.rest;

import net.sparkeek.t2c.rest.dto.DTOCash;
import net.sparkeek.t2c.rest.dto.DTORepo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CashService {
    @GET("pig/engine.php")
    Call<List<DTOCash>> listCash();
}
