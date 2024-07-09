package com.example.completable.implementasi_completable_future.service;

import com.example.completable.implementasi_completable_future.dto.Favorite;
import com.example.completable.implementasi_completable_future.dto.Information;
import com.example.completable.implementasi_completable_future.dto.Recent;
import com.example.completable.implementasi_completable_future.dto.Saving;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
@Service
public class AccountService {

    public Information getInformation() {
        List<Favorite> favorites = getFavorites();
        List<Recent> recents = getRecent();
        List<Saving> savings = getSavings();

        return new Information(favorites,recents,savings);
    }

    public List<Favorite> getFavorites() {
//        karena disini kita pakai mockoon, yg mana tools ini bisa buat api simulasi
//        maka disini kita akan ambil pakai restClient
        log.info("get favorite start");
        RestClient restClient = RestClient.create();

        List<Favorite> favorites = restClient.get()
                .uri("http://localhost:3001/favorite")
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List < Favorite >>(){
                })
                .getBody();
        log.info("get favorite end");
        return favorites;
    }

    public List<Recent> getRecent() {
//        karena disini kita pakai mockoon, yg mana tools ini bisa buat api simulasi
//        maka disini kita akan ambil pakai restClient
        log.info("get Recent start");
        RestClient restClient = RestClient.create();

        List<Recent> recents = restClient.get()
                .uri("http://localhost:3001/recent")
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List < Recent >>(){
                })
                .getBody();
        log.info("get recent end");
        return recents;
    }

    public List<Saving> getSavings() {
//        karena disini kita pakai mockoon, yg mana tools ini bisa buat api simulasi
//        maka disini kita akan ambil pakai restClient
        log.info("get saving start");
        RestClient restClient = RestClient.create();

        List<Saving> savings = restClient.get()
                .uri("http://localhost:3001/saving")
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List < Saving >>(){
                })
                .getBody();
        log.info("get saving end");
        return savings;
    }

//    ParameterizedTypeReference ini wajib karena, ketika di compile java tidak mengetahu
//    apa sih return dari api yg kita ambil
//    nah maka dari sini kita beritahu ke java bahwa typenya apa menggunkan parameterizedTypeReference
}

//.uri("http://localhost:3000/favorit"): Metode .uri() digunakan untuk menentukan URI atau URL yang ingin diakses.
    // Dalam kasus ini, URL yang ditentukan adalah http://localhost:3000/favorit, yang merupakan endpoint API yang ingin Anda akses.
//
//.retrieve(): Metode .retrieve() digunakan untuk mengirimkan permintaan HTTP
    // ke endpoint yang sudah ditentukan sebelumnya. Ini adalah langkah untuk memulai
    // eksekusi permintaan ke server.
//
//.toEntity(new ParameterizedTypeReference<List<Favorite>>(){}):
    // Metode .toEntity() digunakan untuk menentukan bagaimana respons dari server
    // akan diubah menjadi objek Java. Dalam contoh ini,
    // Anda menggunakan ParameterizedTypeReference untuk mengambil respons dalam bentuk List
    // dari objek Favorite. Ini berguna karena Java menggunakan erasure pada waktu kompilasi,
    // sehingga perlu menggunakan ParameterizedTypeReference untuk menangani generic types seperti List<Favorite>.
//
//.getBody(): Metode .getBody() digunakan untuk mendapatkan badan respons
    // dari permintaan HTTP yang telah dikirim. Dalam contoh ini,
    // .getBody() mengembalikan respons dari server dalam bentuk List dari objek Favorite.
