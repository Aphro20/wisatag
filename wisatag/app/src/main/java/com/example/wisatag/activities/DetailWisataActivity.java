package com.example.wisatag.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.wisatag.R;
import com.example.wisatag.model.ModelWisata;

public class DetailWisataActivity extends AppCompatActivity {

    Toolbar tbDetailWisata;
    TextView tvNama, tvDeskripsi, tvAlamat;
    ImageView imgKuliner;
    String id, Nama, Deskripsi, Alamat;
    ModelWisata modelWisata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_wisata);

        tbDetailWisata = findViewById(R.id.tbDetailWisata);
        tbDetailWisata.setTitle("Detail Wisata Kuliner");
        setSupportActionBar(tbDetailWisata);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        modelWisata = (ModelWisata) getIntent().getSerializableExtra("detailWisata");
        if (modelWisata != null) {
            id = modelWisata.getId().toString();
            Nama = modelWisata.getNama();
            Alamat = modelWisata.getAlamat();
            Deskripsi = modelWisata.getDeskripsi();

            //set Id
            imgKuliner = findViewById(R.id.imgKuliner);
            tvNama = findViewById(R.id.tvNama);
            tvAlamat = findViewById(R.id.tvAlamat);
            tvDeskripsi = findViewById(R.id.tvDeskripsi);

            //set text
            tvNama.setText(Nama);
            tvAlamat.setText(Alamat);
            tvDeskripsi.setText(Deskripsi);

            //get Image
            Glide.with(this)
                    .load("http://180.246.49.244:8000/" + modelWisata.getPath())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgKuliner);

        }
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