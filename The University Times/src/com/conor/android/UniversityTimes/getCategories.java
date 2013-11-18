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

public class getCategories extends AsyncTask<Void, Void, String[]> {
    // public String[] getCategories(){
    private InputStream is = null;
    public String[] retd = new String[0];

    public void setResult(String[] in) {
        retd = in;
    }

    public String[] getResult() {
        return retd;
    }

    protected String[] doInBackground(Void... v) {

        String url = "http://universitytimes.ie/mobile/categories";
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
            String str_categories = sb.toString();

            List<String> ret_val = Arrays.asList(str_categories.split("\\s*,\\s*"));
            ArrayList<String> returningStrings = new ArrayList<String>();
            returningStrings.add("main");

            returningStrings.addAll(1, ret_val);
            Log.v("Second category is", returningStrings.get(1));

            return returningStrings.toArray(new String[returningStrings.size()]);


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] a = new String[1];
        return a;

    }

    protected void onProgessUpdate() {
    }

    protected void onPostExecute(String[] result) {
        setResult(result);
    }

}