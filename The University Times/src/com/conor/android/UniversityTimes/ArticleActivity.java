package com.conor.android.UniversityTimes;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ShareActionProvider;

import com.conor.android.theUniversityTimes.R;


public class ArticleActivity extends Activity {
    static LayoutParams lpsmallbutton = new LayoutParams(LayoutParams.MATCH_PARENT, 50);
    private int article_id;
    private static final String TAG = "MyActivity";
    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        article_id = intent.getIntExtra("article_id", 0);

        String fullurl = "http://universitytimes.ie/?p=" + article_id;
        getActionBar().setTitle(intent.getStringExtra("heading"));

        Intent shareintent = new Intent(Intent.ACTION_SEND);
        shareintent.putExtra(Intent.EXTRA_TEXT, fullurl);
        shareintent.setType("text/plain");
        setShareIntent(shareintent);

        setContentView(R.layout.activity_article);
        setupActionBar();

        article_id = intent.getIntExtra("article_id", 0);

        final WebView webView = (WebView) findViewById(R.id.article_page);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url)
            {
                view.loadUrl("javascript:(function() { " +
                        "document.getElementsByTagName('nav')[0].style.display = 'none';"+
                        "})()");

                view.loadUrl("javascript:(function() { " +
                        "document.getElementById('relatedposts').style.display = 'none';"+
                        "})()");

                view.loadUrl("javascript:(function() { " +
                        "document.getElementById('ssba').style.display = 'none';"+
                        "})()");

            }
        });


        webView.loadUrl("http://universitytimes.ie/?p=" + article_id);
    }

    /**
     * Set up the {@link android.app.ActionBar}.
     */
    private void setupActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.article, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) item.getActionProvider();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }
}
