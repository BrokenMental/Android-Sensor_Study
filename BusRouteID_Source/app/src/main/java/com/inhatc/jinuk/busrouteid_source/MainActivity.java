package com.inhatc.jinuk.busrouteid_source;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView objTV;
    private String strServiceUrl, strServiceKey, strRouteId, strUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        objTV = (TextView) findViewById(R.id.txtTitle);

        strServiceUrl = "http://ws.bus.go.kr/api/rest/busRouteInfo/getRouteInfo";
        strServiceKey = "u9fo42I2R0K%2Fsc4Y81Kag3%2Fba%2BJXZQo%2FO213Nf45OSVJ7Rl8m2Lb9RQTjdVt4EUoBHlUE1NbxofSNHzYwjeIqg%3D%3D";
        strRouteId = "100100063";
        strUrl = strServiceUrl + "?serviceKey=" + strServiceKey + "&busRouteId" + strRouteId;

        new DownloadWebpageTask().execute(strUrl);
    }

    private class DownloadWebpageTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            try{
                return (String)downloadUrl((String)strings[0]);
            }catch(IOException e){
                return "Fail download !";
            }
        }

        protected void onPostExecute(String result) { objTV.setText(result);}

        private String downloadUrl(String myurl) throws IOException{
            HttpURLConnection urlConn = null;
            try{
                URL url = new URL(myurl);
                urlConn = (HttpURLConnection) url.openConnection();
                BufferedInputStream inBuf = new BufferedInputStream(urlConn.getInputStream());
                BufferedReader bufReader = new BufferedReader(new InputStreamReader(inBuf, "utf-8"));

                String strLine = null;
                String strPage = "";
                while ((strLine = bufReader.readLine()) != null){
                    strPage += strLine;
                }

                return strPage;

            }finally {
                urlConn.disconnect();
            }
        }
    }
}
