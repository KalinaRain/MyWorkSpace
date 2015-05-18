package com.work.myworkspace;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.location.BDNotifyListener;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends Activity {

    private String TAG = MainActivity.class.getSimpleName();
    private TextView textView;
    private double latitude=0.0;
    private double longitude =0.0;
    private LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.test);

//        BDNotifyListener
        //
        openGPSSettings();

    }

    private void openGPSSettings() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "GPS模块正常", Toast.LENGTH_SHORT).show();
            doWork();
            Log.d("aaaaaaaaaa", "经度：" + longitude + " 纬度: " + latitude);
            return;
        } else {
            Toast.makeText(this, "请开启GPS！", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 传入待请求的百度地图的url--lastMapApi
     * 最后把获取到的城市名称赋值给全局变量 cityName
     */
    /*private void doVolleyGetCityJson(String tlastMapApi) {

        RequestQueue mrequestQueue = Volley.newRequestQueue(MainActivity.this);
        locationString = latitude + "," + longitude + "&key=" + MAP_APP_KEY;
        lastMapApi = tlastMapApi + locationString;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, lastMapApi, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d(TAG, "get请求结果:" + s);
                Log.d(TAG, "lastMapApi" + lastMapApi);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject resultObject = jsonObject.getJSONObject("result");
                    JSONObject addressComponentObject = resultObject
                            .getJSONObject("addressComponent");
                    String city = addressComponentObject.getString("city");
                    cityName = addressComponentObject.getString("city");
                    String district = addressComponentObject.getString("district");

                    city = "城市：" + city;
                    district = " 区：" + district;
                    Log.d(TAG, city + district);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, "lastMapApi" + lastMapApi);
                Log.d(TAG, "get请求结果:" + volleyError.toString());
            }
        });

        mrequestQueue.add(stringRequest);//将StringRequest添加到RequestQueue
//        return city_name;
    }*/
    /**
     * 获取用户的位置信息
     * 返回带有经度和纬度的API地址
     */
    private String getLocation() {
        String cityAPI = null;


        // 获取系统LocationManager服务
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();//获取经度
                latitude = location.getLatitude();//获取纬度
                Log.d(TAG, "经度：" + longitude + " 纬度: " + latitude);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("Latitude", status + "");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d("Latitude", "disable");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d("Latitude", "disable");
            }
        });
        // 如果GPS可用，就获取相应信息
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

//            if(providers.contains(LocationManager.NETWORK_PROVIDER)){
//                //如果是Network
//                locationProvider = LocationManager.NETWORK_PROVIDER;
//            }else if(providers.contains(LocationManager.GPS_PROVIDER)){
//                //如果是GPS
//                locationProvider = LocationManager.GPS_PROVIDER;
//            }else{
//                Toast.makeText(this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
//                return;
//            }

            Log.d(TAG, "GPS已经打开，可以正常使用");

            Criteria criteria = new Criteria();
            // 获得最好的定位效果
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(false);
            // 使用省电模式
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            // 获得当前的位置提供者
            String provider = locationManager.getBestProvider(criteria, true);

            //从GPS获取最近的定位信息
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location != null) {
                // 纬度
                latitude = location.getLatitude();
                // 经度
                longitude = location.getLongitude();
                Log.d(TAG, "即将显示经度和纬度");
                Log.d(TAG, "经度：" + longitude + " 纬度: " + latitude);
            } else {
                Log.d(TAG, "location为空");
            }
        } else {//请求打开GPS
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("您还没有开启GPS^_^").setMessage("确认开启吗？ ").setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //跳转至GPS设置界面
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, 0); //设置完成后返回到获取界面（第二个参数为请求码）
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();//显示对话框


        }


        return cityAPI;
    }
    private void doWork() {

        String msg = "";

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

//        Criteria criteria = new Criteria();
//        // 获得最好的定位效果
//        criteria.setAccuracy(Criteria.ACCURACY_FINE);
//        criteria.setAltitudeRequired(false);
//        criteria.setBearingRequired(false);
//        criteria.setCostAllowed(false);//免费
//        // 使用省电模式
//        criteria.setPowerRequirement(Criteria.POWER_LOW);
//        // 获得当前的位置提供者
//        String provider = locationManager.getBestProvider(criteria, true);
//        // 获得当前的位置
//        Location location = locationManager.getLastKnownLocation(provider);
//
//        latitude = location.getLatitude();
//        longitude = location.getLongitude();

        Log.d("aaaaaaaa", "即将显示经度和纬度");
//        Log.d("aaaaaaaaaa", "经度：" + longitude + " 纬度: " + latitude);
//        locationString = "&location=" + latitude + "," + longitude;
//        keyString = "&key=您的key";
//        questURL = questURL + locationString + keyString;
//
//        new ReadJSONFeedTask().execute(questURL);


        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null){
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }else{
            LocationListener locationListener = new LocationListener() {

                // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                // Provider被enable时触发此函数，比如GPS被打开
                @Override
                public void onProviderEnabled(String provider) {

                }

                // Provider被disable时触发此函数，比如GPS被关闭
                @Override
                public void onProviderDisabled(String provider) {

                }

                //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
                @Override
                public void onLocationChanged(Location location) {
                    if (location != null) {
                        Log.e("Map", "Location changed : Lat: "
                                + location.getLatitude() + " Lng: "
                                + location.getLongitude());
                    }
                }
            };
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000, 0,locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location != null){
                latitude = location.getLatitude(); //经度
                longitude = location.getLongitude(); //纬度
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
