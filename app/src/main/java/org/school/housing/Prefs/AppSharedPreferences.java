package org.school.housing.Prefs;

import android.content.Context;
import android.content.SharedPreferences;

import org.school.housing.AppController;
import org.school.housing.Prefs.enums.PrefKeys;
import org.school.housing.models.admin.Admin;


public class AppSharedPreferences {
    private static AppSharedPreferences Instance;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private AppSharedPreferences(Context context) {
       sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
    }

    public static AppSharedPreferences getInstance() {
        if (Instance == null) {
            assert AppController.getInstance() != null;
            Instance = new AppSharedPreferences(AppController.getInstance());
        }
        return Instance;
    }

    public void save(Admin admin) {
        editor = sharedPreferences.edit();
        editor.putBoolean(PrefKeys.logged_in.name(), true);
        editor.putInt(PrefKeys.id.name(), admin.id);
        editor.putString(PrefKeys.name.name(), admin.name);
        editor.putString(PrefKeys.towerName.name(), admin.towerName);
        editor.putString(PrefKeys.email.name(), admin.email);
        editor.putString(PrefKeys.token.name(), "Bearer " +admin.token);
        editor.apply();
    }

    public String  getToken(){
        return sharedPreferences.getString(PrefKeys.token.name(), "");
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(PrefKeys.logged_in.name(), false);
    }

    public void clear() {
        editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
