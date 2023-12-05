package pl.kurs.zad1.service;

import java.io.Serializable;
import java.util.function.Predicate;

public class MyPredicate<T> implements Predicate<T>, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public boolean test(T t) {
        return true;
    }
}
