package net.sparkeek.piggybank.rest;

import net.sparkeek.piggybank.rest.dto.DTORepo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubService {
    @GET("/users/{user}/repos")
    Call<List<DTORepo>> listRepos(@Path("user") final String psUser);
}
