package com.elmoneyman.yummythreats.Data;


import android.content.UriMatcher;
import android.net.Uri;
import android.util.SparseArray;

public class RecipeUriMatcher {

    private UriMatcher uriMatcher;
    private SparseArray<RecipeMatchEnum> sparseArray;

    public RecipeUriMatcher(){
        buildUriMatcher();
        buildUriMap();
    }

    private void buildUriMatcher(){
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        RecipeMatchEnum[] movieUriEnum=RecipeMatchEnum.values();
        for(RecipeMatchEnum uriEnum:movieUriEnum){
            uriMatcher.addURI( RecipeContract.CONTENT_AUTHORITY,uriEnum.path,uriEnum.code);
        }
    }

    private void buildUriMap(){
        sparseArray =new SparseArray<>();
        RecipeMatchEnum[] movieUriEnum=RecipeMatchEnum.values();
        for(RecipeMatchEnum uriEnum:movieUriEnum){
            sparseArray.put(uriEnum.code,uriEnum);
        }
    }

    public RecipeMatchEnum match(Uri uri){
        final int code=uriMatcher.match(uri);
        if(sparseArray.get(code)==null){
            throw new UnsupportedOperationException("Unknown uri:"+uri.toString()+" with code " + code);
        }
        return sparseArray.get(code);
    }

    public String getType(Uri uri){
        final int code=uriMatcher.match(uri);
        if(sparseArray.get(code)==null){
            throw new UnsupportedOperationException("Unknown uri with code " + code);
        }
        return sparseArray.get(code).contentType;
    }


}
