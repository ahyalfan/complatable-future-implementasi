package com.example.completable.implementasi_completable_future.service;

import com.example.completable.implementasi_completable_future.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Slf4j
@Service
public class AccountServiceFuture {

    public CompletableFuture<List<Suggest>> getSuggest() {
        List<Supplier<List<Suggest>>> suggestions = new ArrayList<>();
        suggestions.add(this::getFavoritesSuggest);
        suggestions.add(this::getRecentSuggest);
        suggestions.add(this::getSavingsSuggest);

        List<CompletableFuture<List<Suggest>>> futures = new LinkedList<>();
        for (Supplier<List<Suggest>> suggestion : suggestions) {
            futures.add(CompletableFuture.supplyAsync(suggestion)
//                    .exceptionally(___ -> List.of())
                            .exceptionally(e -> {
                                log.error("Error fetching suggestions", e);
                                return List.of();
                            })
            ); // jadi ketika terjadi error akan tampilkan
//            array kosong
//            atau misal kita kasih log error
//            agar ketika error tidak mempengaruhi code yg lainya

        }
//        pakai ini juga bisa, tapi disini akan saya contohkan pakai list saja
//        ini pakai lambda suppliernya, jadi lebih singkat
//        List<CompletableFuture<List<Suggest>>> futures = List.of(
//                CompletableFuture.supplyAsync(() -> getFavoritesSuggest()),
//                CompletableFuture.supplyAsync(() -> getRecentSuggest()),
//                CompletableFuture.supplyAsync(() -> getSavingsSuggest())
//        );

//        kita perlu future ke array biasa bukan collection
//        dan kenapa kita kasih new COmpletableFuture, ini agar tahu arraynya tipe datanya ini

//        allof ini akan menunggu semua selesai
//        ini kayak get() tapi kalau get kan hanya menggunu 1 atau paling cepat
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(___ ->{
                    log.info("Successfully");
                    return futures.stream().map(CompletableFuture::join) //kita joinkan semua
                            .flatMap(Collection::stream) // kita buat menjadi satu stream
                            .toList(); // kita rubah ke list
                });
    }

    public CompletableFuture<Information> getInformationV2() {
//        disini kita akan pakai completable future
        CompletableFuture<List<Favorite>> futureFavorite = CompletableFuture
                .supplyAsync(this::getFavorites);
        CompletableFuture<List<Recent>> futureRecent = CompletableFuture
                .supplyAsync(this::getRecent);
        CompletableFuture<List<Saving>> futureSaving = CompletableFuture
                .supplyAsync(this::getSavings);
//        yg mana ini akan dijalankan semua sekaligus tidak menunggu semua selesai

//        kemudian dia akan dimasukan ke Completable baru
//        dan ketika apply, dia akan menjalankan perintah dibawah
//        ___ artinya variabel yg di return tidak dipakai
//        intinya variabel itu tidak akan pernah dipakai, jadi kita buat void saj
//        .thenApply(test -> {
//            List<Recent> recents = futureRecent.join();
//            List<Saving> savings = futureSaving.join();
//            List<Favorite> favorites = futureFavorite.join();
//
//            return new Information(favorites, recents, savings);
//        }); // ini sebenarnya sama, cuma biar lebih enak dibuat ___
//        buat menandai variabelnya tida digunakan
        return CompletableFuture.allOf(futureRecent, futureSaving,futureFavorite)
                .thenApply(___ -> {
                   List<Recent> recents = futureRecent.join();
                   List<Saving> savings = futureSaving.join();
                   List<Favorite> favorites = futureFavorite.join();

                   return new Information(favorites, recents, savings);
                });

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

    //    misal disini kita ingin semua dimasukan ke Suggest
    public List<Suggest> getFavoritesSuggest() {
        log.info("get favoriteSuggest start");
        RestClient restClient = RestClient.create();

        List<Favorite> favorites = restClient.get()
                .uri("http://localhost:3001/favorite")
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List < Favorite >>(){
                })
                .getBody();

        Objects.requireNonNull(favorites);
        log.info("get favoriteSuggest end");
        return favorites.stream()
                .map(v -> new Suggest(v.codeBank(), v.accountNumber()))
                .toList();
    }

    public List<Recent> getRecent() {
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

    public List<Suggest> getRecentSuggest() {
        log.info("get RecentSuggest start");
        RestClient restClient = RestClient.create();

        List<Recent> recent = restClient.get()
                .uri("http://localhost:3001/recent")
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List < Recent >>(){
                })
                .getBody();

        Objects.requireNonNull(recent);
        log.info("get RecentSuggest end");
        return recent.stream()
                .map(v -> new Suggest(v.codeBank(), v.accountNumber()))
                .toList();
    }

    public List<Saving> getSavings() {
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
    public List<Suggest> getSavingsSuggest() {
        log.info("get saving start");
        RestClient restClient = RestClient.create();

        List<Saving> savings = restClient.get()
                .uri("http://localhost:3001/saving")
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List < Saving >>(){
                })
                .getBody();
        Objects.requireNonNull(savings);
        log.info("get saving end");
        return savings.stream()
                .map(v -> new Suggest("Me",v.accountNumber()))
                .toList();
    }
}
