package com.tzhen.mooc.net.drupal;

import com.tongzhen.mooc.entities.AnswerListInfo;
import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.entities.CountryListInfo;
import com.tongzhen.mooc.entities.CourseIntroduceInfo;
import com.tongzhen.mooc.entities.QuestionListInfo;
import com.tongzhen.mooc.entities.TitleListInfo;
import com.tongzhen.mooc.entities.UserInfo;
import com.tongzhen.mooc.entities.UserVerifyInfo;
import com.tongzhen.mooc.entities.UserVerifyParams;
import com.tongzhen.mooc.entities.WorksListInfo;
import com.tzhen.mooc.entities.drupal.AnswerListInfoJson;
import com.tzhen.mooc.entities.drupal.ChatListInfoJson;
import com.tzhen.mooc.entities.drupal.CityListInfoJson;
import com.tzhen.mooc.entities.drupal.CommentListInfoJson;
import com.tzhen.mooc.entities.drupal.CountryListInfoJson;
import com.tzhen.mooc.entities.drupal.CourseEditInfoJson;
import com.tzhen.mooc.entities.drupal.CourseIntroduceInfoJson;
import com.tzhen.mooc.entities.drupal.CourseListInfoJson;
import com.tzhen.mooc.entities.drupal.LoginJson;
import com.tzhen.mooc.entities.drupal.QuestionInfoJson;
import com.tzhen.mooc.entities.drupal.QuestionListInfoJson;
import com.tzhen.mooc.entities.drupal.RegisterJson;
import com.tzhen.mooc.entities.drupal.SendCodeInfoJson;
import com.tzhen.mooc.entities.drupal.SubjectListInfoJson;
import com.tzhen.mooc.entities.drupal.TitleListInfoJson;
import com.tzhen.mooc.entities.drupal.UserInfoJson;
import com.tzhen.mooc.entities.drupal.UserListInfoJson;
import com.tzhen.mooc.entities.drupal.UserVerifyInfoJson;
import com.tzhen.mooc.entities.drupal.WorksInfoJson;
import com.tzhen.mooc.entities.drupal.WorksListInfoJson;

import java.util.List;
import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

public interface Endpoints {

