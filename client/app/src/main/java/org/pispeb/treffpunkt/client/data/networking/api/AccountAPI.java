package org.pispeb.treffpunkt.client.data.networking.api;

import org.pispeb.treffpunkt.server.service.domain.Account;
import org.pispeb.treffpunkt.server.service.domain.Position;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AccountAPI {
    @GET("accounts/{aid}/details")
    @Headers("Content-type: application/json")
    Call<Account> getDetails(@Path("aid") int aid);

    @GET("accounts/id-of/{name}")
    @Headers("Content-type: application/json")
    Call<Integer> getID(@Path("name") String name);

    @PUT("accounts/username")
    @Headers("Content-type: application/json")
    Call<Response> setUsername(@Body String username, @Body String password);

    @PUT("accounts/email")
    @Headers("Content-type: application/json")
    Call<Response> setEmail(@Body String email, @Body String password);

    @PUT("accounts/password")
    @Headers("Content-type: application/json")
    Call<Response> setPassword(@Body String oldPassword, @Body String newPassword);

    @PUT("accounts/position")
    @Headers("Content-type: application/json")
    Call<Response> updatePosition(@Body Position position);
}
