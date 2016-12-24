package com.tzhen.mooc.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tongzhen.mooc.entities.BaseInfo;
import com.tzhen.commen.activity.BaseActivity;
import com.tzhen.mooc.R;
import com.tzhen.mooc.main.ContactsFragment_;
import com.tzhen.mooc.main.MLMFragment_;
import com.tzhen.mooc.main.MeFragment_;
import com.tzhen.mooc.main.QAFragment_;
import com.tzhen.mooc.navigator.Navigator;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity<BaseInfo> implements TabLayout.OnTabSelectedListener {
    private int[] unSelectIcons = {R.drawable.mlm, R.drawable.qa, R.drawable.create, R.drawable.contacts, R.drawable.me};
    private int[] selectIcons = {R.drawable.mlm_press, R.drawable.qa_press, R.drawable.create_press, R.drawable.contacts_press, R.drawable.me_press};
    private int[] titles = {R.string.mlm, R.string.qa, R.string.create_empty, R.string.contacts, R.string.me};
    private final int MLM = 0;
    private final int QA = 1;
    private final int CREATE = 2;
    private final int CONTACTS = 3;
    private final int ME = 4;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.tab_bottom)
    TabLayout tabBottom;

    @Inject
    Navigator navigator;

    private MenuItem rightMenuSearch, rightMenuFilter, rightMenuMore;

    private MLMFragment_.FragmentBuilder_ mlmFragBulder;
    private QAFragment_.FragmentBuilder_ qaFragBuilder;
    private ContactsFragment_.FragmentBuilder_ contactsFragBuilder;
    private MeFragment_.FragmentBuilder_ meFragBuilder;

    private FragmentManager fm;

    @Override
    protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }

    @Override
    protected void initViews() {
        super.initViews();

        setupViews();

        attachFragment(MLM);

    }

    private void setupViews() {

        initToolBar(toolbar, true);

        setToolbarTitle(getString(R.string.mlm));

        toolbar.setNavigationIcon(R.drawable.course_center);

        initTabs();

        initFragments();
        attachFragment(MLM);
    }

    private void initTabs() {

        for (int i = 0; i < titles.length; i++) {
            View view = View.inflate(this, R.layout.layout_tab, null);

            ImageView ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
            TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
            if (i == 2) {
                ivIcon.setVisibility(View.INVISIBLE);
            }

            ivIcon.setImageResource(unSelectIcons[i]);
            tvTitle.setText(titles[i]);
            TabLayout.Tab tab = tabBottom.newTab().setCustomView(view).setTag(i);
            if (i == 0){
                ivIcon.setImageResource(selectIcons[i]);
                tvTitle.setTextColor(ContextCompat.getColor(this, R.color.blue));
            }
            tabBottom.addTab(tab);

        }

        tabBottom.setOnTabSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        rightMenuSearch = menu.findItem(R.id.action_search);
        rightMenuFilter = menu.findItem(R.id.action_filter);
        rightMenuMore = menu.findItem(R.id.action_more);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                navigator.toCourseCenter(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        updateTabState(tab, true);

        updateToolbar(tab);

        attachFragment((int)tab.getTag());
    }

    private void initFragments(){
        mlmFragBulder = MLMFragment_.builder();
        qaFragBuilder = QAFragment_.builder();
        contactsFragBuilder = ContactsFragment_.builder();
        meFragBuilder = MeFragment_.builder();
        fm = getSupportFragmentManager();
    }

    private void attachFragment(int tag) {
        Fragment frag = null;
        frag = fm.findFragmentByTag(tag + "");
        switch (tag){
            case MLM:
                if (frag == null){
                    frag = mlmFragBulder.build();
                    fm.beginTransaction().add(R.id.fl_container, frag, tag + "");
                }
                break;
            case QA:
                if (frag == null){
                    frag = qaFragBuilder.build();
                    fm.beginTransaction().add(R.id.fl_container, frag, tag + "");
                }
                break;
            case CONTACTS:
                if (frag == null){
                    frag = contactsFragBuilder.build();
                    fm.beginTransaction().add(R.id.fl_container, frag, tag + "");
                }
                break;
            case ME:
                if (frag == null){
                    frag = meFragBuilder.build();
                    fm.beginTransaction().add(R.id.fl_container, frag, tag + "");
                }
                break;
        }

        if (frag != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.show(frag).commit();
        }
    }

    private void updateToolbar(TabLayout.Tab tab) {
        int index = (int) tab.getTag();
        int title = -1;
        switch (index) {
            case MLM:
                title = R.string.mlm;
                break;
            case QA:
                title = R.string.qa;
                break;
            case CONTACTS:
                title = R.string.contacts;
                break;
            case ME:
                title = R.string.me;
                break;
        }
        if (title > 0){
            setToolbarTitle(getString(title));
        }

        if (index == CREATE) {
            performClick(R.id.iv_create);
        }

        setupMenuIcon(index);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        updateTabState(tab, false);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void updateTabState(TabLayout.Tab tab, boolean isSelect) {
        View view = tab.getCustomView();
        ImageView ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);

        int index = (int) tab.getTag();
        ivIcon.setImageResource(isSelect ? selectIcons[index] : unSelectIcons[index]);
        tvTitle.setTextColor(ContextCompat.getColor(this, isSelect ? R.color.tab_select : R.color.tab_un_select));
    }

    @Click({R.id.iv_create})
    public void viewsClick(View view) {
        switch (view.getId()) {
            case R.id.iv_create:
                Toast.makeText(this, "asq12", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    public void performClick(int viewId) {
        View view = findViewById(viewId);
        if (view != null) {
            view.performClick();
        }
    }

    public void setupMenuIcon(int index) {
        rightMenuFilter.setVisible(false);
        rightMenuMore.setVisible(false);
        rightMenuSearch.setVisible(false);
        switch (index) {
            case MLM:
            case QA:
                toolbar.setNavigationIcon(R.drawable.course_center);
                rightMenuFilter.setVisible(true);
                break;
            case CONTACTS:
                setToolBarDisplayHomeAsUpEnabled(false);
                rightMenuSearch.setVisible(true);
            case ME:
                setToolBarDisplayHomeAsUpEnabled(false);
                break;
        }
    }
}
