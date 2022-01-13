package com.example.halalfoodauthorityoss.registerbusiness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.halalfoodauthorityoss.CoreActivity;
import com.example.halalfoodauthorityoss.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Pattern;

public class Personal_Detail extends AppCompatActivity {

    TextView btnNext;
    TextInputEditText edtname,edtcnic,edtcontact,edtfathername;
    private static Pattern CNIC_PATTERN,AFG_CNIC_PATTERN;
    CheckBox checkBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);

        initialization();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=edtname.getText().toString().trim();
                String fathername=edtfathername.getText().toString().trim();
                String contact=edtcontact.getText().toString().trim();
                String cnic=edtcnic.getText().toString().trim();

                if (name.equals(""))
                {
                    edtname.setError("Please Enter Name");
                    return;
                }
                if (fathername.equals(""))
                {
                    edtname.setError("Please Enter Father Name");
                    return;
                }
                if(contact.length()<10 && contact.length()>11)
                {
                    edtcontact.setError("Invalid Mobile Number");
                    return;
                }
                if (cnic.equals(""))
                {
                    edtname.setError("Please Enter CNIC");
                    return;
                }
                if (!checkBox.isChecked())
                {
                    if (!CNIC_PATTERN.matcher(cnic).matches()) {
                        edtcnic.setError("Invalid CNIC Number");
                        return;
                    }
                }
                if (checkBox.isChecked())
                {
                    if (!AFG_CNIC_PATTERN.matcher(cnic).matches()) {
                        edtcnic.setError("Invalid CNIC Number");
                        return;
                    }
                }

                Intent intent=new Intent(Personal_Detail.this,Bussiness_Details.class);
                intent.putExtra("name",name);
                intent.putExtra("fathername",fathername);
                intent.putExtra("contact",contact);
                intent.putExtra("cnic",cnic);
                startActivity(intent);
            }
        });

    }

    private void initialization() {
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btnNext=findViewById(R.id.btnNext);
        edtname=findViewById(R.id.edtname);
        edtfathername=findViewById(R.id.edtfathername);
        edtcontact=findViewById(R.id.edtcontact);
        edtcnic=findViewById(R.id.edtcnic);
        checkBox=findViewById(R.id.checkBox);

        CNIC_PATTERN= Pattern.compile("^" +"[0-9]{5}-[0-9]{7}-[0-9]{1}"+"$");
        AFG_CNIC_PATTERN=Pattern.compile("^" +"[0-9]{4}-[0-9]{4}-[0-9]{5}"+"$");
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Personal_Detail.this, CoreActivity.class));
        finish();
    }
}