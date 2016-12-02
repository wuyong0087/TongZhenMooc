package com.tzhen.date;

import com.tongzhen.mooc.entities.BaseInfo;
import com.tongzhen.mooc.entities.CourseApplyParams;
import com.tongzhen.mooc.entities.CourseEditParams;
import com.tongzhen.mooc.entities.UserInfoEditParams;
import com.tongzhen.mooc.entities.UserVerifyParams;
import com.tongzhen.mooc.entities.types.AnswerType;
import com.tongzhen.mooc.entities.types.Gender;
import com.tongzhen.mooc.entities.types.QuestionType;
import com.tongzhen.mooc.entities.types.SendCodeType;
import com.tongzhen.mooc.entities.types.StatusCode;
import com.tongzhen.mooc.entities.types.WorksType;
import com.tzhen.mooc.entities.drupal.AnswerListInfoJson;
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
import com.tzhen.mooc.entities.drupal.WorksInfoJson;
import com.tzhen.mooc.entities.drupal.WorksListInfoJson;
import com.tzhen.mooc.internal.di.DaggerDataComponent;
import com.tzhen.mooc.internal.di.DataModule;
import com.tzhen.mooc.net.drupal.ConfigEndpoints;
import com.tzhen.mooc.net.drupal.Endpoints;
import com.tzhen.mooc.net.drupal.RestApi;
import com.tzhen.mooc.storage.Persistence;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Map;

