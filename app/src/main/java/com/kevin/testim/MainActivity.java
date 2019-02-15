package com.kevin.testim;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.EMClientListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.kevin.testim.adapter.TabFragmentAdapter;
import com.kevin.testim.base.BaseActivity;
import com.kevin.testim.constant.Constants;
import com.kevin.testim.fragment.ChatListFragment;
import com.kevin.testim.fragment.FriendListFragment;
import com.kevin.testim.fragment.SettingFragment;
import com.kevin.testim.view.NoSmoothViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener {
    @BindView(R.id.view_pager)
    NoSmoothViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> tabList = new ArrayList<>();
    private int[] tabIcon = {R.drawable.ic_chat, R.drawable.ic_friend, R.drawable.ic_setting};

    private TabFragmentAdapter tabFragmentAdapter;

    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver broadcastReceiver;
    private ChatListFragment chatListFragment;
    private EMClientListener clientListener;
    private ContactListener contactListener;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        dismissBackButton();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showToast("请授权写入权限");
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
        }
        initFragments();
        initTabList();
        tabFragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager(), this, fragmentList,
                tabList, tabIcon);
        viewPager.setAdapter(tabFragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setCustomView(tabFragmentAdapter.getTabView(i));
        }
        tvTitle.setText("消息");

        registerBoradcastReceiver();
        contactListener = new ContactListener();
        EMClient.getInstance().contactManager().setContactListener(contactListener);
        EMClient.getInstance().addClientListener(clientListener);
        clientListener = new EMClientListener() {
            @Override
            public void onMigrate2x(boolean success) {
                if (success) {
                    refreshUIWithMessage();
                }
            }
        };

        EMClient.getInstance().chatManager().addMessageListener(messageListener);


    }

    @Override
    protected void onStart() {
        super.onStart();
        printLogd("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
//        EMClient.getInstance().addClientListener(clientListener);
        refreshUIWithMessage();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        EMClient.getInstance().removeClientListener(clientListener);
        EMClient.getInstance().contactManager().removeContactListener(contactListener);
    }

    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (viewPager.getCurrentItem() == 0) {
                    //刷新会话列表消息
                    if (chatListFragment != null) {
                        chatListFragment.refreshUI();
                    }
                }
            }
        });
    }

    private void initTabList() {
        tabList.add("消息");
        tabList.add("联系人");
        tabList.add("设置");
    }

    private void initFragments() {
        if (chatListFragment == null) {
            chatListFragment = ChatListFragment.newInstance("chatList");
        }
        fragmentList.add(chatListFragment);
        fragmentList.add(FriendListFragment.newInstance("friend"));
        fragmentList.add(SettingFragment.newInstance("setting"));
    }

    @Override
    protected void initListener() {
        tabLayout.addOnTabSelectedListener(this);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        setTabSelected(tab);

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        setTabUnSelected(tab);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        setTabSelected(tab);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int unSelectedColor = ContextCompat.getColor(this, R.color.grey_500);
        int selectedColor = ContextCompat.getColor(this, R.color.colorPrimary);
        ArgbEvaluator evaluator = new ArgbEvaluator();
        int eSelected = (int) evaluator.evaluate(positionOffset, unSelectedColor, selectedColor);
        int eUnselected = (int) evaluator.evaluate(positionOffset, selectedColor, unSelectedColor);
        if (positionOffset > 0) {
            TabLayout.Tab tab0 = tabLayout.getTabAt(position);
            TabLayout.Tab tab1 = tabLayout.getTabAt(position + 1);
            View customView0 = tab0.getCustomView();
            View customView1 = tab1.getCustomView();
            ((TextView) customView0.findViewById(R.id.tabTitle)).setTextColor(eUnselected);
            ((ImageView) customView0.findViewById(R.id.tabIcon)).setColorFilter(eUnselected);
            ((TextView) customView1.findViewById(R.id.tabTitle)).setTextColor(eSelected);
            ((ImageView) customView1.findViewById(R.id.tabIcon)).setColorFilter(eSelected);
        }
    }

    @Override
    public void onPageSelected(int position) {
        viewPager.setCurrentItem(position, false);
        tvTitle.setText(tabList.get(position));

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setTabSelected(TabLayout.Tab tab) {
        View view = tab.getCustomView();
        TextView tabTitle = view.findViewById(R.id.tabTitle);
        ImageView tabIcon = view.findViewById(R.id.tabIcon);
        tabIcon.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));
        tabTitle.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        tvTitle.setText(tab.getText());
    }

    private void setTabUnSelected(TabLayout.Tab tab) {
        View view = tab.getCustomView();
        TextView tabTitle = view.findViewById(R.id.tabTitle);
        ImageView tabIcon = view.findViewById(R.id.tabIcon);
        tabIcon.setColorFilter(ContextCompat.getColor(this, R.color.grey_500));
        tabTitle.setTextColor(ContextCompat.getColor(this, R.color.grey_500));
    }

    private void registerBoradcastReceiver() {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constants.ACTION_GROUP_CHANAGED);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int currentItem = viewPager.getCurrentItem();
                if (currentItem == 0) {
//刷新会话列表
                    if (chatListFragment != null) {
                        chatListFragment.refreshUI();
                    }
                } else if (currentItem == 1) {
                    //刷新好友列表

                } else {

                }
            }
        };
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    EMMessageListener messageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            refreshUIWithMessage();
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> messages) {

        }

        @Override
        public void onMessageRecalled(List<EMMessage> messages) {
            refreshUIWithMessage();
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {

        }
    };

    public class ContactListener implements EMContactListener {

        @Override
        public void onContactAdded(String username) {

        }

        @Override
        public void onContactDeleted(String username) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
        }

        @Override
        public void onContactInvited(String username, String reason) {

        }

        @Override
        public void onFriendRequestAccepted(String username) {

        }

        @Override
        public void onFriendRequestDeclined(String username) {

        }
    }

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        button = findViewById(R.id.btn_alert);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////// 1. 布局文件转换为View对象
////                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
////                RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.dialog_layout, null);
////
////// 2. 新建对话框对象
////                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
////                //通过setView设置我们自己的布局
////                builder.setView(layout);
////                final AlertDialog dialog = builder.create();
////                dialog.show();
////                WindowManager wm = MainActivity.this.getWindowManager();
////                DisplayMetrics outMetrics = new DisplayMetrics();
////                wm.getDefaultDisplay().getMetrics(outMetrics);
////                int width2 = outMetrics.widthPixels;
////                int height2 = outMetrics.heightPixels;
////
////                //此处设置位置窗体大小
////                dialog.getWindow().setLayout(width2/2,height2/4);
//                EMClient.getInstance().login("TestH", "123456", new EMCallBack() {
//                    @Override
//                    public void onSuccess() {
//                        EMChatManager emChatManager = EMClient.getInstance().chatManager();
//                        emChatManager.loadAllConversations();
//                        Log.d("Main", "登录成功");
//                    }
//
//                    @Override
//                    public void onError(int code, String error) {
//                        Log.d("Main",code+"::"+error);
//                        Log.d("Main", "登录失败");
//                    }
//
//                    @Override
//                    public void onProgress(int progress, String status) {
//
//                    }
//                });
//            }
//        });
//    }
}
