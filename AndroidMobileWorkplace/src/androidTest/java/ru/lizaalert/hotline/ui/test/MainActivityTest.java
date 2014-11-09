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
