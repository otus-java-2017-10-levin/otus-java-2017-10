package ru.otus.mvc.model;

import lombok.Data;
import ru.otus.mvc.view.StatisticView;

@Data
public class Statistics implements CommonModel {
    private long misses;
    private long hits;

    @Override
    public StatisticView getView() {
        return new StatisticView(this);
    }
}