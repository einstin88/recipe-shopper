import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NewRecipeComponent } from './routes/new-recipe/new-recipe.component';
import { UpdateRecipeComponent } from './routes/update-recipe/update-recipe.component';
import { ParseHtmlComponent } from './routes/parse-html/parse-html.component';
import { BrowseRecipeComponent } from './routes/browse-recipe/browse-recipe.component';
import { ViewRecipeComponent } from './routes/view-recipe/view-recipe.component';
import { ViewCartComponent } from './routes/view-cart/view-cart.component';
import { SignInComponent } from './routes/sign-in/sign-in.component';
import { RegistrationComponent } from './routes/registration/registration.component';

const routes: Routes = [
  { path: 'login', component: SignInComponent, title: 'Log In Page' },
  {
    path: 'registration',
    component: RegistrationComponent,
    title: 'Register Account',
  },
  { path: 'cart/view', component: ViewCartComponent, title: 'View Cart' },
  { path: 'parse-html', component: ParseHtmlComponent, title: 'Parse HTML' },
  {
    path: 'recipe/new',
    component: NewRecipeComponent,
    title: 'Create New Recipe',
  },
  { path: 'recipe/update/:recipeId', component: UpdateRecipeComponent },
  { path: 'recipe/view/:recipeId', component: ViewRecipeComponent },
  { path: '', component: BrowseRecipeComponent, title: 'Browse Recipes' },
  { path: '**', redirectTo: '/', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
