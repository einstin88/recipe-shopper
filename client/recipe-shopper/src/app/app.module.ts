import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
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
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
  ],
  providers: [RecipeDataService, ProductDataService],
  bootstrap: [AppComponent],
})
export class AppModule {}
