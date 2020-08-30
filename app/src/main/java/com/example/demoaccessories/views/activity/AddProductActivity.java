package com.example.demoaccessories.views.activity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import com.example.demoaccessories.Interface.ProgressRequestBody;
import com.example.demoaccessories.Interface.SharedPreferencesToken;
import com.example.demoaccessories.R;
import com.example.demoaccessories.api.ApiService;
import com.example.demoaccessories.api.RetrofitClient;
import com.example.demoaccessories.model.ProductList;
import com.example.demoaccessories.model.UserInfo;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.ChooserType;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity implements View.OnClickListener, ProgressRequestBody.UploadCallbacks {

    Bitmap mBitmap;
    private static final int CAMERA_REQUEST_CODE = 7500;
    private static final int CHOOSER_PERMISSIONS_REQUEST_CODE = 7459;
    private static final int DOCUMENTS_REQUEST_CODE = 7501;
    byte[] byteArray;
    String name;
    EasyImage easyImage;

    ImageView img_back, img_add_picture;
    EditText edt_name_product, edt_price_product, edt_address, edt_description_product;
    Spinner spn_category_product;
    Button btn_post_product;
    String arr[] = {"Chọn một hạng mục", "Trang sức & phụ kiện", "Điện thoại", "Đồng hồ"};
    ApiService apiService;
    String name_seller, img_seller, currentDateandTime;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        initView();
        setAdapterSpinner();
        getSharedPreferencesToken(new SharedPreferencesToken(getApplicationContext()).getUser());
        setUpCamera();
    }

    private void getSharedPreferencesToken(String user) {
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getTokenAPI(user).enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                name_seller = response.body().getFullName();
                img_seller = response.body().getImage();
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {

            }
        });
    }

    private void setUpCamera() {
        easyImage = new EasyImage.Builder(AddProductActivity.this)
                .setChooserTitle("Pick media")
                .setCopyImagesToPublicGalleryFolder(false)
                .setChooserType(ChooserType.CAMERA_AND_GALLERY)
                .setFolderName("Give And Take")
                .allowMultiple(true)
                .build();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CHOOSER_PERMISSIONS_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            easyImage.openChooser(AddProductActivity.this);
        } else if (requestCode == CAMERA_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            easyImage.openCameraForImage(AddProductActivity.this);
        }  else if (requestCode == DOCUMENTS_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            easyImage.openDocuments(AddProductActivity.this);
        }
    }

    private void initView() {
        progressBar = findViewById(R.id.progressBar);
        img_back = findViewById(R.id.img_back);
        img_add_picture = findViewById(R.id.img_add_picture);
        edt_name_product = findViewById(R.id.edt_name_product);
        edt_price_product = findViewById(R.id.edt_price_product);
        edt_address = findViewById(R.id.edt_address);
        edt_description_product = findViewById(R.id.edt_description_product);
        spn_category_product = findViewById(R.id.spn_category_product);
        btn_post_product = findViewById(R.id.btn_post_product);
        setOnclick();
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        easyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onMediaFilesPicked(MediaFile[] imageFiles, MediaSource source) {
                if (resultCode == Activity.RESULT_OK) {
                    String filePath = imageFiles[0].getFile().getPath();
                    name = imageFiles[0].getFile().getName();
                    if (filePath != null) {
                        mBitmap = BitmapFactory.decodeFile(filePath);
                        getByteArrayInBackground();
                        img_add_picture.setImageBitmap(mBitmap);
                    }
                }
            }

            private void getByteArrayInBackground() {
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        mBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
                        byteArray = bos.toByteArray();
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                            }
//                        });
                    }
                };
                thread.start();
            }
            @Override
            public void onImagePickerError(@NonNull Throwable error, @NonNull MediaSource source) {
                error.printStackTrace();
            }

            @Override
            public void onCanceled(@NonNull MediaSource source) {

            }
        });
    }

    private void chooseCamera() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn ảnh");
        builder.setMessage("Chọn ảnh từ");
        builder.setCancelable(false);
        builder.setNeutralButton("HUỶ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("MÁY ẢNH", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String[] necessaryPermissions = new String[]{Manifest.permission.CAMERA};
                if (arePermissionsGranted(necessaryPermissions)) {
                    easyImage.openCameraForImage(AddProductActivity.this);
                } else {
                    requestPermissionsCompat(necessaryPermissions, CAMERA_REQUEST_CODE);
                }
            }
        });
        builder.setNegativeButton("THƯ MỤC", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String[] necessaryPermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
                if (arePermissionsGranted(necessaryPermissions)) {
                    easyImage.openDocuments(AddProductActivity.this);
                } else {
                    requestPermissionsCompat(necessaryPermissions, DOCUMENTS_REQUEST_CODE);
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private boolean arePermissionsGranted(String[] necessaryPermissions) {
        for (String permission : necessaryPermissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }
    private void requestPermissionsCompat(String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(AddProductActivity.this, permissions, requestCode);
    }

    private void setOnclick() {
        img_back.setOnClickListener(this);
        img_add_picture.setOnClickListener(this);
        btn_post_product.setOnClickListener(this);
    }

    private void setAdapterSpinner() {
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arr);
        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_category_product.setAdapter(stringArrayAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                setResult(RESULT_OK, new Intent());
                finish();
                break;
            case R.id.img_add_picture:
                chooseCamera();
                break;
            case R.id.btn_post_product:
                addNewProduct(v);
                break;
        }
    }

    private void addNewProduct(View v) {
        String name_product = edt_name_product.getText().toString().trim();
        String price = edt_price_product.getText().toString().trim();
        String address = edt_address.getText().toString().trim();
        if (name_product.isEmpty() || price.isEmpty() || address.isEmpty()){
            Snackbar.make(v, "Vui lòng nhập đầy đủ thông tin", Snackbar.LENGTH_LONG).show();
        } else if (spn_category_product.getSelectedItem() == "Chọn một hàng mục"){
            Snackbar.make(v, "Vui lòng chọn hạng mục", Snackbar.LENGTH_LONG).show();
        }  else {
            progressBar.setVisibility(View.VISIBLE);
            addProduct(v);

        }
    }

    private void addProduct(final View v) {
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        if(byteArray != null) {
            try {
                File filesDir = getApplicationContext().getFilesDir();
                File file = new File(filesDir, "product"+ Calendar.getInstance().getTimeInMillis() + ".png");
                FileOutputStream fos = null;
                fos = new FileOutputStream(file);
                fos.write(byteArray);
                fos.flush();
                fos.close();
                ProgressRequestBody fileBody = new ProgressRequestBody(file, this);
                RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload");
                MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), fileBody);
                //getDateTime
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy 'lúc' HH:mm");
                currentDateandTime = sdf.format(new Date());
                RequestBody seller = RequestBody.create(MediaType.parse("text/plain"), name_seller);
                RequestBody name_product = RequestBody.create(MediaType.parse("text/plain"), edt_name_product.getText().toString());
                RequestBody price = RequestBody.create(MediaType.parse("text/plain"), edt_price_product.getText().toString());
                RequestBody address = RequestBody.create(MediaType.parse("text/plain"), edt_address.getText().toString());
                RequestBody category = RequestBody.create(MediaType.parse("text/plain"), spn_category_product.getSelectedItem().toString());
                RequestBody description = RequestBody.create(MediaType.parse("text/plain"), edt_description_product.getText().toString());
                RequestBody imageSeller = RequestBody.create(MediaType.parse("text/plain"), img_seller);
                RequestBody createAt = RequestBody.create(MediaType.parse("text/plain"), currentDateandTime);
                //add product
                apiService.addProduct(seller, name_product, price, address, category, description, body, name, imageSeller, createAt).enqueue(new Callback<ProductList>() {
                    @Override
                    public void onResponse(Call<ProductList> call, Response<ProductList> response) {
                        progressBar.setVisibility(View.GONE);
                        Snackbar.make(v, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                        clearText();
                    }

                    @Override
                    public void onFailure(Call<ProductList> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Log.d("TAG", "onFailure: " + t.getMessage());
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearText() {
        edt_address.getText().clear();
        edt_description_product.getText().clear();
        edt_price_product.getText().clear();
        edt_name_product.getText().clear();
        img_add_picture.setImageResource(R.drawable.ic_add_picture);
    }

    @Override
    public void onProgressUpdate(int percentage) {
    }

    @Override
    public void onError() {
    }

    @Override
    public void onFinish() {
    }

    @Override
    public void uploadStart() {
    }
}