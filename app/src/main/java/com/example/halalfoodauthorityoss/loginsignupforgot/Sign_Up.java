package com.example.halalfoodauthorityoss.loginsignupforgot;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.halalfoodauthorityoss.BaseClass;
import com.example.halalfoodauthorityoss.R;
import com.example.halalfoodauthorityoss.model.Model;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Sign_Up extends AppCompatActivity {

    private static Pattern CNIC_PATTERN, AFG_CNIC_PATTERN, PASSWORD_PATTERN;
    TextInputEditText edtname, edtcnic, edtnumber, edtpassword, edtconfirmpassword;
    Spinner distspinner;
    TextView register;
    CheckBox checkBox;
    String name, cnic="", number, password, confirmpassword;
    ImageView ic_back;
    List<String> ListDistrictName;
    List<String> ListDistrictID;
    String districtID;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up);

        initialization();
        displayDistrict();

        edtcnic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String cnicNo = edtcnic.getText().toString().trim();
                if (!checkBox.isChecked()) {
                    if (cnicNo.length() == 13) {
                        cnic = cnicNo.substring(0, 5) + "-" + cnicNo.substring(5, 12) + "-" + cnicNo.substring(12);
                        if (CNIC_PATTERN.matcher(cnic).matches()) {
                            return;
                        } else {
                            edtcnic.setError("Invalid Cnic");
                        }
                    } else if (cnicNo.length() > 13) {
                        edtcnic.setError("Invalid Cnic");
                        return;
                    }
                }
                if (checkBox.isChecked()) {
                    if (cnicNo.length() == 13) {
                        cnic = cnicNo.substring(0, 4) + "-" + cnicNo.substring(4, 8) + "-" + cnicNo.substring(8, 13);
                        if (AFG_CNIC_PATTERN.matcher(cnic).matches()) {
                            return;
                        } else {
                            edtcnic.setError("Invalid Cnic");
                        }
                    } else if (cnicNo.length() > 13) {
                        edtcnic.setError("Invalid Cnic");
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Sign_Up.this, Login.class));
                finish();
            }
        });

        distspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                districtID = ListDistrictID.get(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = edtname.getText().toString().trim();
                String cnicNo = edtcnic.getText().toString().trim();
                number = edtnumber.getText().toString().trim();
                password = edtpassword.getText().toString().trim();
                confirmpassword = edtconfirmpassword.getText().toString().trim();

                if (cnicNo.length()<13 || cnicNo.length()>13 || cnicNo.equals(""))
                {
                    edtcnic.setError("Invalid CNIC");
                    return;
                }
                if (!checkBox.isChecked()) {
                    cnic = cnicNo.substring(0, 5) + "-" + cnicNo.substring(5, 12) + "-" + cnicNo.substring(12);
                } else {
                    cnic = cnicNo.substring(0, 4) + "-" + cnicNo.substring(4, 8) + "-" + cnicNo.substring(8, 13);
                }

                if (!checkBox.isChecked()) {
                    if (!CNIC_PATTERN.matcher(cnic).matches()) {
                        edtcnic.setError("Invalid CNIC Number");
                        return;
                    }
                }
                if (checkBox.isChecked()) {
                    if (!AFG_CNIC_PATTERN.matcher(cnic).matches()) {
                        edtcnic.setError("Invalid CNIC Number");
                        return;
                    }
                }
                if (name.equals("")) {
                    edtname.setError("Please Enter Name");
                    return;
                }
                if (number.length() < 10 || number.length() > 11) {
                    edtnumber.setError("Invalid Mobile Number");
                    return;
                }
                if (districtID == "0") {
                    Toast.makeText(Sign_Up.this, "Select District", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!PASSWORD_PATTERN.matcher(password).matches()) {
                    edtpassword.setError("Minimum Length 6, Must Contain Latters and Digits");
                    return;
                }
                if (!confirmpassword.equals(password)) {
                    edtconfirmpassword.setError("Password Does Not Matched");
                    return;
                }
                progressDialog.show();

                int Did = Integer.parseInt(districtID);
                Call<Model> call = BaseClass
                        .getInstance()
                        .getApi()
                          .Sign_Up(cnic,password,name,Did,number);

                call.enqueue(new Callback<Model>() {
                    @Override
                    public void onResponse(Call<Model> call, Response<Model> response) {
                        Model model = response.body();
                        if (response.isSuccessful()) {
                            if (model.getSuccess().equals("0")) {
                                progressDialog.dismiss();
                                DialogBOX();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(Sign_Up.this, "This User Already Registered", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Model> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(Sign_Up.this, "out", Toast.LENGTH_SHORT).show();
                        Log.d("errror", call.toString());
                        Log.d("errror1", call.request().toString());
                    }
                });
            }
        });

    }

    private void DialogBOX() {
        AlertDialog alertDialog = new AlertDialog.Builder(Sign_Up.this).create();
        alertDialog.setTitle("Your Profile has been Created!");
        alertDialog.setCancelable(false);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Sign_Up.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        alertDialog.show();
    }

    private void initialization() {
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        edtname = findViewById(R.id.edtname);
        edtcnic = findViewById(R.id.edtcnic);
        edtnumber = findViewById(R.id.edtnumber);
        edtpassword = findViewById(R.id.edtpassword);
        edtconfirmpassword = findViewById(R.id.edtconfirmpassword);
        register = findViewById(R.id.btnregister);
        checkBox = findViewById(R.id.checkBox);
        distspinner = findViewById(R.id.distspinner);
        ic_back = findViewById(R.id.ic_back);

        ListDistrictName = new ArrayList<String>();
        ListDistrictID = new ArrayList<String>();
        ListDistrictName.add("Select District");
        ListDistrictID.add("0");

        PASSWORD_PATTERN =
                Pattern.compile("^" +
                        "(?=.*[0-9])" +         //at least 1 digit
                        "(?=.*[a-zA-Z])" +      //any letter
                        "(?=\\S+$)" +           //no white spaces
                        ".{6,}" +               //at least 6 characters
                        "$");
        CNIC_PATTERN = Pattern.compile("^" + "[0-9]{5}-[0-9]{7}-[0-9]{1}" + "$");
        AFG_CNIC_PATTERN = Pattern.compile("^" + "[0-9]{4}-[0-9]{4}-[0-9]{5}" + "$");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Sign_Up.this, Login.class));
        finish();
    }

    private void displayDistrict() {
        Call<List<Model>> call = BaseClass
                .getInstance()
                .getApi()
                .getDistrict();
        call.enqueue(new Callback<List<Model>>() {
            @Override
            public void onResponse(Call<List<Model>> call, Response<List<Model>> response) {
                List<Model> model = response.body();
                int size = model.size();
                if (!model.equals(null)) {
                    for (int i = 0; i < size; i++) {

                        ListDistrictName.add(model.get(i).getcName());
                        ListDistrictID.add(model.get(i).getID());
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Sign_Up.this, android.R.layout.simple_spinner_item, ListDistrictName);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        distspinner.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(Sign_Up.this, "Empty Category", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Model>> call, Throwable t) {
                Toast.makeText(Sign_Up.this, "out", Toast.LENGTH_SHORT).show();

            }
        });
    }
}