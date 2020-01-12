package com.englishlearning.android;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.englishlearning.android.activities.OralCardActivity;
import com.englishlearning.android.fragments.CollectionFragment;
import com.englishlearning.android.fragments.PracticeFragment;
import com.englishlearning.android.fragments.UploadFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //声明ViewPager
    private ViewPager mViewPager;
    //适配器
    private FragmentPagerAdapter mAdapter;
    //装载Fragment的集合
    private List<Fragment> mFragments;
    //声明Tab
    private LinearLayout mTabPractice;
    private LinearLayout mTabCollect;
    private LinearLayout mTabUpload;
    //声明ImageButton
    private ImageButton mPracticeImg;
    private ImageButton mCollectImg;
    private ImageButton mUploadImg;


    private TextView mPracticeText;
    private TextView mCollectText;
    private TextView mUploadText;

    private MyApplication app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = (MyApplication)getApplication();
        initViews();//初始化控件
        initDatas();//初始化数据
        initEvents();//初始化事件

    }
    private void initEvents(){
        //设置Tab的点击事件
        mTabPractice.setOnClickListener(this);
        mTabCollect.setOnClickListener(this);
        mTabUpload.setOnClickListener(this);
    }
    private void initDatas(){
        mFragments = new ArrayList<>();
        //将四个Fragment加入集合中
        mFragments.add(new PracticeFragment());
        mFragments.add(new UploadFragment());
        mFragments.add(new CollectionFragment());

        //初始化适配器
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {//从集合中获取对应位置的Fragment
                return mFragments.get(position);
            }

            @Override
            public int getCount() {//获取集合中Fragment的总数
                return mFragments.size();
            }

        };
        //不要忘记设置ViewPager的适配器
        mViewPager.setAdapter(mAdapter);
        //设置ViewPager的切换监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            //页面滚动事件
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //页面选中事件
            @Override
            public void onPageSelected(int position) {
                //设置position对应的集合中的Fragment
                mViewPager.setCurrentItem(position);
                resetImgs();
                selectTab(position);
            }

            @Override
            //页面滚动状态改变事件
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    //初始化控件
    private void initViews(){
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager1);
        mTabPractice=(LinearLayout) findViewById(R.id.tab_practice);
        mTabCollect=(LinearLayout) findViewById(R.id.tab_collect);
        mTabUpload=(LinearLayout) findViewById(R.id.tab_upload);
        mPracticeImg=(ImageButton) findViewById(R.id.tab_practice_img);
        mCollectImg=(ImageButton) findViewById(R.id.tab_collect_img);
        mUploadImg=(ImageButton) findViewById(R.id.tab_upload_img);
        mPracticeText=(TextView) findViewById(R.id.tab_practice_text);
        mCollectText=(TextView) findViewById(R.id.tab_collect_text);
        mUploadText=(TextView) findViewById(R.id.tab_upload_text);
        mPracticeImg.setImageResource(R.drawable.practice_green);
    }

    @Override
    public void onClick(View v) {
        //先将ImageButton都设置成灰色
        resetImgs();
        switch (v.getId()) {
            case R.id.tab_practice:
                //设置viewPager的当前Tab
                mViewPager.setCurrentItem(0);
                //将当前Tab对应的ImageButton设置成绿色
                mPracticeImg.setImageResource(R.drawable.practice_green);
                break;
            case R.id.tab_upload:
                mViewPager.setCurrentItem(1);
                mUploadImg.setImageResource(R.drawable.upload_green);
                break;
            case R.id.tab_collect:
                mViewPager.setCurrentItem(2);
                mCollectImg.setImageResource(R.drawable.collection_green);
                break;
        }
        //根据点击的Tab切换不同的页面及设置对应的ImageButton为绿色
        switch (v.getId()) {
            case R.id.tab_practice:
                selectTab(0);
                break;
            case R.id.tab_upload:
                selectTab(1);
                break;
            case R.id.tab_collect:
                selectTab(2);
                break;
        }
    }
    private void selectTab(int i) {
        //根据点击的Tab设置对应的ImageButton为绿色
        switch (i) {
            case 0:
                mPracticeImg.setImageResource(R.drawable.practice_green);
                break;
            case 1:
                mUploadImg.setImageResource(R.drawable.upload_green);
                break;
            case 2:
                mCollectImg.setImageResource(R.drawable.collection_green);
                break;
        }
        //设置当前点击的Tab所对应的页面
        mViewPager.setCurrentItem(i);
    }
    //将ImageButton设置成灰色
    private void resetImgs () {
        mPracticeImg.setImageResource(R.drawable.practice_gray);
        mUploadImg.setImageResource(R.drawable.upload_gray);
        mCollectImg.setImageResource(R.drawable.collection_gray);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 100:
                boolean temp=true;
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        // PERMISSION_DENIED 这个值代表是没有授权，我们可以把被拒绝授权的权限显示出来
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED){
                            temp=false;
                            Toast.makeText(this, permissions[i] + "权限被拒绝了", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                app.setFlag(temp);
                if(temp){
                    Intent intent=new Intent(MainActivity.this, OralCardActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(this,  "请给予相应权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
