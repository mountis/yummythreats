package com.elmoneyman.yummythreats.FakeData;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Build;

import com.elmoneyman.yummythreats.BuildConfig;
import com.elmoneyman.yummythreats.Model.IngredientSerialized;
import com.elmoneyman.yummythreats.Model.RecipeSerialized;
import com.elmoneyman.yummythreats.Model.StepSerialized;
import com.elmoneyman.yummythreats.Network.RecipeManager;
import com.elmoneyman.yummythreats.Utils.RecipeDatabaseUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static com.elmoneyman.yummythreats.FakeData.RecipeTestUtils.FAKE_ID;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(RobolectricTestRunner.class)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
@PrepareForTest(RecipeDatabaseUtils.class)
@Config(constants = BuildConfig.class,
        manifest = Config.NONE,
        sdk = Build.VERSION_CODES.LOLLIPOP)
public class RecipeManagerTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Mock
    private ContentResolver mockContentResolver;

    @InjectMocks
    private RecipeManager recipeManager;

    @Before
    public void setUp() {
        PowerMockito.mockStatic( DatabaseUtils.class );
        MockitoAnnotations.initMocks( this );
    }

    @Test
    public void handlesInsertionsOfRecipeValues() {
        RecipeSerialized recipeSerialized = RecipeTestUtils.provideRecipeSerialized();
        PowerMockito.when( RecipeDatabaseUtils.toValues( recipeSerialized ) ).thenReturn( new ContentValues() );
        recipeManager.insert( recipeSerialized );
        verify( mockContentResolver ).insert( any( Uri.class ), any( ContentValues.class ) );
        PowerMockito.verifyStatic();
        RecipeDatabaseUtils.toValues( any( RecipeSerialized.class ) );
    }

    @Test
    public void handlesInsertionOfIngredientValues() {
        List<IngredientSerialized> serializedList = RecipeTestUtils.provideIngredientSerializedList();
        PowerMockito.when( RecipeDatabaseUtils.toValues( any( IngredientSerialized.class ) ) ).thenReturn( new ContentValues() );
        recipeManager.insertIngredients( FAKE_ID, serializedList );
        verify( mockContentResolver, times( serializedList.size() * 3 ) ).insert( any( Uri.class ), any( ContentValues.class ) );
        PowerMockito.verifyStatic( times( serializedList.size() ) );
        RecipeDatabaseUtils.toValues( any( IngredientSerialized.class ) );
    }

    @Test
    public void handlesInsertionOfStepValues() {
        List<StepSerialized> serializedList = RecipeTestUtils.provideStepSerializedList();
        PowerMockito.when( RecipeDatabaseUtils.toValues( any( StepSerialized.class ), anyInt() ) ).thenReturn( new ContentValues() );
        recipeManager.insertSteps( FAKE_ID, serializedList );
        verify( mockContentResolver, times( serializedList.size() ) ).insert( any( Uri.class ), any( ContentValues.class ) );
        PowerMockito.verifyStatic( times( serializedList.size() ) );
        RecipeDatabaseUtils.toValues( any( StepSerialized.class ), anyInt() );
    }

    @Test
    public void handlesQueryForSteps() {
        RecipeSerialized serialized = RecipeTestUtils.provideRecipeSerialized();
        Cursor cursor = Mockito.mock( Cursor.class );
        when( cursor.getCount() ).thenReturn( 3 );
        when( cursor.moveToNext() )
                .thenReturn( true )
                .thenReturn( true )
                .thenReturn( true )
                .thenReturn( false );
        when( cursor.isClosed() ).thenReturn( false );
        when( mockContentResolver.query( any( Uri.class ), any(), any(), any(), any() ) ).thenReturn( cursor );
        PowerMockito.when( RecipeDatabaseUtils.toStep( cursor ) ).thenReturn( new StepSerialized() );

        recipeManager.queryStepsFor( serialized );

        verify( mockContentResolver ).query( any(), any(), any(), any(), any() );
        verify( cursor ).getCount();
        verify( cursor, times( 4 ) ).moveToNext();
        verify( cursor ).isClosed();
        verify( cursor ).close();

        PowerMockito.verifyStatic( times( 3 ) );
        RecipeDatabaseUtils.toStep( cursor );
    }

    @Test
    public void handlesQueryForIngredients() {
        RecipeSerialized serialized = RecipeTestUtils.provideRecipeSerialized();
        Cursor cursor = Mockito.mock( Cursor.class );
        when( cursor.getCount() ).thenReturn( 3 );
        when( cursor.moveToNext() )
                .thenReturn( true )
                .thenReturn( true )
                .thenReturn( true )
                .thenReturn( false );
        when( cursor.isClosed() ).thenReturn( false );
        when( mockContentResolver.query( any( Uri.class ), any(), any(), any(), any() ) ).thenReturn( cursor );
        PowerMockito.when( RecipeDatabaseUtils.toIngredient( cursor ) ).thenReturn( new IngredientSerialized() );

        recipeManager.queryIngredientsFor( serialized );

        verify( mockContentResolver ).query( any( Uri.class ), any(), any(), any(), any() );
        verify( cursor ).getCount();
        verify( cursor, times( 4 ) ).moveToNext();
        verify( cursor ).isClosed();
        verify( cursor ).close();

        PowerMockito.verifyStatic( times( 3 ) );
        RecipeDatabaseUtils.toIngredient( cursor );
    }

    @Test
    public void handlesQueryRecipeById() {
        Cursor cursor = Mockito.mock( Cursor.class );
        when( cursor.isClosed() ).thenReturn( false );
        when( mockContentResolver.query( any( Uri.class ), any(), any(), any(), any() ) ).thenReturn( cursor );
        PowerMockito.when( RecipeDatabaseUtils.toRecipe( cursor ) ).thenReturn( new RecipeSerialized() );

        recipeManager.queryById( FAKE_ID );

        verify( mockContentResolver ).query( any( Uri.class ), any(), any(), any(), any() );
        verify( cursor ).isClosed();
        verify( cursor ).close();

        PowerMockito.verifyStatic();
        RecipeDatabaseUtils.toRecipe( cursor );
    }
}
