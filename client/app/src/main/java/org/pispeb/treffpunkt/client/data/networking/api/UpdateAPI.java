package org.pispeb.treffpunkt.client.data.networking.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface UpdateAPI {
    @GET("updates")
    @Headers("Content-type: application/json")
    Call<List<String>> getUpdates();
}
