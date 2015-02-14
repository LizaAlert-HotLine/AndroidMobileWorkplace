/*
    Copyright (c) 2014 Anton Prozorov <avprozorov@gmail.com>
    Copyright (c) 2014 Other contributors as noted in the AUTHORS file.

    Этот файл является частью приложения "Мобильное рабочее место оператора
    Горячей линии по пропавшим детям".

    Данная лицензия разрешает лицам, получившим копию "Мобильного рабочего
    места оператора Горячей линии по пропавшим детям" и сопутствующей
    документации (в дальнейшем именуемыми «Программное Обеспечение»),
    безвозмездно использовать Программное Обеспечение без ограничений, включая
    неограниченное право на использование, копирование, изменение, добавление,
    публикацию, распространение, сублицензирование и/или продажу копий
    Программного Обеспечения, также как и лицам, которым предоставляется данное
    Программное Обеспечение, при соблюдении следующих условий:

    Указанное выше уведомление об авторском праве и данные условия должны быть
    включены во все копии или значимые части данного Программного Обеспечения.

    ДАННОЕ ПРОГРАММНОЕ ОБЕСПЕЧЕНИЕ ПРЕДОСТАВЛЯЕТСЯ «КАК ЕСТЬ», БЕЗ КАКИХ-ЛИБО
    ГАРАНТИЙ, ЯВНО ВЫРАЖЕННЫХ ИЛИ ПОДРАЗУМЕВАЕМЫХ, ВКЛЮЧАЯ, НО НЕ
    ОГРАНИЧИВАЯСЬ ГАРАНТИЯМИ ТОВАРНОЙ ПРИГОДНОСТИ, СООТВЕТСТВИЯ ПО ЕГО
    КОНКРЕТНОМУ НАЗНАЧЕНИЮ И ОТСУТСТВИЯ НАРУШЕНИЙ ПРАВ. НИ В КАКОМ СЛУЧАЕ
    АВТОРЫ ИЛИ ПРАВООБЛАДАТЕЛИ НЕ НЕСУТ ОТВЕТСТВЕННОСТИ ПО ИСКАМ О ВОЗМЕЩЕНИИ
    УЩЕРБА, УБЫТКОВ ИЛИ ДРУГИХ ТРЕБОВАНИЙ ПО ДЕЙСТВУЮЩИМ КОНТРАКТАМ, ДЕЛИКТАМ
    ИЛИ ИНОМУ, ВОЗНИКШИМ ИЗ, ИМЕЮЩИМ ПРИЧИНОЙ ИЛИ СВЯЗАННЫМ С ПРОГРАММНЫМ
    ОБЕСПЕЧЕНИЕМ ИЛИ ИСПОЛЬЗОВАНИЕМ ПРОГРАММНОГО ОБЕСПЕЧЕНИЯ ИЛИ ИНЫМИ
    ДЕЙСТВИЯМИ С ПРОГРАММНЫМ ОБЕСПЕЧЕНИЕМ.

    Кроме содержимого в этом уведомлении, ни название "Горячей линии по
    пропавшим детям", ни название "Добровольного поискового отряда "Лиза
    Алерт", ни имена вышеуказанных держателей авторских прав не должно быть
    использовано в рекламе или иным способом, чтобы увеличивать продажу,
    использование или другие работы в этом Программном обеспечении без
    предшествующего письменного разрешения.

    Permission is hereby granted, free of charge, to any person obtaining a
    copy of this software and associated documentation files (the "Software"),
    to deal in the Software without restriction, including without limitation
    the rights to use, copy, modify, merge, publish, distribute, sublicense,
    and/or sell copies of the Software, and to permit persons to whom the
    Software is furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
    THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
    FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
    DEALINGS IN THE SOFTWARE.

    Except as contained in this notice, the name of Liza Alert or the name of
    Liza Alerts's hotline department or the name(s) the above copyright holders
    shall not be used in advertising or otherwise to promote the sale, use or
    other dealings in this Software without prior written authorization.
 */

package ru.lizaalert.hotline.lib.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings {
    @SuppressWarnings("UnusedDeclaration")
    private static final String TAG = "8800";

    public static Settings self;
    private SharedPreferences sharedPreferences;

    private Settings(Context context) {
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
     * Set last entered applicant's phone number
     * @param phoneNumber String
     */
    public void setPhoneApplRecent(String phoneNumber) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SettingsConsts.PREF_PHONE_APPL_RECENT, phoneNumber);
        editor.apply();
    }
    
    /**
     * Get last entered city of loss
     * @return String
     */
    public String getCityRecent() {
        return sharedPreferences.getString(SettingsConsts.PREF_CITY_RECENT, "");
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
     * Get last entered name
     * @return String
     */
    public String getNameRecent() {
        return sharedPreferences.getString(SettingsConsts.PREF_NAME_RECENT, "");
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
     * Get last entered date of birth
     * @return String
     */
    public String getBirthdayRecent() {
        return sharedPreferences.getString(SettingsConsts.PREF_BIRTHDAY_RECENT, "");
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
     * Get last entered description
     * @return String
     */
    public String getDescrRecent() {
        return sharedPreferences.getString(SettingsConsts.PREF_DESCR_RECENT, "");
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
     *  Get last position in the list of entries
     * @return int
     */
    public int getYellowPagesLastListPosition() {
        return sharedPreferences.getInt(SettingsConsts.PREF_YELLOW_PAGES_LIST_POSITION, 0);
    }
    
    /**
     *  Set last position in the list of entries
     * @param position int
     */
    public void setYellowPagesLastListPosition(int position) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SettingsConsts.PREF_YELLOW_PAGES_LIST_POSITION, position);
        editor.apply();
    }

    /**
     * Get Yellow Pages region
     * @return String
     */
    public String getYellowPagesRegion() {
        return sharedPreferences.getString(SettingsConsts.PREF_YELLOW_PAGES_REGION, "Все регионы");
    }

    /**
     * Set last entered description
     * @param ypRegion String
     */
    public void setYellowPagesRegion(String ypRegion) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SettingsConsts.PREF_YELLOW_PAGES_REGION, ypRegion);
        editor.apply();
    }

    /**
     * Get is licence accepted
     * @return boolean
     */
    public boolean isLicenceAccepted() {
        return sharedPreferences.getBoolean(SettingsConsts.PREF_LICENCE_ACCEPTED, false);
    }

    /**
     * Set licence accepted
     * @param licenceAccepted boolean
     */
    public void setLicenceAccepted(boolean licenceAccepted) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SettingsConsts.PREF_LICENCE_ACCEPTED, licenceAccepted);
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


    /**
     * Get account mail
     * @return String
     */
    public String getAccountMail() {
        return sharedPreferences.getString(SettingsConsts.PREF_ACCOUNT_MAIL, "");
    }

    /**
     * Set account mail
     * @param mail String
     */
    public void setAccountMail(String mail) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SettingsConsts.PREF_ACCOUNT_MAIL, mail);
        editor.apply();
    }

    /**
     * Get account token
     * @return String
     */
    public String getAccountToken() {
        return sharedPreferences.getString(SettingsConsts.PREF_ACCOUNT_TOKEN, "");
    }

    /**
     * Set account token
     * @param token String
     */
    public void setAccountToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SettingsConsts.PREF_ACCOUNT_TOKEN, token);
        editor.apply();
    }



}
