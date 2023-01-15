package ru.practicum.ewm.event.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class ViewStats {
    @NonNull
    private String app;
    @NonNull
    private String uri;
    @NonNull
    private Integer hits;
}
