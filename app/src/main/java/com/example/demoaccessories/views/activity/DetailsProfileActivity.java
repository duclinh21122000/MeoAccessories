package com.example.demoaccessories.views.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.demoaccessories.Interface.ProgressRequestBody;
import com.example.demoaccessories.R;
import com.example.demoaccessories.api.ApiService;
import com.example.demoaccessories.api.RetrofitClient;
import com.example.demoaccessories.model.ProductList;
import com.example.demoaccessories.model.User;
import com.example.demoaccessories.model.UserInfo;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
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

import static com.example.demoaccessories.api.RetrofitClient.URL;

public class DetailsProfileActivity extends AppCompatActivity implements View.OnClickListener, ProgressRequestBody.UploadCallbacks {
    Bitmap mBitmap;
    private static final int CAMERA_REQUEST_CODE = 7500;
    private static final int CHOOSER_PERMISSIONS_REQUEST_CODE = 7459;
    private static final int DOCUMENTS_REQUEST_CODE = 7501;
    byte[] byteArray;
    String name;
    EasyImage easyImage;
    //
    ImageView img_back, img_update_user;
    CircleImageView img_user;
    EditText edt_fullName, edt_email, edt_phone, edt_address;
    String email;
    ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_profile);
        initView();
        getData();
        getDataByEmail();
        setUpCamera();
    }

    private void setUpCamera() {
        easyImage = new EasyImage.Builder(DetailsProfileActivity.this)
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
            easyImage.openChooser(DetailsProfileActivity.this);
        } else if (requestCode == CAMERA_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            easyImage.openCameraForImage(DetailsProfileActivity.this);
        }  else if (requestCode == DOCUMENTS_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            easyImage.openDocuments(DetailsProfileActivity.this);
        }
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
                        img_user.setImageBitmap(mBitmap);
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
                    easyImage.openCameraForImage(DetailsProfileActivity.this);
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
                    easyImage.openDocuments(DetailsProfileActivity.this);
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
        ActivityCompat.requestPermissions(DetailsProfileActivity.this, permissions, requestCode);
    }

    private void getDataByEmail() {
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getTokenAPI(email).enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response.isSuccessful()){
                    edt_fullName.setText(response.body().getFullName());
                    edt_email.setText(response.body().getEmail());
                    edt_address.setText(response.body().getAddress());
                    edt_phone.setText(response.body().getPhone());
                    Picasso.get().load(URL+"uploads/"+response.body().getImage()).error(R.drawable.avatar).into(img_user);
                }
            }
            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
            }
        });
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        email = bundle.getString("email");
    }

    private void initView() {
        img_back = findViewById(R.id.img_back);
        img_update_user = findViewById(R.id.img_update_user);
        img_user = findViewById(R.id.img_user);
        edt_fullName = findViewById(R.id.edt_fullName);
        edt_email = findViewById(R.id.edt_email);
        edt_phone = findViewById(R.id.edt_phone);
        edt_address = findViewById(R.id.edt_address);
        setOnclick();
    }

    private void setOnclick() {
        img_back.setOnClickListener(this);
        img_user.setOnClickListener(this);
        img_update_user.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                setResult(RESULT_OK, new Intent());
                finish();
                break;
            case R.id.img_update_user:
                UpdateUser(v);
                break;
            case R.id.img_user:
                chooseCamera();
                break;
        }
    }

    private void UpdateUser(View v) {
        if (edt_fullName.getText().toString().isEmpty() || edt_address.getText().toString().isEmpty() || edt_phone.getText().toString().isEmpty()){
            Snackbar.make(v, "Vui lòng nhập đầy đủ thông tin", Snackbar.LENGTH_LONG).show();
        } else {
            EditUser(v);
        }
    }

    private void EditUser(final View v) {
        final ProgressDialog dialog;
        dialog = new ProgressDialog(DetailsProfileActivity.this);
        dialog.setTitle("Đang xoá sản phẩm");
        dialog.setMessage("Vui lòng đợi...");
        dialog.show();
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        if(byteArray != null) {
            try {
                File filesDir = getApplicationContext().getFilesDir();
                File file = new File(filesDir, "user"+ Calendar.getInstance().getTimeInMillis() + ".png");
                FileOutputStream fos = null;
                fos = new FileOutputStream(file);
                fos.write(byteArray);
                fos.flush();
                fos.close();
                ProgressRequestBody fileBody = new ProgressRequestBody(file, this);
                RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload");
                MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), fileBody);
                RequestBody email = RequestBody.create(MediaType.parse("text/plain"), edt_email.getText().toString());
                RequestBody fullName = RequestBody.create(MediaType.parse("text/plain"), edt_fullName.getText().toString());
                RequestBody phone = RequestBody.create(MediaType.parse("text/plain"), edt_phone.getText().toString());
                RequestBody address = RequestBody.create(MediaType.parse("text/plain"), edt_address.getText().toString());

                //add product
                apiService.updateUser(email, fullName, phone, address, body, name).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            Snackbar.make(v, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        dialog.dismiss();
                        Log.d("TAG", "onFailure: " + t.getMessage());
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            dialog.dismiss();
            Toast.makeText(this, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
        }
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