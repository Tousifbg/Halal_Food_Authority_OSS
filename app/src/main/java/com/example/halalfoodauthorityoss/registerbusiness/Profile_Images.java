package com.example.halalfoodauthorityoss.registerbusiness;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Path;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.halalfoodauthorityoss.BaseClass;
import com.example.halalfoodauthorityoss.CoreActivity;
import com.example.halalfoodauthorityoss.model.Model;
import com.example.halalfoodauthorityoss.R;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile_Images extends AppCompatActivity {

   // CircleImageView profileImage;
    ImageView cnicImage,profileImage;
    Button btnNext;
    EditText edtCNIC;
    String selectedImagePath,selectedImagePath1;
    File profileFile,cnicFile;
    String imageone,imagetwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_images);
        
        initialization();

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        cnicImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage1();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();
            }
        });
    }

    private void initialization() {
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        profileImage=findViewById(R.id.profileImage);
        cnicImage=findViewById(R.id.imgcnic);
        btnNext=findViewById(R.id.btnNext);
        edtCNIC=findViewById(R.id.edtcnic);
    }

    private void selectImage() {
        final CharSequence[] options = { "Take from Camera", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(Profile_Images.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take from Camera"))
                {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickPhoto.setType("image/*");
                    startActivityForResult(pickPhoto , 1);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectImage1() {
        final CharSequence[] options = { "Take from Camera", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(Profile_Images.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take from Camera"))
                {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 2);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickPhoto.setType("image/*");
                    startActivityForResult(pickPhoto , 3);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                   if (requestCode == 0)
                       if(resultCode == RESULT_OK) {
                           {
                               Bitmap photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
                               File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                               final int min = 200000;
                               final int max = 800000;
                               final int random = new Random().nextInt((max - min) + 1) + min;
                               String name = String.valueOf(random);
                               try {
                                   profileFile = File.createTempFile(
                                           "PNG_",
                                           ".png",
                                           dir
                                   );
                               } catch (IOException e) {
                                   e.printStackTrace();
                               }

                               if (profileFile != null) {
                                   FileOutputStream fout;
                                   try {
                                       fout = new FileOutputStream(profileFile);
                                       photo.compress(Bitmap.CompressFormat.PNG, 100, fout);
                                       fout.flush();
                                   } catch (Exception e) {
                                       e.printStackTrace();
                                   }
                                   Toast.makeText(this, "" + profileFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                                   Toast.makeText(this, "" + profileFile.getName(), Toast.LENGTH_SHORT).show();
                                   profileImage.setImageURI(Uri.parse(profileFile.getAbsolutePath()));
                               }
                           }
                       }
                break;
            case 1:
                if(resultCode == RESULT_OK){
                    if (requestCode == 1) {
                        Uri selectedImageUri = imageReturnedIntent.getData();
                        selectedImagePath = getRealPathFromURIForGallery(selectedImageUri);
                        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                        Bitmap b = BitmapFactory.decodeFile(selectedImagePath);
                        Bitmap out = getResizedBitmap(b, 1536, selectedImagePath);
                        final int min = 200000;
                        final int max = 800000;
                        final int random = new Random().nextInt((max - min) + 1) + min;
                        String name = random + ".jpg";
                        profileFile = new File(dir, name);
                        FileOutputStream fOut;
                        try {
                            fOut = new FileOutputStream(profileFile);
                            out.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                            fOut.flush();
                            fOut.close();
                            b.recycle();
                            out.recycle();
                        } catch (Exception e) {
                        }
                        Toast.makeText(this, "" + profileFile, Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "" + profileFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "" + profileFile.getName(), Toast.LENGTH_SHORT).show();
                        profileImage.setImageURI(Uri.parse(profileFile.getAbsolutePath()));
                    }
                }
                break;
            case 2:
                if (requestCode == 2)
                    if(resultCode == RESULT_OK){{
                    Bitmap photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                    final int min = 200000;
                    final int max = 800000;
                    final int random = new Random().nextInt((max - min) + 1) + min;
                    String name = String.valueOf(random);
                    try {
                        cnicFile = File.createTempFile(
                                "PNG_",
                                ".png",
                                dir
                        );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (cnicFile != null) {
                        FileOutputStream fout;
                        try {
                            fout = new FileOutputStream(cnicFile);
                            photo.compress(Bitmap.CompressFormat.PNG, 100, fout);
                            fout.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(this, "" + cnicFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "" + cnicFile.getName(), Toast.LENGTH_SHORT).show();
                        cnicImage.setImageURI(Uri.parse(cnicFile.getAbsolutePath()));
                    }
                }}
                break;
            case 3:
                if(resultCode == RESULT_OK){
                    if (requestCode == 3) {
                        Uri selectedImageUri = imageReturnedIntent.getData();
                        selectedImagePath1 = getRealPathFromURIForGallery(selectedImageUri);
                        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                        Bitmap b = BitmapFactory.decodeFile(selectedImagePath1);
                        Bitmap out = getResizedBitmap(b, 1536, selectedImagePath1);
                        final int min = 200000;
                        final int max = 800000;
                        final int random = new Random().nextInt((max - min) + 1) + min;
                        String name = random + ".jpg";
                        cnicFile = new File(dir, name);
                        FileOutputStream fOut;
                        try {
                            fOut = new FileOutputStream(cnicFile);
                            out.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                            fOut.flush();
                            fOut.close();
                            b.recycle();
                            out.recycle();
                        } catch (Exception e) {
                        }
                        Toast.makeText(this, "" + cnicFile, Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "" + cnicFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "" + cnicFile.getName(), Toast.LENGTH_SHORT).show();
                        cnicImage.setImageURI(Uri.parse(cnicFile.getAbsolutePath()));
                    }
                }
             break;
        }
    }

    public String getRealPathFromURIForGallery(Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.getContentResolver().query(uri, projection, null,
                null, null);
        if (cursor != null) {
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        assert false;
        cursor.close();
        return uri.getPath();
    }

    private void uploadFile() {

        if (!profileFile.equals("") || !cnicFile.equals(""))
        {
            File PROFILEImage = new File(profileFile.getAbsolutePath());
            File CNICImage = new File(cnicFile.getAbsolutePath());
            RequestBody requestBodyPROFILE = RequestBody.create(MediaType.parse("multipart/form-data"), PROFILEImage);
            RequestBody requestBodyCNIC = RequestBody.create(MediaType.parse("multipart/form-data"), CNICImage);
            MultipartBody.Part profileimageFile = MultipartBody.Part.createFormData("profile_image", PROFILEImage.getName(), requestBodyPROFILE);
            MultipartBody.Part cnicimageFile = MultipartBody.Part.createFormData("cnic_image", CNICImage.getName(), requestBodyCNIC);

            Call<Model> call = BaseClass
                    .getInstance()
                    .getApi()
                    .Add_Owner(getIntent().getStringExtra("name"),getIntent().getStringExtra("fathername"),getIntent().getStringExtra("cnic"),
                            getIntent().getStringExtra("contact"),profileimageFile,getIntent().getStringExtra("businessaddress"),
                            getIntent().getStringExtra("Businessname")
                            ,getIntent().getStringExtra("categoryid"),getIntent().getStringExtra("latitude"),getIntent().getStringExtra("longitude"),cnicimageFile,"male",getIntent().getStringExtra("districtid"),"owner");
            call.enqueue(new Callback<Model>() {
                @Override
                public void onResponse(Call<Model> call, Response<Model> response) {
                    if (response.isSuccessful()) {
                        Log.d("datashowhere", response.body().toString());
                        Toast.makeText(Profile_Images.this, "Business Registered", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Profile_Images.this, CoreActivity.class));
                    } else {
                        Log.v("Response", response.toString());
                    }
                }
                @Override
                public void onFailure(Call<Model> call, Throwable t) {
                    Log.d("errror", call.toString());
                    Log.d("errror1", call.request().toString());
                }
            });
        }
        else
        {
            Toast.makeText(this, "Please Upload Images", Toast.LENGTH_SHORT).show();
        }

    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize,String photoPath) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        Bitmap resizedBitmap1= RotateImage(photoPath,Bitmap.createScaledBitmap(image, width, height, true));
        return resizedBitmap1;
    }

    private Bitmap RotateImage(String photoPath, Bitmap bitmap) {
        Bitmap rotatedBitmap = null;
        try {
            ExifInterface ei = null;
            ei = new ExifInterface(photoPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
            }
        }
        catch (Exception e){}
        return rotatedBitmap;
    }

    public static Bitmap rotateImage(Bitmap source, float angle){
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                    matrix, true);
        }}