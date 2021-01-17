package com.example.wisatag.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.wisatag.R;
import com.example.wisatag.adapter.WisataAdapter;
import com.example.wisatag.api.ApiService;
import com.example.wisatag.api.ApiUtils;
import com.example.wisatag.decoration.LayoutMarginDecoration;
import com.example.wisatag.model.ModelWisata;
import com.example.wisatag.utils.Tools;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BudayaActivity extends AppCompatActivity implements WisataAdapter.onSelectData {

    private RecyclerView rvWisata;
    private LayoutMarginDecoration gridMargin;
    private WisataAdapter wisataAdapter;
    private ProgressDialog progressDialog;
    private Toolbar tbWisata;
    private List<ModelWisata> WisataList;
    private ApiService mAPIService;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wisata);

        tbWisata = findViewById(R.id.toolbar_wisata);
        tbWisata.setTitle("Daftar Wisata Budaya");
        setSupportActionBar(tbWisata);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAPIService = ApiUtils.getAPIService();
        WisataList = new ArrayList<>();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mohon Tunggu");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sedang menampilkan data...");

        rvWisata = findViewById(R.id.rvWisata);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this,
                2, RecyclerView.VERTICAL, false);
        rvWisata.setLayoutManager(mLayoutManager);
        gridMargin = new LayoutMarginDecoration(2, Tools.dp2px(this, 4));
        rvWisata.addItemDecoration(gridMargin);
        rvWisata.setHasFixedSize(true);

        getWisataBudaya();
    }

    private void getWisataBudaya() {
        path = "budaya";
        progressDialog.show();

        Call<List<ModelWisata>> PhotoCall = mAPIService.getWisata(
                path
        );
        PhotoCall.enqueue(new Callback<List<ModelWisata>>() {
            @Override
            public void onResponse(Call<List<ModelWisata>> call, Response<List<ModelWisata>> response) {
                progressDialog.dismiss();
                WisataList = (List<ModelWisata>) response.body();
                Log.d("Retrofit Get", "Jumlah Data : " +String.valueOf(WisataList.size()));
                showWisata();
            }

            @Override
            public void onFailure(Call<List<ModelWisata>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(BudayaActivity.this,"Gagal menampilkan data!", Toast.LENGTH_SHORT).show();
                Log.e("Retrofit Get", t.toString());
            }

        });
    }

    private void showWisata() {
        wisataAdapter = new WisataAdapter(BudayaActivity.this, WisataList, this);
        rvWisata.setAdapter(wisataAdapter);
    }

    @Override
    public void onSelected(ModelWisata modelWisata) {
        Intent intent = new Intent(BudayaActivity.this, DetailWisataActivity.class);
        intent.putExtra("detailWisata", modelWisata);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
