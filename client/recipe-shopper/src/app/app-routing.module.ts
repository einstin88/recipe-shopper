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
import { SignOutComponent } from './routes/sign-out/sign-out.component';
import { AuthGuard } from './route-guards/auth.guard';
import { AnonymousGuard } from './route-guards/anonymous.guard';
import { MyRecipesComponent } from './routes/my-recipes/my-recipes.component';

const routes: Routes = [
  {
    path: 'login',
    component: SignInComponent,
    title: 'Log In Page',
    canMatch: [AnonymousGuard],
  },
  {
    path: 'logout',
    component: SignOutComponent,
    title: 'Log Out',
    canActivate: [AuthGuard],
  },
  {
    path: 'registration',
    component: RegistrationComponent,
    title: 'Register Account',
    canMatch: [AnonymousGuard],
  },
  {
    path: 'cart/view',
    component: ViewCartComponent,
    title: 'View Cart',
    canActivate: [AuthGuard],
  },
  {
    path: 'parse-html',
    component: ParseHtmlComponent,
    title: 'Parse HTML',
    canActivate: [AuthGuard],
  },
  {
    path: 'recipe/new',
    component: NewRecipeComponent,
    title: 'Create New Recipe',
    canActivate: [AuthGuard],
  },
  {
    path: 'recipe/update/:recipeId',
    component: UpdateRecipeComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'recipe/view/:recipeId',
    component: ViewRecipeComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'recipes/view/:username',
    component: MyRecipesComponent,
    canActivate: []
  },
  {
    path: 'recipes/all',
    component: BrowseRecipeComponent,
    title: 'Browse Recipes',
  },
  { path: '**', redirectTo: '/recipes/all', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
