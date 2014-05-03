package com.hongguaninfo.ocnandroid.main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.hongguaninfo.ocnandroid.adapters.MultiSpinnerAdapter;
import com.hongguaninfo.ocnandroid.beans.Fault;
import com.hongguaninfo.ocnandroid.main.CommonPage;
import com.hongguaninfo.ocnandroid.main.R;
import com.hongguaninfo.ocnandroid.main.SignPage;
import com.hongguaninfo.ocnandroid.service.EastService;
import com.hongguaninfo.ocnandroid.service.LinkInfo;
import com.hongguaninfo.ocnandroid.service.ServiceFactory;
import com.hongguaninfo.ocnandroid.utils.JsonUtil;
import com.hongguaninfo.ocnandroid.utils.OSService;
import com.hongguaninfo.ocnandroid.utils.OSUtil;
import com.hongguaninfo.ocnandroid.utils.OcnUtil;
import com.hongguaninfo.ocnandroid.utils.UploadUtil;
import com.hongguaninfo.ocnandroid.utils.ViewFactory;
import com.hongguaninfo.ocnandroid.widgets.Builders;

/**
 * 回单页面
 *
 * @author chengkai
 */
public class OrderBackPage extends CommonPage {
    //网络连接的检查结果默认未连接
    private int netResult=-1;
    // 标题栏
    private ProgressBar pb;
    private TextView title;
    private boolean signSuccess = false;
    // 同步处理--签名和提交信息
    private Integer index1199 = 0;
    // =====================判断各个选项是否符合要求====================
    //如果是自动恢复---只需要判断故障选项即可
    private boolean flag = false;
    //电话已联系好
    private boolean flag1=false;
    private boolean normalBug1 = false;
    private boolean normalBug3 = false;
    private boolean normalBug5 = false;
    private boolean normalBug6 = false;
    private boolean normalNgb = false;
    private boolean normalSign = false;
    private boolean normalBusiness = false;
    // 网络数据是否正常
    private boolean isNormal = true;
    // =====================要提交的数据相关======9=================
    private String finalValue = "";
    // 辅料数据====需要处理成json数据
    private List<Map<String, String>> dataMaters = new ArrayList<Map<String, String>>();
    // 室内数据
    private List<Map<String, String>> dataSN = new ArrayList<Map<String, String>>();
    // 楼道数据
    private List<Map<String, String>> dataLD = new ArrayList<Map<String, String>>();
    // 处理提交表象---获取dtid值
    private List<Fault> faultHandFeature = new ArrayList<Fault>();
    // 处理提交手段
    private List<Fault> faultHandMeans = new ArrayList<Fault>();
    // 辅料最终处理成的json数据
    private String matersValue = "";
    private String snValue = "";
    private String ldValue = "";
    // 表象值
    private String bugDtIdValue5 = "";
    // 排障值
    private String bugDtIdValue6 = "";

    private String bugDtIdValue1 = "";
    private String bugDstIdValue2 = "";
    private String bugDtIdValue3 = "";
    private String bugDstIdValue4 = "";
    private String isNGBValue = "";
    private String evaluIdValue = "";
    private String beizhuValue = "";
    // 回单结果
    private String maintainResult = "";

    // =======================下拉多选===============================
    private PopupWindow popupFeature;
    private PopupWindow popupMeans;
    private LayoutInflater inflater;
    private View rootView, spView5, spView6;
    private ListView multiList5, multiList6;
    private Button spinnerEnsure5, spinnerEnsure6;
    private MultiSpinnerAdapter msadapter5;
    private MultiSpinnerAdapter msadapter6;

    // 默认故障表象
    // 主页面
    private ViewFlipper flipper;
    private ImageView img;
    private Button tab1, tab2, tab3, tab4;

    private int index = 0;
    // 保存标签页面
    // 添加辅料
    private Button add;
    // 动态添加辅料
    private LinearLayout dllMaters;
    // 故障标签页面--故障类型
    private Spinner sp1;
    // 解决手段
    private Spinner sp2;
    // 故障原因
    private Spinner sp3;
    // 故障等级
    private Spinner sp4;
    // 辅料下拉菜单
    private RelativeLayout relative5;
    //
    private TextView number5;
    // 显示被选数据
    private TextView num_data5;
    private RelativeLayout relative6;
    //
    private TextView number6;
    // 显示被选数据
    private TextView num_data6;
    private Spinner access_sp;
    private EditText num;
    private EditText beizhu;
    private SeekBar sb;
    private Spinner ngb_sp;
    // 业务标签页面
    private LinearLayout dllSN, dllLD;
    // 业务下拉菜单
    private Spinner btSp;
    // 评价标签页面
    private RadioGroup erg;
    private Button sign;
    private ImageView signBitmap;
    // 确认页面
    // 故障信息
    private TextView desc1;
    // 辅料及数量信息
    private TextView desc2;
    // 回单备注
    private TextView desc3;
    // ngb区域
    private TextView desc4;
    // 业务类型
    private TextView desc5;
    // 终端信号
    private TextView desc6;
    // tap信号
    private TextView desc7;
    // 评价
    private TextView desc8;
    private Button save;
    private EastService service;
    // 用于显示的值
    private String bugValue1 = "";
    private String bugValue2 = "";
    private String bugValue3 = "";
    private String bugValue4 = "";
    private String bugValue5 = "";
    private String bugValue6 = "";
    private String[] evalus;
    private String evaluValue = "";
    private String[] NGBs;
    private String isNGB = "";
    // 辅料
    private String accessoryValue = "";
    private String accessoryDstIdValue = "";
    private String businessValue = "";
    private String businessDtIdValue = "";
    private ArrayAdapter<String> adapter9;
    // 处理显示
    private List<String> buss = new ArrayList<String>();
    private List<String> data1 = new ArrayList<String>();
    private List<String> data2 = new ArrayList<String>();
    private ArrayAdapter<String> adapter2;
    private List<String> data3 = new ArrayList<String>();
    private List<String> data4 = new ArrayList<String>();
    private ArrayAdapter<String> adapter4;
    private List<String> data5 = new ArrayList<String>();
    private List<String> data6 = new ArrayList<String>();
    // 处理辅料显示
    private List<String> materials = new ArrayList<String>();
    // ======================处理逻辑=============================
    // 辅料控件--用于回显数据
    private Map<View, String> matersAll = new HashMap<View, String>();
    // 辅料控件--用于处理数据
    private Map<View, String> matersKey = new HashMap<View, String>();

