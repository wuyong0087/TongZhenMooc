package com.tzhen.mooc.net.drupal;

import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.entities.ChatParams;
import com.tongzhen.mooc.entities.params.CourseApplyParams;
import com.tongzhen.mooc.entities.params.CourseEditParams;
import com.tongzhen.mooc.entities.params.UserInfoEditParams;
import com.tongzhen.mooc.entities.params.UserVerifyParams;
import com.tongzhen.mooc.entities.params.WorksUpLoadParams;
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
import com.tzhen.mooc.storage.Persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.RequestBody;
import rx.Observable;

public class RestApi {
    private Map<String, String> paramsMap;

    private final Endpoints endpoints;
    private final Persistence persistence;
    private List<String> paramParies;

    @Inject
    public RestApi(Endpoints endpoints, Persistence persistence) {
        this.endpoints = endpoints;
        this.persistence = persistence;
        paramsMap = new HashMap<>();
        paramParies = new ArrayList<>();
    }

    private boolean notEmpty(String param) {
        if (param == null || "".equals(param)) {
            return false;
        }

        return true;
    }

    public Observable<LoginJson> login(String username, String password) {

        return endpoints.login(username, password, encryptParams());
    }

    public Observable<UserInfoJson> my_info(String uid) {

        return endpoints.my_info(uid, encryptParams());
    }

    public Map<String, String> encryptParams() {

        return paramsMap;
    }

    private Map<String, RequestBody> paramsParts() {
        Map<String, RequestBody> bodys = new HashMap<>();

        bodys.put("kind", RequestBody.create(null, "android"));
        bodys.put("version", RequestBody.create(null, "0.1"));

        return bodys;
    }

    public Observable<RegisterJson> register(String username, String password, String nickname, int sex, int country, String description) {

        return endpoints.register(username, password, nickname, sex, country, description, encryptParams());
    }

    public Observable<BaseInfo> check_username(String username) {

        return endpoints.check_username(username, encryptParams());
    }

    public Observable<BaseInfo> check_nickname(String nickname) {

        return endpoints.check_nickname(nickname, encryptParams());
    }

    public Observable<UserInfoJson> user_edit(String uid,UserInfoEditParams params) {
        Map<String, String> paramsMap = encryptParams();

        if (notEmpty(params.getHead())){
            paramsMap.put("head", params.getHead());
        }
        if (notEmpty(params.getNickname())){
            paramsMap.put("nickname", params.getNickname());
        }
        if (notEmpty(params.getName())){
            paramsMap.put("name", params.getName());
        }
        if (notEmpty(params.getCity())){
            paramsMap.put("city", params.getCity());
        }
        if (notEmpty(params.getCountry())){
            paramsMap.put("country", params.getCountry());
        }
        if (notEmpty(params.getDescription())){
            paramsMap.put("description", params.getDescription());
        }
        if (notEmpty(params.getEmail())){
            paramsMap.put("email", params.getEmail());
        }
        if (notEmpty(params.getSchool())){
            paramsMap.put("school", params.getSchool());
        }
        if (notEmpty(params.getSex())){
            paramsMap.put("sex", params.getSex());
        }

        return endpoints.user_edit(uid, this.paramsMap);
    }

    public Observable<BaseInfo> up_psd(String uid, String old_psd, String new_psd) {
        return endpoints.up_psd(uid, old_psd, new_psd, encryptParams());
    }

    public Observable<BaseInfo> up_fagpsd(String username, String new_psd) {
        return endpoints.up_fagpsd(username, new_psd, encryptParams());
    }

    public Observable<SendCodeInfoJson> send_code(String username, String type) {
        return endpoints.send_code(username, type, encryptParams());
    }

    public Observable<WorksListInfoJson> my_video(String uid, String type, String page, String max) {
        return endpoints.my_video(uid, type, page, max, encryptParams());
    }

    public Observable<WorksListInfoJson> my_share(String uid, String page, String max) {
        return endpoints.my_share(uid, page, max, encryptParams());
    }

    public Observable<WorksListInfoJson> my_collection(String uid, String page, String max) {
        return endpoints.my_collection(uid, page, max, encryptParams());
    }

    public Observable<QuestionListInfoJson> my_question(String uid, String type) {
        return endpoints.my_question(uid, type, encryptParams());
    }

    public Observable<WorksListInfoJson> works_good_list(String uid, String page, String max) {
        return endpoints.works_good_list(uid, page, max, encryptParams());
    }

    public Observable<WorksListInfoJson> works_share_list(String uid, String page, String max) {
        return endpoints.works_share_list(uid, page, max, encryptParams());
    }

