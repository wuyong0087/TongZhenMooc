package com.tzhen.mooc.repositories;

import android.content.Context;

import com.tongzhen.mooc.entities.AnswerListInfo;
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
import com.tongzhen.mooc.repositories.SessionRepository;
import com.tongzhen.mooc.entities.BaseInfo;
import com.tzhen.mooc.entities.drupal.UserListInfoJson;
import com.tzhen.mooc.entities.drupal.UserVerifyInfoJson;
import com.tzhen.mooc.entities.drupal.WorksInfoJson;
import com.tzhen.mooc.entities.drupal.WorksListInfoJson;
import com.tzhen.mooc.storage.Persistence;
import com.tzhen.mooc.net.drupal.RestApi;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by russellgutierrez on 17/5/2016.
 */
public class SessionDataRepository implements SessionRepository {
    private final RestApi restApi;
    private Context context;
    private Persistence persistence;

    @Inject
    public SessionDataRepository(Context context, RestApi restApi, Persistence persistence) {
        this.context = context;
        this.restApi = restApi;
        this.persistence = persistence;
    }

    @Override
    public Observable<RegisterInfo> register(String username, String password, String nickname, int sex, int country, String description) {
        return restApi.register(username, password, nickname, sex, country, description).map(new Func1<RegisterJson, RegisterInfo>() {
            @Override
            public RegisterInfo call(RegisterJson registerJson) {
                RegisterInfo data = registerJson.getData();
                data.setResult(registerJson.getResult());
                data.setErrorMsg(registerJson.getErrorMsg());
                return data;
            }
        });
    }

    @Override
    public Observable<UserInfo> login(String username, String password) {
        return restApi.login(username, password).map(new Func1<LoginJson, UserInfo>() {
            @Override
            public UserInfo call(LoginJson loginJson) {
                UserInfo data = loginJson.getData();
                data.setResult(loginJson.getResult());
                data.setErrorMsg(loginJson.getErrorMsg());
                return data;
            }
        });
    }

    @Override
    public Observable<BaseInfo> check_username(String username) {
        return restApi.check_username(username);
    }

    @Override
    public Observable<BaseInfo> check_nickname(String nickname) {
        return restApi.check_nickname(nickname);
    }

    @Override
    public Observable<UserInfo> my_info(String uid) {
        return restApi.my_info(uid).map(new Func1<UserInfoJson, UserInfo>() {
            @Override
            public UserInfo call(UserInfoJson userInfoJson) {
                UserInfo data = userInfoJson.getData();
                data.setResult(userInfoJson.getResult());
                data.setErrorMsg(userInfoJson.getErrorMsg());
                return data;
            }
        });
    }

    @Override
    public Observable<UserInfo> user_edit(String uid, UserInfoEditParams params) {
        return restApi.user_edit(uid, params).map(new Func1<UserInfoJson, UserInfo>() {
            @Override
            public UserInfo call(UserInfoJson userInfoJson) {
                UserInfo data = userInfoJson.getData();
                data.setResult(userInfoJson.getResult());
                data.setErrorMsg(userInfoJson.getErrorMsg());
                return data;
            }
        });
    }

    @Override
    public Observable<BaseInfo> up_psd(String uid, String old_psd, String new_psd) {
        return restApi.up_psd(uid, old_psd, new_psd);
    }

    @Override
    public Observable<BaseInfo> up_fagpsd(String username, String new_psd) {
        return restApi.up_fagpsd(username, new_psd);
    }

    @Override
    public Observable<SendCodeInfo> send_code(String username, String type) {
        return restApi.send_code(username, type).map(new Func1<SendCodeInfoJson, SendCodeInfo>() {
            @Override
            public SendCodeInfo call(SendCodeInfoJson sendCodeInfoJson) {
                SendCodeInfo sendCodeInfo = new SendCodeInfo();
                sendCodeInfo.setErrorMsg(sendCodeInfo.getErrorMsg());
                sendCodeInfo.setResult(sendCodeInfo.getResult());
                sendCodeInfo.setCode(sendCodeInfo.getCode());
                return sendCodeInfo;
            }
        });
    }

