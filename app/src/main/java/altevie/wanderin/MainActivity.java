package altevie.wanderin;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import com.estimote.indoorsdk.IndoorLocationManagerBuilder;
import com.estimote.indoorsdk_module.algorithm.OnPositionUpdateListener;
import com.estimote.indoorsdk_module.algorithm.ScanningIndoorLocationManager;
import com.estimote.indoorsdk_module.cloud.Location;
import com.estimote.indoorsdk_module.cloud.LocationPosition;
import com.estimote.indoorsdk_module.view.IndoorLocationView;
import com.estimote.internal_plugins_api.cloud.CloudCredentials;

import java.util.ArrayList;
import java.util.HashMap;

import altevie.wanderin.utility.ClsGetJson;
import altevie.wanderin.utility.ClsGraph;
import altevie.wanderin.utility.GlobalObject;
import altevie.wanderin.utility.PathView;
import altevie.wanderin.utility.SyncResult;
import altevie.wanderin.utility.graphDec.AdjacencyListGraph;

import static altevie.wanderin.R.*;

public class MainActivity extends AppCompatActivity {

    protected ArrayList<HashMap<String,String>> listHashMap;
    protected ArrayList<AdjacencyListGraph.DecVertex> listHashMapPoi;
    protected ArrayList<AdjacencyListGraph.DecVertex> listHashMapPon;
    protected AdjacencyListGraph graph;

    private ClsGetJson getJson;
    private ClsGraph CGraph = new ClsGraph();
    private PathView pathView;
    private ListView listView;
    private BaseAdapter mAdapter;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private LocationPosition locPos = new LocationPosition();
    public RequestQueue queue;
    protected Location loc;
    protected IndoorLocationView indoorLocationView;
    protected final SyncResult syncResult = new SyncResult();
    protected Context context;

    ScanningIndoorLocationManager indoorLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(drawable.ic_menu);

        getJson = new ClsGetJson();

        listHashMap = new ArrayList<HashMap<String, String>>();
        listHashMapPoi = new ArrayList<AdjacencyListGraph.DecVertex>();
        listHashMapPon = new ArrayList<AdjacencyListGraph.DecVertex>();
        graph = new AdjacencyListGraph();

        listView = (ListView)findViewById(R.id.POIList);

        queue = Volley.newRequestQueue(this);
        context = this;
        indoorLocationView = (IndoorLocationView) findViewById(R.id.indoor_view);
        pathView = (PathView) findViewById(R.id.pathView);

        GlobalObject g = (GlobalObject)getApplication();
        loc = g.getLocation();

        String[] from = new String[] {"NOME"};
        int[] to = new int[]  {id.textView};
        mAdapter = new SimpleAdapter(this, listHashMap, layout.line_style, from, to);
        listView.setAdapter(mAdapter);

        final Button update = (Button)findViewById(id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listHashMap.clear();
                listHashMapPoi.clear();
                listHashMapPon.clear();
                graph = new AdjacencyListGraph();

                getJson.getJSONFromUrl(context,getString(string.url),getString(string.url_pon),getString(string.url_graph), queue, listHashMap, listHashMapPoi, listHashMapPon, graph, mAdapter, indoorLocationView, update);
            }
        });
        getJson.getJSONFromUrl(context,getString(string.url),getString(string.url_pon),getString(string.url_graph), queue, listHashMap, listHashMapPoi, listHashMapPon, graph, mAdapter, indoorLocationView, update);

        mDrawerLayout = findViewById(id.drawer_layout);
        mActivityTitle = getTitle().toString();
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, string.drawer_open, string.drawer_close){
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            // Called when a drawer has settled in a completely closed state.
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        CloudCredentials cloudCredentials = g.getCloudCredentials();
        indoorLocationView.setLocation(loc);
        indoorLocationManager = new IndoorLocationManagerBuilder(this, loc, cloudCredentials)
                .withDefaultScanner()
                .build();
        indoorLocationManager.setOnPositionUpdateListener(new OnPositionUpdateListener() {
            @Override
            public void onPositionUpdate(LocationPosition locationPosition) {
                indoorLocationView.updatePosition(locationPosition);
                //ricalcolo posizione
                locPos = locationPosition;
            }

            @Override
            public void onPositionOutsideLocation() {
                indoorLocationView.hidePosition();
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Called when user click on item list

                Log.i("",locPos.toString());

                //calculate Dikstra
                //CGraph.calcPath(locPos, listHashMapPon,graph, listHashMapPoi, i, context);
                CGraph.calcPath(locPos, listHashMapPon,graph, listHashMapPoi, i, context, pathView);

                mDrawerLayout.closeDrawers();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        indoorLocationManager.startPositioning();
    }

    @Override
    protected void onStop() {
        super.onStop();
        indoorLocationManager.stopPositioning();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
