package com.example.completable.implementasi_completable_future.controller;

import com.example.completable.implementasi_completable_future.dto.Information;
import com.example.completable.implementasi_completable_future.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/account")
public class AccountController {
    private final AccountService accountService;

    @GetMapping
    public Information getAccountInformation(){
        return accountService.getInformation();
    }
}
