package com.conor.android.UniversityTimes;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class getValids extends AsyncTask<String, Void, String[]> {
    public List<String> retd = new ArrayList<String>();
    private InputStream is = null;

    //public List<String> getValids(String cat)
    protected String[] doInBackground(String... catarr) {
        String cat = catarr[0];
        String base_url = "http://universitytimes.ie/mobile/";
        String url = null;
        List<String> list = Arrays.asList("aaaa".split("\\s*"));
        Log.v("We are getting a HTTP Response", "2");
        if (cat.equals("main")) {
            Log.v("We think its main", "http main");
            url = base_url + "valids";
        } else {
            Log.v("We think its 5", "" + cat);
            url = base_url + cat + "/" + "valids";
        }

        Log.v("We are getting a HTTP Response", "2 and we think the url is" + url);

        try {
            // default HTTPClient
            Log.v("HTTPZZ", "before http");

            DefaultHttpClient httpClient = new DefaultHttpClient();
            Log.v("HTTPZZ", "before http 1");

            HttpGet httpGet = new HttpGet(url);
            Log.v("HTTPZZ", "before http 2");

            HttpResponse httpResponse = httpClient.execute(httpGet);

            Log.v("We are getting a HTTP Response", "The status " + httpResponse.getStatusLine().getStatusCode());
            Log.v("HTTPZZ", "after http");
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = "";

            Log.v("We are getting a HTTP Response", "The status " + httpResponse.getStatusLine().getStatusCode());
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            String str_valids = sb.toString();

            list = Arrays.asList(str_valids.split("\\s*,\\s*"));


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list.toArray(new String[list.size()]);

    } //end do in bg

    public void setResult(List<String> in) {
        Log.v("We are getting a HTTP Response", "1");
        retd = in;
    }

    public List<String> getResult() {
        return retd;
    }

    protected void onPreExecute() {
        Log.v("", "We're in pre execute");
    }

    protected void onProgessUpdate() {
    }

    protected void onPostExecute(List<String> result) {
        setResult(result);
    }
}
