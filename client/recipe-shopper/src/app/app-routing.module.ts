import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NewRecipeComponent } from './routes/new-recipe/new-recipe.component';
import { UpdateRecipeComponent } from './routes/update-recipe/update-recipe.component';
import { ParseHtmlComponent } from './routes/parse-html/parse-html.component';
import { BrowseRecipeComponent } from './routes/browse-recipe/browse-recipe.component';
import { ViewRecipeComponent } from './routes/view-recipe/view-recipe.component';
import { ViewCartComponent } from './routes/view-cart/view-cart.component';

const routes: Routes = [
  { path: 'cart/view', component: ViewCartComponent },
  { path: 'parse-html', component: ParseHtmlComponent },
  { path: 'recipe/new', component: NewRecipeComponent },
  { path: 'recipe/update/:recipeId', component: UpdateRecipeComponent },
  { path: 'recipe/view/:recipeId', component: ViewRecipeComponent },
  { path: '', component: BrowseRecipeComponent },
  { path: '**', redirectTo: '/', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
