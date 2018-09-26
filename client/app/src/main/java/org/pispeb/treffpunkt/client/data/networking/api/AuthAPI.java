package org.pispeb.treffpunkt.client.data.networking.api;

import org.pispeb.treffpunkt.server.service.domain.AuthDetails;
import org.pispeb.treffpunkt.server.service.domain.Credentials;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AuthAPI {
    @POST("auth/register")
    @Headers("Content-type: application/json")
    Call<AuthDetails> register(@Body Credentials creds);

    @POST("auth/login")
    @Headers("Content-type: application/json")
    Call<AuthDetails> login(@Body Credentials creds);
}
