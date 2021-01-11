package com.example.wisatag.api;

import com.example.wisatag.model.ModelWisata;

import retrofit2.Call;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
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
