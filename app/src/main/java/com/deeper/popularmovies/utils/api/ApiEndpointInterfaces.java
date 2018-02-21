package com.deeper.popularmovies.utils.api;

import com.deeper.popularmovies.utils.api.model.movie.MovieResponse;
import com.deeper.popularmovies.utils.api.model.movieList.MovieListResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by paoloc on 26/01/17.
 */

public interface ApiEndpointInterfaces {

    @GET("popular")
    Call<MovieListResponse> getPopular(@QueryMap Map<String, String> params);

    @GET("top_rated")
    Call<MovieListResponse> getTopRated(@QueryMap Map<String, String> params);

    @GET("{id_movie}")
    Call<MovieResponse> getMovie(@Path("id_movie") String idMovie, @QueryMap Map<String, String> params);

    /*@GET("menu")
    Call<MenuResponse> getMenu(@QueryMap Map<String, String> params);

    @GET("newsstand")
    Call<EdicolaResponse> getNewsstand(@Query("check_subscriptions") boolean check_subscription,
                                       @QueryMap Map<String, String> params);

    @GET("archive")
    Call<ArretratiResponse> getArretrati(@QueryMap Map<String, String> params);

    @GET("device_status")
    Call<DeviceStatus> getDeviceStatus(@QueryMap Map<String, String> params);

    @GET("device/refresh-token")
    Call<KabotoResponse> getRefreshToken(@QueryMap Map<String, String> params);

    @GET("conf/service/{action}")
    Call<KabotoResponse> getServiceUrl(@Path("action") String action, @QueryMap Map<String, String> params);

    @GET("conf/service")
    Call<KabotoResponse> getConfService(@QueryMap Map<String, String> params);

    @GET("conf/service/device_type/{devicetype}")
    Call<KabotoResponse> getConfServiceDevice(@Query("devicetype") String devicetype, @QueryMap Map<String, String> params);

    @GET("auth/newspaper/{newspaper}/edition/{edition}/issue/{issue}")
    Call<AuthIssueResponse> authIssue(@Path("newspaper") String newspaper,
                                      @Path("edition") String edition,
                                      @Path("issue") String issue,
                                      @QueryMap Map<String, String> params);

    @GET("newspaper/{newspaper}/edition/{edition}/issue/{issue}/timone")
    Call <ResponseBody> getTimone(@Path("newspaper") String newspaper,
                                  @Path("edition") String edition,
                                  @Path("issue") String issue,
                                  @QueryMap Map<String, String> params);

    @GET("newspaper/{newspaper}/edition/{edition}/issue/{issue}/version/{version}/page/-/enrichment")
    Call<EnrichmentsResponse> getEnrichments(@Path("newspaper") String newspaper,
                                             @Path("edition") String edition,
                                             @Path("issue") String issue,
                                             @Path("version") String version,
                                             @QueryMap Map<String, String> params);

    @GET("edition/{edition}/articles")
    Call<ArticlesResponse> getArticles(@Path("edition") String edition,
                                       @QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST("device/buy")
    Call<PaywallResponse> deviceBuy(@Field("marketplace_code") String marketplace_code,
                                    @Field("hash") String hash,
                                    @Field("signed_data") String signed_data,
                                    @Field("signature") String signature,
                                    @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("authenticate")
    Call<LoginResponse> userLogin(@Field("user_info") String data, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("use_scalare")
    Call<ScalareResponse> scalare(@Field("packageid") String packageid,
                                  @Field("scalare_id") String scalare_id,
                                  @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("device/bind")
    Call<DeviceBindResponse> bindDevice(@Field("username") String nickname, @FieldMap Map<String, String> params);

    @DELETE("device/unbind_mobile")
    Call<KabotoResponse> unbindDevice(@QueryMap Map<String, String> params);

    @GET("gigyauserprofile")
    Call<String> getLoyalty(@QueryMap Map<String, String> params);

    *//*@HTTP(method = "DELETE",path = "device/unbind_mobile", hasBody = true)
    Call<KabotoResponse> unbindDevice(@Body Map<String, String> params);*//*

    *//*@GET("home-1.2691530")
    Call<NewsResponse> getNews();*//*

    *//*@GET(".")
    Call<NewsResponse> getNews();*//*

    @GET("{url}")
    Call<NewsResponse> getNews(@Path("url") String url);

    @FormUrlEncoded
    @POST("device_push_reg")
    Call<ResponseBody> registerPush(@FieldMap Map<String, String> params);*/
}
