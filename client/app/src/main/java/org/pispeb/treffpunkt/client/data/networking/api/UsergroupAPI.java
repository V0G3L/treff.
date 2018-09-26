package org.pispeb.treffpunkt.client.data.networking.api;

import org.pispeb.treffpunkt.server.service.domain.Event;
import org.pispeb.treffpunkt.server.service.domain.Usergroup;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UsergroupAPI {
    @GET("groups")
    @Headers("Content-type: application/json")
    Call<List<Integer>> list();

    @POST("groups")
    @Headers("Content-type: application/json")
    Call<Integer> create(@Body Usergroup group);

    @GET("groups/{gid}")
    @Headers("Content-type: application/json")
    Call<Usergroup> getDetails(@Path("gid") int gid);

    @PUT("groups/{gid}")
    @Headers("Content-type: application/json")
    Call<Response> edit(@Path("gid") int gid, @Body Usergroup usergroup);

    @POST("groups/{gid}/members")
    @Headers("Content-type: application/json")
    Call<Response> addMembers(@Path("gid") int gid, @Body List<Integer> memberIds);

    @DELETE("groups/{gid}/members")
    @Headers("Content-type: application/json")
    Call<Response> removeMembers(@Path("gid") int gid, @Body List<Integer> memberIds);

    @POST("groups/{gid}/leave")
    @Headers("Content-type: application/json")
    Call<Response> leaveGroup(@Path("gid") int gid);

    @POST("groups/{gid}/events")
    @Headers("Content-type: application/json")
    Call<Integer> addEvent(@Path("gid") int gid, @Body Event event);

    @DELETE("groups/{gid}/events/{eid}")
    @Headers("Content-type: application/json")
    Call<Response> removeEvent(@Path("gid") int gid, @Path("eid") int eid);

    @POST("groups/{gid}/chat")
    @Headers("Content-type: application/json")
    Call<Response> sendChatMessage(@Path("gid") int gid, @Body String chatMessage);

    @POST("groups/{gid}/positionrequest")
    @Headers("Content-type: application/json")
    Call<Response> sendPositionRequest(@Path("gid") int gid, @Body long untilTimestamp);

    @PUT("groups/{gid}/positionsharing")
    @Headers("Content-type: application/json")
    Call<Response> sharePosition(@Path("gid") int gid, @Body long untilTimestamp);

    @GET("groups/{gid}/events/{eid}")
    @Headers("Content-type: application/json")
    Call<Event> getEventDetails(@Path("gid") int gid, @Path("eid") int eid);

    @PUT("groups/{gid}/events/{eid}")
    @Headers("Content-type: application/json")
    Call<Response> editEvent(@Path("gid") int gid, @Path("eid") int eid, @Body Event event);

    @PUT("groups/{gid}/events/{eid}/participation")
    @Headers("Content-type: application/json")
    Call<Response> joinEvent(@Path("gid") int gid, @Path("eid") int eid);

    @DELETE("groups/{gid}/events/{eid}/participation")
    @Headers("Content-type: application/json")
    Call<Response> leaveEvent(@Path("gid") int gid, @Path("eid") int eid);
}
