package ru.otus.mvc.controller;

import ru.otus.mvc.model.Result;
import ru.otus.utils.AuthUtil;

public class AuthController {

    public AuthController(Result result) {
        this.result = result;
    }

    private final Result result;

    public void update(String name, String pass) {
        if (AuthUtil.isValidUser(name, pass)) {
            result.setSuccess(true);
            result.setMessage(AuthUtil.generateHash(name, pass));
        } else {
            result.setSuccess(false);
            result.setMessage("");
        }
    }

    public void update(String hash) {
        if (AuthUtil.validateHash(hash)) {
            result.setSuccess(true);
            result.setMessage("");
        } else {
            result.setMessage("");
            result.setSuccess(false);
        }
    }
}