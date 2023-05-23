import { NgModule } from '@angular/core';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavBarComponent } from './nav/nav-bar/nav-bar.component';
import { RecipeDataService } from './services/recipe-data.service';
import { NewRecipeComponent } from './routes/new-recipe/new-recipe.component';
import { ProductDataService } from './services/product-data.service';
import { ProductListComponent } from './components/product-list/product-list.component';
import { CategoryChoserComponent } from './components/category-choser/category-choser.component';
import { RecipeFormComponent } from './components/recipe-form/recipe-form.component';
import { BrowseRecipeComponent } from './routes/browse-recipe/browse-recipe.component';
import { ParseHtmlComponent } from './routes/parse-html/parse-html.component';
import { UpdateRecipeComponent } from './routes/update-recipe/update-recipe.component';
import { ViewRecipeComponent } from './routes/view-recipe/view-recipe.component';
import { ViewCartComponent } from './routes/view-cart/view-cart.component';
import { SignInComponent } from './routes/sign-in/sign-in.component';
import { RegistrationComponent } from './routes/registration/registration.component';
import { StoreModule } from '@ngrx/store';
import { reducers, metaReducers } from './flux/reducers';
import { SignOutComponent } from './routes/sign-out/sign-out.component';
import { EffectsModule } from '@ngrx/effects';
import { persistStoreEffect } from './flux/store-persist-hydrate/persist-store.effect';
import { HydrationEffect } from './flux/store-persist-hydrate/hydration.effect';
import { BearerTokenInterceptor } from './interceptors/bearer-token.interceptor';
import { IngredientSelectionsComponent } from './components/ingredient-selections/ingredient-selections.component';
import { AuthDataService } from './services/auth-data.service';
import { CartDataService } from './services/cart-data.service';
import { AuthEffects } from './flux/auth/auth.effect';
import { BootStrapModule } from 'src/app-bootstrap.module';

@NgModule({
  declarations: [
    AppComponent,
    NavBarComponent,
    NewRecipeComponent,
    ProductListComponent,
    CategoryChoserComponent,
    RecipeFormComponent,
    BrowseRecipeComponent,
    ParseHtmlComponent,
    UpdateRecipeComponent,
    ViewRecipeComponent,
    ViewCartComponent,
    SignInComponent,
    RegistrationComponent,
    SignOutComponent,
    IngredientSelectionsComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    StoreModule.forRoot(reducers, {
      metaReducers,
    }),
    EffectsModule.forRoot([
      AuthEffects,
      HydrationEffect,
      { persistStoreEffect },
    ]),
    BootStrapModule
  ],
  providers: [
    AuthDataService,
    CartDataService,
    ProductDataService,
    RecipeDataService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: BearerTokenInterceptor,
      multi: true,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
