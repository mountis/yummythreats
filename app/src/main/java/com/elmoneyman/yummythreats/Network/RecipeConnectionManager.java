package com.elmoneyman.yummythreats.Network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.elmoneyman.yummythreats.Mapper.Mapper;
import com.elmoneyman.yummythreats.Model.Recipe;
import com.elmoneyman.yummythreats.Model.RecipeSerialized;
import com.elmoneyman.yummythreats.Utils.BaseSchedulerProvider;
import com.elmoneyman.yummythreats.Utils.RecipeLogUtils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Completable;
import rx.Observable;

@Singleton
public class RecipeConnectionManager implements RecipeService<Recipe> {


    private final Mapper<Recipe,RecipeSerialized> recipesMapper;
    private final RecipeDataSource<RecipeSerialized> localDataSource;
    private final RecipeDataSource<RecipeSerialized> remoteDataSource;
    private final BaseSchedulerProvider schedulerProvider;
    private final Context context;
    private  SparseArray<Recipe> inMemoryCache;

    @Inject
    public RecipeConnectionManager(@NonNull @Local RecipeDataSource<RecipeSerialized> localDataSource,
                                   @NonNull @Remote RecipeDataSource<RecipeSerialized> remoteDataSource,
                                   @NonNull Mapper<Recipe,RecipeSerialized> recipesMapper,
                                   @NonNull BaseSchedulerProvider schedulerProvider,
                                   @NonNull Context context){
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
        this.recipesMapper = recipesMapper;
        this.context=context;
        this.schedulerProvider=schedulerProvider;
        this.inMemoryCache=new SparseArray<>();
    }

    @Override
    public Observable<List<Recipe>> getRecipes() {
        if(isThereAnNetworkConnection()){
            return remoteDataSource.getRecipes()
                    .doOnNext(list->
                        Completable
                                .fromCallable(()->saveToDisk(list))
                                .subscribeOn(schedulerProvider.multi())
                                .subscribe())
                    .map( recipesMapper::map)
                    .doOnNext(this::cacheData)
                    .doOnNext(list-> RecipeLogUtils.logD(list,this));
        }
        return localDataSource.getRecipes()
                .map( recipesMapper::map)
                .doOnNext(this::cacheData)
                .doOnNext(list-> RecipeLogUtils.logD(list,this));
    }

    private void cacheData(List<Recipe> recipeList){
        if(inMemoryCache.size()>=100) inMemoryCache.clear();
        for (Recipe recipe : recipeList) {
            if (inMemoryCache.get( recipe.getId() ) == null) {
                inMemoryCache.put( recipe.getId(), recipe );
            }
        }
    }

    private boolean saveToDisk(List<RecipeSerialized> list){
        if(list!=null){
            for (RecipeSerialized recipeSerialized : list) {
                localDataSource.insert( recipeSerialized );
            }
            return true;
        }
        return false;
    }

    @Override
    public Observable<Recipe> getRecipeById(int id) {
        if(inMemoryCache.get(id)!=null) {
            return Observable.just(inMemoryCache.get(id));
        }
        return localDataSource.getRecipeById(id).map( recipesMapper::map)
                .doOnNext(recipe -> inMemoryCache.put(id,recipe));
    }


    private boolean isThereAnNetworkConnection(){
        ConnectivityManager manager=ConnectivityManager.class
                .cast(context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        return activeNetwork!=null && activeNetwork.isConnectedOrConnecting();
    }
}
