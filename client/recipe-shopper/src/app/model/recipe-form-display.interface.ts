import { Observable, Subject } from 'rxjs';
import { Product } from './product.model';
import { RecipeFormComponent } from '../components/recipe-form/recipe-form.component';

/**
 * Interface that standardizes the class structure and attributes of the component which displays a recipe form
 */
export interface RecipeFormDisplay {
  /**
   * @description Variable to hold the reusable {@link RecipeFormComponent} for view projection
   */
  recipeForm: RecipeFormComponent;

  /**
   * @description Receiver of the {@link RecipeFormComponent}'s validity status
   */
  isValid$: Observable<boolean>;

  /**
   * @description Producer of products to add to the {@link RecipeFormComponent}
   */
  ingredientToAdd: Subject<Product>;

  /**
   * @description Optional variable for use pnly when updating a recipe with Recipe Id
   */
  recipeId?: string;

  /**
   * @description Function that takes data from the {@link RecipeFormComponent} and post it to a relevant endpoint via {@link RecipeDataService}
   */
  processForm(): void;

  /**
   * @description Function to handle the product data added by a button's click
   * @param product The added product to push to the stream of {@link ingredientToAdd}
   */
  addIngredient(product: Product): void;
}
