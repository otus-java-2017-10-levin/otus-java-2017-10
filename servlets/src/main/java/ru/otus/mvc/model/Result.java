package ru.otus.mvc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.mvc.view.ResultView;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result implements CommonModel {
    private int result;
    private String message;

    @Override
    public ResultView getView() {
        final ResultView resultView = new ResultView(this);
        resultView.setStatus(this.result);
        resultView.setMessage(this.message);
        return resultView;
    }
}