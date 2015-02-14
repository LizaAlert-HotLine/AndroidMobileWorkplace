/*
    Copyright (c) 2014 Ivan Demushkin <ivndgtl@gmail.com>
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

package ru.lizaalert.hotline;

/**
 * Created by Ivan Demushkin on 30.01.2015
 */

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

public class GoogleSheetManager extends AsyncTask<Object, Void, Boolean> {

    private static final String TAG = "8800";

    private static final String SHEET_META_URL = "https://spreadsheets.google.com/feeds/spreadsheets/private/full";
    private static final String SHEET_NAME = "LizaAlert data";
    private static final String GOOGLE_LOGIN = "lizaalert.mobile";
    private static final String GOOGLE_PWD = "LiZa@lErT";

    private SpreadsheetService service;
    private SpreadsheetFeed feedMain;
    private SpreadsheetEntry sheet;
    private WorksheetEntry worksheet;
    private URL urlFeedList;
    private ListFeed feedList;

    private Context context;

    private String phone;
    private String city;
    private String name;
    private String birthday;
    private String description;

    public GoogleSheetManager(Context context, String phone, String city, String name, String birthday, String description) {
        this.context = context;
        this.phone = phone;
        this.city = city;
        this.name = name;
        this.birthday = birthday;
        this.description = description;
    }

    @Override
    protected Boolean doInBackground(Object... arg) {
        // get parameters
        service = new SpreadsheetService("LizaAlert.GoogleSpreadsheetIntegration");
        service.setProtocolVersion(SpreadsheetService.Versions.V3);

        try {
            service.setUserCredentials(GOOGLE_LOGIN, GOOGLE_PWD);

            feedMain = service.getFeed(new URL(SHEET_META_URL), SpreadsheetFeed.class);
            List<SpreadsheetEntry> spreadsheets = feedMain.getEntries();

            if (spreadsheets == null || spreadsheets.size() == 0) {
                Log.e(TAG, "There are no any spreadsheet");
                return false;
            }

            // find required spreadsheet
            for(SpreadsheetEntry spreadsheet : spreadsheets) {
                Log.d(TAG, spreadsheet.getTitle().getPlainText());

                if (spreadsheet.getTitle().getPlainText().equals(SHEET_NAME)) {
                    sheet = spreadsheet;
                    break;
                }
            }

            if (sheet == null) {
                Log.e(TAG, String.format("Spreadsheet '%s' not found", SHEET_NAME));
                return false;
            }

            // get default worksheet
            worksheet = sheet.getDefaultWorksheet();
            Log.d(TAG, "Worksheet: " + worksheet);

            URL urlFeedList = worksheet.getListFeedUrl();
            feedList = service.getFeed(urlFeedList, ListFeed.class);
            Log.d(TAG, "feedList:" + feedList);

            // create new data row
            ListEntry row = new ListEntry();

            row.getCustomElements().setValueLocal("Phone", phone);
            row.getCustomElements().setValueLocal("City", city);
            row.getCustomElements().setValueLocal("Name", name);
            row.getCustomElements().setValueLocal("Birthday", birthday);
            row.getCustomElements().setValueLocal("Description", description);

            // append new data row to worksheet
            row = service.insert(urlFeedList, row);
        }
        catch(AuthenticationException e) {
            Log.e(TAG, "Error on authenticating in Google", e);
            return false;
        }
        catch (ServiceException e) {
            Log.e(TAG, "Error on row appending", e);
            return false;
        }
        catch (IOException e) {
            Log.e(TAG, "Error on row appending", e);
            return false;
        }
        catch(Exception e) {
            Log.e(TAG, "Unknown error", e);
            return false;
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result == true) {
            Toast.makeText(context, "Successfully saved", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(context, "Ops... An error occurred", Toast.LENGTH_LONG).show();
        }
    }
}
