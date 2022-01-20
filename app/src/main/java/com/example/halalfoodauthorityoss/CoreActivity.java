package com.example.halalfoodauthorityoss;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import com.example.halalfoodauthorityoss.adapter.PageAdapter;
import com.example.halalfoodauthorityoss.complaint.Complaint;
import com.example.halalfoodauthorityoss.loginsignupforgot.Login;
import com.example.halalfoodauthorityoss.productregistration.ProductRegistration;
import com.example.halalfoodauthorityoss.registerbusiness.Personal_Detail;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;


public class CoreActivity extends AppCompatActivity {

    FloatingActionButton fabMain,fabFeedback, fabComplaint, fabTraining, fabRegisterProduct,fabRegisterBusiness;
 //   LinearLayout layoutFeedback, layoutComplaint, layoutTraining, layoutRegisterProduct, layoutRegisterBusiness;
    boolean isFABOpen = false;
    LinearLayout tabMenu;
    SharedPreferences sharedPreferences = null;
    SharedPreferences.Editor editor;

    TabLayout layout;
    ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.ic_location,
            R.drawable.ic_location,
            R.drawable.ic_add,
            R.drawable.ic_location};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core);

        initialization();

        layout=findViewById(R.id.layout);
        viewPager=findViewById(R.id.show);

        for (int i = 0; i < 4; i++) {
            layout.getTabAt(i).setIcon(tabIcons[i]);
        }

        final PageAdapter pageAdapter=new PageAdapter(getSupportFragmentManager(),layout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition()==0 || tab.getPosition()==1 || tab.getPosition()==2 || tab.getPosition()==3 || tab.getPosition()==4){
                    pageAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(layout));
        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    fabMain.animate().rotation(getResources().getDimension(R.dimen.standard_75));
                    showFABMenu();
                }else{
                    fabMain.animate().rotation(getResources().getDimension(R.dimen.standard_00));
                    closeFABMenu();
                }
            }
        });
        fabRegisterBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CoreActivity.this, Personal_Detail.class));
            }
        });
        fabRegisterProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CoreActivity.this, ProductRegistration.class));
            }
        });
        fabComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CoreActivity.this, Complaint.class));
            }
        });
        fabFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogOut();
            }
        });
    }

    private void LogOut() {
        sharedPreferences=getSharedPreferences("Profile",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(CoreActivity.this,Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void initialization() {
       /* layoutFeedback = (LinearLayout) findViewById(R.id.layoutFeedback);
        layoutComplaint = (LinearLayout) findViewById(R.id.layoutComplaint);
        layoutTraining = (LinearLayout) findViewById(R.id.layoutTraining);
        layoutRegisterProduct = (LinearLayout) findViewById(R.id.layoutRegisterProduct);
        layoutRegisterBusiness = (LinearLayout) findViewById(R.id.layoutRegisterBusiness);*/

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        tabMenu = (LinearLayout) findViewById(R.id.tabmenu);
        fabMain = (FloatingActionButton) findViewById(R.id.fabMain);
        fabFeedback = (FloatingActionButton) findViewById(R.id.fabFeedback);
        fabComplaint = (FloatingActionButton) findViewById(R.id.fabCompliant);
        fabTraining = (FloatingActionButton) findViewById(R.id.fabTraining);
        fabRegisterProduct = (FloatingActionButton) findViewById(R.id.fabRegisterProduct);
        fabRegisterBusiness = (FloatingActionButton) findViewById(R.id.fabRegisterBusiness);

      /*  one=findViewById(R.id.one);
        two=findViewById(R.id.two);
        four=findViewById(R.id.four);
        five=findViewById(R.id.five);*/
    }

    private void showFABMenu(){
        isFABOpen=true;
        tabMenu.setVisibility(View.VISIBLE);
       /* layoutFeedback.setVisibility(View.VISIBLE);
        layoutComplaint.setVisibility(View.VISIBLE);
        layoutTraining.setVisibility(View.VISIBLE);
        layoutRegisterProduct.setVisibility(View.VISIBLE);
        layoutRegisterBusiness.setVisibility(View.VISIBLE);*/
    }

    private void closeFABMenu(){
        isFABOpen=false;
        tabMenu.setVisibility(View.INVISIBLE);
       /* layoutFeedback.setVisibility(View.GONE);
        layoutComplaint.setVisibility(View.GONE);
        layoutTraining.setVisibility(View.GONE);
        layoutRegisterProduct.setVisibility(View.GONE);
        layoutRegisterBusiness.setVisibility(View.GONE);*/
}
    @Override
    public void onBackPressed() {
        if (isFABOpen) {
            closeFABMenu();
        } else {
            super.onBackPressed();
            finish();
        }
    }
}