package com.tongzhen.mooc.repositories;

import com.tongzhen.mooc.entities.AnswerListInfo;
import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.entities.ChatListInfo;
import com.tongzhen.mooc.entities.ChatParams;
import com.tongzhen.mooc.entities.CityListInfo;
import com.tongzhen.mooc.entities.CommentListInfo;
import com.tongzhen.mooc.entities.CountryListInfo;
import com.tongzhen.mooc.entities.CourseApplyParams;
import com.tongzhen.mooc.entities.CourseEditInfo;
import com.tongzhen.mooc.entities.CourseEditParams;
import com.tongzhen.mooc.entities.CourseIntroduceInfo;
import com.tongzhen.mooc.entities.CourseListInfo;
import com.tongzhen.mooc.entities.QuestionInfo;
import com.tongzhen.mooc.entities.QuestionListInfo;
import com.tongzhen.mooc.entities.RegisterInfo;
import com.tongzhen.mooc.entities.SendCodeInfo;
import com.tongzhen.mooc.entities.SubjectListInfo;
import com.tongzhen.mooc.entities.TitleListInfo;
import com.tongzhen.mooc.entities.UserInfo;
import com.tongzhen.mooc.entities.UserInfoEditParams;
import com.tongzhen.mooc.entities.UserListInfo;
import com.tongzhen.mooc.entities.UserVerifyInfo;
import com.tongzhen.mooc.entities.UserVerifyParams;
import com.tongzhen.mooc.entities.WorksInfo;
import com.tongzhen.mooc.entities.WorksListInfo;
import com.tongzhen.mooc.entities.WorksUpLoadParams;

import rx.Observable;

/**
 * Created by wuyong on 2016/11/26.
 */
public interface SessionRepository {
    Observable<RegisterInfo> register(String username, String password, String nickname, int sex, int country, String description);

    Observable<UserInfo> login(String username, String password);

    Observable<BaseInfo> check_username(String username);

    Observable<BaseInfo> check_nickname(String nickname);

    Observable<UserInfo> my_info(String uid);

    Observable<UserInfo> user_edit(String uid, UserInfoEditParams params);

    Observable<BaseInfo> up_psd(String uid, String old_psd, String new_psd);

    Observable<BaseInfo> up_fagpsd(String username, String new_psd);

    Observable<SendCodeInfo> send_code(String username, String type);

    Observable<WorksListInfo> my_video(String uid, String type, String page, String max);

    Observable<WorksListInfo> my_share(String uid, String page, String max);

    Observable<WorksListInfo> my_collection(String uid, String page, String max);

    Observable<QuestionListInfo> my_question(String uid, String type);

    Observable<WorksListInfo> works_good_list(String uid, String page, String max);

    Observable<WorksListInfo> works_share_list(String uid, String page, String max);

    Observable<WorksInfo> video_upload(String uid, WorksUpLoadParams params);

    Observable<BaseInfo> video_edit(int vid, String uid, String title, String tags);

    Observable<BaseInfo> video_del(int vid, String uid);

    Observable<WorksInfo> video_info(int vid, String uid);

    Observable<BaseInfo> works_browse(int vid, String uid);

    Observable<BaseInfo> works_share_more(int vid, String uid);

    Observable<BaseInfo> works_collection(int vid, String uid, int val);

    Observable<BaseInfo> works_praise(int vid, String uid, int val);

    Observable<BaseInfo> works_comments(int vid, String uid, String content);

    Observable<CommentListInfo> works_comment_list(int vid, int page, int max);

    Observable<BaseInfo> works_share(int cid, int vid, String uid, String reason);

    Observable<BaseInfo> user_verify(String uid, UserVerifyParams params);

    Observable<UserVerifyInfo> user_verify_info(int uid);

    Observable<CourseListInfo> course_list(int subject, int page, int max);

    Observable<CourseIntroduceInfo> course_info_introduce(int cid, String uid);

    Observable<WorksListInfo> course_info_works(int cid, String uid, int is_teacher);

    Observable<QuestionListInfo> course_info_question(int cid);

    Observable<CourseEditInfo> course_edit(int cid, CourseEditParams params);

    Observable<BaseInfo> course_apply(CourseApplyParams params);

    Observable<CourseListInfo> my_course(String uid);

    Observable<BaseInfo> course_enroll(int cid, String uid, int val);

    Observable<BaseInfo> question_ask(int cid, String uid, String question, String img);

    Observable<QuestionListInfo> question_list(int page, int max);

    Observable<QuestionListInfo> my_watch(String uid, int page, int max);

    Observable<QuestionInfo> question_info(String uid, int qid);

    Observable<BaseInfo> question_watch(String uid, int qid);

    Observable<BaseInfo> answer_question(String uid, int qid, int atype, String content, int vid);

    Observable<AnswerListInfo> answer_list(String uid, int qid);

    Observable<BaseInfo> anwser_help(String uid, int qid, int val);

    Observable<WorksListInfo> answer_works_list(String uid, String type);

    Observable<SubjectListInfo> subject();

    Observable<TitleListInfo> title();

    Observable<CountryListInfo> country();

    Observable<CityListInfo> city();

    Observable<BaseInfo> user_follow(String uid, String oid, int val);

    Observable<UserListInfo> my_friends(String uid);

    Observable<UserListInfo> my_fans(String uid);

    Observable<UserListInfo> my_follows(String uid);

//    Observable<UserListInfo> user_search(String uid);

    Observable<UserInfo> user_info(String oid, String uid);

    Observable<BaseInfo> chat_send(ChatParams params);

    Observable<ChatListInfo> chat(String oid, String uid);
}
