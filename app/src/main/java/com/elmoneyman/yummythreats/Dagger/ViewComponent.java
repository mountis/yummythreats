package com.elmoneyman.yummythreats.Dagger;

import com.elmoneyman.yummythreats.Fragment.RecipesFragment;
import com.elmoneyman.yummythreats.Fragment.StepsFragment;
import com.elmoneyman.yummythreats.Fragment.DetailFragment;

import dagger.Component;

@ViewScope
@Component(dependencies = AppComponent.class,
        modules = {PresenterModule.class})
public interface ViewComponent {
    void inject(RecipesFragment fragment);
    void inject(DetailFragment fragment);
    void inject(StepsFragment fragment);
}
