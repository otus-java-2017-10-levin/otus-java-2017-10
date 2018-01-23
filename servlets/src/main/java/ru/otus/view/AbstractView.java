package ru.otus.view;

import com.google.gson.Gson;
import ru.otus.model.CommonModel;

public abstract class AbstractView implements CommonView {
    private final static Gson gson = new Gson();
    private final CommonModel model;

    public AbstractView(CommonModel model) {
        this.model = model;
    }

    @Override
    public String getView() {
        return gson.toJson(model);
    }
}
