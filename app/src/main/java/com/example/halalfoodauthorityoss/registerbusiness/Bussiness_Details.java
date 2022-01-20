package com.example.halalfoodauthorityoss.registerbusiness;


import androidx.fragment.app.FragmentActivity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.halalfoodauthorityoss.BaseClass;
import com.example.halalfoodauthorityoss.model.Model;
import com.example.halalfoodauthorityoss.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Bussiness_Details extends FragmentActivity implements OnMapReadyCallback{

    Spinner districtSpinner,categorySpinner;
    TextView btnNext;
    TextInputEditText edtBusinessName,edtBusinessAdress;
    List<String> ListDistrictName;
    List<String> ListDistrictID;
    String districtID;
    String districtName;

    List<String> ListCategoryName;
    List<String> ListCategoryId;
    String categoryID;

    ImageView ic_back;
    GoogleMap mMap;
    TextInputEditText edtlocation;
    TextView btnSearch;
    double latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_details);

        initialization();
        displayDistrict();
        displayCategory();


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Location=edtlocation.getText().toString().trim();

                Geocoder geocoder=new Geocoder(Bussiness_Details.this);
                try {
                    mMap.clear();
                    List<Address> AddressList=geocoder.getFromLocationName(Location,1);
                    if (AddressList.size()!=0) {
                        Address Address = AddressList.get(0);
                        latitude = Address.getLatitude();
                        longitude = Address.getLongitude();
                        LatLng latLng = new LatLng(latitude, longitude);
                        mMap.addMarker(new MarkerOptions().position(latLng).title(Location));
                        // mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15), 2500, null);
                    }
                    else {
                        Toast.makeText(Bussiness_Details.this, "Invalid Address Location", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Bussiness_Details.this,Personal_Detail.class));
            }
        });

        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                districtID=ListDistrictID.get(i);
                districtName=ListDistrictName.get(i);
                if (!districtName.equals("Select District"))
                {
                    Geocoder geocoder=new Geocoder(Bussiness_Details.this);
                    try {
                        mMap.clear();
                        List<Address> AddressList=geocoder.getFromLocationName(districtName,1);
                        if (AddressList.size()!=0) {
                            Address Address = AddressList.get(0);
                            latitude = Address.getLatitude();
                            longitude = Address.getLongitude();
                            LatLng latLng = new LatLng(latitude, longitude);
                            mMap.addMarker(new MarkerOptions().position(latLng).title(districtName));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15), 2500, null);
                        }
                        else {
                            Toast.makeText(Bussiness_Details.this, "Invalid Address Location", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {}

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoryID=ListCategoryId.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Businessname=edtBusinessName.getText().toString().trim();
                String BusinessAddress=edtBusinessAdress.getText().toString().trim();
                if (Businessname.equals(""))
                {
                    Toast.makeText(Bussiness_Details.this, "Please Enter Business Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (categoryID.equals("0") && districtID.equals("0")){
                    Toast.makeText(Bussiness_Details.this, "Select Category and District", Toast.LENGTH_SHORT).show();
                    return;

                }
                if (BusinessAddress.equals(""))
                {
                    Toast.makeText(Bussiness_Details.this, "Please Enter Business Adddress", Toast.LENGTH_SHORT).show();
                    return;
                }
                Call<Model> call = BaseClass
                        .getInstance()
                        .getApi()
                        .GetName(Businessname);
                call.enqueue(new Callback<Model>() {
                    @Override
                    public void onResponse(Call<Model> call, Response<Model> response) {
                        Model model=response.body();
                        if (response.isSuccessful())
                        {
                            if (model.getSuccess().equals("0"))
                            {
                                Intent intent=new Intent(Bussiness_Details.this,Profile_Images.class);
                                intent.putExtra("name",getIntent().getStringExtra("name"));
                                intent.putExtra("cnic",getIntent().getStringExtra("cnic"));
                                intent.putExtra("contact",getIntent().getStringExtra("contact"));
                                intent.putExtra("fathername",getIntent().getStringExtra("fathername"));
                                intent.putExtra("businessname",Businessname);
                                intent.putExtra("categoryid",categoryID);
                                intent.putExtra("districtid",districtID);
                                intent.putExtra("businessaddress",BusinessAddress);
                                intent.putExtra("latitude",latitude);
                                intent.putExtra("longitude",longitude);
                                startActivity(intent);
                            }
                            else {
                                edtBusinessName.setError("Bussiness On that Name Already Registered");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Model> call, Throwable t) {
                        Log.d("errror3", call.request().toString());
                        Toast.makeText(Bussiness_Details.this, "No Response", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }


    private void initialization() {
      //  getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ic_back=findViewById(R.id.ic_back);
        btnNext=findViewById(R.id.btnNext);
        edtBusinessName=findViewById(R.id.edtBusinessName);
        edtBusinessAdress=findViewById(R.id.edtBusinessAddress);
      //  edtlatitude=findViewById(R.id.edtlatitude);
      //  edtlongitude=findViewById(R.id.edtlongitude);
        edtlocation=findViewById(R.id.edtlocation);
        btnSearch=findViewById(R.id.btnsearch);

        districtSpinner=findViewById(R.id.districtSpinner);
        ListDistrictName =  new ArrayList<String>();
        ListDistrictID =  new ArrayList<String>();
        ListDistrictName.add("Select District");
        ListDistrictID.add("0");

        categorySpinner=findViewById(R.id.categorySpinner);
        ListCategoryName =  new ArrayList<String>();
        ListCategoryId =  new ArrayList<String>();
        ListCategoryName.add("Select Category");
        ListCategoryId.add("0");
    }
    private void displayDistrict() {
        Call<List<Model>> call = BaseClass
                .getInstance()
                .getApi()
                .getDistrict();
        call.enqueue(new Callback<List<Model>>() {
            @Override
            public void onResponse(Call<List<Model>> call, Response<List<Model>> response) {
                List<Model> model=response.body();
                int size=model.size();
                if (!model.equals(null))
                {
                    for (int i=0;i<size;i++)
                    {
                        ListDistrictName.add(model.get(i).getcName());
                        ListDistrictID.add(model.get(i).getID());
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Bussiness_Details.this, android.R.layout.simple_spinner_item, ListDistrictName);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        districtSpinner.setAdapter(adapter);
                    }
                }
                else {
                    Toast.makeText(Bussiness_Details.this, "Empty Category", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Model>> call, Throwable t) {
                Toast.makeText(Bussiness_Details.this, "out", Toast.LENGTH_SHORT).show();
                Log.d("errror2", call.request().toString());

            }
        });
    }

    private void displayCategory() {
        Call<List<Model>> call = BaseClass
                .getInstance()
                .getApi()
                .getCategory();
        call.enqueue(new Callback<List<Model>>() {
            @Override
            public void onResponse(Call<List<Model>> call, Response<List<Model>> response) {
                List<Model> model=response.body();
                int size=model.size();
                if (!model.equals(null))
                {
                    for (int i=0;i<size;i++)
                    {

                        ListCategoryName.add(model.get(i).getcName());
                        ListCategoryId.add(model.get(i).getID());
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Bussiness_Details.this, android.R.layout.simple_spinner_item, ListCategoryName);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        categorySpinner.setAdapter(adapter);
                    }
                }
                else {
                    Toast.makeText(Bussiness_Details.this, "Empty Category", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Model>> call, Throwable t) {
                Toast.makeText(Bussiness_Details.this, "out", Toast.LENGTH_SHORT).show();
                Log.d("errror1", call.request().toString());

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(34.0043, 71.5448);
        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker In Peshawar"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

}