package com.elmoneyman.yummythreats.FakeData;


import android.os.Build;

import com.elmoneyman.yummythreats.BuildConfig;
import com.elmoneyman.yummythreats.Contract.StepsContract;
import com.elmoneyman.yummythreats.Model.Step;
import com.elmoneyman.yummythreats.Presenter.StepsPresenter;
import com.elmoneyman.yummythreats.Utils.MessageNotificationProvider;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,
        manifest = Config.NONE,
        sdk = Build.VERSION_CODES.LOLLIPOP)
public class StepsPresenterTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private StepsContract.View mockView;

    @Mock
    private MessageNotificationProvider mockMessageProvider;

    @Mock
    private StepsPresenter.StepsWrapper wrapper;

    @InjectMocks
    private StepsPresenter presenter;

    @Before
    public void setUp() {
        presenter.attachView( mockView );
    }

    @Test
    public void playsVideoWhenShowCurrentIsInvoked() {
        when( wrapper.count() ).thenReturn( 10 );
        when( wrapper.isInBounds() ).thenReturn( true );
        when( wrapper.current() ).thenReturn( RecipeTestUtils.provideStep() );
        presenter.showCurrent();

        verify( wrapper, times( 2 ) ).count();
        verify( wrapper ).current();
        verify( wrapper ).isInBounds();
        verify( mockView ).showPageNumber( anyInt(), anyInt() );
        verify( mockView ).showDescription( anyString(), anyString() );
        verify( mockView ).playVideo( anyString() );
    }

    @Test
    public void hidesPlayerWhenShowCurrentIsInvoked() {
        Step step = RecipeTestUtils.provideStep();
        step.setImageUrl( null );
        step.setVideoUrl( null );
        when( wrapper.count() ).thenReturn( 10 );
        when( wrapper.isInBounds() ).thenReturn( true );
        when( wrapper.current() ).thenReturn( step );
        presenter.showCurrent();

        verify( wrapper, times( 2 ) ).count();
        verify( wrapper ).current();
        verify( wrapper ).isInBounds();
        verify( mockView ).showPageNumber( anyInt(), anyInt() );
        verify( mockView ).showDescription( anyString(), anyString() );
        verify( mockView ).hidePlayer();
    }

    @Test
    public void showsEmptyMessageWhenShowCurrentIsInvoked() {
        when( wrapper.isInBounds() ).thenReturn( false );
        presenter.showCurrent();

        verify( wrapper ).isInBounds();
        verify( mockMessageProvider ).emptyMessage();
        verify( mockView ).showMessage( anyString() );
    }

    @Test
    public void showsRequestStepWhenPositionIsValid() {
        Mockito.when( wrapper.count() ).thenReturn( 1 );
        StepsPresenter presenter = Mockito.spy( this.presenter );
        presenter.requestStep( 0 );
        verify( presenter ).showCurrent();
    }

    @Test
    public void showsNothingWhenRequestedStepPositionIsNotValid() {
        StepsPresenter presenter = Mockito.spy( this.presenter );
        presenter.requestStep( 0 );
        verify( presenter, times( 0 ) ).showCurrent();
    }


}
