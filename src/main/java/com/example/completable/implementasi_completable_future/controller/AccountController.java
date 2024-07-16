package com.example.completable.implementasi_completable_future.controller;

import com.example.completable.implementasi_completable_future.dto.Information;
import com.example.completable.implementasi_completable_future.dto.Suggest;
import com.example.completable.implementasi_completable_future.service.AccountService;
import com.example.completable.implementasi_completable_future.service.AccountServiceFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/account")
public class AccountController {
    private final AccountService accountService;

    private final AccountServiceFuture accountFuture;

    @GetMapping
    public Information getAccountInformation(){
        return accountService.getInformation();
    }

    @GetMapping("/v2")
//    jadi dengan completable future, jika ada kendala seperti ada code yg sedikit lama
//    maka thread tidaka akn menunggu, di akan dijalankan
//    jadi ini harusnya lebih cepat
    public CompletableFuture<Information> getAccountV2(){
        return accountFuture.getInformationV2();
    }

    @GetMapping("/suggest")
//    jadi dengan completable future, jika ada kendala seperti ada code yg sedikit lama
//    maka thread tidaka akn menunggu, di akan dijalankan
//    jadi ini harusnya lebih cepat
    public CompletableFuture<List<Suggest>> getSuggest(){
        return accountFuture.getSuggest();
    }
}