    public Observable<WorksInfoJson> video_upload(String uid, WorksUpLoadParams params) {
        Map<String, RequestBody> paramsMap = paramsParts();
        paramsMap.put("title", RequestBody.create(null, params.getTitle()));
        paramsMap.put("files", RequestBody.create(null, params.getFiles()));
        paramsMap.put("type", RequestBody.create(null, params.getType()));
        paramsMap.put("zipfile", RequestBody.create(null, params.getZipfile()));
        paramsMap.put("createtime", RequestBody.create(null, params.getCreatetime() + ""));
        paramsMap.put("duration", RequestBody.create(null, params.getDuration() + ""));

        if (notEmpty(params.getVersions())){
            paramsMap.put("versions", RequestBody.create(null, params.getVersions() + ""));
        }
        if (notEmpty(params.getTags())){
            paramsMap.put("tags", RequestBody.create(null, params.getTags() + ""));
        }
        if (notEmpty(params.getPages())){
            paramsMap.put("pages", RequestBody.create(null, params.getPages() + ""));
        }
        if (notEmpty(params.getOld_vid())){
            paramsMap.put("old_vid", RequestBody.create(null, params.getOld_vid() + ""));
        }
        return endpoints.video_upload(uid, this.paramsMap);
    }

    public Observable<BaseInfo> video_edit(int vid, String uid, String title, String tags) {
        return endpoints.video_edit(vid, uid, title, tags, encryptParams());
    }

    public Observable<BaseInfo> video_del(int vid, String uid) {
        return endpoints.video_del(vid, uid, encryptParams());
    }

    public Observable<WorksInfoJson> video_info(int vid, String uid) {
        encryptParams();
        if (notEmpty(uid)){
            paramsMap.put("uid", uid);
        }
        return endpoints.video_info(vid, paramsMap);
    }

    public Observable<BaseInfo> works_browse(int vid, String uid) {
        return endpoints.works_browse(vid, uid, encryptParams());
    }

    public Observable<BaseInfo> works_share_more(int vid, String uid) {
        return endpoints.works_share_more(vid, uid, encryptParams());
    }

    public Observable<BaseInfo> works_collection(int vid, String uid, int val) {
        return endpoints.works_collection(vid, uid, val, encryptParams());
    }

    public Observable<BaseInfo> works_praise(int vid, String uid, int val) {
        return endpoints.works_praise(vid, uid, val, encryptParams());
    }

    public Observable<BaseInfo> works_comments(int vid, String uid, String content) {
        return endpoints.works_comments(vid, uid, content, encryptParams());
    }

    public Observable<CommentListInfoJson> works_comment_list(int vid, int page, int max) {
        return endpoints.works_comment_list(vid, page, max, encryptParams());
    }

    public Observable<BaseInfo> works_share(int cid, int vid, String uid, String reason) {
        return endpoints.works_share(cid, vid, uid, reason, encryptParams());
    }

    public Observable<BaseInfo> user_verify(String uid, UserVerifyParams params) {
        encryptParams();
        paramsMap.put("name", params.getName());
        paramsMap.put("address", params.getAddress());
        paramsMap.put("city", params.getCity());
        paramsMap.put("country", params.getCountry());
        paramsMap.put("email", params.getEmail());
        paramsMap.put("img", params.getImg());
        paramsMap.put("school", params.getSchool());
        paramsMap.put("title", params.getTitle());
        paramsMap.put("val", params.getVal());

        return endpoints.user_verify(uid, paramsMap);
    }

    public Observable<UserVerifyInfoJson> user_verify_info(String uid) {
        return endpoints.user_verify_info(uid, encryptParams());
    }

    public Observable<CourseListInfoJson> course_list(int subject, int page, int max) {
        encryptParams();
        if (subject > 0){
            paramsMap.put("subject", subject + "");
        }

        if (page > 0){
            paramsMap.put("page", page + "");
        }
        if (max > 0){
            paramsMap.put("max", max + "");
        }

        return endpoints.course_list(paramsMap);
    }

    public Observable<CourseIntroduceInfoJson> course_info_introduce(int cid, String uid) {
        return endpoints.course_info_introduce(cid, uid, encryptParams());
    }

    public Observable<WorksListInfoJson> course_info_works(int cid, String uid, int is_teacher) {
        return endpoints.course_info_works(cid, uid, is_teacher, encryptParams());
    }

    public Observable<QuestionListInfoJson> course_info_question(int cid) {
        return endpoints.course_info_question(cid, encryptParams());
    }

