import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http'
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavBarComponent } from './nav/nav-bar/nav-bar.component';
import { RecipeDataService } from './services/recipe-data.service';
import { NewRecipeComponent } from './components/new-recipe/new-recipe.component';
import { RecipeListComponent } from './components/recipe-list/recipe-list.component';
import { UpdateRecipeComponent } from './components/update-recipe/update-recipe.component';
import { ParseHtmlComponent } from './components/parse-html/parse-html.component';
import { ProductDataService } from './services/product-data.service';
import { ProductListComponent } from './components/product-list/product-list.component';
import { RecipeIngredientsComponent } from './components/recipe-ingredients/recipe-ingredients.component';
import { CategoryChoserComponent } from './components/category-choser/category-choser.component';
import { RecipeFormComponent } from './components/recipe-form/recipe-form.component';

@NgModule({
  declarations: [
    AppComponent,
    NavBarComponent,
    NewRecipeComponent,
    RecipeListComponent,
    UpdateRecipeComponent,
    ParseHtmlComponent,
    ProductListComponent,
    RecipeIngredientsComponent,
    CategoryChoserComponent,
    RecipeFormComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule
  ],
  providers: [RecipeDataService, ProductDataService],
  bootstrap: [AppComponent]
})
export class AppModule { }