    @Override
    public Observable<WorksListInfo> my_video(String uid, String type, String page, String max) {
        return restApi.my_video(uid, type, page, max).map(new Func1<WorksListInfoJson, WorksListInfo>() {
            @Override
            public WorksListInfo call(WorksListInfoJson worksListInfoJson) {
                WorksListInfo worksListInfo = new WorksListInfo();
                worksListInfo.setResult(worksListInfo.getResult());
                worksListInfo.setErrorMsg(worksListInfoJson.getErrorMsg());
                worksListInfo.setWorksInfoList(worksListInfoJson.getData());
                return worksListInfo;
            }
        });
    }

    @Override
    public Observable<WorksListInfo> my_share(String uid, String page, String max) {
        return restApi.my_share(uid, page, max).map(new Func1<WorksListInfoJson, WorksListInfo>() {
            @Override
            public WorksListInfo call(WorksListInfoJson worksListInfoJson) {
                WorksListInfo worksListInfo = new WorksListInfo();
                worksListInfo.setResult(worksListInfo.getResult());
                worksListInfo.setErrorMsg(worksListInfoJson.getErrorMsg());
                worksListInfo.setWorksInfoList(worksListInfoJson.getData());
                return worksListInfo;
            }
        });
    }

    @Override
    public Observable<WorksListInfo> my_collection(String uid, String page, String max) {
        return restApi.my_collection(uid, page, max).map(new Func1<WorksListInfoJson, WorksListInfo>() {
            @Override
            public WorksListInfo call(WorksListInfoJson worksListInfoJson) {
                WorksListInfo worksListInfo = new WorksListInfo();
                worksListInfo.setResult(worksListInfo.getResult());
                worksListInfo.setErrorMsg(worksListInfoJson.getErrorMsg());
                worksListInfo.setWorksInfoList(worksListInfoJson.getData());
                return worksListInfo;
            }
        });
    }

    @Override
    public Observable<QuestionListInfo> my_question(String uid, String type) {
        return restApi.my_question(uid, type).map(new Func1<QuestionListInfoJson, QuestionListInfo>() {
            @Override
            public QuestionListInfo call(QuestionListInfoJson questionListInfoJson) {
                QuestionListInfo questionListInfo = new QuestionListInfo();
                questionListInfo.setResult(questionListInfoJson.getResult());
                questionListInfo.setErrorMsg(questionListInfoJson.getErrorMsg());
                questionListInfo.setQuestionInfoList(questionListInfoJson.getData());
                return questionListInfo;
            }
        });
    }

    @Override
    public Observable<WorksListInfo> works_good_list(String uid, String page, String max) {
        return restApi.works_good_list(uid, page, max).map(new Func1<WorksListInfoJson, WorksListInfo>() {
            @Override
            public WorksListInfo call(WorksListInfoJson worksListInfoJson) {
                WorksListInfo worksListInfo = new WorksListInfo();
                worksListInfo.setResult(worksListInfo.getResult());
                worksListInfo.setErrorMsg(worksListInfoJson.getErrorMsg());
                worksListInfo.setWorksInfoList(worksListInfoJson.getData());
                return worksListInfo;
            }
        });
    }

    @Override
    public Observable<WorksListInfo> works_share_list(String uid, String page, String max) {
        return restApi.works_share_list(uid, page, max).map(new Func1<WorksListInfoJson, WorksListInfo>() {
            @Override
            public WorksListInfo call(WorksListInfoJson worksListInfoJson) {
                WorksListInfo worksListInfo = new WorksListInfo();
                worksListInfo.setResult(worksListInfo.getResult());
                worksListInfo.setErrorMsg(worksListInfoJson.getErrorMsg());
                worksListInfo.setWorksInfoList(worksListInfoJson.getData());
                return worksListInfo;
            }
        });
    }

    @Override
    public Observable<WorksInfo> video_upload(String uid, WorksUpLoadParams params) {
        return restApi.video_upload(uid, params).map(new Func1<WorksInfoJson, WorksInfo>() {
            @Override
            public WorksInfo call(WorksInfoJson worksInfoJson) {
                WorksInfo worksInfo = worksInfoJson.getData();
                worksInfo.setResult(worksInfoJson.getResult());
                worksInfo.setErrorMsg(worksInfoJson.getErrorMsg());
                return worksInfo;
            }
        });
    }

