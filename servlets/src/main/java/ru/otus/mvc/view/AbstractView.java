package ru.otus.mvc.view;

import com.google.gson.Gson;
import ru.otus.mvc.model.CommonModel;

@SuppressWarnings("FieldCanBeLocal")
public abstract class AbstractView implements CommonView {
    private transient final static Gson gson = new Gson();
    private final CommonModel model;
    private int status;
    private String message;

    AbstractView(CommonModel model, int status, String message) {
        this.model = model;
        this.status = status;
        this.message = message;
    }

    AbstractView(CommonModel model) {
        this.model = model;
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int getStatus() {
        return status;
    }

    public String toJson() {
        return gson.toJson(this);
    }

}
