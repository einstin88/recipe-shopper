import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NewRecipeComponent } from './components/new-recipe/new-recipe.component';
import { RecipeListComponent } from './components/recipe-list/recipe-list.component';
import { UpdateRecipeComponent } from './components/update-recipe/update-recipe.component';

const routes: Routes = [
  { path: 'recipe/new', component: NewRecipeComponent },
  { path: 'recipe/update', component: UpdateRecipeComponent },
  { path: '', component: RecipeListComponent },
  { path: '**', redirectTo: '/', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
