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

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.lizaalert.hotline.R;
import ru.lizaalert.hotline.Settings;
import ru.lizaalert.hotline.SpreadsheetXmlParser;
import ru.lizaalert.hotline.adapters.OrganizationsArrayAdapter;

/**
 * Created by defuera on 10/10/14.
 */
public class YellowPagesFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<SpreadsheetXmlParser.Entry>> {

    private static final String LOG_TAG = YellowPagesFragment.class.getSimpleName();
    private static final String YELLO_PAGES_FILENAME = "yellow_pages";
    private static final int LOCAL_DATA_LOADER_ID = 0;

    private final String YELLOW_PAGES_KEY = "18WABg03Ja4dJHJxVMqWBeEfFYs23D3ArCEuYgQGpk7s";
    private final String YELLOW_PAGES_URL = "http://spreadsheets.google.com/feeds/list/" + YELLOW_PAGES_KEY + "/od6/public/values";
    private SpreadsheetXmlParser parser;
    private List<SpreadsheetXmlParser.Entry> entries;
    private File file;
    private View contentView;
    private List<String> regions = new ArrayList<>();
    private Spinner spinner;
    private ArrayAdapter<String> spinnerAdapter;
    private ListView list;
    private OrganizationsArrayAdapter organizationsAdapter;


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
            contentView = inflater.inflate(R.layout.fragment_yellow_pages, container, false);

            spinner = (Spinner) findViewById(R.id.spinner);
            spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, regions);
            spinner.setAdapter(spinnerAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String filter = spinnerAdapter.getItem(position);
                    organizationsAdapter.applyFilter(filter);
                    Settings.instance(getActivity()).setLastOrganizationsRegionPosition(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            list = (ListView) findViewById(R.id.list);
            organizationsAdapter = new OrganizationsArrayAdapter(getActivity(), entries);
            list.setAdapter(organizationsAdapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView description = ((TextView) view.findViewById(R.id.descriprion));
                    if (description.getLineCount() == 4)
                        description.setMaxLines(Integer.MAX_VALUE);
                    else
                        description.setMaxLines(4);
                    view.invalidate();
                }
            });
        }
        return contentView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        file = new File(getActivity().getFilesDir(), YELLO_PAGES_FILENAME);

        getLoaderManager().initLoader(LOCAL_DATA_LOADER_ID, null, this).forceLoad();
        parser = SpreadsheetXmlParser.getInstance();
    }

    @Override
    public Loader<List<SpreadsheetXmlParser.Entry>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<SpreadsheetXmlParser.Entry>>(getActivity()) {
            @Override
            public List<SpreadsheetXmlParser.Entry> loadInBackground() {

                List<SpreadsheetXmlParser.Entry> entries = null;

                String xml = null;
                try {
                    xml = readFromFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (xml != null)
                    try {
                        entries = parser.parse(xml); //TODO: fix npe here
                    } catch (XmlPullParserException | IOException e) {
                        e.printStackTrace();
                    }

                return entries;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<SpreadsheetXmlParser.Entry>> loader, List<SpreadsheetXmlParser.Entry> data) {
        displayData(data);
        fetchDataAsync();
    }

    @Override
    public void onLoaderReset(Loader<List<SpreadsheetXmlParser.Entry>> loader) {
        entries = null;
    }

    private void displayData(List<SpreadsheetXmlParser.Entry> data) {
        entries = data;
        if (data != null) {
            getRegions();
            spinnerAdapter.notifyDataSetChanged();
            spinner.setSelection(Settings.instance(getActivity()).getLastOrganizationsRegionPosition());

            organizationsAdapter.swapData(entries);
        } else {
            showNoDataMessage();
        }
    }

    private void getRegions() {
        for (SpreadsheetXmlParser.Entry e : entries) {
            if (!regions.contains(e.region))
                regions.add(e.region);
        }
        Log.d(LOG_TAG, "regions " + Arrays.toString(regions.toArray()));
    }

    /**
     * Fetchs yellow pages from server.
     * <p/>
     * On load writes data to file and displays it if nothing has been displayed yet
     */
    private void fetchDataAsync() {
        new AsyncTask<Object, Object, List<SpreadsheetXmlParser.Entry>>() {

            @Override
            protected List<SpreadsheetXmlParser.Entry> doInBackground(Object... params) {
                List<SpreadsheetXmlParser.Entry> entries = null;
                try {
                    String xml = fetchData();
                    if (xml != null)
                        entries = parser.parse(xml);
                } catch (XmlPullParserException | IOException e) {
                    e.printStackTrace();
                }

                return entries;
            }

            @Override
            protected void onPostExecute(List<SpreadsheetXmlParser.Entry> data) {
                super.onPostExecute(data);

                if (YellowPagesFragment.this.entries == null) //activity isn't stopped and no data has been shown yet
                    displayData(data);
            }
        }.execute();
    }

    private void showNoDataMessage() {
        Toast.makeText(getActivity(), R.string.no_data_retry_fetch, Toast.LENGTH_SHORT).show();
    }

    /**
     * Loads data from server and writes it to disk
     *
     * @return String with loaded xml
     */
    private String fetchData() {
        String xml = null;
        try {
            URL url = new URL(YELLOW_PAGES_URL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            xml = inputStreamToString(in);

            writeToFile(xml);

            in.close();
            urlConnection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return xml;
    }

    private void writeToFile(String data) {
        try {
            FileWriter out = new FileWriter(file);
            out.write(data);
            out.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     * read a file and converting it to String using StringBuilder
     */
    public String readFromFile() throws IOException {
        FileInputStream fStream = new FileInputStream(file);
        String text = inputStreamToString(fStream);

        fStream.close();
        return text;
    }


    private String inputStreamToString(InputStream inputStream) throws IOException {
        StringBuilder sbuilder;
        BufferedReader input = null;
        try {
            input = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            sbuilder = new StringBuilder();
            String str = input.readLine();

            while (str != null) {
                sbuilder.append(str);
                str = input.readLine();
                if (str != null) {
                    sbuilder.append("\n");
                }
            }

            return sbuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            inputStream.close();
            if (input != null)
                input.close();
        }
        return null;
    }

}
