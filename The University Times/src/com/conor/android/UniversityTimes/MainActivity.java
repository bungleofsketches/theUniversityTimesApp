package com.conor.android.UniversityTimes;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.conor.android.theUniversityTimes.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    public boolean firstLaunch = true;
    public boolean pauseScroll = true;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    public String[] mCategories;
    public categoryManager manager;
    public FragmentManager fragmentManager;
    public ArrayList<ArrayList<Article>> m_articles = new ArrayList<ArrayList<Article>>();
    public ArrayList<Article> newArticles = new ArrayList<Article>();

    private String TAG = "Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (savedInstanceState == null) {
            super.onCreate(savedInstanceState);
           m_articles.add(new ArrayList<Article>());
        }

        setContentView(R.layout.activity_main);
        firstLaunch = false;

        fragmentManager = getFragmentManager();
        mTitle = mDrawerTitle = getTitle();
        manager = new categoryManager();

        //All the drawer shit
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, mCategories));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        if (savedInstanceState == null) {
            selectItem(0);
        }
        Log.v(TAG, "index End of MainActivity");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_refresh).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    public static boolean isNetworkAvailable(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        Log.v(TAG, "onOptionsItemSelected");

        // Handle action buttons
        switch (item.getItemId()) {
            case R.id.action_refresh:
                if(isNetworkAvailable(getApplication())==true){
                    Toast toast = Toast.makeText(getApplicationContext(), "Loads of internet...", 1);
                    toast.show();
                    //manager.reLoadCategory(getActionBar().getTitle().toString());
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), "No internet..." + "Soz babes xOxOx", 1);
                    toast.show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {

        ListFragment fragment = new MyCategoryFragment();
        Bundle args = new Bundle();
        fragmentManager = getFragmentManager();
        pauseScroll = true;
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, mCategories[position]).commit();


        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mCategories[position]);
        mDrawerLayout.closeDrawer(mDrawerList);

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public class MyCategoryFragment extends ListFragment implements OnScrollListener {
        public ArticleAdapter m_artadapter;
        public ArticleAdapter getAdapter() {
            return this.m_artadapter;
        }
        public MyCategoryFragment() {
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            Log.v(TAG, "onActivityCreated");

            getListView().setOnScrollListener(this);

            super.onActivityCreated(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_category, container, false);
            int index = 0;
            if (firstLaunch == false) {
                index = Arrays.asList(mCategories).indexOf(getActionBar().getTitle());
            }

            manager.loadCategory(getActionBar().getTitle().toString(), 10);
            m_artadapter = new ArticleAdapter(this.getActivity(), R.layout.article_button, m_articles.get(index));
            this.setListAdapter(m_artadapter);
            return rootView;
        }

        public void onScroll(AbsListView arg0, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            Log.v("entered onScroll", " " + firstVisibleItem + visibleItemCount
                    + totalItemCount);
            if (((firstVisibleItem + visibleItemCount) >= totalItemCount - 1)) {
                Log.v("loading more", " " + firstVisibleItem + visibleItemCount
                        + totalItemCount);
                // if we're at the bottom of the listview, load more data
                if (!pauseScroll) {
                    manager.loadMoreCategory(getActionBar().getTitle().toString());
                }
                //addData(getActionBar().getTitle().toString());
            }
        }

        private void addData(String name) {
            manager.loadMoreCategory("");
            m_artadapter.notifyDataSetChanged();

        }

        public void onScrollStateChanged(AbsListView arg0, int arg1) {
            Log.v("entered onScroll", " ");
        }


        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            super.onListItemClick(l, v, position, id);
            Intent intent = new Intent(this.getActivity(), ArticleActivity.class);

            String name = getActionBar().getTitle().toString();
            int index = Arrays.asList(mCategories).indexOf(name);
            Article a = m_articles.get(index).get(position);
            String url = a.getUrl();
            String heading = a.getHeading();
            int code = a.getId();
            intent.putExtra("URL", url);
            intent.putExtra("heading", heading);
            intent.putExtra("article_id", code);
            startActivity(intent);

        }
    }

    public class categoryManager {

        categoryManager() {
            try {
                mCategories = new getCategories().execute().get();
            } catch (ExecutionException e) {
            } catch (InterruptedException e) {
            }

            for (int i = 1; i < mCategories.length; i++) {
                m_articles.add(new ArrayList<Article>());
            }
        }

        void loadCategory(String name, int amount) {
            pauseScroll = true;
            int index = Arrays.asList(mCategories).indexOf(name);
            if (m_articles.get(index).isEmpty()) {
                grabArticlesParams params = new grabArticlesParams(amount, name, 0);
                new grabArticles().execute(params);
                Log.v("", "Called grabArticlesfor cat " + name);
                //return m_articles.get(index);
            } else {
            }
        }


        boolean reLoadCategory(String name) {
            int index = Arrays.asList(mCategories).indexOf(name);
            m_articles.get(index).get(0);
            MyCategoryFragment frag = (MyCategoryFragment) fragmentManager.findFragmentByTag(name);

            //grabArticlesParams params = new grabArticlesParams(10, name, 0, index);
            //new grabArticles().execute(params);
            Log.v("", "Called grabArticlesfor cat " + name);

            Log.v("frag", "Should be updating the category now");
            frag.getAdapter().notifyDataSetChanged();
            return true;
        }

        boolean loadMoreCategory(String name) {
            pauseScroll = true;
            int index = Arrays.asList(mCategories).indexOf(name);
            int last = m_articles.get(index).size() - 1;
            int id = m_articles.get(index).get(last).getId();

            grabArticlesParams params = new grabArticlesParams(5, name, id);
            new grabArticles().execute(params);
            Log.v("", "Called grabMoreArticles for cat " + name);
            //return m_articles.get(index);

            return true;
        }
    }

    public class grabArticles extends AsyncTask<grabArticlesParams, Void, ArrayList<Article>> {
        private InputStream is = null;
        private JSONObject jObj = null;
        private String json = "";
        private String TAG = "grabArticles";
        String name = getActionBar().getTitle().toString();
        int index = Arrays.asList(mCategories).indexOf(name);
        ArrayList<Article> articles = new ArrayList<Article>();


        //public ArrayList<Article> grabArticles(int amount, String category, int before_id){
        public ArrayList<Article> retd = new ArrayList<Article>(0);

        public void setResult(ArrayList<Article> in) {
            retd = in;
        }

        public ArrayList<Article> getResult() {
            return retd;
        }

        public void onPostExecute(ArrayList<Article> result) {
            pauseScroll = false;
            newArticles = result;
            for (int i = 0; i < newArticles.size(); i++) {
                m_articles.get(index).add(newArticles.get(i));
                try {
                    Log.v("loadCategories", newArticles.get(i).getHeading());
                } catch (NullPointerException n) {
                    Log.v("Null pointer", "");
                }
            }
            try{
            MyCategoryFragment frag = (MyCategoryFragment) fragmentManager.findFragmentByTag(name);
            Log.v("frag", "Should be Loading the category now");
            frag.getAdapter().notifyDataSetChanged();
            }catch(NullPointerException n){
                Log.v("Fragment data set changed", "null pointer exception");
            }
        }

        public ArrayList<Article> doInBackground(grabArticlesParams... params) {
            Log.v("", "entered doInBackground");
            int amount = params[0].amount;
            String category = params[0].category;
            int before_id = params[0].before_id;
            Article myArticle = null;
            String base_url = "http://universitytimes.ie/mobile/";
            String url = "";

            Log.v("", "Category chosen is " + category + "id is" + before_id);


            if (amount < 1) {
                System.out.println("article amount passed to backend <1");
                return null;
            }

            //no category, draw from mobile root
            if (category.equals("main") && before_id == 0) {
                Log.v("", "entered main & 0");
                String[] str_valids = new String[0];
                try {
                    String[] str_validsI = null;
                    validParams val = new validParams("main");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) { // Build.VERSION_CODES.HONEYCOMB = 11
                        str_validsI = new getValids().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{"main"}).get();
                    } else {
                        str_validsI = new getValids().execute("main").get();
                    }
                    str_valids = str_validsI;
                } catch (InterruptedException e) {
                } catch (ExecutionException f) {
                }

                List<String> valids = new ArrayList(Arrays.asList(str_valids));
                int idx = valids.size() - 1;

                boolean first = true;
                for (int i = 0; i < amount; i++) {
                    Log.v("", "constructing url for" + valids.get(idx));
                    url = base_url + valids.get(idx);
                    try {
                        Log.v("Downloading Json from", url);
                        JSONObject jObj = getJSONFromUrl(url);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if (jObj == null)
                        Log.v("", "jObj is null ffs");

                    if (jObj != null) {
                        try {
                            CharSequence heading = Html.fromHtml(jObj.getString("Heading"));

                            myArticle = new Article(jObj.getInt("ID"), heading.toString(),
                                    jObj.getString("Body"), jObj.getString("Imageurl"));
                            Log.v("article id: ", String.valueOf(myArticle.getId()));
                        } catch (JSONException e) {
                            Log.e("JSON Parser", "Error parsing data " + e.toString());
                            articles.add(myArticle);
                        }
                    }
                    if (jObj != null) {
                        articles.add(myArticle);
                    }
                    idx--;
                }
                return articles;
            }

            //no category, before_id
            if (category.equals("main")) {
                Log.v("", "entered main alone");
                String[] str_valids = new String[0];
                try {
                    String[] str_validsI = null;
                    validParams val = new validParams("main");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) { // Build.VERSION_CODES.HONEYCOMB = 11
                        str_validsI = new getValids().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{"main"}).get();
                    } else {
                        str_validsI = new getValids().execute("main").get();
                    }
                    str_valids = str_validsI;
                } catch (InterruptedException e) {
                } catch (ExecutionException f) {
                }
                List<String> valids = new ArrayList(Arrays.asList(str_valids));

                Log.v(TAG, "Looking for" + before_id);
                int idx = valids.indexOf(Integer.toString(before_id)) - 1;

                for (int i = 0; i < amount; i++) {
                    url = base_url + valids.get(idx);
                    try {
                        JSONObject jObj = getJSONFromUrl(url);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (jObj != null) {
                        try {
                            CharSequence heading = Html.fromHtml(jObj.getString("Heading"));

                            myArticle = new Article(jObj.getInt("ID"), heading.toString(),
                                    jObj.getString("Body"), jObj.getString("Imageurl"));
                            Log.v("article id: ", String.valueOf(myArticle.getId()));
                        } catch (JSONException e) {
                            Log.e("JSON Parser", "Error parsing data " + e.toString());

                        }
                    }

                    if (jObj != null) {
                        articles.add(myArticle);
                    }

                    idx--;
                }
                return articles;

            }

            //category, no id
            if (before_id == 0) {
                Log.v("", "no category");
                String[] str_valids = new String[0];
                try {
                    String[] str_validsI = null;
                    validParams val = new validParams(category);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) { // Build.VERSION_CODES.HONEYCOMB = 11
                        str_validsI = new getValids().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{category}).get();
                    } else {
                        str_validsI = new getValids().execute(category).get();
                    }
                    str_valids = str_validsI;
                } catch (InterruptedException e) {
                } catch (ExecutionException f) {
                }

                List<String> valids = new ArrayList(Arrays.asList(str_valids));
                int idx = valids.size() - 1;

                Log.v(TAG, "we are looking at position " + idx);

                if(valids.size() < 10)
                    amount = valids.size();

                for (int i = 0; i < amount; i++) {
                    url = base_url + category + "/" + valids.get(idx);
                    try {
                        JSONObject jObj = getJSONFromUrl(url);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (jObj != null) {
                        try {
                            CharSequence heading = Html.fromHtml(jObj.getString("Heading"));

                            myArticle = new Article(jObj.getInt("ID"), heading.toString(),
                                    jObj.getString("Body"), jObj.getString("Imageurl"));
                            Log.v("article id: ", String.valueOf(myArticle.getId()));
                        } catch (JSONException e) {
                            Log.e("JSON Parser", "Error parsing data " + e.toString());

                        }
                        articles.add(myArticle);

                    }
                    idx--;
                }
                return articles;

            }

            //category, before_id
            //List<String> valids = getValids(category);
            String[] str_valids = new String[0];
            Log.v("", "some other shit");
            try {
                str_valids = new getValids().execute(category, null).get();
            } catch (InterruptedException e) {
            } catch (ExecutionException f) {
            }

            List<String> valids = new ArrayList(Arrays.asList(str_valids));

            int idx = valids.indexOf(before_id) - 1;

            for (int i = 0; i < amount; i++) {
                url = base_url + category + "/" + valids.get(idx);
                try {
                    JSONObject jObj = getJSONFromUrl(url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jObj != null) {
                    try {
                        CharSequence heading = Html.fromHtml(jObj.getString("Heading"));

                        myArticle = new Article(jObj.getInt("ID"), heading.toString(),
                                jObj.getString("Body"), jObj.getString("Imageurl"));
                        Log.v("article id: ", String.valueOf(myArticle.getId()));
                    } catch (JSONException e) {
                        Log.e("JSON Parser", "Error parsing data " + e.toString());

                    }
                    Log.i("article id: ", String.valueOf(myArticle.getId()));
                    articles.add(myArticle);

                }
                idx--;
            }
            return articles;
        }

        public JSONObject getJSONFromUrl(String url) throws JSONException {

            // Making HTTP request
            try {
                // default HTTPClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                System.out.println("The status " + httpResponse.getStatusLine().getStatusCode());
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                json = sb.toString();

                jObj = new JSONObject(json);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting Result " + e.toString());
            }
            // return JSON String
            System.out.println(" jOBJ " + jObj);
            return jObj;

        }
    }
}