package org.pispeb.treffpunkt.client.data.networking.api;

import org.pispeb.treffpunkt.server.service.domain.ContactList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ContactsAPI {
    @GET("contacts")
    @Headers("Content-type: application/json")
    Call<ContactList> getContactList();

    @POST("contacts/{aid}")
    @Headers("Content-type: application/json")
    Call<Response> sendRequest(@Path("aid") int aid);

    @DELETE("contacts/{aid}")
    @Headers("Content-type: application/json")
    Call<Response> removeContact(@Path("aid") int aid);

    @POST("contacts/{aid}/accept")
    @Headers("Content-type: application/json")
    Call<Response> acceptRequest(@Path("aid") int aid);

    @DELETE("contacts/{aid}/deny")
    @Headers("Content-type: application/json")
    Call<Response> rejectRequest(@Path("aid") int aid);

    @DELETE("contacts/{aid}/cancel")
    @Headers("Content-type: application/json")
    Call<Response> cancelRequest(@Path("aid") int aid);

    @POST("contacts/{aid}/block")
    @Headers("Content-type: application/json")
    Call<Response> block(@Path("aid") int aid);

    @DELETE("contacts/{aid}/block")
    @Headers("Content-type: application/json")
    Call<Response> unblock(@Path("aid") int aid);
}
