package com.tzhen.mooc.storage;

import android.content.Context;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.tzhen.mooc.repositories.SessionDataRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by wuyong on 2016/11/26.
 */
public class Persistence {
    public static final String KEY_USER_ID = "KEY_USER_ID";
    public static final String KEY_LOGGED_IN_USER = "KEY_LOGGED_IN_USER";
    public static final String KEY_LANGUAGE = "KEY_LANGUAGE";

    private final Context context;

    @Inject public Persistence(Context context) {
        this.context = context;
    }

    public boolean save(String key, Object data) {
        String wrapperJSONSerialized = new Gson().toJson(data);
        try {
            File file = new File(context.getFilesDir(), key);

            FileWriter fileWriter = new FileWriter(file, false);
            fileWriter.write(wrapperJSONSerialized);
            fileWriter.flush();
            fileWriter.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String key) {
        File file = new File(context.getFilesDir(), key);
        return file.delete();
    }

    @Nullable
    public <T> T retrieve(String key, Class<T> clazz) {
        try {
            File file = new File(context.getFilesDir(), key);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file.getAbsoluteFile()));
            return new Gson().fromJson(bufferedReader, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable public <T> List<T> retrieveList(String key, Type type) {
        try {
            File file = new File(context.getFilesDir(), key);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file.getAbsoluteFile()));
            return new Gson().fromJson(bufferedReader, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void clearAll() {

        //TODO: need to find a better way to indicate all the key, or maybe switch to SharedPreferences later

        String[] persistenceKey = new String[]{
                KEY_USER_ID,
                KEY_LOGGED_IN_USER,
                KEY_LANGUAGE,
                SessionDataRepository.class.getSimpleName() + "." + Persistence.KEY_LOGGED_IN_USER
        };

        for (String key: persistenceKey) {
            delete(key);
        }
    }
}