    // 业务数据
    private List<Fault> busis = new ArrayList<Fault>();
    // 故障类型数据
    private List<Fault> faultClass = new ArrayList<Fault>();
    // 解决手段
    private List<Fault> faultMethod = new ArrayList<Fault>();
    // 故障原因数据
    private List<Fault> faultCause = new ArrayList<Fault>();
    // 故障等级
    private List<Fault> faultLevel = new ArrayList<Fault>();
    // 故障表象---
    private List<Fault> faultFeature = new ArrayList<Fault>();

    // 排障手段
    private List<Fault> faultMeans = new ArrayList<Fault>();

    // 室内信息数据
    private List<Fault> faultSN = new ArrayList<Fault>();
    // 楼道信息数据
    private List<Fault> faultLD = new ArrayList<Fault>();
    // 辅料数据
    private List<Fault> faultMaters = new ArrayList<Fault>();
    // 保存(*)标记的集合
    // 室内
    private List<Integer> starsSN = new ArrayList<Integer>();
    // 楼道
    private List<Integer> starsLD = new ArrayList<Integer>();
    private Map<Integer, EditText> sn = new HashMap<Integer, EditText>();
    private Map<Integer, EditText> ld = new HashMap<Integer, EditText>();
    // 处理动态创建的控件
    private Map<View, View> mater_items = new HashMap<View, View>();

    // 是否是进入
    private boolean isEnter = true;
    // 签名传过来的值
    private String signPath = "";
    // 签名位图路径
    private String filename = "";
    // 签名位图
    private Bitmap bitmap;
    // 上传地址
    private static String requestURL1;

    private int[] tabbgs = { R.drawable.order_tab1, R.drawable.order_tab2,
            R.drawable.order_tab3, R.drawable.order_tab4 };

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            pb.setVisibility(View.GONE);
            // 保存时发送此消息
            if (msg.arg1 == 0x6666) {
                String str = (String) msg.obj;
                // System.out.println("6666--->" + str);
                String[] srr = str.split("##");
                if (srr.length == 6) {
                    try {
                        // 故障分类
                        faultClass = JsonUtil.getFaultDtList(srr[0]);
                        // 故障原因
                        faultCause = JsonUtil.getFaultDtList(srr[1]);
                        // 故障表象
                        faultFeature = JsonUtil.getFaultDtList(srr[2]);
                        // 解决手段
                        faultMeans = JsonUtil.getFaultDtList(srr[3]);
                        // 辅料列表
                        faultMaters = JsonUtil.getFaultDstList(srr[4]);
                        // 业务类型
                        busis = JsonUtil.getFaultDtList(srr[5]);
                        initData();
                    } catch (Exception e) {
//						System.out.println("返回格式错误!");
                        e.printStackTrace();
                    }
                    isNormal = true;
                } else {
                    showHint("数据错误！请重新进入回单界面!");
                    isNormal = false;
                }
            }
            if (msg.what == 0x9999) {
                showHint("数据错误！请重新进入回单界面!");
                isNormal = false;
            }
            if (msg.arg1 == 0x7777) {
                String str = (String) msg.obj;
                String[] srr = str.split("##");
                if (srr.length == 2) {
                    try {
                        faultSN = JsonUtil.getFaultDstList(srr[0]);
                        faultLD = JsonUtil.getFaultDstList(srr[1]);
                        initBusisData();
                    } catch (Exception e) {
//						System.out.println("返回格式错误!");
                        normalBusiness = false;
                        e.printStackTrace();
                    }
                } else {
                    showHint("业务请求失败!");
                    normalBusiness = false;
                }
            }
            if (msg.what == 0x888) {
                showHint("业务请求失败!");
                normalBusiness = false;
            }
            // 获取解决手段列表
            if (msg.arg1 == 0x222) {
                String str = (String) msg.obj;
                faultMethod = JsonUtil.getFaultDstList(str);
                data2.clear();
                if(adapter2!=null){
                    adapter2.notifyDataSetChanged();
                    bugValue2="";
                    bugDstIdValue2="";
                }
                if (faultMethod.size() > 0) {
                    for (int i = 0; i < faultMethod.size(); i++) {
                        data2.add(faultMethod.get(i).getDstValue());
                    }
                    adapter2 = new ArrayAdapter<String>(OrderBackPage.this,
                            android.R.layout.simple_spinner_item, data2);
                    // 设置自顶下拉项
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // 为下拉列表设置适配
                    sp2.setSelection(0, true);
                    sp2.setAdapter(adapter2);
                    bugValue2 = data2.get(0);
                    bugDstIdValue2 = faultMethod.get(0).getDstId();
                }
            }
            // 获取故障等级列表
            if (msg.arg1 == 0x444) {
                String str = (String) msg.obj;
                faultLevel = JsonUtil.getFaultDstList(str);
                data4.clear();
                if(adapter4!=null){
                    adapter4.notifyDataSetChanged();
                    bugValue4="";
                    bugDstIdValue4="";
                }
                if (faultLevel.size() > 0) {
                    for (int i = 0; i < faultLevel.size(); i++) {
                        data4.add(faultLevel.get(i).getDstValue());
                    }
                    adapter4 = new ArrayAdapter<String>(OrderBackPage.this,
                            android.R.layout.simple_spinner_item, data4);
                    // 设置自顶下拉项
                    adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // 为下拉列表设置适配
                    sp4.setSelection(0, true);
                    sp4.setAdapter(adapter4);
                    bugValue4 = data4.get(0);
                    bugDstIdValue4 = faultLevel.get(0).getDstId();
                }
            }
            // 这里主要处理删除签名文件
            if (msg.arg1 == 0x977) {
                calIndex1199();
                showHint("签名上传成功!");
                if (signPath != null) {
                    File file = new File(signPath);
                    if (file.exists()) {
                        file.delete();
                    }
                }
                signSuccess = true;
            }
            if (msg.what == 0x988) {
                signSuccess = false;
                calIndex1199();
                showHint("签名上传失败!");
            }
            if (msg.arg1 == 0x1166) {
                calIndex1199();
                String str = (String) msg.obj;
                if (str.equals("true")) {
                    showHint("提交信息成功!");
                    finish();
                } else {
                    showHint("提交信息失败!");
                    save.setEnabled(true);
                }
            }

