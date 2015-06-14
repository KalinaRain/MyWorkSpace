package com.work.pm25;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bean.City;
import bean.County;
import bean.Province;

/**
 * Created by KalinaRain on 2015/5/3.
 */
public class CityChosenActivity extends Activity {

    //01|北京,02|上海,03|天津,04|重庆,05|黑龙江,06|吉林,07|辽宁,08|内蒙古,09|河北,10|山西,
    // 11|陕西,12|山东,13|新疆,14|西藏,15|青海,16|甘肃,17|宁夏,18|河南,19|江苏,20|湖北,21|浙江,
    // 22|安徽,23|福建,24|江西,25|湖南,26|贵州,27|四川,28|广东,29|云南,30|广西,31|海南,32|香港,33|澳门,34|台湾
    String[] province = new String[]{
            "北京","上海","天津","重庆","黑龙江","吉林","辽宁","内蒙古","河北","山西",
            "陕西","山东","新疆","西藏","青海","甘肃","宁夏","河南","江苏","湖北","浙江",
            "安徽","福建","江西","湖南","贵州","四川","广东","云南","广西","海南","香港","澳门","台湾"
    };

    String[][] cities = new String[][]{
            new String[]{"北京"}, new String[]{"上海"},
            new String[]{"天津"}, new String[]{"重庆"},
            // 黑龙江
            //0501|哈尔滨,0502|齐齐哈尔,0503|牡丹江,0504|佳木斯,0505|绥化,0506|黑河,0507|大兴安岭,
            //0508|伊春,0509|大庆,0510|七台河,0511|鸡西,0512|鹤岗,0513|双鸭山
            new String[]{"哈尔滨","齐齐哈尔","牡丹江","佳木斯","绥化","黑河","大兴安岭",
                    "伊春","大庆","七台河","鸡西","鹤岗","双鸭山"},
            // 吉林省
            //0601|长春,0602|吉林,0603|延边,0604|四平,0605|通化,0606|白城,0607|辽源,0608|松原,0609|白山
            new String[]{"长春","吉林","延边","四平","通化","白城","辽源","松原","白山",},
            // 辽宁
            //0701|沈阳,0702|大连,0703|鞍山,0704|抚顺,0705|本溪,0706|丹东,0707|锦州,
            // 0708|营口,0709|阜新,0710|辽阳,0711|铁岭,0712|朝阳,0713|盘锦,0714|葫芦岛
            new String[]{"沈阳", "大连", "鞍山", "抚顺", "本溪", "丹东","锦州",
                    "营口", "阜新", "辽阳", "盘锦", "铁岭", "朝阳", "葫芦岛"},
            // 内蒙古

            new String[]{"呼和浩特", "包头", "乌海", "赤峰", "通辽", "鄂尔多斯",
                    "呼伦贝尔", "巴彦淖尔", "乌兰察布", "兴安盟", "锡林郭勒", "阿拉善盟"},
            // 河北
            //0901|石家庄,0902|保定,0903|张家口,0904|承德,0905|唐山,0906|廊坊,
            // 0907|沧州,0908|衡水,0909|邢台,0910|邯郸,0911|秦皇岛
            new String[]{"承德", "张家口", "保定", "石家庄", "邢台", "邯郸",
                    "衡水", "沧州", "廊坊", "唐山", "秦皇岛"},
            // 山西省
            new String[]{"太原", "大同", "阳泉", "长治", "晋城", "朔州",
                    "晋中", "运城", "忻州", "临汾", "吕梁"},

            // 陕西省
            new String[]{"西安", "铜川", "宝鸡", "咸阳", "渭南", "延安",
                    "汉中", "榆林", "安康", "商洛"},
            // 山东省
            new String[]{"济南", "青岛", "淄博", "枣庄", "东营", "烟台",
                    "潍坊", "济宁", "泰安", "威海", "日照", "莱芜", "临沂", "德州",
                    "聊城", "滨州", "菏泽"},
            // 新疆省
            new String[]{"乌鲁木齐", "克拉玛依", "吐鲁番", "哈密", "昌吉",
                    "博尔塔拉", "库尔勒", "阿克苏", "阿图什", "喀什", "和田", "伊犁",
                    "塔城", "阿勒泰", "自治区直辖县级行政单位"},
            // 西藏
            new String[]{"拉萨", "昌都", "山南", "日喀则", "那曲", "阿里",
                    "林芝"},
            // 青海省
            new String[]{"广州", "韶关", "深圳", "珠海", "汕头", "佛山",
                    "江门", "湛江", "茂名", "肇庆", "惠州", "梅州", "汕尾", "河源",
                    "阳江", "清远", "东莞", "中山", "潮州", "揭阳", "云浮"},
            // 甘肃省
            new String[]{"兰州", "嘉峪关", "金昌", "白银", "天水", "武威",
                    "张掖", "平凉", "酒泉", "庆阳", "定西", "陇南", "临夏", "甘南"},
            // 宁夏
            new String[]{"银川", "石嘴山", "吴忠", "固原", "中卫"},
            // 河南
            new String[]{"郑州", "洛阳", "商丘", "安阳", "开封", "平顶山",
                    "焦作", "新乡", "鹤壁", "濮阳", "许昌", "漯河", "三门峡",
                    "信阳", "周口", "驻马店", "济源"},
            //19江苏省
            new String[]{"南京", "无锡", "徐州", "常州", "苏州", "南通",
                    "连云港", "淮安", "盐城", "扬州", "镇江", "泰州", "宿迁"},
            //20湖北省
            //2001|武汉,2002|襄阳,2003|鄂州,2004|孝感,2005|黄冈,2006|黄石,2007|咸宁,2008|荆州,2009|宜昌,
            // 2010|恩施,2011|十堰,2012|神农架,2013|随州,2014|荆门,2015|天门,2016|仙桃,2017|潜江
            new String[]{"武汉", "黄石", "十堰", "宜昌", "襄樊", "鄂州",
                    "荆门", "孝感", "荆州", "黄冈", "咸宁", "随州", "恩施",
                    "省直辖县级行政单位"},
            // 浙江省
            new String[]{"杭州", "宁波", "温州", "嘉兴", "湖州", "绍兴",
                    "金华", "衢州", "舟山", "台州", "丽水"},
            // 安徽省
            new String[]{"合肥", "芜湖", "蚌埠", "淮南", "马鞍山", "淮北",
                    "铜陵", "安庆", "黄山", "滁州", "阜阳", "宿州", "巢湖", "六安",
                    "亳州", "池州", "宣城"},
            // 福建省
            new String[]{"福州", "厦门", "莆田", "三明", "泉州", "漳州",
                    "南平", "龙岩", "宁德"},
            // 江西省
            new String[]{"南昌", "景德镇", "萍乡", "九江", "新余", "鹰潭",
                    "赣州", "吉安", "宜春", "抚州", "上饶"},
            // 湖南省
            new String[]{"长沙", "株洲", "湘潭", "衡阳", "邵阳", "岳阳",
                    "常德", "张家界", "益阳", "郴州", "永州", "怀化", "娄底", "湘西"},
            // 贵州省
            new String[]{"贵阳", "六盘水", "遵义", "安顺", "铜仁", "兴义",
                    "毕节", "凯里", "都匀"},
            // 四川
            new String[]{"成都", "自贡", "攀枝花", "泸州", "德阳", "绵阳",
                    "广元", "遂宁", "内江", "乐山", "南充", "眉山", "宜宾", "广安",
                    "达州", "雅安", "巴中", "资阳", "阿坝", "甘孜", "凉山"},
            // 广东省
            new String[]{"广州", "韶关", "深圳", "珠海", "汕头", "佛山",
                    "江门", "湛江", "茂名", "肇庆", "惠州", "梅州", "汕尾", "河源",
                    "阳江", "清远", "东莞", "中山", "潮州", "揭阳", "云浮"},
            // 云南
            new String[]{"昆明", "曲靖", "玉溪"},
            // 广西省
            new String[]{"南宁", "柳州", "桂林", "梧州", "北海", "防城港",
                    "钦州", "贵港", "玉林", "百色", "贺州", "河池", "来宾", "崇左"},
            // 海南
            new String[]{"海口", "三亚"},
            //香港
            new String[]{"香港岛", "九龙", "新界"},
            //澳门
            new String[]{"澳门"},
            // 台湾
            new String[]{"台北", "高雄", "基隆", "台中", "台南", "新竹", "嘉义"}};





    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private ProgressDialog progressDialog;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private PM25DB pm25DB;
    private List<String> dataList = new ArrayList<String>();
    /**
     * 省列表
     */
    private List<Province> provinceList;
    /**
     * 市列表
     */
    private List<City> cityList;
    /**
     * 县列表
     */
    private List<County> countyList;
    /**
     * 选中的省份
     */
    private Province selectedProvince;
    /**
     * 选中的城市
     */
    private City selectedCity;
    /**
     * 当前选中的级别
     */
    private int currentLevel;



