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

package ru.lizaalert.hotline.lib.yp.ui;

import android.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import ru.lizaalert.hotline.lib.R;
import ru.lizaalert.hotline.lib.settings.Settings;
import ru.lizaalert.hotline.lib.YM;
import ru.lizaalert.hotline.lib.yp.YellowPagesLoader;
import ru.lizaalert.hotline.lib.yp.YPOrganizationsAdapter;
import ru.lizaalert.hotline.lib.yp.YPEntry;
import ru.lizaalert.hotline.lib.yp.YPRegion;
import ru.lizaalert.hotline.lib.yp.YPRegionSpinnerAdapter;

public class YellowPagesFragment extends Fragment {

    private static final String TAG = "8800";

    private Realm realm;
    private RealmResults<YPRegion> regions;

    private View contentView;
    private Spinner spinner;
    private YPRegionSpinnerAdapter spinnerAdapter;
    private ListView list;
    private YPOrganizationsAdapter organizationsAdapter;

    public View findViewById(int id) {
        if (contentView == null) {
            return null;
        }
        return contentView.findViewById(id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = super.onCreateView(inflater, container, savedInstanceState);
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.fragment_yellow_pages, container, false);
        }
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "YellowPagesFragment - onActivityCreated");


        realm = Realm.getInstance(getActivity());
        regions = realm.where(YPRegion.class).findAll();
        regions.sort("region");

        spinner = (Spinner) findViewById(R.id.spinner);
        spinnerAdapter = new YPRegionSpinnerAdapter(getActivity(), regions, true, android.R.layout.simple_list_item_1, android.R.id.text1);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                YPRegion region = spinnerAdapter.getItem(position);
                Log.d(TAG, "Selected region " + region.getRegion());

                Settings.instance(getActivity()).setYellowPagesRegion(region.getRegion());
                setEntriesAdapter(getYPEntries(region.getRegion()));

                // track event
                YM.reportRegionEvent(region.getRegion());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        list = (ListView) findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView description = ((TextView) view.findViewById(R.id.descriprion));
                if (description.getLineCount() == 4) {
                    description.setMaxLines(Integer.MAX_VALUE);
                } else {
                    description.setMaxLines(4);
                }
                view.invalidate();
            }
        });

        String regionName = Settings.instance(getActivity()).getYellowPagesRegion();
        setEntriesAdapter(getYPEntries(regionName));
        YM.reportRegionEvent(regionName);
    }

    private void setEntriesAdapter (RealmResults<YPEntry> entries) {
        organizationsAdapter = new YPOrganizationsAdapter(getActivity(), entries, true,
                R.layout.list_item_organization,
                R.id.section,
                R.id.organization_name,
                R.id.phones,
                R.id.descriprion
        );
        list.setAdapter(organizationsAdapter);

        organizationsAdapter.notifyDataSetChanged();
    }

    private int findPosition(String what) {

        if (regions.size() == 0) {
            return -1;
        }

        for (int i = 0; i < regions.size(); i++) {
            if (regions.get(i).getRegion().equals(what)) {
                return i;
            }
        }

        return -1;
    }

    private RealmResults<YPEntry> getYPEntries(String region) {
        RealmQuery<YPEntry> query = realm.where(YPEntry.class).equalTo("region.region", region);
        RealmResults<YPEntry> result = query.findAll();
        result.sort("sortstring");
        return result;
    }

    @Override
    public void onPause() {
        super.onPause();

        Settings.instance(getActivity()).setYellowPagesLastListPosition(list.getFirstVisiblePosition());
    }

    @Override
    public void onResume() {
        super.onResume();

        list.setSelection(Settings.instance(getActivity()).getYellowPagesLastListPosition());

        if (regions.size() > 0) {
            String region = Settings.instance(getActivity()).getYellowPagesRegion();
            Log.d(TAG, "try to open region " + region);
            int position = findPosition(region);
            if (position < 0) {
                position = findPosition("Все регионы");
            }
            if (position < 0) {
                position = 0;
            }
            spinner.setSelection(position);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (realm != null) {
            realm.addChangeListener(new RealmChangeListener() {
                @Override
                public void onChange() {
                    Log.d(TAG, "realm dataset changed");
                    setEntriesAdapter(getYPEntries(Settings.instance(getActivity()).getYellowPagesRegion()));
                    organizationsAdapter.notifyDataSetChanged();
                    spinnerAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (realm != null) {
            realm.removeAllChangeListeners();
        }
    }
}
