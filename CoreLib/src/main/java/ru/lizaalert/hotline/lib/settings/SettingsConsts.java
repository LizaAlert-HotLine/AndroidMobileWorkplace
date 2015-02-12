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

public class SettingsConsts {
    /**
     * {@value} account mail
     */
    public final static String PREF_ACCOUNT_MAIL = "pref_account_mail";

    /**
     * {@value} account token
     */
    public final static String PREF_ACCOUNT_TOKEN = "pref_account_token";

    /**
     * {@value} phone number to send SMS
     */
    public final static String PREF_PHONE_DEST = "pref_phone_dest";

    /**
     * {@value} last entered applicant's phone number
     */
    public final static String PREF_PHONE_APPL_RECENT = "pref_phone_appl_recent";

    /**
     * {@value} last entered city of loss
     */
    public final static String PREF_CITY_RECENT = "pref_city_recent";

    /**
     * {@value} last entered name
     */
    public final static String PREF_NAME_RECENT = "pref_name_recent";

    /**
     * {@value} last entered date of birth
     */
    public final static String PREF_BIRTHDAY_RECENT = "pref_birthday_recent";

    /**
     * {@value} last entered description
     */
    public final static String PREF_DESCR_RECENT = "pref_descr_recent";

    /**
     * {@value} last chosen organization region position in Yellow Pages
     */
    public static final String PREF_YELLOW_PAGES_REGION = "pref_organization_region";

    /**
     * {@value} last chosen organization region position in Yellow Pages
     */
    public static final String PREF_YELLOW_PAGES_LIST_POSITION = "pref_yellow_pages_list_position";

    /**
     * {@value} first lauch flag
     */
    public static final String PREF_LICENCE_ACCEPTED = "pref_first_launch";

    /**
     * {@value} SMTP host
     */
    public static final String PREF_SMTP_MAILHOST = "pref_smtp_mailhost";

    /**
     * {@value} SMTP login
     */
    public static final String PREF_SMTP_LOGIN = "pref_smtp_login";

    /**
     * {@value} SMTP password
     */
    public static final String PREF_SMTP_PASSWORD = "pref_smtp_password";

    /**
     * {@value} SMTP from
     */
    public static final String PREF_SMTP_FROM = "pref_smtp_from";

    /**
     * {@value} SMTP to
     */
    public static final String PREF_SMTP_TO = "pref_smtp_to";

}
