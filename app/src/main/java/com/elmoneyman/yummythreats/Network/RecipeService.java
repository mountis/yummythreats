package com.elmoneyman.yummythreats.Network;


import java.util.List;

import rx.Observable;

public interface RecipeService<T>  {
    Observable<List<T>> getRecipes();
    Observable<T> getRecipeById(int id);
}