    @Override
    public Observable<BaseInfo> video_edit(int vid, String uid, String title, String tags) {
        return restApi.video_edit(vid, uid, title, tags);
    }

    @Override
    public Observable<BaseInfo> video_del(int vid, String uid) {
        return restApi.video_del(vid, uid);
    }

    @Override
    public Observable<WorksInfo> video_info(int vid, String uid) {
        return restApi.video_info(vid, uid).map(new Func1<WorksInfoJson, WorksInfo>() {
            @Override
            public WorksInfo call(WorksInfoJson worksInfoJson) {
                WorksInfo worksInfo = worksInfoJson.getData();
                worksInfo.setResult(worksInfoJson.getResult());
                worksInfo.setErrorMsg(worksInfoJson.getErrorMsg());
                return worksInfo;
            }
        });
    }

    @Override
    public Observable<BaseInfo> works_browse(int vid, String uid) {
        return restApi.works_browse(vid, uid);
    }

    @Override
    public Observable<BaseInfo> works_share_more(int vid, String uid) {
        return restApi.works_share_more(vid, uid);
    }

    @Override
    public Observable<BaseInfo> works_collection(int vid, String uid, int val) {
        return restApi.works_collection(vid, uid, val);
    }

    @Override
    public Observable<BaseInfo> works_praise(int vid, String uid, int val) {
        return restApi.works_praise(vid, uid, val);
    }

    @Override
    public Observable<BaseInfo> works_comments(int vid, String uid, String content) {
        return restApi.works_comments(vid, uid, content);
    }

    @Override
    public Observable<CommentListInfo> works_comment_list(int vid, int page, int max) {
        return restApi.works_comment_list(vid, page, max).map(new Func1<CommentListInfoJson, CommentListInfo>() {
            @Override
            public CommentListInfo call(CommentListInfoJson commentListInfoJson) {
                CommentListInfo commentListInfo = new CommentListInfo();
                commentListInfo.setCommentInfoList(commentListInfoJson.getData());
                commentListInfo.setResult(commentListInfoJson.getResult());
                commentListInfo.setErrorMsg(commentListInfoJson.getErrorMsg());
                return commentListInfo;
            }
        });
    }

    @Override
    public Observable<BaseInfo> works_share(int cid, int vid, String uid, String reason) {
        return restApi.works_share(cid, vid, uid, reason);
    }

    @Override
    public Observable<BaseInfo> user_verify(String uid, UserVerifyParams params) {
        return restApi.user_verify(uid, params);
    }

    @Override
    public Observable<UserVerifyInfo> user_verify_info(String uid) {
        return restApi.user_verify_info(uid).map(new Func1<UserVerifyInfoJson, UserVerifyInfo>() {
            @Override
            public UserVerifyInfo call(UserVerifyInfoJson userVerifyInfoJson) {
                UserVerifyInfo userVerifyInfo = userVerifyInfoJson.getData();
                userVerifyInfo.setResult(userVerifyInfoJson.getResult());
                userVerifyInfo.setErrorMsg(userVerifyInfoJson.getErrorMsg());
                return userVerifyInfo;
            }
        });
    }

    @Override
    public Observable<CourseListInfo> course_list(int subject, int page, int max) {
        return restApi.course_list(subject, page, max).map(new Func1<CourseListInfoJson, CourseListInfo>() {
            @Override
            public CourseListInfo call(CourseListInfoJson courseListInfoJson) {
                CourseListInfo courseListInfo = new CourseListInfo();
                courseListInfo.setCourseInfoList(courseListInfoJson.getData());
                courseListInfo.setResult(courseListInfoJson.getResult());
                courseListInfo.setErrorMsg(courseListInfoJson.getErrorMsg());

                return courseListInfo;
            }
        });
    }

