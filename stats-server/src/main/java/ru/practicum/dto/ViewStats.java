package ru.practicum.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode
public class ViewStats {
    @NonNull
    private String app;
    @NonNull
    private String uri;
    private int hits;
}
