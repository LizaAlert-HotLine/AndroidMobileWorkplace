package ru.lizaalert.hotline.lib.yp.ui;

import android.app.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmQuery;

import io.realm.RealmResults;
import ru.lizaalert.hotline.lib.R;
import ru.lizaalert.hotline.lib.settings.Settings;
import ru.lizaalert.hotline.lib.yp.YPEntry;
import ru.lizaalert.hotline.lib.yp.YPOrganizationsAdapter;
import ru.lizaalert.hotline.lib.yp.YellowPagesLoader;

public class SearchActivity extends Activity {
    public static final String TAG = "8800";

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        listView = (ListView)findViewById(R.id.list);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            doMySearch(query);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    private void doMySearch(String query) {
        Log.d(TAG, "query " + query);
//        ((TextView)findViewById(R.id.te)).setText("* " + query + " *");

        String region = Settings.instance(this).getYellowPagesRegion();

        Realm realm = Realm.getInstance(this);

        RealmQuery<YPEntry> dbquery = realm.where(YPEntry.class).equalTo("region.region", region);
        dbquery
                .beginGroup()
                .contains("searchstring", YellowPagesLoader.string4search(query), false)
                .endGroup();

        RealmResults<YPEntry> entries = dbquery.findAll();

        listView = (ListView) findViewById(ru.lizaalert.hotline.lib.R.id.list);
        YPOrganizationsAdapter organizationsAdapter = new YPOrganizationsAdapter(this, entries, true,
                R.layout.list_item_organization,
                R.id.organization_name,
                R.id.phones,
                R.id.descriprion
        );

        listView.setAdapter(organizationsAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.yp_search_menu, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();

        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        return true;
    }


}
