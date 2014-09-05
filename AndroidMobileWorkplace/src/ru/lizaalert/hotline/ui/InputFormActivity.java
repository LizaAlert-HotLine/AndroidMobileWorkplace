package ru.lizaalert.hotline.ui;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ru.lizaalert.hotline.IntentFields;
import ru.lizaalert.hotline.R;
import ru.lizaalert.hotline.Settings;


public class InputFormActivity extends ActionBarActivity implements View.OnClickListener {

    private EditText etPhone;
    private EditText etCity;
    private EditText etName;
    private EditText etBirthday;
    private EditText etDescr;

    private BroadcastReceiver receiver;
    private IntentFilter intentFilter;

    private SmsManager smsManager = SmsManager.getDefault();

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

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (IntentFields.SMS_SENT.equals(action)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(InputFormActivity.this, "SMS sent", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                if (IntentFields.SMS_DELIVERED.equals(action)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(InputFormActivity.this, "SMS delivered", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        };

        intentFilter = new IntentFilter();
        intentFilter.addAction(IntentFields.SMS_SENT);
        intentFilter.addAction(IntentFields.SMS_DELIVERED);

        findViewById(R.id.btn_clear).setOnClickListener(this);
        findViewById(R.id.btn_sms).setOnClickListener(this);
        findViewById(R.id.btn_email).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
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
        }
    }

    private void sendSms() {
        String result = etPhone.getText() + "\n"
                + etCity.getText() + "\n"
                + etName.getText() + "\n"
                + etBirthday.getText() + "\n"
                + etDescr.getText();

        PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent(IntentFields.SMS_SENT), 0);
        PendingIntent deliveredIntent = PendingIntent.getBroadcast(this, 0, new Intent(IntentFields.SMS_DELIVERED), 0);

        smsManager.sendTextMessage(Settings.instance().getPhoneDest(), null, result, sentIntent, deliveredIntent);
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