    @FormUrlEncoded
    @POST("?mod=wetube&act=login")
    Observable<LoginJson> login(@Field("mobile") String mobile, @Field("password") String password, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=my_info")
    Observable<UserInfoJson> my_info(@Field("uid") String uid, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=register")
    Observable<RegisterJson> register(@Field("username") String username, @Field("password") String password, @Field("nickname") String nickname, @Field("sex") int sex, @Field("country") int country, @Field("description") String description, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=check_username")
    Observable<BaseInfo> check_username(@Field("username") String username, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=check_username")
    Observable<BaseInfo> check_nickname(@Field("nickname") String nickname, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=user_edit")
    Observable<UserInfoJson> user_edit(@Field("uid") String uid, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=up_psd")
    Observable<BaseInfo> up_psd(@Field("uid") String uid, @Field("old_psd") String old_psd, @Field("new_psd") String new_psd, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=up_fagpsd")
    Observable<BaseInfo> up_fagpsd(@Field("username") String username, @Field("new_psd") String new_psd, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=send_code")
    Observable<SendCodeInfoJson> send_code(@Field("username") String username, @Field("type") String type, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=my_video")
    Observable<WorksListInfoJson> my_video(@Field("uid") String uid, @Field("type") String type, @Field("page") String page, @Field("max") String max, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=my_share")
    Observable<WorksListInfoJson> my_share(@Field("uid") String uid, @Field("page") String page, @Field("max") String max, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=my_collection")
    Observable<WorksListInfoJson> my_collection(@Field("uid") String uid, @Field("page") String page, @Field("max") String max, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=my_question")
    Observable<QuestionListInfoJson> my_question(@Field("uid") String uid, @Field("type") String type, Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=works_good_list")
    Observable<WorksListInfoJson> works_good_list(@Field("uid") String uid, @Field("page") String page, @Field("max") String max, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=works_share_list")
    Observable<WorksListInfoJson> works_share_list(@Field("uid") String uid, @Field("page") String page, @Field("max") String max, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=video_upload")
    Observable<WorksInfoJson> video_upload(@Field("uid") String uid, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=video_edit")
    Observable<BaseInfo> video_edit(@Field("vid") int vid, @Field("uid") String uid, @Field("title") String title, @Field("tags") String tags, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=video_del")
    Observable<BaseInfo> video_del(@Field("vid") int vid, @Field("uid") String uid, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=video_info")
    Observable<WorksInfoJson> video_info(@Field("vid") int vid, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=works_browse")
    Observable<BaseInfo> works_browse(@Field("vid") int vid, @Field("uid") String uid, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=works_share_more")
    Observable<BaseInfo> works_share_more(@Field("vid") int vid, @Field("uid") String uid, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=works_collection")
    Observable<BaseInfo> works_collection(@Field("vid") int vid, @Field("uid") String uid, @Field("val") int val, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=works_praise")
    Observable<BaseInfo> works_praise(@Field("vid") int vid, @Field("uid") String uid, @Field("val") int val, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=works_comments")
    Observable<BaseInfo> works_comments(@Field("vid") int vid, @Field("uid") String uid, @Field("content") String content, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=works_comment_list")
    Observable<CommentListInfoJson> works_comment_list(@Field("vid") int vid, @Field("page") int page, @Field("max") int max, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=works_share")
    Observable<BaseInfo> works_share(@Field("cid") int cid, @Field("vid") int vid, @Field("uid") String uid, @Field("reason") String reason, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=user_verify")
    Observable<BaseInfo> user_verify(@Field("uid") String uid, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=user_verify_info")
    Observable<UserVerifyInfoJson> user_verify_info(@Field("uid") String uid, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=course_list")
    Observable<CourseListInfoJson> course_list(@Field("uid") int uid, @Field("page") int page, @Field("max") int max, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=course_info_introduce")
    Observable<CourseIntroduceInfoJson> course_info_introduce(@Field("cid") int cid, @Field("uid") String uid, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=course_info_works")
    Observable<WorksListInfoJson> course_info_works(@Field("cid") int cid, @Field("uid") String uid, @Field("is_teacher") int is_teacher, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=course_info_question")
    Observable<QuestionListInfoJson> course_info_question(@Field("cid") int cid, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=course_edit")
    Observable<CourseEditInfoJson> course_edit(@Field("cid") int cid, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=course_apply")
    Observable<BaseInfo> course_apply(@FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=my_course")
    Observable<CourseListInfoJson> my_course(@Field("uid") String uid, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=course_enroll")
    Observable<BaseInfo> course_enroll(@Field("cid") int cid, @Field("uid") String uid, @Field("val") int val, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=question_ask")
    Observable<BaseInfo> question_ask(@Field("cid") int cid, @Field("uid") String uid, @Field("question") String question, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=question_list")
    Observable<QuestionListInfoJson> question_list(@Field("page") int page, @Field("max") int max, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=my_watch")
    Observable<QuestionListInfoJson> my_watch(@Field("uid") String uid, @Field("page") int page, @Field("max") int max, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=question_info")
    Observable<QuestionInfoJson> question_info(@Field("uid") String uid, @Field("qid") int qid, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=question_watch")
    Observable<BaseInfo> question_watch(@Field("uid") String uid, @Field("qid") int qid, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=answer_question")
    Observable<BaseInfo> answer_question(@Field("uid") String uid, @Field("qid") int qid, @Field("atype") int atype, @Field("content") String content, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=answer_list")
    Observable<AnswerListInfoJson> answer_list(@Field("uid") String uid, @Field("qid") int qid, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=anwser_help")
    Observable<BaseInfo> anwser_help(@Field("uid") String uid, @Field("qid") int qid, @Field("val") int val, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=answer_works_list")
    Observable<WorksListInfoJson> answer_works_list(@Field("uid") String uid, @Field("type") String type, @FieldMap Map<String, String> paramsMap);

    @GET("?mod=wetube&act=subject")
    Observable<SubjectListInfoJson> subject();

    @GET("?mod=wetube&act=title")
    Observable<TitleListInfoJson> title();

    @GET("?mod=wetube&act=country")
    Observable<CountryListInfoJson> country();

    @GET("?mod=wetube&act=country")
    Observable<CityListInfoJson> city();

    @FormUrlEncoded
    @POST("?mod=wetube&act=user_follow")
    Observable<BaseInfo> user_follow(@Field("uid") String uid, @Field("oid") String oid, @Field("val") int val, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=my_friends")
    Observable<UserListInfoJson> my_friends(@Field("uid") String uid, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=my_fans")
    Observable<UserListInfoJson> my_fans(@Field("uid") String uid, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=my_follows")
    Observable<UserListInfoJson> my_follows(@Field("uid") String uid, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=user_info")
    Observable<UserInfoJson> user_info(@Field("oid") String oid, @Field("uid") String uid, @FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=chat_send")
    Observable<BaseInfo> chat_send(@FieldMap Map<String, String> paramsMap);

    @FormUrlEncoded
    @POST("?mod=wetube&act=chat")
    Observable<ChatListInfoJson> chat(@Field("oid") String oid, @Field("uid") String uid, @FieldMap Map<String, String> paramsMap);
}