import rx.observers.TestSubscriber;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class RestApiTest {
    private RestApi restApi;
    @Mock
    private Persistence persistence;

    @Before
    public void setUp() {
        Endpoints endpoints = DaggerDataComponent.builder().dataModule(new DataModule(ConfigEndpoints.BASE_URL)).build().endpoints();
        restApi = new RestApi(endpoints, persistence);
    }

    @Test
    public void login(){
        TestSubscriber<LoginJson> subscriber = new TestSubscriber<>();

        String mobile = "18800000001@163.com";
        String password = "000000";
        restApi.login(mobile, password).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void register(){
        TestSubscriber<RegisterJson> subscriber = new TestSubscriber<>();

        String username = "18800000001@163.com";
        String password = "000000";
        String nickname = "123";
        int sex = Gender.MALE;
        int country = 86;
        String description = "AAAAAA";
        restApi.register(username, password, nickname, sex, country, description).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void check_username(){
        TestSubscriber<BaseInfo> subscriber = new TestSubscriber<>();

        String username = "qqqqqq";
        restApi.check_username(username).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void check_nickname(){
        TestSubscriber<BaseInfo> subscriber = new TestSubscriber<>();

        String nickname = "qqqqqq";
        restApi.check_nickname(nickname).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void my_info(){
        TestSubscriber<UserInfoJson> subscriber = new TestSubscriber<>();

        String uid = "qqqqqq";
        restApi.my_info(uid).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void user_edit(){
        TestSubscriber<UserInfoJson> subscriber = new TestSubscriber<>();

        String uid = "qqqqqq";
        UserInfoEditParams params = new UserInfoEditParams.Builder()
                .setEmail("5461412@qq.com")
                .setNickname("NBA").build();
        restApi.user_edit(uid, params).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void up_psd(){
        TestSubscriber<BaseInfo> subscriber = new TestSubscriber<>();

        String uid = "qqqqqq";
        String old_psd = "123456";
        String new_psd = "654321";
        restApi.up_psd(uid, old_psd, new_psd).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void up_fagpsd(){
        TestSubscriber<BaseInfo> subscriber = new TestSubscriber<>();

        String username = "qqqqqq";
        String new_psd = "654321";
        restApi.up_fagpsd(username, new_psd).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void send_code(){
        TestSubscriber<SendCodeInfoJson> subscriber = new TestSubscriber<>();

        String username = "qqqqqq";
        String type = SendCodeType.REG;
        restApi.send_code(username, type).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void my_video(){
        TestSubscriber<WorksListInfoJson> subscriber = new TestSubscriber<>();

        String uid = "qqqqqq";
        String type = WorksType.VIDEO;
        String page = "1";
        String max = "24";
        restApi.my_video(uid, type, page, max).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void my_share(){
        TestSubscriber<WorksListInfoJson> subscriber = new TestSubscriber<>();

        String uid = "qqqqqq";
        String page = "1";
        String max = "24";
        restApi.my_share(uid, page, max).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void my_collection(){
        TestSubscriber<WorksListInfoJson> subscriber = new TestSubscriber<>();

        String uid = "qqqqqq";
        String page = "1";
        String max = "24";
        restApi.my_collection(uid, page, max).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void my_question(){
        TestSubscriber<QuestionListInfoJson> subscriber = new TestSubscriber<>();

        String uid = "qqqqqq";
        String type = QuestionType.MY_ASK + "";
        restApi.my_question(uid, type).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void works_good_list(){
        TestSubscriber<WorksListInfoJson> subscriber = new TestSubscriber<>();

        String uid = "qqqqqq";
        String page = "1";
        String max = "24";
        restApi.works_good_list(uid, page, max).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void video_edit(){
        TestSubscriber<BaseInfo> subscriber = new TestSubscriber<>();

        int vid = 1;
        String uid = "qqqqqq";
        String title = "1";
        String tags = "24";
        restApi.video_edit(vid, uid, title, tags).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void video_del(){
        TestSubscriber<BaseInfo> subscriber = new TestSubscriber<>();

        int vid = 1;
        String uid = "qqqqqq";
        restApi.video_del(vid, uid).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void video_info(){
        TestSubscriber<WorksInfoJson> subscriber = new TestSubscriber<>();

        int vid = 1;
        String uid = "qqqqqq";
        restApi.video_info(vid, uid).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void works_browse(){
        TestSubscriber<BaseInfo> subscriber = new TestSubscriber<>();

        int vid = 1;
        String uid = "qqqqqq";
        restApi.works_browse(vid, uid).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void works_share_more(){
        TestSubscriber<BaseInfo> subscriber = new TestSubscriber<>();

        int vid = 1;
        String uid = "qqqqqq";
        restApi.works_share_more(vid, uid).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void works_collection(){
        TestSubscriber<BaseInfo> subscriber = new TestSubscriber<>();

        int vid = 1;
        String uid = "qqqqqq";
        int val = StatusCode.NO;
        restApi.works_collection(vid, uid, val).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void works_comments(){
        TestSubscriber<BaseInfo> subscriber = new TestSubscriber<>();

        int vid = 1;
        String uid = "qqqqqq";
        String content = "123";
        restApi.works_comments(vid, uid, content).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void works_comment_list(){
        TestSubscriber<CommentListInfoJson> subscriber = new TestSubscriber<>();

        int vid = 1;
        int page = 1;
        int max = 24;
        restApi.works_comment_list(vid, page, max).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void works_share(){
        TestSubscriber<BaseInfo> subscriber = new TestSubscriber<>();

        int vid = 1;
        int cid = 1;
        String uid = "123";
        String reason = "This is OK";
        restApi.works_share(cid, vid, uid, reason).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void user_verify(){
        TestSubscriber<BaseInfo> subscriber = new TestSubscriber<>();

        String uid = "123";
        UserVerifyParams params = new UserVerifyParams.Builder()
                .setAddress("")
                .setCity("")
                .setCountry("")
                .setEmail("")
                .setImg("")
                .setName("")
                .setSchool("")
                .setTitle("")
                .setVal("1").build();
        restApi.user_verify(uid, params).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void course_list(){
        TestSubscriber<CourseListInfoJson> subscriber = new TestSubscriber<>();

        int page = 1;
        int subject = 1;
        int max = 24;
        restApi.course_list(subject, page, max).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void course_info_introduce(){
        TestSubscriber<CourseIntroduceInfoJson> subscriber = new TestSubscriber<>();

        String uid = "1";
        int cid = 1;
        restApi.course_info_introduce(cid, uid).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void course_info_works(){
        TestSubscriber<WorksListInfoJson> subscriber = new TestSubscriber<>();

        int cid = 1;
        String uid = "1";
        int is_teacher = 1;
        restApi.course_info_works(cid, uid, is_teacher).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void course_info_question(){
        TestSubscriber<QuestionListInfoJson> subscriber = new TestSubscriber<>();

        int cid = 1;
        restApi.course_info_question(cid).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void course_edit(){
        TestSubscriber<CourseEditInfoJson> subscriber = new TestSubscriber<>();

        int cid = 1;
        CourseEditParams params = new CourseEditParams.Builder()
                .setUid("321")
                .setDescription("qasdsff")
                .setCover("2424")
                .setCourse_title("title")
                .build();
        restApi.course_edit(cid, params).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void course_apply(){
        TestSubscriber<BaseInfo> subscriber = new TestSubscriber<>();

        CourseApplyParams params = new CourseApplyParams.Builder()
                .setCourse_title("123")
                .setCover("321")
                .setDescription("qwer")
                .setSubject(1)
                .setUid("123").build();
        restApi.course_apply(params).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void my_course(){
        TestSubscriber<CourseListInfoJson> subscriber = new TestSubscriber<>();

        String uid = "123";
        restApi.my_course(uid).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void course_enroll(){
        TestSubscriber<BaseInfo> subscriber = new TestSubscriber<>();

        String uid = "123";
        int cid = 1;
        int val = StatusCode.YES;
        restApi.course_enroll(cid, uid, val).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void question_ask(){
        TestSubscriber<BaseInfo> subscriber = new TestSubscriber<>();

        String uid = "123";
        int cid = 1;
        String question = "This is question is from Jon";
        String img = "";
        restApi.question_ask(cid, uid, question, img).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void question_list(){
        TestSubscriber<QuestionListInfoJson> subscriber = new TestSubscriber<>();

        int page = 1;
        int max = 24;
        restApi.question_list(page, max).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void my_watch(){
        TestSubscriber<QuestionListInfoJson> subscriber = new TestSubscriber<>();

        int page = 1;
        int max = 24;
        String uid = "321";
        restApi.my_watch(uid, page, max).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void question_info(){
        TestSubscriber<QuestionInfoJson> subscriber = new TestSubscriber<>();

        String uid = "123";
        int qid = 1;
        restApi.question_info(uid, qid).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void question_watch(){
        TestSubscriber<BaseInfo> subscriber = new TestSubscriber<>();

        String uid = "123";
        int qid = 1;
        restApi.question_watch(uid, qid).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void answer_question(){
        TestSubscriber<BaseInfo> subscriber = new TestSubscriber<>();

        String uid = "123";
        int qid = 1;
        int atype = AnswerType.ONLY_WORDS;
        String content = "12345";
        int vid = -1;
        restApi.answer_question(uid, qid, atype, content, vid).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void answer_list(){
        TestSubscriber<AnswerListInfoJson> subscriber = new TestSubscriber<>();

        String uid = "123";
        int qid = 1;
        restApi.answer_list(uid, qid).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void anwser_help(){
        TestSubscriber<BaseInfo> subscriber = new TestSubscriber<>();

        String uid = "123";
        int qid = 1;
        int val = StatusCode.YES;
        restApi.anwser_help(uid, qid, val).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void answer_works_list(){
        TestSubscriber<WorksListInfoJson> subscriber = new TestSubscriber<>();

        String uid = "123";
        String type = WorksType.VIDEO;
        restApi.answer_works_list(uid,type).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void subject(){
        TestSubscriber<SubjectListInfoJson> subscriber = new TestSubscriber<>();

        restApi.subject().subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void title(){
        TestSubscriber<TitleListInfoJson> subscriber = new TestSubscriber<>();

        restApi.title().subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void country(){
        TestSubscriber<CountryListInfoJson> subscriber = new TestSubscriber<>();

        restApi.country().subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void city(){
        TestSubscriber<CityListInfoJson> subscriber = new TestSubscriber<>();

        restApi.city().subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void user_follow(){
        TestSubscriber<BaseInfo> subscriber = new TestSubscriber<>();

        String uid = "123";
        String oid = "32";
        int val = StatusCode.YES;
        restApi.user_follow(uid, oid, val).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void my_friends(){
        TestSubscriber<UserListInfoJson> subscriber = new TestSubscriber<>();

        String uid = "123";
        restApi.my_friends(uid).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void my_fans(){
        TestSubscriber<UserListInfoJson> subscriber = new TestSubscriber<>();

        String uid = "123";
        restApi.my_fans(uid).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void my_follows(){
        TestSubscriber<UserListInfoJson> subscriber = new TestSubscriber<>();

        String uid = "123";
        restApi.my_follows(uid).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }

    @Test
    public void user_info(){
        TestSubscriber<UserInfoJson> subscriber = new TestSubscriber<>();

        String uid = "123";
        String oid = "32";
        restApi.user_info(oid, uid).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents().size(), is(1));
    }
}
