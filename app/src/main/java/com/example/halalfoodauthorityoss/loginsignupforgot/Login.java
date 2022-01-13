package com.example.halalfoodauthorityoss.loginsignupforgot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.halalfoodauthorityoss.BaseClass;
import com.example.halalfoodauthorityoss.CoreActivity;
import com.example.halalfoodauthorityoss.R;
import com.example.halalfoodauthorityoss.model.AppData;
import com.example.halalfoodauthorityoss.model.LoginResponse;
import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  Login extends AppCompatActivity {

    TextView signup,forgot,btnlogin;
    TextInputEditText edtcnic,edtpassword;
    CheckBox checkBox;
    private static Pattern CNIC_PATTERN,AFG_CNIC_PATTERN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialization();

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String cnic=edtcnic.getText().toString().trim();
                String password=edtpassword.getText().toString().trim();

                if (cnic.equals(""))
                {
                 edtcnic.setError("Please Enter CNIC ");
                 return;
                }
                if (password.equals(""))
                {
                    edtpassword.setError("Please Enter Password");
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

                Call<LoginResponse> call = BaseClass
                        .getInstance()
                        .getApi()
                        .Login(cnic,password);

                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        LoginResponse loginResponse=response.body();
                        if (!loginResponse.equals(null))
                        {
                            if (!loginResponse.success.equals("0"))
                            {
                                AppData.name=loginResponse.user_data.getName();
                                AppData.cnic=loginResponse.user_data.getCnic();
                                AppData.id=loginResponse.user_data.getUser_id();
                                Toast.makeText(Login.this, "CNIC:"+cnic, Toast.LENGTH_SHORT).show();
                                Toast.makeText(Login.this, "ID:"+loginResponse.user_data.getUser_id(), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Login.this,CoreActivity.class));
                                finish();
                            }
                            else
                                {
                                    Toast.makeText(Login.this, "Invalid CNIC or Password", Toast.LENGTH_SHORT).show();
                                }
                        }
                    }
                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                    }
                });
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Sign_Up.class));
                finish();
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Forgot_Passoword.class));
                finish();
            }
        });
    }

    public void initialization()
    {
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        signup=findViewById(R.id.btnsignup);
        forgot=findViewById(R.id.forgot);
        edtcnic=findViewById(R.id.edtcnic);
        edtpassword=findViewById(R.id.edtpassword);
        btnlogin=findViewById(R.id.btnlogin);
        checkBox=findViewById(R.id.checkBox);

        CNIC_PATTERN= Pattern.compile("^" +"[0-9]{5}-[0-9]{7}-[0-9]{1}"+"$");
        AFG_CNIC_PATTERN=Pattern.compile("^" +"[0-9]{4}-[0-9]{4}-[0-9]{5}"+"$");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}