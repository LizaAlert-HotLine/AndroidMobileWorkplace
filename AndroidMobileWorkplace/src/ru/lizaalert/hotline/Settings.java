package ru.lizaalert.hotline;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ololoev on 03.09.14.
 */
public class Settings {

    public static Settings self;
    private Context context;
    private SharedPreferences sharedPreferences;

    private Settings(Context context) {
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static Settings instance(Context context) {
        if (self == null) {
            self = new Settings(context);
        }
        return self;
    }

    public static Settings instance() {
        return self;
    }

    /**
     * Get destination phone number to send SMS
     * @return String
     */
    public String getPhoneDest() {
        return sharedPreferences.getString(SettingsConsts.PREF_PHONE_DEST, "");
    }

    /**
     * Get last entered applicant's phone number
     * @return String
     */
    public String getPhoneApplRecent() {
        return sharedPreferences.getString(SettingsConsts.PREF_PHONE_APPL_RECENT, "");
    }

    /**
     * Get last entered city of loss
     * @return String
     */
    public String getCityRecent() {
        return sharedPreferences.getString(SettingsConsts.PREF_CITY_RECENT, "");
    }

    /**
     * Get last entered name
     * @return String
     */
    public String getNameRecent() {
        return sharedPreferences.getString(SettingsConsts.PREF_NAME_RECENT, "");
    }

    /**
     * Get last entered date of birth
     * @return String
     */
    public String getBirthdayRecent() {
        return sharedPreferences.getString(SettingsConsts.PREF_BIRTHDAY_RECENT, "");
    }

    /**
     * Get last entered description
     * @return String
     */
    public String getDescrRecent() {
        return sharedPreferences.getString(SettingsConsts.PREF_DESCR_RECENT, "");
    }

    /**
     * Set last entered applicant's phone number
     * @param phoneNumber String
     */
    public void setPhoneApplRecent(String phoneNumber) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SettingsConsts.PREF_PHONE_APPL_RECENT, phoneNumber);
        editor.apply();
    }

    /**
     * Set last entered city of loss
     * @param cityRecent String
     */
    public void setCityRecent(String cityRecent) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SettingsConsts.PREF_CITY_RECENT, cityRecent);
        editor.apply();
    }

    /**
     * Set last entered name
     * @param nameRecent String
     */
    public void setNameRecent(String nameRecent) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SettingsConsts.PREF_NAME_RECENT, nameRecent);
        editor.apply();
    }

    /**
     * Set last entered date of birth
     * @param birthdayRecent String
     */
    public void setBirthdayRecent(String birthdayRecent) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SettingsConsts.PREF_BIRTHDAY_RECENT, birthdayRecent);
        editor.apply();
    }

    /**
     * Set last entered description
     * @param descrRecent String
     */
    public void setDescrRecent(String descrRecent) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SettingsConsts.PREF_DESCR_RECENT, descrRecent);
        editor.apply();
    }

    /**
     * Clear last entered values
     * Don't forget to use this after you send data
     */
    public void clearRecent() {
        setPhoneApplRecent("");
        setCityRecent("");
        setNameRecent("");
        setBirthdayRecent("");
        setDescrRecent("");
    }
}
