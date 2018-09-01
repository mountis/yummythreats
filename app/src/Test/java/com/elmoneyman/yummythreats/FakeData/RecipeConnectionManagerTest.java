package com.elmoneyman.yummythreats.FakeData;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import com.elmoneyman.yummythreats.BuildConfig;
import com.elmoneyman.yummythreats.Mapper.Mapper;
import com.elmoneyman.yummythreats.Model.Recipe;
import com.elmoneyman.yummythreats.Model.RecipeSerialized;
import com.elmoneyman.yummythreats.Network.Local;
import com.elmoneyman.yummythreats.Network.RecipeConnectionManager;
import com.elmoneyman.yummythreats.Network.RecipeDataSource;
import com.elmoneyman.yummythreats.Network.Remote;
import com.elmoneyman.yummythreats.Utils.ImmediateSchedulerProvider;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import rx.Observable;
import rx.schedulers.Schedulers;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,
        manifest = Config.NONE,
        sdk = Build.VERSION_CODES.LOLLIPOP)
public class RecipeConnectionManagerTest {


    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    @Remote
    private RecipeDataSource<RecipeSerialized> mockRemote;

    @Mock
    @Local
    private RecipeDataSource<RecipeSerialized> mockLocal;

    @Mock
    private Mapper<Recipe, RecipeSerialized> mockMapper;

    @Mock
    private ConnectivityManager mockManager;

    @Mock
    private NetworkInfo mockInfo;

    @Mock
    private Context context;

    private RecipeConnectionManager recipeConnectionManager;


    @Before
    public void setUp() {
        when( mockLocal.getRecipes() ).thenReturn( Observable.empty() );
        when( mockRemote.getRecipes() ).thenReturn( Observable.empty() );
        when( mockMapper.map( any( RecipeSerialized.class ) ) ).thenReturn( new Recipe() );
        when( mockManager.getActiveNetworkInfo() ).thenReturn( mockInfo );
        when( context.getSystemService( Context.CONNECTIVITY_SERVICE ) ).thenReturn( mockManager );
        when( mockMapper.map( anyListOf( RecipeSerialized.class ) ) ).thenReturn( new ArrayList<>() );

        recipeConnectionManager = new RecipeConnectionManager( mockLocal, mockRemote, mockMapper,
                new ImmediateSchedulerProvider(), context ) {

        };
    }


    @Test
    public void returnsRecipesWhenThereIsNetwork() {
        when( mockInfo.isConnectedOrConnecting() ).thenReturn( true );
        recipeConnectionManager.getRecipes()
                .subscribeOn( Schedulers.immediate() )
                .observeOn( Schedulers.immediate() )
                .subscribe( this::shouldNotBeNullAndInvokeMap );
        verify( mockRemote ).getRecipes();
    }

    @Test
    public void returnsRecipesWhenThereIsNotNetwork() {
        when( mockInfo.isConnectedOrConnecting() ).thenReturn( false );
        recipeConnectionManager.getRecipes()
                .subscribeOn( Schedulers.immediate() )
                .observeOn( Schedulers.immediate() )
                .subscribe( this::shouldNotBeNullAndInvokeMap );
        verify( mockLocal ).getRecipes();
    }

    private void shouldNotBeNullAndInvokeMap(Object object) {
        assertTrue( object != null );
        verify( mockMapper ).map( anyListOf( RecipeSerialized.class ) );
    }

}
