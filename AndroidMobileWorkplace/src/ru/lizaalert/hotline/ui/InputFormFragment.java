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

package ru.lizaalert.hotline.ui;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.vk.sdk.VKUIHelper;

import java.util.Calendar;

import ru.lizaalert.hotline.ChannelHandler;
import ru.lizaalert.hotline.R;
import ru.lizaalert.hotline.Settings;
import ru.lizaalert.hotline.SmsChannel;
import ru.lizaalert.hotline.VkManager;

/**
 * Created by defuera on 09/10/14.
 */
public class InputFormFragment extends Fragment implements View.OnClickListener, ChannelHandler {


    private EditText etPhone;
    private EditText etCity;
    private EditText etName;
    private EditText etBirthday;
    private EditText etDescr;

    private SmsChannel smsChannel;
    private VkManager vkManager;
    private View contentView;

    @Override
    public void onResume() {
        super.onResume();
        VKUIHelper.onResume(getActivity());
        vkManager = VkManager.getInstance(getActivity());
        vkManager.initVk();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(getActivity());
    }

    public View findViewById(int id) {
        if (contentView == null) {
            return null;
        }
        return contentView.findViewById(id);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = super.onCreateView(inflater, container, savedInstanceState);
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.fragment_input_form, container, false);
            initUi();
        }
        return contentView;
    }

    private void initUi() {
        etPhone = (EditText) findViewById(R.id.et_phone);
        etPhone.setText(Settings.instance(getActivity().getApplicationContext()).getPhoneApplRecent());

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VKUIHelper.onCreate(getActivity());
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(getString(R.string.error_no_phone));
                    builder.setPositiveButton(getString(R.string.got_it), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(getActivity(), SettingsActivity.class));
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                } else {
                    sendSms();
                }

                break;
            case R.id.btn_email:
                Toast.makeText(getActivity(), "getActivity() is dummy Email button", Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_vk:
                vkManager.requestWallPost(composeMessage());
                break;
        }
    }

    private void sendSms() {
        String result = composeMessage();
        (new SmsChannel(getActivity())).send(result, Settings.instance().getPhoneDest(), this);
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
        Toast.makeText(getActivity(), "Sent at " + c.getTime(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void error(Calendar c, String message) {

    }

    @Override
    public void delivered(Calendar c) {
        Toast.makeText(getActivity(), "Delivered at " + c.getTime(), Toast.LENGTH_LONG).show();
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
