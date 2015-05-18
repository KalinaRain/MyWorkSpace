package com.work.pm25;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity {

    static final String TAG = MainActivity.class.getSimpleName();

    private ImageView img_city_chosen;//手动选择城市
    private TextView tv_city_name;//相应城市的名称

    private boolean isGPSOpen = false;


    /**
     * ************* baidu map ******************************
     */
    private LocationClient mLocationClient;
    private MyLocationListener mLocationListener;
    private String MAP_APP_KEY = "GrY3SWPzhy2akgS4USNTOin1";//我的百度地图的Key
    private String baseMapApi = "http://api.map.baidu.com/geocoder?output=json&location=";
    private String lastMapApi = "";
    private String locationString = "";//url后面接的参数
    private double latitude;//纬度
    private double longitude;//经度
    private String cityName;//在主页上显示的城市名称
    private boolean hasCityChanged = false;//默认的城市名称是否已经改变
    //最终示例
    //http://api.map.baidu.com/geocoder?output=json&location=39.983228,116.491146&key=GrY3SWPzhy2akgS4USNTOin1

    /**
     * ***************** detect the pm2.5 ***********************************
     */
    private String base_PM_URL = "http://www.pm25.in/api/querys/pm2_5.json";//Get  token=5j1znBVAsnSf5xQyNQyq
    private String last_PM_URL = "";
    private String citySubmit = null;//发送给PM检测网站的城市名称
    private int position_num = 0;//每个城市有多少个观测站点
    private ArrayList<HashMap<String, String>> lists = new ArrayList<HashMap<String, String>>();
    private GridView gridView;
    private MyBaseAdapter mBaseAdapter;
    private RelativeLayout relative_main_detail;
    private TextView tv_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initBDLocation();

//        if()
//        autoGetCity();

        //由于location需要时间，cityName也许呀时间来获取，虽然很短，但是比程序执行的时间慢
        //所以得间隔一秒
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (cityName.endsWith("市") || cityName.endsWith("县")) {
                    citySubmit = cityName.substring(0, cityName.length() - 1);
                    Log.d(TAG, "citySubmit " + citySubmit);
                }
//                doVolleyGetPM(encodeCity(citySubmit));
//                doVolleyGetPM(citySubmit);

                doVolleyGetPM("zhuhai");
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask,1000);



//        doVolleyGet("zhuhai");
        //手动选择城市
        img_city_chosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CityChosenActivity.class);
                startActivityForResult(intent, 2);//请求码为2
            }
        });
    }

    private void initView() {
        img_city_chosen = (ImageView) findViewById(R.id.city_chosen);
        tv_city_name = (TextView) findViewById(R.id.city_name);
        relative_main_detail = (RelativeLayout) findViewById(R.id.main_detail);
        tv_error = (TextView) findViewById(R.id.tv_nodata);//没有监测数据
        gridView = (GridView) findViewById(R.id.grid_detail);
    }

    /**
     * 获取用户所在城市的名称
     * 1. 优先使用GPS定位
     * 2. 如果用户硬是不开GPS，则使用IP定位的方法
     */
    private String autoGetCity() {


        String city_name = null;


        return city_name;
    }

    /**
     * 判断网络连接是否打开，且网络是否可用
     * @return
     */
    private boolean isNetConnected() {
        return false;
    }





    /**
     * @param city 传入的城市名称
     * @return 返回经过UTF-8编码后的城市名称
     */
    private String encodeCity(String city) {
        try {
            URLEncoder.encode(city, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return city;
    }

    /**
     * 在更新了用户的位置之后，更改首页显示的城市
     */
    private void changeUserCity() {
        tv_city_name.setTextColor(Color.WHITE);
        tv_city_name.setText(cityName);
    }


    /**
     * 使用百度sdk开始定位
     */
    private void initBDLocation() {
        Log.v(TAG, "start to initLocation");
        //初始化LocationClient类,LocationClient类必须在主线程中声明
        mLocationClient = new LocationClient(MainActivity.this.getApplicationContext());
        mLocationListener = new MyLocationListener();
        // 注册监听函数,当没有注册监听函数时，无法发起网络请求。
        mLocationClient.registerLocationListener(mLocationListener);
        //配置定位SDK
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式 1 2 3
//        option.setOpenGps(true);
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02
        option.setScanSpan(5000);// 设置发起定位请求的间隔时间为3000ms
        option.setIsNeedAddress(true);//返回的定位结果包含地址信息
        mLocationClient.setLocOption(option);

        mLocationClient.start();

        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.requestLocation();
        } else {
            Log.d("LocSDK5", "locClient is null or not started");
        }


    }


    /**
     *
     */
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            Log.v("LocationActivity", "MyLocationListener-onReceiveLocation");
            if (location == null) {
                Log.d(TAG, "location is null");
                return;
            }

            cityName = location.getCity();//获取城市名称
            Log.d(TAG, "city " + cityName);
            if (!hasCityChanged) { // 如果没有更改显示的城市的话，则更改
                changeUserCity();//更改显示的城市名称
                hasCityChanged = true;
            }
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            latitude = location.getLatitude();//设置纬度
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            longitude = location.getLongitude();//设置经度
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
            }

            locationString = "&location=" + location.getLatitude() + ","
                    + location.getLongitude() + "&key=GrY3SWPzhy2akgS4USNTOin1";
