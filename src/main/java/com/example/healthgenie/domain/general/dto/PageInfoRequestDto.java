package com.example.healthgenie.domain.general.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
@AllArgsConstructor
public class PageInfoRequestDto {
    private int page;
    private int size;
}
