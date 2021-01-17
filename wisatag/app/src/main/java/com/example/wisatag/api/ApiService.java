package com.example.wisatag.api;

import com.example.wisatag.model.ModelWisata;

import java.util.List;

import retrofit2.Call;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {
    @GET("api/data/{jenis_wisata}")
    Call<List<ModelWisata>> getWisata(
            @Path("jenis_wisata") String path
    );
    @Multipart
    @POST("api/store")
    Call<ModelWisata> savePost(@Part("nama") RequestBody nama,
                               @Part("alamat") RequestBody alamat,
                               @Part("deskripsi") RequestBody deskripsi,
                               @Part("kategori") RequestBody kategori,
                               @Part("latitude") RequestBody latitude,
                               @Part("longitude") RequestBody longitude,
                               @Part MultipartBody.Part foto);
}
