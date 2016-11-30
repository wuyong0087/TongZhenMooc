package com.tzhen.mooc.net.drupal;

import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.entities.LoginJson;
import com.tongzhen.mooc.entities.RegisterJson;
import com.tongzhen.mooc.entities.UserInfoJson;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface Endpoints {

    @FormUrlEncoded
    @POST("?mod=wetube&act=login")
    Observable<LoginJson> login(@Field("mobile") String mobile, @Field("password") String password, @FieldMap Map<String, String> filedMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=my_info")
    Observable<UserInfoJson> my_info(@Field("uid") String uid, @FieldMap Map<String, String> filedMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=register")
    Observable<RegisterJson> register(@Field("username") String username, @Field("password") String password, @Field("nickname") String nickname, @Field("sex") int sex, @Field("country") int country, @Field("description") String description,@FieldMap  Map<String, String> filedMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=check_username")
    Observable<BaseInfo> check_username(@Field("username") String username, @FieldMap Map<String, String> filedMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=check_username")
    Observable<BaseInfo> check_nickname(@Field("nickname") String nickname, @FieldMap Map<String, String> filedMap);
}