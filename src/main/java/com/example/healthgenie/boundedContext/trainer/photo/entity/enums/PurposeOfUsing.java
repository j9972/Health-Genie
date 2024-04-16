package com.example.healthgenie.boundedContext.trainer.photo.entity.enums;

import lombok.Getter;

@Getter
public enum PurposeOfUsing {
    PROFILE("profile"),
    ETC("etc");

    private final String purposeOfUsing;

    PurposeOfUsing(String purposeOfUsing) {
        this.purposeOfUsing = purposeOfUsing;
    }

}
