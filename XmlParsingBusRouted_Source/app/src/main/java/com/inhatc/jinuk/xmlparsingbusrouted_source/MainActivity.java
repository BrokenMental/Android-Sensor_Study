package com.inhatc.jinuk.xmlparsingbusrouted_source;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView objTV;
    private String strServiceUrl, strServiceKey, strbusRouteId, strUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        objTV = (TextView) findViewById(R.id.txtTitle);

        strServiceUrl = "http://ws.bus.go.kr/api/rest/busRouteInfo/getRouteInfo";
        strServiceKey = "u9fo42I2R0K%2Fsc4Y81Kag3%2Fba%2BJXZQo%2FO213Nf45OSVJ7Rl8m2Lb9RQTjdVt4EUoBHlUE1NbxofSNHzYwjeIqg%3D%3D";
        strbusRouteId = "100100063";

        strUrl = strServiceUrl + "?serviceKey=" + strServiceKey + "&busRouteId" + strbusRouteId;

        DownloadWebpageTask1 objTask1 = new DownloadWebpageTask1();
        objTask1.execute(strUrl);
    }

    private class DownloadWebpageTask1 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try{
                return downloadUrl(strings[0]);
            }catch(IOException e){
                return "Fail download !";
            }
        }

        protected void onPostExecute(String result) {
            String strHeaderCd = "";
            String strBusRouteId = "";
            String strBusRouteNo = "";

            boolean bSet_HeaderCd = false;
            boolean bSet_BusRouteId = false;
            boolean bSet_BusRouteNo = false;

            objTV.append("=====[ Route ID ]=====\n");

            try{
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(new StringReader(result));
                int eventType = xpp.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT){
                    if(eventType == XmlPullParser.START_DOCUMENT){

                    }else if(eventType == XmlPullParser.START_TAG){
                        String tag_name = xpp.getName();
                        if(tag_name.equals("headerCd")) bSet_HeaderCd = true;
                        if(tag_name.equals("busRouteId")) bSet_BusRouteId = true;
                        if(tag_name.equals("busRouteNo")) bSet_BusRouteNo = true;
                    }else if(eventType == XmlPullParser.TEXT){
                        if(bSet_HeaderCd){
                            strHeaderCd = xpp.getText();
                            objTV.append("headerCd: " + strHeaderCd +  "\n");
                            bSet_HeaderCd = false;
                        }
                        if(strHeaderCd.equals("0")){
                            if(bSet_BusRouteId){
                                strBusRouteId = xpp.getText();
                                objTV.append("busRouteId" + strBusRouteId + "\n");
                                bSet_BusRouteId = false;
                            }
                            if(bSet_BusRouteNo){
                                strBusRouteNo = xpp.getText();
                                objTV.append("busRouteNo" + strBusRouteNo + "\n");
                                bSet_BusRouteNo = false;
                            }
                        }
                    }else if(eventType == XmlPullParser.END_TAG){

                    }
                    eventType = xpp.next();
                }
            }catch (Exception e){
                objTV.setText(e.getMessage());
            }

            strServiceUrl = "http://ws.bus.go.kr/api/rest/buspos/getBusPosByRtid";
            strUrl = strServiceUrl + "?serviceKey=" + strServiceKey + "&busRouteId" + strbusRouteId;

            DownloadWebpageTask2 objTask2 = new DownloadWebpageTask2();
            objTask2.execute(strUrl);
        }
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
    private class DownloadWebpageTask2 extends DownloadWebpageTask1 {
        protected void onPostExcute(String result){
            String strHeaderCd = "";
            String strGpsX = "";
            String strGpsY = "";
            String strPlainNo = "";

            boolean bSet_HeaderCd = false;
            boolean bSet_GpsX = false;
            boolean bSet_GpsY = false;
            boolean bSet_PlainNo = false;

            objTV.append("=====[ Bus Position]=====\n");

            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(new StringReader(result));
                int eventType = xpp.getEventType();

                int nCount = 0;

                while (eventType != XmlPullParser.END_DOCUMENT){
                    if(eventType == XmlPullParser.START_DOCUMENT){

                    }else if(eventType == XmlPullParser.START_TAG){
                        String tag_name = xpp.getName();
                        if (tag_name.equals("headerCd")) bSet_HeaderCd = true;
                        if (tag_name.equals("gpsX")) bSet_GpsX = true;
                        if (tag_name.equals("gpsY")) bSet_GpsY = true;
                        if (tag_name.equals("plainNo")) bSet_PlainNo = true;
                    } else if(eventType == XmlPullParser.TEXT){
                        if(bSet_HeaderCd){
                            strHeaderCd = xpp.getText();
                            bSet_HeaderCd = false;
                        }
                        if(strHeaderCd.equals("0")){
                            if(bSet_GpsX){
                                nCount++;
                                strGpsX = xpp.getText();
                                objTV.append("["+nCount+"]"+"gpsX: "+strGpsX + "\n");
                                bSet_GpsX = false;
                            }
                            if(bSet_GpsY){
                                strGpsY = xpp.getText();
                                objTV.append("["+nCount+"]"+"gpsY: "+strGpsY + "\n");
                                bSet_GpsY = false;
                            }
                            if(bSet_PlainNo){
                                strPlainNo = xpp.getText();
                                objTV.append("["+nCount+"]"+"plainNo: "+strPlainNo + "\n");
                                bSet_PlainNo = false;
                            }
                        }
                    }else if(eventType == XmlPullParser.END_TAG){

                    }
                    eventType = xpp.next();
                }
            }catch(Exception e){
                objTV.setText(e.getMessage());
            }
        }
    }
}