    @Override
    public Observable<CourseIntroduceInfo> course_info_introduce(int cid, String uid) {
        return restApi.course_info_introduce(cid, uid).map(new Func1<CourseIntroduceInfoJson, CourseIntroduceInfo>() {
            @Override
            public CourseIntroduceInfo call(CourseIntroduceInfoJson courseIntroduceInfoJson) {
                CourseIntroduceInfo data = courseIntroduceInfoJson.getData();
                data.setResult(courseIntroduceInfoJson.getResult());
                data.setErrorMsg(courseIntroduceInfoJson.getErrorMsg());
                return data;
            }
        });
    }

    @Override
    public Observable<WorksListInfo> course_info_works(int cid, String uid, int is_teacher) {
        return restApi.course_info_works(cid, uid, is_teacher).map(new Func1<WorksListInfoJson, WorksListInfo>() {
            @Override
            public WorksListInfo call(WorksListInfoJson worksListInfoJson) {
                WorksListInfo worksListInfo = new WorksListInfo();
                worksListInfo.setResult(worksListInfo.getResult());
                worksListInfo.setErrorMsg(worksListInfoJson.getErrorMsg());
                worksListInfo.setWorksInfoList(worksListInfoJson.getData());
                return worksListInfo;
            }
        });
    }

    @Override
    public Observable<QuestionListInfo> course_info_question(int cid) {
        return restApi.course_info_question(cid).map(new Func1<QuestionListInfoJson, QuestionListInfo>() {
            @Override
            public QuestionListInfo call(QuestionListInfoJson questionListInfoJson) {
                QuestionListInfo questionListInfo = new QuestionListInfo();
                questionListInfo.setResult(questionListInfoJson.getResult());
                questionListInfo.setErrorMsg(questionListInfoJson.getErrorMsg());
                questionListInfo.setQuestionInfoList(questionListInfoJson.getData());
                return questionListInfo;
            }
        });
    }

    @Override
    public Observable<CourseEditInfo> course_edit(int cid, CourseEditParams params) {
        return restApi.course_edit(cid, params).map(new Func1<CourseEditInfoJson, CourseEditInfo>() {
            @Override
            public CourseEditInfo call(CourseEditInfoJson courseEditInfoJson) {
                CourseEditInfo data = courseEditInfoJson.getData();
                data.setResult(courseEditInfoJson.getResult());
                data.setErrorMsg(courseEditInfoJson.getErrorMsg());
                return data;
            }
        });
    }

    @Override
    public Observable<BaseInfo> course_apply(CourseApplyParams params) {
        return restApi.course_apply(params);
    }

    @Override
    public Observable<CourseListInfo> my_course(String uid) {
        return restApi.my_course(uid).map(new Func1<CourseListInfoJson, CourseListInfo>() {
            @Override
            public CourseListInfo call(CourseListInfoJson courseListInfoJson) {
                CourseListInfo courseListInfo = new CourseListInfo();
                courseListInfo.setCourseInfoList(courseListInfoJson.getData());
                courseListInfo.setResult(courseListInfoJson.getResult());
                courseListInfo.setErrorMsg(courseListInfoJson.getErrorMsg());

                return courseListInfo;
            }
        });
    }

    @Override
    public Observable<BaseInfo> course_enroll(int cid, String uid, int val) {
        return restApi.course_enroll(cid, uid, val);
    }

    @Override
    public Observable<BaseInfo> question_ask(int cid, String uid, String question, String img) {
        return restApi.question_ask(cid, uid, question, img);
    }

    @Override
    public Observable<QuestionListInfo> question_list(int page, int max) {
        return restApi.question_list(page, max).map(new Func1<QuestionListInfoJson, QuestionListInfo>() {
            @Override
            public QuestionListInfo call(QuestionListInfoJson questionListInfoJson) {
                QuestionListInfo questionListInfo = new QuestionListInfo();
                questionListInfo.setResult(questionListInfoJson.getResult());
                questionListInfo.setErrorMsg(questionListInfoJson.getErrorMsg());
                questionListInfo.setQuestionInfoList(questionListInfoJson.getData());
                return questionListInfo;
            }
        });
    }

