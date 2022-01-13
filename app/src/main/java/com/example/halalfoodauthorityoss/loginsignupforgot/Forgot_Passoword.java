package com.example.halalfoodauthorityoss.loginsignupforgot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.halalfoodauthorityoss.BaseClass;
import com.example.halalfoodauthorityoss.R;
import com.example.halalfoodauthorityoss.model.Model;
import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Forgot_Passoword extends AppCompatActivity {

    TextView btnverify;
    TextInputEditText edtcnic;
    CheckBox checkBox;
    private static Pattern CNIC_PATTERN,AFG_CNIC_PATTERN;
    ImageView ic_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cnic__verification);

        initi();

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Forgot_Passoword.this,Login.class));
                finish();
            }
        });

        btnverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String cnic=edtcnic.getText().toString().trim();

                if (cnic.equals(""))
                {
                    edtcnic.setError("Please Enter CNIC ");
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

                Call<Model> call = BaseClass
                        .getInstance()
                        .getApi()
                        .Forgot(cnic);

                call.enqueue(new Callback<Model>() {
                    @Override
                    public void onResponse(Call<Model> call, Response<Model> response) {
                        Model model=response.body();
                        if (!model.equals(null))
                        {
                            if (model.getSuccess().equals("Valid user"))
                            {
                                Toast.makeText(Forgot_Passoword.this, "Mobile Number:"+model.getC_mobile(), Toast.LENGTH_SHORT).show();
                                Toast.makeText(Forgot_Passoword.this, "Password:"+model.getCpass(), Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(Forgot_Passoword.this, "CNIC Doesn't Found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<Model> call, Throwable t) {
                        Toast.makeText(Forgot_Passoword.this, "I am Out", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void initi() {
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btnverify=findViewById(R.id.btnverify);
        edtcnic=findViewById(R.id.edtcnic);
        checkBox=findViewById(R.id.checkBox);
        ic_back=findViewById(R.id.ic_back);

        CNIC_PATTERN= Pattern.compile("^" +"[0-9]{5}-[0-9]{7}-[0-9]{1}"+"$");
        AFG_CNIC_PATTERN=Pattern.compile("^" +"[0-9]{4}-[0-9]{4}-[0-9]{5}"+"$");
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Forgot_Passoword.this,Login.class));
        finish();
    }
}