    public Observable<CourseEditInfoJson> course_edit(int cid, CourseEditParams params) {
        encryptParams();

        paramsMap.put("uid", params.getUid());
        if (notEmpty(params.getCover())){
            paramsMap.put("cover", params.getCover());
        }
        if (notEmpty(params.getDescription())){
            paramsMap.put("description", params.getDescription());
        }
        if (notEmpty(params.getCourse_title())){
            paramsMap.put("course_title", params.getCourse_title());
        }

        return endpoints.course_edit(cid, paramsMap);
    }

    public Observable<BaseInfo> course_apply(CourseApplyParams params) {
        encryptParams();

        paramsMap.put("uid", params.getUid());
        paramsMap.put("course_title", params.getCourse_title());
        paramsMap.put("cover", params.getCover());
        paramsMap.put("description", params.getDescription());
        paramsMap.put("subject", params.getSubject() + "");

        return endpoints.course_apply(paramsMap);
    }

    public Observable<CourseListInfoJson> my_course(String uid) {
        return endpoints.my_course(uid, encryptParams());
    }

    public Observable<BaseInfo> course_enroll(int cid, String uid, int val) {
        return endpoints.course_enroll(cid, uid, val, encryptParams());
    }

    public Observable<BaseInfo> question_ask(int cid, String uid, String question, String img) {
        encryptParams();

        if (notEmpty(img)){
            paramsMap.put("img", img);
        }

        return endpoints.question_ask(cid, uid, question, paramsMap);
    }

    public Observable<QuestionListInfoJson> question_list(int page, int max) {
        return endpoints.question_list(page, max, encryptParams());
    }

    public Observable<QuestionListInfoJson> my_watch(String uid, int page, int max) {
        return endpoints.my_watch(uid, page, max, encryptParams());
    }

    public Observable<QuestionInfoJson> question_info(String uid, int qid) {
        return endpoints.question_info(uid, qid, encryptParams());
    }

    public Observable<BaseInfo> question_watch(String uid, int qid) {
        return endpoints.question_watch(uid, qid, encryptParams());
    }

    public Observable<BaseInfo> answer_question(String uid, int qid, int atype, String content, int vid) {
        encryptParams();
        if (vid > 0){
            paramsMap.put("vid", vid + "");
        }
        return endpoints.answer_question(uid, qid, atype, content, paramsMap);
    }

    public Observable<AnswerListInfoJson> answer_list(String uid, int qid) {
        return endpoints.answer_list(uid, qid, encryptParams());
    }

    public Observable<BaseInfo> anwser_help(String uid, int qid, int val) {
        return endpoints.anwser_help(uid, qid, val, encryptParams());
    }

    public Observable<WorksListInfoJson> answer_works_list(String uid, String type) {
        return endpoints.answer_works_list(uid, type, encryptParams());
    }

    public Observable<SubjectListInfoJson> subject() {
        return endpoints.subject();
    }

    public Observable<TitleListInfoJson> title() {
        return endpoints.title();
    }

    public Observable<CountryListInfoJson> country() {
        return endpoints.country();
    }

    public Observable<CityListInfoJson> city() {
        return endpoints.city();
    }

    public Observable<BaseInfo> user_follow(String uid, String oid, int val) {
        return endpoints.user_follow(uid, oid, val, encryptParams());
    }

    public Observable<UserListInfoJson> my_friends(String uid) {
        return endpoints.my_friends(uid, encryptParams());
    }

    public Observable<UserListInfoJson> my_fans(String uid) {
        return endpoints.my_fans(uid, encryptParams());
    }

    public Observable<UserListInfoJson> my_follows(String uid) {
        return endpoints.my_follows(uid, encryptParams());
    }

    public Observable<UserInfoJson> user_info(String oid, String uid) {
        return endpoints.user_info(oid, uid, encryptParams());
    }

    public Observable<BaseInfo> chat_send(ChatParams params) {
        encryptParams();

        paramsMap.put("uid", params.getUid());
        paramsMap.put("oid", params.getOid());
        paramsMap.put("addtime", params.getAddtime() + "");
        paramsMap.put("type", params.getType() + "");
        if (notEmpty(params.getContent())){
            paramsMap.put("content", params.getContent());
        }
        if (params.getVid() > 1){
            paramsMap.put("vid", params.getVid() + "");
        }

        return endpoints.chat_send(paramsMap);
    }

    public Observable<ChatListInfoJson> chat(String oid, String uid) {
        return endpoints.chat(oid, uid, encryptParams());
    }
}
