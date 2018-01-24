package ru.otus.mvc.model;

import lombok.Data;

@Data
public class Statistics implements CommonModel {
    private long misses;
    private long hits;
}