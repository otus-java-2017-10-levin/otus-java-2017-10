package ru.otus.model;

import lombok.Data;

@Data
public class Result implements CommonModel {
    private boolean success;
    private String message;
}