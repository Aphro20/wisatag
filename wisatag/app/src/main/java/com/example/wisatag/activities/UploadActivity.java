package com.example.wisatag.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.example.wisatag.R;
import com.example.wisatag.api.ApiService;
import com.example.wisatag.api.ApiUtils;
import com.example.wisatag.model.ModelWisata;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Spinner spinner;
    private LocationManager locationmanager;
    private LocationListener locationlistener;
    ImageView foto;
    String txtlat, txtlong;
    Button kamera, upload;
    EditText nama, alamat, deskripsi;
    String txtnama, txtalamat, txtdeskripsi, txtkategori;
    private ApiService mAPIService;
    Bitmap bitmap;
    ProgressDialog progressDialog;
    final int kodeKamera = 99;
    private String[] Kategori = {
            "Alam",
            "Kuliner",
            "Budaya"
    };
    private Toolbar tbWisata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        kamera = (Button) findViewById(R.id.kamera);
        upload = (Button) findViewById(R.id.upload);

        nama = (EditText) findViewById(R.id.et_nama);
        alamat = (EditText) findViewById(R.id.et_alamat);
        deskripsi = (EditText) findViewById(R.id.et_deskripsi);
        foto = (ImageView) findViewById(R.id.foto);

        spinner = (Spinner) findViewById(R.id.spinner);

        mAPIService = ApiUtils.getAPIService();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mohon Tunggu");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sedang mengupload data");

        tbWisata = findViewById(R.id.tbUpload);
        tbWisata.setTitle("Upload Wisata");
        setSupportActionBar(tbWisata);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Kategori);
        spinner.setAdapter(dataAdapter);

        locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationlistener = new locationlistener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, locationlistener);

        kamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intentCamera, kodeKamera);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadData();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                txtkategori = dataAdapter.getItem(i).toString();
//                Toast.makeText(UploadActivity.this, "Selected: " + txtkategori, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == kodeKamera && resultCode == RESULT_OK){
            bitmap = (Bitmap) data.getExtras().get("data");
            foto.setImageBitmap(bitmap);
        }
    }

    private File createTempFile(Bitmap bitmap) {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                , System.currentTimeMillis() +"_image.png");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG,0, bos);
        byte[] bitmapdata = bos.toByteArray();
        //write the bytes in file

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public void UploadData(){
        progressDialog.show();
        File file = createTempFile(bitmap);
        txtnama = nama.getText().toString();
        txtalamat = alamat.getText().toString();
        txtdeskripsi = deskripsi.getText().toString();

        RequestBody reqnama = RequestBody.create(MediaType.parse("text/plain"), txtnama);
        RequestBody reqalamat = RequestBody.create(MediaType.parse("text/plain"), txtalamat);
        RequestBody reqdeskripsi = RequestBody.create(MediaType.parse("text/plain"), txtdeskripsi);
        RequestBody reqkategori = RequestBody.create(MediaType.parse("text/plain"), txtkategori);
        RequestBody reqlat = RequestBody.create(MediaType.parse("text/plain"), txtlat);
        RequestBody reqlong = RequestBody.create(MediaType.parse("text/plain"), txtlong);
        RequestBody reqfoto = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("foto", file.getName(), reqfoto);

        mAPIService.savePost(reqnama, reqalamat, reqdeskripsi, reqkategori, reqlat, reqlong, body).enqueue(new Callback<ModelWisata>() {
            @Override
            public void onResponse(Call<ModelWisata> call, Response<ModelWisata> response) {

                if(response.isSuccessful()) {
                    progressDialog.dismiss();
                    Log.d("Retrofit Get", response.body().toString());
                    Log.i(TAG, "post submitted to API." + response.body().toString());
                    Toast.makeText(UploadActivity.this, "Berhasil mengupload data!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UploadActivity.this, MainActivity.class));
                }
            }

            @Override
            public void onFailure(Call<ModelWisata> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("Retrofit Get", t.toString());
                Toast.makeText(UploadActivity.this, "Gagal mengupload data!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class locationlistener implements LocationListener{
        @SuppressLint("SetTextI18n")
        @Override
        public void onLocationChanged(@NonNull Location location) {
            txtlat = String.valueOf(location.getLatitude());
            txtlong = String.valueOf(location.getLongitude());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {

        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {

        }
    }
}