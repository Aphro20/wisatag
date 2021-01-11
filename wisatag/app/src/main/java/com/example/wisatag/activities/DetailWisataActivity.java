package com.example.wisatag.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wisatag.R;
import com.example.wisatag.model.ModelWisata;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetailWisataActivity extends AppCompatActivity implements OnMapReadyCallback {

    Toolbar tbDetailWisata;
    TextView tvNama, tvDeskripsi, tvAlamat;
    ImageView imgKuliner;
    String id, Nama, Deskripsi, Alamat, Lat, Long;
    Double nlang, nlong;
    ModelWisata modelWisata;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_wisata);

        tbDetailWisata = findViewById(R.id.tbDetailWisata);
        tbDetailWisata.setTitle("Detail Wisata");
        setSupportActionBar(tbDetailWisata);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get maps
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);

        modelWisata = (ModelWisata) getIntent().getSerializableExtra("detailWisata");
        if (modelWisata != null) {
            id = modelWisata.getId().toString();
            Nama = modelWisata.getNama();
            Alamat = modelWisata.getAlamat();
            Deskripsi = modelWisata.getDeskripsi();
            Lat = modelWisata.getLatitude();
            Long = modelWisata.getLongitude();

            //set Id
            imgKuliner = findViewById(R.id.imgWisata);
            tvNama = findViewById(R.id.tvNama);
            tvAlamat = findViewById(R.id.tvAlamat);
            tvDeskripsi = findViewById(R.id.tvDeskripsi);

            //set text
            tvNama.setText(Nama);
            tvAlamat.setText(Alamat);
            tvDeskripsi.setText(Deskripsi);

            nlang = Double.parseDouble(Lat);
            nlong = Double.parseDouble(Long);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in ITS and move the camera
        LatLng ITS = new LatLng(nlang,nlong);
        mMap.addMarker(new MarkerOptions().position(ITS).title("Marker in " +nlang+ ":" +nlong));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ITS, 15));
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