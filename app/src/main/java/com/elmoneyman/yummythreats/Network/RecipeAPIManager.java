package com.elmoneyman.yummythreats.Network;


import com.elmoneyman.yummythreats.Model.RecipeSerialized;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

public interface RecipeAPIManager {
    @GET("/android-baking-app-json ")
    Observable<List<RecipeSerialized>> queryRecipes();
}
