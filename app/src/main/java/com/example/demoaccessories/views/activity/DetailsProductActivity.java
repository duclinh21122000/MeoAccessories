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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.demoaccessories.Interface.ProgressRequestBody;
import com.example.demoaccessories.R;
import com.example.demoaccessories.api.ApiService;
import com.example.demoaccessories.api.RetrofitClient;
import com.example.demoaccessories.model.CommentList;
import com.example.demoaccessories.model.Product;
import com.example.demoaccessories.model.ProductList;
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

public class DetailsProductActivity extends AppCompatActivity implements View.OnClickListener, ProgressRequestBody.UploadCallbacks{
    Bitmap mBitmap;
    private static final int CAMERA_REQUEST_CODE = 7500;
    private static final int CHOOSER_PERMISSIONS_REQUEST_CODE = 7459;
    private static final int DOCUMENTS_REQUEST_CODE = 7501;
    byte[] byteArray;
    String name;
    EasyImage easyImage;
    //
    ImageView img_back, img_delete, img_product;
    EditText edt_name_product, edt_price, edt_address, edt_description;
    Spinner spn_category;
    Button btn_update_product;
    ProgressBar progressBar;
    String[] arr = {"Chọn một hạng mục", "Trang sức & phụ kiện", "Điện thoại", "Đồng hồ"};
    ApiService apiService;
    String name_seller, img_seller, id, currentDateandTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_product);
        initViewAdmin();
        setAdapterSpinner();
        getData();
        getDataByID();
        setUpCamera();
    }

    private void setUpCamera() {
        easyImage = new EasyImage.Builder(DetailsProductActivity.this)
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
            easyImage.openChooser(DetailsProductActivity.this);
        } else if (requestCode == CAMERA_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            easyImage.openCameraForImage(DetailsProductActivity.this);
        }  else if (requestCode == DOCUMENTS_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            easyImage.openDocuments(DetailsProductActivity.this);
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
                        img_product.setImageBitmap(mBitmap);
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
                    easyImage.openCameraForImage(DetailsProductActivity.this);
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
                    easyImage.openDocuments(DetailsProductActivity.this);
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
        ActivityCompat.requestPermissions(DetailsProductActivity.this, permissions, requestCode);
    }

    private void getDataByID() {
        final ProgressDialog dialog;
        dialog = new ProgressDialog(DetailsProductActivity.this);
        dialog.setTitle("Đang tải");
        dialog.setMessage("Vui lòng đợi...");
        dialog.show();
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getProductById(id).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                dialog.dismiss();
                if (response.isSuccessful()){
                    name_seller = response.body().getNameSeller();
                    img_seller = response.body().getImageSeller();
                    edt_name_product.setText(response.body().getNameProduct());
                    edt_price.setText(response.body().getPrice());
                    edt_address.setText(response.body().getAddress());
                    edt_description.setText(response.body().getDescription());
                    Picasso.get().load(URL+"uploads/"+response.body().getImage()).error(R.drawable.ic_add_picture).into(img_product);
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                dialog.dismiss();
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
    }

    private void initViewAdmin() {
        img_back = findViewById(R.id.img_back);
        img_delete = findViewById(R.id.img_delete);
        img_product = findViewById(R.id.img_add_picture);
        edt_name_product = findViewById(R.id.edt_name_product);
        edt_price = findViewById(R.id.edt_price_product);
        edt_address = findViewById(R.id.edt_address);
        edt_description = findViewById(R.id.edt_description_product);
        spn_category = findViewById(R.id.spn_category_product);
        btn_update_product = findViewById(R.id.btn_update_product);
        progressBar = findViewById(R.id.progressBar);
        setOnclickAdmin();
    }

    private void setOnclickAdmin() {
        img_back.setOnClickListener(this);
        img_delete.setOnClickListener(this);
        img_product.setOnClickListener(this);
        btn_update_product.setOnClickListener(this);
    }

    private void setAdapterSpinner() {
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arr);
        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_category.setAdapter(stringArrayAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                setResult(RESULT_OK, new Intent());
                finish();
                break;
            case R.id.img_delete:
                deleteProduct();
                break;
            case R.id.img_add_picture:
                chooseCamera();
                break;
            case R.id.btn_update_product:
                EditProduct(v);
                break;
        }
    }

    private void deleteProduct() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailsProductActivity.this);
        builder.setTitle("Xoá sản phẩm");
        builder.setMessage("Bạn có chắc muốn xoá sản phẩm này không?");
        builder.setCancelable(false);
        builder.setNegativeButton("HUỶ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("ĐỒNG Ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteProductByID();
                deleteCommentByID();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteCommentByID() {
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.delete_commentByID(id).enqueue(new Callback<CommentList>() {
            @Override
            public void onResponse(Call<CommentList> call, Response<CommentList> response) {
                if (response.isSuccessful()){

                }
            }

            @Override
            public void onFailure(Call<CommentList> call, Throwable t) {

            }
        });
    }

    private void deleteProductByID() {
        final ProgressDialog dialog;
        dialog = new ProgressDialog(DetailsProductActivity.this);
        dialog.setTitle("Đang xoá sản phẩm");
        dialog.setMessage("Vui lòng đợi...");
        dialog.show();
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.deleteProduct(id).enqueue(new Callback<ProductList>() {
            @Override
            public void onResponse(Call<ProductList> call, Response<ProductList> response) {
                if (response.isSuccessful()){
                    deleteCommentByID();
                    dialog.dismiss();
                    Toast.makeText(DetailsProductActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK, new Intent());
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ProductList> call, Throwable t) {
                dialog.dismiss();
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void EditProduct(View v) {
        String name_product = edt_name_product.getText().toString().trim();
        String price = edt_price.getText().toString().trim();
        String address = edt_address.getText().toString().trim();
        if (name_product.isEmpty() || price.isEmpty() || address.isEmpty()){
            Snackbar.make(v, "Vui lòng nhập đầy đủ thông tin", Snackbar.LENGTH_LONG).show();
        } else if (spn_category.getSelectedItem() == "Chọn một hàng mục"){
            Snackbar.make(v, "Vui lòng chọn hạng mục", Snackbar.LENGTH_LONG).show();
        }  else {
            progressBar.setVisibility(View.VISIBLE);
            UpdateProduct(v);
        }
    }

    private void UpdateProduct(final View v) {
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
                Log.d("TAG", "addProduct: " + currentDateandTime);
                RequestBody _id = RequestBody.create(MediaType.parse("text/plain"), id);
                RequestBody seller = RequestBody.create(MediaType.parse("text/plain"), name_seller);
                RequestBody name_product = RequestBody.create(MediaType.parse("text/plain"), edt_name_product.getText().toString());
                RequestBody price = RequestBody.create(MediaType.parse("text/plain"), edt_price.getText().toString());
                RequestBody address = RequestBody.create(MediaType.parse("text/plain"), edt_address.getText().toString());
                RequestBody category = RequestBody.create(MediaType.parse("text/plain"), spn_category.getSelectedItem().toString());
                RequestBody description = RequestBody.create(MediaType.parse("text/plain"), edt_description.getText().toString());
                RequestBody image_seller = RequestBody.create(MediaType.parse("text/plain"), img_seller);
                RequestBody createAt = RequestBody.create(MediaType.parse("text/plain"), currentDateandTime);
                //add product
                apiService.updateProduct(_id, seller, name_product, price, address, category, description, body, name, image_seller, createAt).enqueue(new Callback<ProductList>() {
                    @Override
                    public void onResponse(Call<ProductList> call, Response<ProductList> response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.isSuccessful()){
                            Snackbar.make(v, response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                        }
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