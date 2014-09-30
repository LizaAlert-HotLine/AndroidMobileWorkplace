package ru.lizaalert.hotline.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.vk.sdk.VKUIHelper;

import java.util.Calendar;

import ru.lizaalert.hotline.ChannelHandler;
import ru.lizaalert.hotline.R;
import ru.lizaalert.hotline.Settings;
import ru.lizaalert.hotline.SmsChannel;
import ru.lizaalert.hotline.VkManager;


public class InputFormActivity extends ActionBarActivity implements View.OnClickListener, ChannelHandler {

    private EditText etPhone;
    private EditText etCity;
    private EditText etName;
    private EditText etBirthday;
    private EditText etDescr;

    private SmsChannel smsChannel = new SmsChannel(this);
    private VkManager vkManager;

    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
        vkManager = VkManager.getInstance(this);
        vkManager.initVk();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
    }

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
        findViewById(R.id.btn_vk).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.input_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
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
                Settings.instance().clearRecent();
                break;
            case R.id.btn_sms:

                if (Settings.instance().getPhoneDest().equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(getString(R.string.error_no_phone));
                    builder.setPositiveButton(getString(R.string.got_it), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(InputFormActivity.this, SettingsActivity.class));
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                } else {
                    sendSms();
                }

                break;
            case R.id.btn_email:
                Toast.makeText(this, "This is dummy Email button", Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_vk:
                vkManager.requestWallPost(composeMessage());
                break;
        }
    }

    private void sendSms() {
        String result = composeMessage();
        smsChannel.send(result, Settings.instance().getPhoneDest(), this);
    }

    private String composeMessage() {
        return etPhone.getText() + "\n"
                + etCity.getText() + "\n"
                + etName.getText() + "\n"
                + etBirthday.getText() + "\n"
                + etDescr.getText();
    }

    @Override
    public void sent(Calendar c) {
        Toast.makeText(this, "Sent at " + c.getTime(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void error(Calendar c, String message) {

    }

    @Override
    public void delivered(Calendar c) {
        Toast.makeText(this, "Delivered at " + c.getTime(), Toast.LENGTH_LONG).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        VKUIHelper.onActivityResult(requestCode, resultCode, data);
    }
}
