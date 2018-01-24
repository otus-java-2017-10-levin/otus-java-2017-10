package ru.otus.mvc.controller;

import ru.otus.mvc.model.CommonModel;
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
