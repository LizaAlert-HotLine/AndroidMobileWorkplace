/*
    Copyright (c) 2014 Igor Tseglevskiy <igor.tseglevskiy@gmail.com>
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

package ru.lizaalert.hotline.ui.test;

import android.app.ActionBar;
import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.EditText;

import ru.lizaalert.hotline.R;
import ru.lizaalert.hotline.ui.MainActivity;

public class MainActivityTest extends
        ActivityInstrumentationTestCase2<MainActivity> {

    MainActivity mActivity;
    ActionBar mActionBar;
    ViewPager mViewPager;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();

        setActivityInitialTouchMode(false);

        mActivity = getActivity();
        mActionBar = mActivity.getActionBar();

        mViewPager = (ViewPager) mActivity.findViewById(R.id.pager);
    }

    public void testPreconditions() {
        assertTrue(mActionBar != null);
        assertEquals(mActionBar.getTabCount(), 2);

        assertTrue(mViewPager != null);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private final static String TEST_PHONE_1 = "+78122128506";
    private final static String TEST_CITY_1 = "Москва";
    private final static String TEST_NAME_1 = "Иван Иванович";
    private final static String TEST_BIRTHDAY_1 = "1975";
    private final static String TEST_DESCR_1 = "Раз два";

    private final static String TEST_PHONE_2 = "+78122128507";
    private final static String TEST_CITY_2 = "Санкт-Петербург";
    private final static String TEST_NAME_2 = "Петр Петрович";
    private final static String TEST_BIRTHDAY_2 = "1976";
    private final static String TEST_DESCR_2 = "Три четыре";

    @UiThreadTest
    public void testStateDestroy() {
        mViewPager = (ViewPager) mActivity.findViewById(R.id.pager);
        mViewPager.setCurrentItem(0);

        EditText phone, city, name, birthday, descr;

        phone = (EditText)mActivity.findViewById(R.id.et_phone);
        city = (EditText)mActivity.findViewById(R.id.et_city);
        name = (EditText)mActivity.findViewById(R.id.et_name);
        birthday = (EditText)mActivity.findViewById(R.id.et_birthday);
        descr = (EditText)mActivity.findViewById(R.id.et_descr);

        assertTrue(phone != null);
        assertTrue(city != null);
        assertTrue(name != null);
        assertTrue(birthday != null);
        assertTrue(descr != null);

        phone.setText(TEST_PHONE_1);
        city.setText(TEST_CITY_1);
        name.setText(TEST_NAME_1);
        birthday.setText(TEST_BIRTHDAY_1);
        descr.setText(TEST_DESCR_1);

        mActivity.finish();

        mActivity = this.getActivity();

        phone = (EditText)mActivity.findViewById(R.id.et_phone);
        city = (EditText)mActivity.findViewById(R.id.et_city);
        name = (EditText)mActivity.findViewById(R.id.et_name);
        birthday = (EditText)mActivity.findViewById(R.id.et_birthday);
        descr = (EditText)mActivity.findViewById(R.id.et_descr);

        assertTrue(phone != null);
        assertTrue(city != null);
        assertTrue(name != null);
        assertTrue(birthday != null);
        assertTrue(descr != null);

        assertEquals(TEST_PHONE_1, phone.getText().toString());
        assertEquals(TEST_CITY_1, city.getText().toString());
        assertEquals(TEST_NAME_1, name.getText().toString());
        assertEquals(TEST_BIRTHDAY_1, birthday.getText().toString());
        assertEquals(TEST_DESCR_1, descr.getText().toString());

        phone.setText(TEST_PHONE_2);
        city.setText(TEST_CITY_2);
        name.setText(TEST_NAME_2);
        birthday.setText(TEST_BIRTHDAY_2);
        descr.setText(TEST_DESCR_2);

        mActivity.finish();

        mActivity = this.getActivity();

        phone = (EditText)mActivity.findViewById(R.id.et_phone);
        city = (EditText)mActivity.findViewById(R.id.et_city);
        name = (EditText)mActivity.findViewById(R.id.et_name);
        birthday = (EditText)mActivity.findViewById(R.id.et_birthday);
        descr = (EditText)mActivity.findViewById(R.id.et_descr);

        assertTrue(phone != null);
        assertTrue(city != null);
        assertTrue(name != null);
        assertTrue(birthday != null);
        assertTrue(descr != null);

        assertEquals(TEST_PHONE_2, phone.getText().toString());
        assertEquals(TEST_CITY_2, city.getText().toString());
        assertEquals(TEST_NAME_2, name.getText().toString());
        assertEquals(TEST_BIRTHDAY_2, birthday.getText().toString());
        assertEquals(TEST_DESCR_2, descr.getText().toString());
    }


    /*
    @UiThreadTest
    public void testOne() {
        Instrumentation instr = this.getInstrumentation();

        mViewPager.setCurrentItem(1);
        assertEquals(1, mViewPager.getCurrentItem());

        instr.callActivityOnPause(mActivity);

        mViewPager.setCurrentItem(0);

        instr.callActivityOnResume(mActivity);
        assertEquals(1, mViewPager.getCurrentItem());

    }
    */

}