    private ImageView img_back,img_confirm;

//    Intent intent = getIntent();


    private void initView() {
        img_back = (ImageView) findViewById(R.id.chosen_back);
        img_confirm = (ImageView) findViewById(R.id.chosen_back);
        listView = (ListView) findViewById(R.id.listview);
        img_back.setOnClickListener(new MyOnclickListenter());
        img_confirm.setOnClickListener(new MyOnclickListenter());
    }

    class MyOnclickListenter implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.chosen_back:
                    Intent intent = new Intent(CityChosenActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.chosen_confirm:
                    //如果用户什么都没有选中
//                    if () {
//
//                    } else {
//
//                    }
                    Intent intent2 = new Intent(CityChosenActivity.this, MainActivity.class);
//                    intent2.putExtra("city_name",)
                    startActivity(intent2);
                    finish();
                    break;
            }
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_city_chosen);
        initView();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        pm25DB = PM25DB.getInstance(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int index,
                                    long arg3) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get(index);
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(index);
                    queryCounties();
                }
            }
        });
        queryProvinces();  // 加载省级数据
    }

    /**
     * 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询。
     */
    private void queryProvinces() {
        provinceList = pm25DB.loadProvinces();
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        } else {
            queryFromServer(null, "province");
        }
    }

    /**
     * 查询选中省内所有的市，优先从数据库查询，如果没有查询到再去服务器上查询。
     */
    private void queryCities() {
        cityList = pm25DB.loadCities(selectedProvince.getId());
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        } else {
            queryFromServer(selectedProvince.getProvinceCode(), "city");
        }
    }

    /**
     * 查询选中市内所有的县，优先从数据库查询，如果没有查询到再去服务器上查询。
     */
    private void queryCounties() {
        countyList = pm25DB.loadCounties(selectedCity.getId());
        if (countyList.size() > 0) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
//            titleText.setText(selectedCity.getCityName());
            currentLevel = LEVEL_COUNTY;
        } else {
            queryFromServer(selectedCity.getCityCode(), "county");
        }
    }

    /**
     * 根据传入的代号和类型从服务器上查询省市县数据。
     */
    private void queryFromServer(final String code, final String type) {
        String address;
        if (!TextUtils.isEmpty(code)) {
            address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
        } else {
            address = "http://www.weather.com.cn/data/list3/city.xml";
        }
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpUtil.HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                if ("province".equals(type)) {
                    result = Utility.handleProvincesResponse(pm25DB, response);
                } else if ("city".equals(type)) {
                    result = Utility.handleCitiesResponse(pm25DB, response, selectedProvince.getId());
                } else if ("county".equals(type)) {
                    result = Utility.handleCountiesResponse(pm25DB, response, selectedCity.getId());
                }
                if (result) {
                    // 通过runOnUiThread()方法回到主线程处理逻辑
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)) {
                                queryProvinces();
                            } else if ("city".equals(type)) {
                                queryCities();
                            } else if ("county".equals(type)) {
                                queryCounties();
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                // 通过runOnUiThread()方法回到主线程处理逻辑
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(CityChosenActivity.this,
                                "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /**
     * 捕获Back按键，根据当前的级别来判断，此时应该返回市列表、省列表、还是直接退出。
     */
    @Override
    public void onBackPressed() {
        if (currentLevel == LEVEL_COUNTY) {
            queryCities();
        } else if (currentLevel == LEVEL_CITY) {
            queryProvinces();
        } else {
            finish();
        }
    }

}