//            lastMapApi = baseMapApi + locationString;


        }
    }

    /**
     * 如果该地区有 PM 2.5的检测数据，则
     * 在获取到了PM2.5的检测数据后，开始显示
     */
    private void startDisplayDetail(boolean hasData) {
        if (hasData) {
            tv_error.setVisibility(View.INVISIBLE);

            gridView.setAdapter(new MyBaseAdapter(MainActivity.this));
//gridView.
        } else {
            gridView.setVisibility(View.INVISIBLE);
            tv_error.setText("该城市还未有PM2.5数据");
        }

    }

    class MyBaseAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public MyBaseAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public Object getItem(int position) {
            return lists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = mInflater.inflate(R.layout.griditem, null);
            TextView tv_home_district = (TextView) convertView.findViewById(R.id.home_district);
            TextView tv_home_pollution_rating = (TextView) convertView.findViewById(R.id.home_pollution_rating);
            TextView tv_home_level_num = (TextView) convertView.findViewById(R.id.home_level_num);

            tv_home_district.setText(lists.get(position).get("position_name"));
            tv_home_pollution_rating.setText(lists.get(position).get("quality"));
            tv_home_level_num.setText(lists.get(position).get("pm2_5"));
            return convertView;
        }
    }

    /**
     * PM2.5
     * 根据传入的城市信息返回json数据
     *
     * @return
     */
    private String doVolleyGetPM(String mcity) {
        String result = null;
        RequestQueue mrequestQueue = Volley.newRequestQueue(MainActivity.this);
        last_PM_URL = base_PM_URL + "?city=" + mcity + "&token=" + "5j1znBVAsnSf5xQyNQyq";
        Log.d(TAG, last_PM_URL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, last_PM_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d(TAG, "PM2.5检测的结果为:" + s);
                if (s.startsWith("{")) {//说明返回的是一个对象，也就是说这个地方没有监测数据
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        jsonObject.getString("error");
                        startDisplayDetail(false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.d(TAG+"a", last_PM_URL);
                    try {
                        JSONArray jsonArray = new JSONArray(s);
                        position_num = jsonArray.length();
                        Log.d(TAG, "观测站点个数为:" + position_num);

                        HashMap<String, String> map = null;
                        for (int i = 0; i < position_num; i++) {
                            map= new HashMap<>();
                            map.put("position_name", jsonArray.getJSONObject(i).getString("position_name"));
                            map.put("quality", jsonArray.getJSONObject(i).getString("quality"));
                            map.put("pm2_5", jsonArray.getJSONObject(i).getString("pm2_5"));
                            lists.add(map);
                        }
                        startDisplayDetail(true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, "错误的请求码:" + volleyError.toString());
            }
        });

        mrequestQueue.add(stringRequest);//将StringRequest添加到RequestQueue
        return result;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 根据上面发送过去的请求吗来区别
        switch (requestCode) {
            case 0://设置完GPS后:
                autoGetCity();//再次获取城市信息
                break;
            case 2://手动选择城市之后，返回相应城市名称:  关闭自动获取城市
//                data.getStringExtra("")

                break;
            default:
                break;
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

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