    @Override
    public Observable<QuestionListInfo> my_watch(String uid, int page, int max) {
        return restApi.my_watch(uid, page, max).map(new Func1<QuestionListInfoJson, QuestionListInfo>() {
            @Override
            public QuestionListInfo call(QuestionListInfoJson questionListInfoJson) {
                QuestionListInfo questionListInfo = new QuestionListInfo();
                questionListInfo.setResult(questionListInfoJson.getResult());
                questionListInfo.setErrorMsg(questionListInfoJson.getErrorMsg());
                questionListInfo.setQuestionInfoList(questionListInfoJson.getData());
                return questionListInfo;
            }
        });
    }

    @Override
    public Observable<QuestionInfo> question_info(String uid, int qid) {
        return restApi.question_info(uid, qid).map(new Func1<QuestionInfoJson, QuestionInfo>() {
            @Override
            public QuestionInfo call(QuestionInfoJson questionInfoJson) {
                QuestionInfo data = questionInfoJson.getData();
                data.setResult(questionInfoJson.getResult());
                data.setErrorMsg(questionInfoJson.getErrorMsg());
                return data;
            }
        });
    }

    @Override
    public Observable<BaseInfo> question_watch(String uid, int qid) {
        return restApi.question_watch(uid, qid);
    }

    @Override
    public Observable<BaseInfo> answer_question(String uid, int qid, int atype, String content, int vid) {
        return restApi.answer_question(uid, qid, atype, content, vid);
    }

    @Override
    public Observable<AnswerListInfo> answer_list(String uid, int qid) {
        return restApi.answer_list(uid, qid).map(new Func1<AnswerListInfoJson, AnswerListInfo>() {
            @Override
            public AnswerListInfo call(AnswerListInfoJson answerListInfoJson) {
                AnswerListInfo answerListInfo = new AnswerListInfo();
                answerListInfo.setAnswerInfoList(answerListInfoJson.getData());
                answerListInfo.setResult(answerListInfoJson.getResult());
                answerListInfo.setErrorMsg(answerListInfoJson.getErrorMsg());
                return answerListInfo;
            }
        });
    }

    @Override
    public Observable<BaseInfo> anwser_help(String uid, int qid, int val) {
        return restApi.anwser_help(uid, qid, val);
    }

    @Override
    public Observable<WorksListInfo> answer_works_list(String uid, String type) {
        return restApi.answer_works_list(uid, type).map(new Func1<WorksListInfoJson, WorksListInfo>() {
            @Override
            public WorksListInfo call(WorksListInfoJson worksListInfoJson) {
                WorksListInfo worksListInfo = new WorksListInfo();
                worksListInfo.setResult(worksListInfo.getResult());
                worksListInfo.setErrorMsg(worksListInfoJson.getErrorMsg());
                worksListInfo.setWorksInfoList(worksListInfoJson.getData());
                return worksListInfo;
            }
        });
    }

    @Override
    public Observable<SubjectListInfo> subject() {
        return restApi.subject().map(new Func1<SubjectListInfoJson, SubjectListInfo>() {
            @Override
            public SubjectListInfo call(SubjectListInfoJson subjectListInfoJson) {
                SubjectListInfo subjectListInfo = new SubjectListInfo();
                subjectListInfo.setSubjectInfoList(subjectListInfo.getSubjectInfoList());
                subjectListInfo.setResult(subjectListInfoJson.getResult());
                subjectListInfo.setErrorMsg(subjectListInfoJson.getErrorMsg());
                return subjectListInfo;
            }
        });
    }

    @Override
    public Observable<TitleListInfo> title() {
        return restApi.title().map(new Func1<TitleListInfoJson, TitleListInfo>() {
            @Override
            public TitleListInfo call(TitleListInfoJson titleListInfoJson) {
                TitleListInfo titleListInfo = new TitleListInfo();
                titleListInfo.setSubjectInfoList(titleListInfoJson.getData());
                titleListInfo.setResult(titleListInfoJson.getResult());
                titleListInfo.setErrorMsg(titleListInfoJson.getErrorMsg());
                return titleListInfo;
            }
        });
    }

