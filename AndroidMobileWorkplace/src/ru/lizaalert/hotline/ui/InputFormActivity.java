package ru.lizaalert.hotline.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ru.lizaalert.hotline.R;
import ru.lizaalert.hotline.Settings;


public class InputFormActivity extends ActionBarActivity implements View.OnClickListener {

    private EditText etPhone;
    private EditText etCity;
    private EditText etName;
    private EditText etBirthday;
    private EditText etDescr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_form);

        etPhone = (EditText) findViewById(R.id.et_phone);
        etPhone.setText(Settings.instance(getApplicationContext()).getPhoneApplRecent());

        etCity = (EditText) findViewById(R.id.et_city);
        etCity.setText(Settings.instance().getCityRecent());

        etName = (EditText) findViewById(R.id.et_name);
        etName.setText(Settings.instance().getNameRecent());

        etBirthday = (EditText) findViewById(R.id.et_birthday);
        etBirthday.setText(Settings.instance().getBirthdayRecent());

        etDescr = (EditText) findViewById(R.id.et_descr);
        etDescr.setText(Settings.instance().getDescrRecent());

        etPhone.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                Settings.instance().setPhoneApplRecent(editable.toString());
            }
        });

        etCity.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                Settings.instance().setCityRecent(editable.toString());
            }
        });

        etName.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                Settings.instance().setNameRecent(editable.toString());
            }
        });

        etBirthday.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                Settings.instance().setBirthdayRecent(editable.toString());
            }
        });

        etDescr.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                Settings.instance().setDescrRecent(editable.toString());
            }
        });

        findViewById(R.id.btn_clear).setOnClickListener(this);
        findViewById(R.id.btn_sms).setOnClickListener(this);
        findViewById(R.id.btn_email).setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.input_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearInput() {
        etPhone.setText("");
        etCity.setText("");
        etName.setText("");
        etBirthday.setText("");
        etDescr.setText("");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_clear:
                clearInput();
                break;
            case R.id.btn_sms:
                Toast.makeText(this, "This is dummy SMS button", Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_email:
                Toast.makeText(this, "This is dummy Email button", Toast.LENGTH_LONG).show();
                // FIXME throwing NPE to test saving input data
                throw new NullPointerException();
//                break;
        }
        Settings.instance().clearRecent();
    }

    abstract class SimpleTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public abstract void afterTextChanged(Editable editable);
    }
}
