package com.conor.android.UniversityTimes;

import android.os.AsyncTask;
import android.util.Log;

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

public class grabArticles extends AsyncTask<grabArticlesParams, Void, ArrayList<Article>> {
    private InputStream is = null;
    private JSONObject jObj = null;
    private String json = "";


    //public ArrayList<Article> grabArticles(int amount, String category, int before_id){
    public ArrayList<Article> retd = new ArrayList<Article>(0);

    public void setResult(ArrayList<Article> in) {
        retd = in;
    }

    public ArrayList<Article> getResult() {
        return retd;
    }

    public ArrayList<Article> doInBackground(grabArticlesParams... params) {
        int amount = params[0].amount;
        String category = params[0].category;
        int before_id = params[0].before_id;
        ArrayList<Article> articles = new ArrayList<Article>();
        Article myArticle = null;
        String base_url = "http://universitytimes.ie/mobile/";
        String url = "";


        if (amount < 1) {
            System.out.println("article amount passed to backend <1");
            return null;
        }

        //no category, draw from mobile root
        if (category.equals("main") && before_id == 0) {
            Log.v("", "entered main & 0");
            String[] str_valids = new String[0];
            try {
                str_valids = new getValids().execute().get();
            } catch (InterruptedException e) {
            } catch (ExecutionException f) {
            }

            Log.v("", "entered main & 0 part II");

            List<String> valids = new ArrayList(Arrays.asList(str_valids));
            int idx = valids.size() - 1;

            Log.v("", "entered main & 0 part III");

            for (int i = 0; i < amount; i++) {
                url = base_url + valids.get(idx);
                try {
                    JSONObject jObj = getJSONFromUrl(url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jObj != null) {
                    try {
                        myArticle = new Article(jObj.getInt("ID"), jObj.getString("Post_title"),
                                jObj.getString("Post_body"), jObj.getString("Thumb_url"));
                        Log.v("article id: ", String.valueOf(myArticle.getId()));
                    } catch (JSONException e) {
                        Log.e("JSON Parser", "Error parsing data " + e.toString());
                    }
                }
                articles.add(myArticle);
                idx--;
            }
            return articles;
        }

        //no category, before_id
        if (category.equals("main")) {
            Log.v("", "entered main alone");
            String[] str_valids = new String[0];
            try {
                str_valids = new getValids().execute().get();
            } catch (InterruptedException e) {
            } catch (ExecutionException f) {
            }

            List<String> valids = new ArrayList(Arrays.asList(str_valids));
            int idx = valids.indexOf(before_id) - 1;

            for (int i = 0; i < amount; i++) {
                url = base_url + valids.get(idx);
                try {
                    JSONObject jObj = getJSONFromUrl(url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jObj != null) {
                    try {
                        myArticle = new Article(jObj.getInt("ID"), jObj.getString("Post_title"), jObj.getString("Post_body"), jObj.getString("Thumb_url"));
                        Log.i("article id: ", String.valueOf(myArticle.getId()));
                    } catch (JSONException e) {
                        Log.e("JSON Parser", "Error parsing data " + e.toString());
                    }

                }
                articles.add(myArticle);
                idx--;
            }
            return articles;

        }

        //category, no id
        if (before_id == 0) {
            Log.v("", "no category");
            String[] str_valids = new String[0];
            try {
                str_valids = new getValids().execute().get();
            } catch (InterruptedException e) {
            } catch (ExecutionException f) {
            }

            List<String> valids = new ArrayList(Arrays.asList(str_valids));
            int idx = valids.size() - 1;

            for (int i = 0; i < amount; i++) {
                url = base_url + category + valids.get(idx);
                try {
                    JSONObject jObj = getJSONFromUrl(url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jObj != null) {
                    try {
                        myArticle = new Article(jObj.getInt("ID"), jObj.getString("Post_title"), jObj.getString("Post_body"), jObj.getString("Thumb_url"));
                        Log.i("article id: ", String.valueOf(myArticle.getId()));
                    } catch (JSONException e) {
                        Log.e("JSON Parser", "Error parsing data " + e.toString());
                    }
                    Log.i("article id: ", String.valueOf(myArticle.getId()));
                }
                articles.add(myArticle);
                idx--;
            }
            return articles;

        }

        //category, before_id
        //List<String> valids = getValids(category);
        String[] str_valids = new String[0];
        try {
            str_valids = new getValids().execute().get();
        } catch (InterruptedException e) {
        } catch (ExecutionException f) {
        }

        List<String> valids = new ArrayList(Arrays.asList(str_valids));

        int idx = valids.indexOf(before_id) - 1;

        for (int i = 0; i < amount; i++) {
            url = base_url + category + valids.get(idx);
            try {
                JSONObject jObj = getJSONFromUrl(url);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (jObj != null) {
                try {
                    myArticle = new Article(jObj.getInt("ID"), jObj.getString("Post_title"), jObj.getString("Post_body"), jObj.getString("Thumb_url"));
                    Log.i("article id: ", String.valueOf(myArticle.getId()));
                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }
                Log.i("article id: ", String.valueOf(myArticle.getId()));
            }
            articles.add(myArticle);
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
