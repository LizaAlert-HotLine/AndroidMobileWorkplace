/*
    Copyright (c) 2014 Denis Volyntsev <fortun777@gmail.com>
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

import android.app.Activity;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import ru.lizaalert.hotline.R;
import ru.lizaalert.hotline.SpreadsheetXmlParser;

/**
 * This is a temporary solution working only to load and parse spreadsheet data.
 * Please make sure to publish the document to the web, before trying to access it via this app
 * You can find instruction on publishing document here http://josephfitzsimmons.com/getting-json-data-from-google-spreadsheets-and-using-it-in-google-maps/
 * To access desired spreadsheet instantiate YELLOW_PAGES_KEY constant with your key.
 */
public class YellowPagesActivity extends Activity implements LoaderManager.LoaderCallbacks<List<SpreadsheetXmlParser.Entry>> {

    private static final String LOG_TAG = YellowPagesActivity.class.getSimpleName();
    private final String YELLOW_PAGES_KEY = "18WABg03Ja4dJHJxVMqWBeEfFYs23D3ArCEuYgQGpk7s";
    private final String YELLOW_PAGES_URL = "http://spreadsheets.google.com/feeds/list/" + YELLOW_PAGES_KEY + "/od6/public/values";
    private SpreadsheetXmlParser parser;
    private List<SpreadsheetXmlParser.Entry> entries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yellow_pages);

        getLoaderManager().initLoader(0, null, this).forceLoad();
        parser = SpreadsheetXmlParser.getInstance();
    }

    @Override
    public Loader<List<SpreadsheetXmlParser.Entry>> onCreateLoader(int id, Bundle args) {
        return  new AsyncTaskLoader<List<SpreadsheetXmlParser.Entry>>(this) {
            @Override
            public List<SpreadsheetXmlParser.Entry> loadInBackground() {

                List<SpreadsheetXmlParser.Entry> entries = null;
                try {
                    URL url = new URL(YELLOW_PAGES_URL);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    entries = parser.parse(in);

                    urlConnection.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return entries;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<SpreadsheetXmlParser.Entry>> loader, List<SpreadsheetXmlParser.Entry> data) {
        if (data != null)
            for (SpreadsheetXmlParser.Entry e : data) {
                Log.d(LOG_TAG, "e: " + e.region + " " + e.name + " " + e.phone + " " + e.description);
                this.entries = data;
            }
        else {
            Log.d(LOG_TAG, "no entries");
            Toast.makeText(this, R.string.no_data, Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onLoaderReset(Loader<List<SpreadsheetXmlParser.Entry>> loader) {
        entries = null;
    }


}
