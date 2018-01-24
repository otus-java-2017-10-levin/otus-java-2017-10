package ru.otus.controller;

import ru.otus.model.CommonModel;
import ru.otus.utils.AuthUtil;

public class LogoutController {
    private final CommonModel model;

    public LogoutController(CommonModel model) {
        this.model = model;
    }

    public void update(String hash) {
        if (AuthUtil.validateHash(hash)) {
            AuthUtil.deleteHash(hash);
        }
    }

}