    @Override
    public Observable<CountryListInfo> country() {
        return restApi.country().map(new Func1<CountryListInfoJson, CountryListInfo>() {
            @Override
            public CountryListInfo call(CountryListInfoJson countryListInfoJson) {
                CountryListInfo countryListInfo = new CountryListInfo();
                countryListInfo.setResult(countryListInfoJson.getResult());
                countryListInfo.setErrorMsg(countryListInfoJson.getErrorMsg());
                countryListInfo.setCountryInfoList(countryListInfoJson.getData());
                return countryListInfo;
            }
        });
    }

    @Override
    public Observable<CityListInfo> city() {
        return restApi.city().map(new Func1<CityListInfoJson, CityListInfo>() {
            @Override
            public CityListInfo call(CityListInfoJson cityListInfoJson) {
                CityListInfo cityListInfo = new CityListInfo();
                cityListInfo.setResult(cityListInfoJson.getResult());
                cityListInfo.setErrorMsg(cityListInfoJson.getErrorMsg());
                cityListInfo.setCityInfoList(cityListInfoJson.getData());
                return cityListInfo;
            }
        });
    }

    @Override
    public Observable<BaseInfo> user_follow(String uid, String oid, int val) {
        return restApi.user_follow(uid, oid, val);
    }

    @Override
    public Observable<UserListInfo> my_friends(String uid) {
        return restApi.my_friends(uid).map(new Func1<UserListInfoJson, UserListInfo>() {
            @Override
            public UserListInfo call(UserListInfoJson userListInfoJson) {
                UserListInfo userListInfo = new UserListInfo();
                userListInfo.setResult(userListInfoJson.getResult());
                userListInfo.setErrorMsg(userListInfoJson.getErrorMsg());
                userListInfo.setUserInfoList(userListInfoJson.getData());
                return userListInfo;
            }
        });
    }

    @Override
    public Observable<UserListInfo> my_fans(String uid) {
        return restApi.my_fans(uid).map(new Func1<UserListInfoJson, UserListInfo>() {
            @Override
            public UserListInfo call(UserListInfoJson userListInfoJson) {
                UserListInfo userListInfo = new UserListInfo();
                userListInfo.setResult(userListInfoJson.getResult());
                userListInfo.setErrorMsg(userListInfoJson.getErrorMsg());
                userListInfo.setUserInfoList(userListInfoJson.getData());
                return userListInfo;
            }
        });
    }

    @Override
    public Observable<UserListInfo> my_follows(String uid) {
        return restApi.my_follows(uid).map(new Func1<UserListInfoJson, UserListInfo>() {
            @Override
            public UserListInfo call(UserListInfoJson userListInfoJson) {
                UserListInfo userListInfo = new UserListInfo();
                userListInfo.setResult(userListInfoJson.getResult());
                userListInfo.setErrorMsg(userListInfoJson.getErrorMsg());
                userListInfo.setUserInfoList(userListInfoJson.getData());
                return userListInfo;
            }
        });
    }

    @Override
    public Observable<UserInfo> user_info(String oid, String uid) {
        return restApi.user_info(oid, uid).map(new Func1<UserInfoJson, UserInfo>() {
            @Override
            public UserInfo call(UserInfoJson userInfoJson) {
                UserInfo data = userInfoJson.getData();
                data.setResult(userInfoJson.getResult());
                data.setErrorMsg(userInfoJson.getErrorMsg());
                return data;
            }
        });
    }

    @Override
    public Observable<BaseInfo> chat_send(ChatParams params) {
        return restApi.chat_send(params);
    }

    @Override
    public Observable<ChatListInfo> chat(String oid, String uid) {
        return restApi.chat(oid, uid).map(new Func1<ChatListInfoJson, ChatListInfo>() {
            @Override
            public ChatListInfo call(ChatListInfoJson chatListInfoJson) {
                ChatListInfo chatListInfo = new ChatListInfo();
                chatListInfo.setResult(chatListInfoJson.getResult());
                chatListInfo.setErrorMsg(chatListInfoJson.getErrorMsg());
                chatListInfo.setChatInfoList(chatListInfoJson.getData());
                return chatListInfo;
            }
        });
    }
}