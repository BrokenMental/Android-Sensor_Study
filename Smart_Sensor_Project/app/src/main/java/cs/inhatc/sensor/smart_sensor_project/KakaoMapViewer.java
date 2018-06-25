package cs.inhatc.sensor.smart_sensor_project;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.TextView;

import net.daum.mf.map.api.MapView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.kakao.util.maps.helper.Utility.getPackageInfo;

public class KakaoMapViewer extends AppCompatActivity {

    public final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    public static valueVO vo;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.view_main);

        MapView mapView = new MapView(this);
        mapView.setDaumMapApiKey("ad85475be6af5302b83e3ef713460b83");
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        // Assume thisActivity is the current activity
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        // Here, thisActivity is the current activity
        if (permissionCheck
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        //Log.i("키해시",getKeyHash(getApplicationContext())); // 키해시 얻는 방법
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public static String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return android.util.Base64.encodeToString(md.digest(), android.util.Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
