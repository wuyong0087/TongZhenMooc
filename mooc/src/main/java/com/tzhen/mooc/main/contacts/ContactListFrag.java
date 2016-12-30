package com.tzhen.mooc.main.contacts;

import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.tongzhen.mooc.entities.UserInfo;
import com.tongzhen.mooc.entities.UserListInfo;
import com.tongzhen.mooc.entities.types.ResultCodes;
import com.tongzhen.mooc.views.UserListView;
import com.tzhen.commen.adapters.ContactsAdapter;
import com.tzhen.commen.fragment.LazyLoadFrag;
import com.tzhen.mooc.R;
import com.tzhen.mooc.activities.CountryListActivity;
import com.tzhen.mooc.countrylist.model.ContactItem;
import com.tzhen.mooc.countrylist.model.CountryItem;
import com.tzhen.mooc.countrylist.widget.ContactListViewImpl;
import com.tzhen.mooc.countrylist.widget.ItemInterface;
import com.tzhen.mooc.countrylist.widget.pinyin.PinYin;
import com.tzhen.mooc.navigator.Navigator;
import com.tzhen.mooc.storage.Persistence;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by wuyong on 16/12/30.
 */
@EFragment(R.layout.fragment_contact_list)
public abstract class ContactListFrag extends LazyLoadFrag<UserListInfo> implements UserListView, TextWatcher {

    @ViewById(R.id.lv_contact_list) protected ContactListViewImpl lvContacts;

    @ViewById(R.id.et_search_content) EditText etSearchContent;

    @Inject protected Navigator navigator;

    @Inject Persistence persistence;

    private String searchString;

    protected String uid;

    private Object searchLock = new Object();

    boolean inSearchMode = false;

    private final static String TAG = CountryListActivity.class.getSimpleName();

    List<ItemInterface> contactList;

    List<ItemInterface> filterList;

    private SearchListTask curSearchTask = null;

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void initViews() {
        super.initViews();

        uid = persistence.retrieve(Persistence.KEY_USER_ID, String.class);

        filterList = new ArrayList<>();

        lvContacts.setFastScrollEnabled(true);

        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position,
                                    long id) {
                List<ItemInterface> searchList = inSearchMode ? filterList
                        : contactList;

                ItemInterface item = searchList.get(position);
                navigator.toUserInfo(getContext(), item.getUserInfo().getUid());
            }
        });
        etSearchContent.addTextChangedListener(this);
    }

    @Override
    public void onSuccess(UserListInfo value) {
        if (ResultCodes.OK == value.getResult()){
            contactList = getContactList(value.getUserInfoList());
            ContactsAdapter adapter = new ContactsAdapter(getContext(), R.layout.item_contact, contactList);
            lvContacts.setAdapter(adapter);
        } else{
            showMsg("Get data failed");
        }
    }

    private List<ItemInterface> getContactList(List<UserInfo> contactList) {
        List<ItemInterface> list = new ArrayList<ItemInterface>();
        for (UserInfo user : contactList) {
            list.add(new ContactItem(user.getName(), PinYin.getPinYin(user.getName()), user.getUid(), user.getHead()));
        }
        return list;
    }

    @Override
    public void afterTextChanged(Editable s) {
        searchString = etSearchContent.getText().toString().trim();
        if (curSearchTask != null
                && curSearchTask.getStatus() != AsyncTask.Status.FINISHED) {
            try {
                curSearchTask.cancel(true);
            } catch (Exception e) {
                Log.i(TAG, "Fail to cancel running search task");
            }

        }
        curSearchTask = new SearchListTask();
        curSearchTask.execute(searchString);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // do nothing
    }

    private class SearchListTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            filterList.clear();

            String keyword = params[0];

            inSearchMode = (keyword.length() > 0);

            if (inSearchMode) {
                // get all the items matching this
                for (ItemInterface item : contactList) {
                    CountryItem contact = (CountryItem) item;

                    boolean isPinyin = contact.getFullName().toUpperCase().indexOf(keyword) > -1;
                    boolean isChinese = contact.getCountryName().indexOf(keyword) > -1;

                    if (isPinyin || isChinese) {
                        filterList.add(item);
                    }
                }
            }
            return null;
        }

        protected void onPostExecute(String result) {

            synchronized (searchLock) {

                if (inSearchMode) {

                    ContactsAdapter adapter = new ContactsAdapter(getContext(), R.layout.item_city, filterList);
                    adapter.setInSearchMode(true);
                    lvContacts.setInSearchMode(true);
                    lvContacts.setAdapter(adapter);
                } else {
                    ContactsAdapter adapter = new ContactsAdapter(getContext(), R.layout.item_city, contactList);
                    adapter.setInSearchMode(false);
                    lvContacts.setInSearchMode(false);
                    lvContacts.setAdapter(adapter);
                }
            }

        }
    }
}
