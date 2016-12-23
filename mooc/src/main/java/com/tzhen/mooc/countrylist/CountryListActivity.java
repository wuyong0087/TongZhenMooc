package com.tzhen.mooc.countrylist;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tongzhen.mooc.entities.CityListInfo;
import com.tongzhen.mooc.entities.CountryInfo;
import com.tzhen.commen.activity.BaseActivity;
import com.tzhen.mooc.R;
import com.tzhen.mooc.countrylist.adapter.CityAdapter;
import com.tzhen.mooc.countrylist.model.CountryItem;
import com.tzhen.mooc.countrylist.widget.CountryItemInterface;
import com.tzhen.mooc.countrylist.widget.ContactListViewImpl;
import com.tzhen.mooc.countrylist.widget.pinyin.PinYin;
import com.tzhen.mooc.events.CountryEvent;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_citylist)
public class CountryListActivity extends BaseActivity<CityListInfo> implements TextWatcher {
    private Context context_ = CountryListActivity.this;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.lv_citys)
    ContactListViewImpl lvCityList;

    @ViewById(R.id.input_search_query)
    EditText searchBox;

    private String searchString;

    private Object searchLock = new Object();
    boolean inSearchMode = false;

    private final static String TAG = CountryListActivity.class.getSimpleName();

    List<CountryItemInterface> countryList;
    List<CountryItemInterface> filterList;
    private SearchListTask curSearchTask = null;

    private EventBus eventBus;

    @Override
    protected void init() {
        super.init();
        getApplicationComponent().inject(this);
    }

    @Override
    protected void initViews() {
        super.initViews();

        initToolBar(toolbar, true);

        setToolbarTitle(getString(R.string.country_list));
        filterList = new ArrayList<>();
        countryList = getCityList(getCountries());
        CityAdapter adapter = new CityAdapter(this, R.layout.item_city, countryList);

        lvCityList = (ContactListViewImpl) this.findViewById(R.id.lv_citys);
        lvCityList.setFastScrollEnabled(true);
        lvCityList.setAdapter(adapter);

        lvCityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position,
                                    long id) {
                List<CountryItemInterface> searchList = inSearchMode ? filterList
                        : countryList;

                postCountryEvent(searchList.get(position).getCountryInfo());
                finish();
            }
        });
        searchBox.addTextChangedListener(this);
    }

    @Override
    public void afterTextChanged(Editable s) {
        searchString = searchBox.getText().toString().trim().toUpperCase();

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
                for (CountryItemInterface item : countryList) {
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

                    CityAdapter adapter = new CityAdapter(context_, R.layout.item_city, filterList);
                    adapter.setInSearchMode(true);
                    lvCityList.setInSearchMode(true);
                    lvCityList.setAdapter(adapter);
                } else {
                    CityAdapter adapter = new CityAdapter(context_, R.layout.item_city, countryList);
                    adapter.setInSearchMode(false);
                    lvCityList.setInSearchMode(false);
                    lvCityList.setAdapter(adapter);
                }
            }

        }
    }

    public List<CountryItemInterface> getCityList(List<CountryInfo> countryInfoList) {
        List<CountryItemInterface> list = new ArrayList<CountryItemInterface>();
        for (CountryInfo country : countryInfoList) {
            list.add(new CountryItem(country.getName(), PinYin.getPinYin(country.getName()), country.getId()));
        }
        return list;
    }

    public ArrayList<CountryInfo> getCountries(){
        ArrayList<CountryInfo> countries = null;
        InputStream is = null;
        try {
            is = getAssets().open("countries.txt");
            StringBuffer out = new StringBuffer();
            byte[] b = new byte[4096];
            for (int n; (n = is.read(b)) != -1;) {
                out.append(new String(b, 0, n));
            }
            Type listType = new TypeToken<ArrayList<CountryInfo>>() {
            }.getType();
            Gson gson = new Gson();
            countries = gson.fromJson(out.toString(), listType);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return countries;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        eventBus = EventBus.getDefault();
        eventBus.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        eventBus.unregister(this);
    }

    private void postCountryEvent(CountryInfo info){
        eventBus.post(new CountryEvent(info));
    }
}
