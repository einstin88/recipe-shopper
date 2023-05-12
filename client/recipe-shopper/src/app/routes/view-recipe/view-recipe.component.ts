import { Component, OnDestroy, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { Subscription } from 'rxjs';
import { selectCurrentUser } from 'src/app/flux/auth/auth.selector';
import { CartActions } from 'src/app/flux/cart/cart.action';
import { State } from 'src/app/flux/reducers';
import { CartIngredient } from 'src/app/model/cart-ingredient.model';
import { CartItem } from 'src/app/model/cart-item.model';
import { Recipe } from 'src/app/model/recipe.model';
import { RecipeDataService } from 'src/app/services/recipe-data.service';

/**
 * @description A component associated with a router path for displaying a single recipe with its details
 */
@Component({
  templateUrl: './view-recipe.component.html',
  styleUrls: ['./view-recipe.component.css'],
})
export class ViewRecipeComponent implements OnInit, OnDestroy {
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private svc: RecipeDataService,
    private title: Title,
    private store: Store<State>
  ) {}

  currentUser!: string;
  errMsg: string = '';

  recipeId!: string;
  recipe!: Recipe;

  sub$!: Subscription;

  ngOnInit(): void {
    this.recipeId = this.route.snapshot.params['recipeId'];

    this.fetchRecipe();

    this.sub$ = this.store
      .select(selectCurrentUser)
      .subscribe((user) => (this.currentUser = user));
  }

  ngOnDestroy(): void {
    this.sub$.unsubscribe();
  }

  /**
   * @description Initiates the API call for retrieving a recipe via {@link RecipeDataService}.
   */
  private fetchRecipe() {
    this.svc
      .getRecipeById(this.recipeId)
      .then((recipe) => {
        this.recipe = recipe;
        this.title.setTitle(`View Recipe: ${this.recipe.recipeName}`);
      })
      .catch((err: Error) => {
        this.errMsg = err.message;
        this.title.setTitle(`Recipe ${this.recipeId} Not Found`);
      });
  }

  addToCart(ingredients: CartIngredient[]) {
    const cartItem: CartItem = {
      recipeId: this.recipe.recipeId,
      recipeName: this.recipe.recipeName,
      recipeCreator: this.recipe.recipeCreator,
      ingredients,
    };

    this.store.dispatch(CartActions.addToCart({ cartItem }));

    this.router.navigate(['/']);
  }
}