            if (msg.what == 0x1199) {
                save.setEnabled(true);
                calIndex1199();
                showHint("提交信息失败!");
            }
        }
    };

    private String orderId = "";
    private String userId = "";
    private ThreadPoolExecutor executor;

    public void onCreate(Bundle savel) {
        super.onCreate(savel);
        setContentView(R.layout.order_back);
        evalus = this.getResources().getStringArray(R.array.evalus);
        inflater = LayoutInflater.from(this);
        evaluValue = evalus[0];
        evaluIdValue = "1";
        NGBs = this.getResources().getStringArray(R.array.NGBs);
        isNGB = NGBs[0];
        isNGBValue = "";
        orderId = this.getIntent().getStringExtra("orderId");
        userId = this.queryUserInfo().getUserId();
        maintainResult = this.getIntent().getStringExtra("mtainResult");
        service = ServiceFactory.getService(this);
        // 初始化线程池---同时开启的线程越多---指定存活时间要拉长
        executor = new ThreadPoolExecutor(5, 10, 5, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>());
        initRoot();
        initBug();
        initBusiness();
        initEvaluate();
        initEnsure();
    }

    private void initBusisData() {
        dllSN.removeAllViews();
        dllLD.removeAllViews();
        sn.clear();
        ld.clear();
        starsSN.clear();
        starsLD.clear();
        if (faultSN.size() > 0) {
            for (int i = 0; i < faultSN.size(); i++) {
                String st = faultSN.get(i).getDstValue();
                if (hasStar(st)) {
                    // 这个集合用于保存必填的项---检查使用
                    starsSN.add(Integer.valueOf(i));
                }
                TextView tv = ViewFactory.createText(OrderBackPage.this, st);
                EditText et = ViewFactory.createEdit(OrderBackPage.this);
                sn.put(i, et);
                dllSN.addView(tv);
                dllSN.addView(et);
            }
        }
        if (faultLD.size() > 0) {
            for (int i = 0; i < faultLD.size(); i++) {
                String st = faultLD.get(i).getDstValue();
                if (hasStar(st)) {
                    starsLD.add(Integer.valueOf(i));
                }
                TextView tv = ViewFactory.createText(OrderBackPage.this, st);
                EditText et = ViewFactory.createEdit(OrderBackPage.this);
                ld.put(i, et);
                dllLD.addView(tv);
                dllLD.addView(et);
            }
        }
    }

    private void initData() {
        if (faultClass.size() > 0) {
            data1.add("请选择");
            for (int i = 0; i < faultClass.size(); i++) {
                data1.add(faultClass.get(i).getDtValue());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    OrderBackPage.this, android.R.layout.simple_spinner_item,
                    data1);
            // 设置自顶下拉项
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // 为下拉列表设置适配
            sp1.setSelection(0, true);
            sp1.setAdapter(adapter);
//			bugValue1 = data1.get(0);
//			bugDtIdValue1 = faultClass.get(0).getDtId();
        } else {
            isNormal = false;
        }
        if (faultCause.size() > 0) {
            data3.add("请选择");
            for (int i = 0; i < faultCause.size(); i++) {
                data3.add(faultCause.get(i).getDtValue());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    OrderBackPage.this, android.R.layout.simple_spinner_item,
                    data3);
            // 设置自顶下拉项
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // 为下拉列表设置适配
            sp3.setSelection(0, true);
            sp3.setAdapter(adapter);
//			bugValue3 = data3.get(0);
//			bugDtIdValue3 = faultCause.get(0).getDtId();
        } else {
            isNormal = false;
        }
        if (faultFeature.size() > 0) {
            data5.add("请选择");
            for (int i = 0; i < faultFeature.size(); i++) {
                data5.add(faultFeature.get(i).getDtValue());
            }
            msadapter5 = new MultiSpinnerAdapter(OrderBackPage.this, data5);
            // 为下拉列表设置适配
            num_data5.setText(data5.get(0));
            multiList5.setAdapter(msadapter5);
        } else {
            isNormal = false;
        }
        if (faultMeans.size() > 0) {
            data6.add("请选择");
            for (int i = 0; i < faultMeans.size(); i++) {
                // System.out.println("排障手段：" + faultMeans.get(i).getDtValue());
                data6.add(faultMeans.get(i).getDtValue());
            }
            msadapter6 = new MultiSpinnerAdapter(OrderBackPage.this, data6);
            // 为下拉列表设置适配
            num_data6.setText(data6.get(0));
            multiList6.setAdapter(msadapter6);
        } else {
            isNormal = false;
        }
        if (faultMaters.size() > 0) {
            materials.add("请选择");
            for (int i = 0; i < faultMaters.size(); i++) {
                materials.add(faultMaters.get(i).getDstValue());
            }
            adapter9 = new ArrayAdapter<String>(OrderBackPage.this,
                    android.R.layout.simple_spinner_item, materials);
            // 设置自顶下拉项
            adapter9.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // 为下拉列表设置适配
            access_sp.setSelection(0, true);
            access_sp.setAdapter(adapter9);
//			accessoryValue = materials.get(0);
//			accessoryDstIdValue = faultMaters.get(0).getDstId();
        } else {
            isNormal = false;
        }
        if (busis.size() > 0) {
            buss.add("请选择");
            for (int i = 0; i < busis.size(); i++) {
                buss.add(busis.get(i).getDtValue());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    OrderBackPage.this, android.R.layout.simple_spinner_item,
                    buss);
            // 设置自顶下拉项
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // 为下拉列表设置适配
            btSp.setSelection(0, true);
            btSp.setAdapter(adapter);
            // businessValue = buss.get(0);
//			businessDtIdValue = busis.get(0).getDtId();
        } else {
            isNormal = false;
        }
    }

    private void initRoot() {
        rootView = OSService.getLayoutInflater(this).inflate(
                R.layout.order_back, null);
        title = (TextView) findViewById(R.id.base_top_title);
        pb = (ProgressBar) findViewById(R.id.base_top_progress);
        title.setText("回单");

        flipper = (ViewFlipper) findViewById(R.id.order_back_flipper);
        img = (ImageView) findViewById(R.id.order_back_top_img);
        tab1 = (Button) findViewById(R.id.order_back_tab1);
        tab2 = (Button) findViewById(R.id.order_back_tab2);
        tab3 = (Button) findViewById(R.id.order_back_tab3);
        tab4 = (Button) findViewById(R.id.order_back_tab4);

        tab1.setOnClickListener(listener);
        tab2.setOnClickListener(listener);
        tab3.setOnClickListener(listener);
        tab4.setOnClickListener(listener);
        flipper.setDisplayedChild(index);
    }

    // 初始化第一个选项
    private void initBug() {
        // 辅料下拉菜单
        access_sp = (Spinner) findViewById(R.id.order_back_accessory_sp);

        add = (Button) findViewById(R.id.order_back_add);
        dllMaters = (LinearLayout) findViewById(R.id.order_back_mater_items);
        ngb_sp = (Spinner) findViewById(R.id.order_back_isngb);

        // 故障
        sp1 = (Spinner) findViewById(R.id.order_back_bug1);
        sp2 = (Spinner) findViewById(R.id.order_back_bug2);
        sp3 = (Spinner) findViewById(R.id.order_back_bug3);
        sp4 = (Spinner) findViewById(R.id.order_back_bug4);
        relative5 = (RelativeLayout) findViewById(R.id.order_back_btn_bug5);
        number5 = (TextView) findViewById(R.id.spinner_number5);
        num_data5 = (TextView) findViewById(R.id.spinner_data5);
        relative6 = (RelativeLayout) findViewById(R.id.order_back_btn_bug6);
        number6 = (TextView) findViewById(R.id.spinner_number6);
        num_data6 = (TextView) findViewById(R.id.spinner_data6);
        spView5 = inflater.inflate(R.layout.spinner_list, null);
        spView6 = inflater.inflate(R.layout.spinner_list, null);
        spinnerEnsure5 = (Button) spView5
                .findViewById(R.id.spinner_main_ensure);
        spinnerEnsure6 = (Button) spView6
                .findViewById(R.id.spinner_main_ensure);
        multiList5 = (ListView) spView5.findViewById(R.id.spinner_main_list);
        multiList6 = (ListView) spView6.findViewById(R.id.spinner_main_list);
        popupFeature = new PopupWindow(spView5, -1, -2, true);
        popupMeans = new PopupWindow(spView6, -1, -2, true);
        popupFeature.setAnimationStyle(R.style.menu_animation);
        popupFeature.setOutsideTouchable(true);// 设置popup之外可以触摸
        // 此行用于响应返回键--加入此行对象不能为null值否则返回键不响应
        popupFeature.setBackgroundDrawable(new BitmapDrawable());
        popupMeans.setAnimationStyle(R.style.menu_animation);
        popupMeans.setOutsideTouchable(true);// 设置popup之外可以触摸
        // 此行用于响应返回键--加入此行对象不能为null值否则返回键不响应
        popupMeans.setBackgroundDrawable(new BitmapDrawable());
        multiList5.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position != 0) {
                    msadapter5.check(position);
                }
            }
        });
        multiList6.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position != 0) {
                    msadapter6.check(position);
                }
            }
        });
        relative5.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                popupFeature.showAtLocation(rootView, Gravity.CENTER, 0, 0);
            }
        });
        relative6.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                popupMeans.showAtLocation(rootView, Gravity.CENTER, 0, 0);
            }
        });
        spinnerEnsure5.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                faultHandFeature.clear();
                boolean[] bool = msadapter5.getIsSelected();
                int bLen = calTrue(bool);
                number5.setText("" + bLen);
                if (bLen == 0) {
                    showHint("至少选一项哟");
                    num_data5.setTextColor(Color.BLUE);
                    num_data5.setText(data5.get(0));
                    bugValue5 = "";
                    bugDtIdValue5 = "";
                    normalBug5 = false;
                } else {
                    normalBug5 = true;
                    //组合id
                    String stmpId5 = "";
                    //组合字符串
                    String tmp = "";
                    //确认页面回显
                    String stmp = "";
                    num_data5.setTextColor(Color.BLACK);
                    for (int i = 0, j = 0; i < bool.length; i++) {
                        if (bool[i]) {
                            j++;
                            int k=i-1;
                            if (j == bLen) {
                                tmp += faultFeature.get(k).getDtValue();
                                stmp += faultFeature.get(k).getDtValue();
                                stmpId5 += faultFeature.get(k).getDtId();
                                break;
                            } else {
                                tmp += faultFeature.get(k).getDtValue() + "/";
                                stmp += faultFeature.get(k).getDtValue() + "\n";
                                stmpId5 += faultFeature.get(k).getDtId() + ",";
                            }
                        }
                    }
                    num_data5.setText(tmp);
                    bugValue5 = stmp;
                    bugDtIdValue5 = stmpId5;
                }
                if (popupFeature.isShowing()) {
                    popupFeature.dismiss();
                }
                num_data5.requestFocus();
            }
        });
        spinnerEnsure6.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                faultHandMeans.clear();
                boolean[] bool = msadapter6.getIsSelected();
                int bLen = calTrue(bool);
                number6.setText("" + bLen);
                if (bLen == 0) {
                    normalBug6 = false;
                    showHint("至少选一项哟");
                    num_data6.setTextColor(Color.BLUE);
                    num_data6.setText(data6.get(0));
                    bugValue6 = "";
                    bugDtIdValue6 = "";
                } else {
                    normalBug6 = true;
                    String stmpId6 = "";
                    String tmp = "";
                    String stmp = "";
                    num_data6.setTextColor(Color.BLACK);
                    for (int i = 0, j = 0; i < bool.length; i++) {
                        if (bool[i]) {
                            j++;
                            int k=i-1;
                            if (j ==bLen) {
                                tmp += faultMeans.get(k).getDtValue();
                                stmp += faultMeans.get(k).getDtValue();
                                stmpId6 += faultMeans.get(k).getDtId();
                                break;
                            } else {
                                tmp += faultMeans.get(k).getDtValue() + "/";
                                stmp += faultMeans.get(k).getDtValue() + "\n";
                                stmpId6 += faultMeans.get(k).getDtId() + ",";
                            }
                        }
                    }
                    num_data6.setText(tmp);
                    bugValue6 = stmp;
                    bugDtIdValue6 = stmpId6;
                }
                if (popupMeans.isShowing()) {
                    popupMeans.dismiss();
                }
                num_data6.requestFocus();
            }
        });

        num = (EditText) findViewById(R.id.order_back_accessory_num);
        beizhu = (EditText) findViewById(R.id.order_back_accessory_beizhu);
        sb = (SeekBar) findViewById(R.id.order_back_accessory_sb);
        add.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // 获取添加辅料的数量
                String tmp = num.getText().toString().trim();
                if (!tmp.equals("0") && !tmp.equals("")) {
                    if (accessoryValue.equals("")) {
                        showHint("请选择辅料");
                        return;
                    }
                    String str = accessoryValue + ":\t" + tmp;
                    View viewTmp = createView(str);
                    // 将控件添加进映射集合
                    matersAll.put(viewTmp, str);
                    // 动态添加控件
                    dllMaters.addView(viewTmp);
                    matersKey.put(viewTmp, accessoryDstIdValue);
                    Map<String, String> map = new HashMap<String, String>();
                    // 辅料以dstId作为key;no作为value
                    map.put(accessoryDstIdValue, tmp);
                    dataMaters.add(map);
                    sb.setProgress(0);
                    num.setText("0");
                } else {
                    showHint("请选择辅料数量");
                }
            }
        });
        sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                num.setText("" + progress);
            }
        });
        ngb_sp.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                isNGB = NGBs[position];
                if (position == 0) {
                    normalNgb = false;
                    isNGBValue = "";
                } else {
                    normalNgb = true;
                    if (position == 1) {
                        isNGBValue = "1";
                    } else if (position == 2) {
                        isNGBValue = "0";
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        // 初始化时监听器内的内容都会被执行一次
        access_sp.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (materials.size() > 0) {
                    if (position == 0) {
                        accessoryValue ="";
                    } else {
                        accessoryValue = materials.get(position);
                        accessoryDstIdValue = faultMaters.get(position - 1)
                                .getDstId();
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        sp1.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (data1.size() > 0) {
                    if (position == 0) {
                        normalBug1 = false;
                        bugValue1="";
                        bugDtIdValue1="";
                        data2.clear();
                        if (adapter2 != null) {
                            adapter2.notifyDataSetChanged();
                        }
                    } else {
                        if(data1.get(position).trim().equals("自动恢复")){
                            flag=true;
                        }else{
                            flag=false;
                        }
                        normalBug1 = true;
                        int tmp=position-1;
                        bugValue1 = data1.get(position);
                        bugDtIdValue1 = faultClass.get(tmp).getDtId();
                        pb.setVisibility(View.VISIBLE);
                        executor.execute(new Thread2(bugDtIdValue1));
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        sp2.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (data2.size() > 0) {
                    bugValue2 = data2.get(position).trim();

                    bugDstIdValue2 = faultMethod.get(position).getDstId();
                    //判断“电话联系已好”时，不要求所有必填
                    if("12007002".equals(bugDstIdValue2)){
                        flag1=true;
                    }else{
                        flag1=false;
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        sp3.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (data3.size() > 0) {
                    if (position == 0) {
                        normalBug3 = false;
                        bugValue3="";
                        bugDtIdValue3="";
                        data4.clear();
                        if (adapter4 != null) {
                            adapter4.notifyDataSetChanged();
                        }
                    } else {
                        normalBug3 = true;
                        int tmp=position-1;
                        bugValue3 = data3.get(position);
                        bugDtIdValue3 = faultCause.get(tmp).getDtId();
                        pb.setVisibility(View.VISIBLE);
                        executor.execute(new Thread4(bugDtIdValue3));
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        sp4.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (data4.size() > 0) {
                    bugValue4 = data4.get(position);
                    bugDstIdValue4 = faultLevel.get(position).getDstId();
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    // 初始化业务页面
    private void initBusiness() {
        // 业务下拉菜单
        btSp = (Spinner) findViewById(R.id.order_back_business_sp);
        dllSN = (LinearLayout) findViewById(R.id.order_back_dll_sn);
        dllLD = (LinearLayout) findViewById(R.id.order_back_dll_ld);
        btSp.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (buss.size() > 0) {
                    if (position == 0) {
                        normalBusiness = false;
                        dllSN.removeAllViews();
                        dllLD.removeAllViews();
                        sn.clear();
                        ld.clear();
                        starsSN.clear();
                        starsLD.clear();
                        businessValue = "";
                        businessDtIdValue = "";
                    } else {
                        String dtId = busis.get(position - 1).getDtId();
                        businessDtIdValue = dtId;
                        businessValue = buss.get(position);
                        pb.setVisibility(View.VISIBLE);
                        executor.execute(new BusinessThread(dtId));
                        normalBusiness = true;
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    // 初始化评价页面
    private void initEvaluate() {
        erg = (RadioGroup) findViewById(R.id.order_back_evaluate_rg);
        sign = (Button) findViewById(R.id.order_back_sign);
        signBitmap = (ImageView) findViewById(R.id.order_back_img);
        sign.setOnClickListener(listener);
        erg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.order_back_evaluate_yes:
                        evaluValue = evalus[0];
                        evaluIdValue = "1";
                        break;
                    case R.id.order_back_evaluate_middle:
                        evaluValue = evalus[1];
                        evaluIdValue = "2";
                        break;
                    case R.id.order_back_evaluate_no:
                        evaluValue = evalus[2];
                        evaluIdValue = "3";
                        break;
                }
            }
        });
    }

    // 初始化确认页面
    private void initEnsure() {
        desc1 = (TextView) findViewById(R.id.order_ensure_desc1);
        desc2 = (TextView) findViewById(R.id.order_ensure_desc2);
        desc3 = (TextView) findViewById(R.id.order_ensure_desc3);
        desc4 = (TextView) findViewById(R.id.order_ensure_desc4);
        desc5 = (TextView) findViewById(R.id.order_ensure_desc5);
        desc6 = (TextView) findViewById(R.id.order_ensure_desc6);
        desc7 = (TextView) findViewById(R.id.order_ensure_desc7);
        desc8 = (TextView) findViewById(R.id.order_ensure_desc8);
        save = (Button) findViewById(R.id.order_back_save);
        save.setOnClickListener(listener);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new Builders(OrderBackPage.this)
                    .setContent("你确定要离开回单页面吗？")
                    .setView(R.layout.builder_dialog, R.id.custom_content,
                            R.id.custom_positive, R.id.custom_negative)
                    .setPositiveButton("确定", new OnClickListener() {
                        public void onClick(View v) {
                            finish();
                        }
                    }).setNegativeButton("取消", null).show();
        } else if (keyCode == KeyEvent.KEYCODE_HOME) {
            new Builders(OrderBackPage.this)
                    .setContent("你确定要离开回单页面吗？")
                    .setView(R.layout.builder_dialog, R.id.custom_content,
                            R.id.custom_positive, R.id.custom_negative)
                    .setPositiveButton("确定", new OnClickListener() {
                        public void onClick(View v) {
                            finish();
                        }
                    }).setNegativeButton("取消", null).show();
        }
        return super.onKeyDown(keyCode, event);
    }

    private class Thread2 extends Thread {
        String fci;

        public Thread2(String faultClassId) {
            fci = faultClassId;
        }

        @Override
        public void run() {
            String str = service.faultSolutionList(fci);
            if (str != null) {
                Message msg = new Message();
                msg.obj = str;
                msg.arg1 = 0x222;
                handler.sendMessage(msg);
            } else {
                handler.sendEmptyMessage(0x999);
            }
        }
    }

    private class Thread4 extends Thread {
        String id;

        public Thread4(String dtId) {
            id = dtId;
        }

        @Override
        public void run() {
            String str = service.faultLevelList(id);
            if (str != null) {
                Message msg = new Message();
                msg.obj = str;
                msg.arg1 = 0x444;
                handler.sendMessage(msg);
            } else {
                handler.sendEmptyMessage(0x999);
            }
        }
    }

    /**
     * 上传用户签名线程
     */
    private class Thread10 extends Thread {
        public void run() {
            if (filename == null) {
                return;
            }
            File file = new File(signPath);
            // 上传签名--返回null代表上传失败--
            String str = UploadUtil.uploadFile(file, OcnUtil.getUpload());
            if (str != null) {
                Message msg = new Message();
                msg.arg1 = 0x977;
                msg.obj = str;
                handler.sendMessage(msg);
            } else {
                // 上传签名失败
                handler.sendEmptyMessage(0x988);
            }
        };
    }

    /**
     * 最后提交所有信息的线程
     */
    private class Thread11 extends Thread {
        public void run() {
            String str = service.backOrderSave(finalValue, snValue, ldValue,
                    matersValue);
            if (str != null) {
                Message msg = new Message();
                msg.arg1 = 0x1166;
                msg.obj = str;
                handler.sendMessage(msg);
            } else {
                // 提交信息失败
                handler.sendEmptyMessage(0x1199);
            }
        }
    }

    private class FirstThread extends Thread {
        public void run() {
            String str = service.backOrderType();
            if (str != null) {
                Message msg = new Message();
                msg.arg1 = 0x6666;
                msg.obj = str;
                handler.sendMessage(msg);
            } else {
                // 提交信息失败
                handler.sendEmptyMessage(0x9999);
            }
        }
    }

    private class BusinessThread extends Thread {
        private String digTypeId;

        public BusinessThread(String digTypeId) {
            this.digTypeId = digTypeId;
        }

        public void run() {
            String str = service.signalParams(digTypeId);
            if (str != null) {
                Message msg = new Message();
                msg.arg1 = 0x7777;
                msg.obj = str;
                handler.sendMessage(msg);
            } else {
                // 提交信息失败
                handler.sendEmptyMessage(0x8888);
            }
        }
    }

    /**
     * 回显信息
     */
    private void echoAllInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("故障类型:" + bugValue1 + "\n")
                .append("解决手段:" + bugValue2 + "\n")
                .append("故障原因:" + bugValue3 + "\n")
                .append("故障等级:" + bugValue4 + "\n")
                .append("故障表象:" + "\n" + bugValue5 + "\n")
                .append("排障手段:" + "\n" + bugValue6);
        // System.out.println("故障相关--->" + sb);
        // 故障
        desc1.setText(sb);
        String ssn = "";
        String sld = "";
        if (btSp.getSelectedItemPosition() != 0) {
            // 业务--- 室内
            for (int i = 0; i < faultSN.size(); i++) {
                Map<String, String> map = new HashMap<String, String>();
                map.put(faultSN.get(i).getDstId(), sn.get(i).getText()
                        .toString().trim());
                dataSN.add(map);
                if (i == faultSN.size() - 1) {
                    ssn += faultSN.get(i).getDstValue() + "\n"
                            + sn.get(i).getText().toString().trim();
                } else {
                    ssn += faultSN.get(i).getDstValue() + "\n"
                            + sn.get(i).getText().toString().trim() + "\n";
                }
            }
            for (int i = 0; i < faultLD.size(); i++) {
                Map<String, String> map = new HashMap<String, String>();
                map.put(faultLD.get(i).getDstId(), ld.get(i).getText()
                        .toString().trim());
                dataLD.add(map);
                if (i == faultLD.size() - 1) {
                    sld += faultLD.get(i).getDstValue() + "\n"
                            + ld.get(i).getText().toString().trim();
                } else {
                    sld += faultLD.get(i).getDstValue() + "\n"
                            + ld.get(i).getText().toString().trim() + "\n";
                }
            }
        } else {
            // System.out.println("业务未选择!");
        }
        // System.out.println(sb);
        // 辅料内容
        String fl = "";
        for (int i = 0; i < dllMaters.getChildCount(); i++) {
            View vtp = dllMaters.getChildAt(i);
            if (i == dllMaters.getChildCount() - 1) {
                fl += matersAll.get(vtp);
            } else {
                fl += matersAll.get(vtp) + "\n";
            }
        }
        // 辅料显示信息
        desc2.setText(fl);
        // System.out.println("辅料--->" + accessoryDstIdValue);
        // 备注信息
        beizhuValue = beizhu.getText().toString().trim();
        desc3.setText(beizhuValue);
        // System.out.println("备注信息--->" + beizhuValue);
        // NGB
        desc4.setText(isNGB);
        // System.out.println("NGB--->" + isNGBValue);
        // 业务
        desc5.setText(businessValue);
        // System.out.println("业务--->" + businessDtIdValue);
        // 室内
        desc6.setText(ssn);
        // 楼道
        desc7.setText(sld);
        // 评价
        desc8.setText(evaluValue);
        // System.out.println("评价--->" + evaluIdValue);
        // 要上传的辅料值
        matersValue = JsonUtil.listToJson(dataMaters);
        snValue = JsonUtil.listToJson(dataSN);
        ldValue = JsonUtil.listToJson(dataLD);
        // System.out.println("辅料json--->" + matersValue);
        finalValue = "";
        finalValue = "{'userId':'" + userId + "','faultClass':'"
                + bugDtIdValue1 + "','faultSolution':'" + bugDstIdValue2
                + "','faultCause':'" + bugDtIdValue3 + "','faultLevel':'"
                + bugDstIdValue4 + "','faultDisp':'" + bugDtIdValue5
                + "','faultMethod':'" + bugDtIdValue6 + "','bType':'"
                + businessDtIdValue + "','isNGB':'" + isNGBValue
                + "','evaluate':'" + evaluIdValue + "','remark':'"
                + beizhuValue + "','orderId':'" + orderId + "','mtainResult':'"
                + maintainResult + "'}";
    }

    private View createView(String str) {
        View root = OSService.getLayoutInflater(this).inflate(
                R.layout.material_item, null);
        Button btn = (Button) root.findViewById(R.id.material_del);
        TextView info = (TextView) root.findViewById(R.id.material_info);
        info.setText(str);
        btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                View relay = mater_items.get(v);
                dllMaters.removeView(relay);
                matersAll.remove(relay);
                String key = matersKey.get(relay);
                for (int i = 0; i < dataMaters.size(); i++) {
                    String st = dataMaters.get(i).keySet().iterator().next();
                    if (st.equals(key)) {
                        // 删除对象---也可以直接删除i
                        dataMaters.remove(dataMaters.get(i));
                        break;// 此处可以提高运行效率
                    }
                }
            }
        });
        mater_items.put(btn, root);
        return root;
    }

    private boolean hasStar(String str) {
        String s = "(*)";
        return str.contains(s);
    }

    OnClickListener listener = new OnClickListener() {
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.order_back_tab1:
                    index = 0;
                    flipper.setDisplayedChild(index);
                    img.setBackgroundResource(tabbgs[index]);
                    break;
                case R.id.order_back_tab2:
                    index = 1;
                    flipper.setDisplayedChild(index);
                    img.setBackgroundResource(tabbgs[index]);
                    break;
                case R.id.order_back_tab3:
                    index = 2;
                    flipper.setDisplayedChild(index);
                    img.setBackgroundResource(tabbgs[index]);
                    break;
                // 确认标签页面
                case R.id.order_back_tab4:
                    index = 3;
                    flipper.setDisplayedChild(index);
                    img.setBackgroundResource(tabbgs[index]);
                    // 回显所有信息
                    echoAllInfo();
                    //检查网络连接的结果
                    netResult=checkNet();
                    break;
                case R.id.order_back_save:
//                    save.setEnabled(false);
                    String info="";
                    if(OSUtil.getClientStatus(OrderBackPage.this)){ //判断预约结果是否为电话支持排障
                        info = checkFault();
                    }else if(flag&&flag1){
                        info = checkFault();
                    }else{
                        info=checkAll();
                    }
                    if (info.equals("true")) {
                        if(netResult==1){
                            showHint("请稍后...");
                            pb.setVisibility(View.VISIBLE);
                            if (!signSuccess) {
                                executor.execute(new Thread10());
                            }
                            executor.execute(new Thread11());
                        }else if(netResult==0){
                            save.setEnabled(true);
                            showHint("当前网络不可用!");
                        }else if(netResult==-1){
                            save.setEnabled(true);
                            showHint("网络连接未打开!");
                        }else{
                            save.setEnabled(true);
                            showHint("正在检查网络连接状态");
                        }
                    } else {
                        showHint(info);
                        save.setEnabled(true);
                    }
                    break;
                case R.id.order_back_sign:
                    Intent intent = new Intent(OrderBackPage.this, SignPage.class);
                    intent.putExtra("orderId", orderId);
                    intent.putExtra("signPath", signPath);
                    startActivityForResult(intent, 0);
                    break;
            }
        }
    };

    /***
     * 只判断一个选项卡
     * @return
     */
    private String checkFault(){
        if (!isNormal) {
            return "回单数据为获取完整!请重新进入回单界面";
        }
        if (!normalBug1) {
            return "故障类型未选择";
        }
        if (!normalBug3) {
            return "故障原因未选择";
        }
        if (!normalBug5) {
            return "故障表象未选择";
        }
        if (!normalBug6) {
            return "排障手段未选择";
        }
        if (!normalNgb) {
            return "NGB区域未选择";
        }
        return "true";
    }
    /**
     * 检查必填的是否填完--填完返回true---否则返回false
     *
     * @return
     */
    private String checkAll() {
        if (!isNormal) {
            return "回单数据为获取完整!请重新进入回单界面";
        }
        if (!normalBug1) {
            return "故障类型未选择";
        }
        if (!normalBug3) {
            return "故障原因未选择";
        }
        if (!normalBug5) {
            return "故障表象未选择";
        }
        if (!normalBug6) {
            return "排障手段未选择";
        }
        if (!normalNgb) {
            return "NGB区域未选择";
        }
        if (!normalBusiness) {
            return "业务未选择";
        }
        if (!normalSign) {
            return "尚未签名";
        }
        // 检查动态创建的输入框是否有数据

        for (int i = 0; i < starsSN.size(); i++) {
            int tmp = starsSN.get(i);
            String strTmp = sn.get(tmp).getText().toString().trim();
            if (strTmp.equals("")) {
                return "室内信息未填写完整";
            }
        }
        // 检查动态创建的输入框是否有数据
        for (int i = 0; i < starsLD.size(); i++) {
            int tmp = starsLD.get(i);
            String strTmp = ld.get(tmp).getText().toString().trim();
            if (strTmp.equals("")) {
                return "楼道信息未填写完整";
            }
        }
        return "true";
    }

    private int calTrue(boolean[] bool) {
        int num = 0;
        for (int i = 0; i < bool.length; i++) {
            if (bool[i]) {
                ++num;
            }
        }
        return num;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == 0 && resultCode == 0 && intent != null) {
            Bundle bundle = intent.getExtras();
            signPath = bundle.getString("path");
            filename = bundle.getString("filename");
            byte[] b = bundle.getByteArray("bitmap");
            if (b != null) {
                BitmapFactory.Options op = new BitmapFactory.Options();
                bitmap = BitmapFactory.decodeByteArray(b, 0, b.length, op);
            }
            if (bitmap != null) {
                signBitmap.setImageBitmap(bitmap);
                normalSign = true;
            } else {
                normalSign = false;
            }
        } else if (requestCode == 0 && resultCode == 1 && intent != null) {
//			System.out.println("取消签名");
        }
    }

    public void onResume() {
        super.onResume();
        if (isEnter) {
            pb.setVisibility(View.VISIBLE);
            // 使用循环执行线程；避免同时创建大量线程
            executor.execute(new FirstThread());
        }
    }

    private void calIndex1199() {
        synchronized (index1199) {
            // 如果签名成功
            if (signSuccess) {
                pb.setVisibility(View.GONE);
            } else {
                if (index1199 < 1) {
                    index1199++;
                } else if (index1199 == 1) {
                    pb.setVisibility(View.GONE);
                    index1199 = 0;
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isEnter = false;
    }
    /**
     * 网络可用返回1;不可用返回0;未连接返回-1
     * @return
     */
    public int checkNet(){
        NetworkInfo net=OSService.getConnectivityManager(OrderBackPage.this).getActiveNetworkInfo();
        if(net!=null){
            boolean isVailable=net.isConnected();
            //判断当前网络是否连接
            if(isVailable){
                return 1;
            }else{
                return 0;
            }
        }else{
            return -1;
        }
    }
}
