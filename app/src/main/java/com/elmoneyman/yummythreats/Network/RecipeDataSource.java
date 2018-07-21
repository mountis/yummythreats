package com.elmoneyman.yummythreats.Network;


import rx.Observable;

public abstract class RecipeDataSource<T> implements RecipeService<T> {
    @Override
    public Observable<T> getRecipeById(int id) {
        return null;
    }

    public abstract void insert(T item);
}
