package ru.otus.mvc.view;

import com.google.gson.Gson;
import ru.otus.mvc.model.CommonModel;

@SuppressWarnings("FieldCanBeLocal")
public abstract class AbstractView implements CommonView {
    private transient final static Gson gson = new Gson();
    private final CommonModel model;
    private int status;
    private String message;

    AbstractView(CommonModel model) {
        this.model = model;
    }

    @Override
    public String getView() {
        return gson.toJson(this);
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

    @Override
    public String getMessage() {
        return message;
    }
}
