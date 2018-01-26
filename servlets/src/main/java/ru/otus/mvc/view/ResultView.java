package ru.otus.mvc.view;

import ru.otus.mvc.model.CommonModel;

public class ResultView extends AbstractView {


    public ResultView(CommonModel model, int status, String message) {
        super(model, status, message);
    }

    public ResultView(CommonModel model) {
        super(model);
    }
}
