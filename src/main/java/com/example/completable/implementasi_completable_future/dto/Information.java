package com.example.completable.implementasi_completable_future.dto;

import java.util.List;

public record Information(
        List<Favorite> information,
        List<Recent> recent,
        List<Saving> saving
) {
}
