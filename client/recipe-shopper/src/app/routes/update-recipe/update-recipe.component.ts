import {
  AfterContentChecked,
  AfterViewChecked,
  AfterViewInit,
  Component,
  OnDestroy,
  OnInit,
  ViewChild,
} from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { Subject, Subscription } from 'rxjs';
import { RecipeFormComponent } from 'src/app/components/recipe-form/recipe-form.component';
import { Product } from 'src/app/model/product.model';
import { RecipeFormDisplay } from 'src/app/model/recipe-form-display.interface';
import { RecipeDataService } from 'src/app/services/recipe-data.service';
import { ToastNotificationService } from 'src/app/services/toast-notification.service';

/**
 * @description A component associated with a router path for updating recipes
 */
@Component({
  templateUrl: './update-recipe.component.html',
  styleUrls: ['./update-recipe.component.css'],
})
export class UpdateRecipeComponent
  implements OnInit, OnDestroy, AfterViewChecked, RecipeFormDisplay
{
  constructor(
    private recipeSvc: RecipeDataService,
    private route: ActivatedRoute,
    private title: Title,
    private toastSvc: ToastNotificationService
  ) {}

  @ViewChild(RecipeFormComponent)
  recipeForm!: RecipeFormComponent;

  // Variables
  isInvalid!: boolean;

  ingredientToAdd = new Subject<Product>();
  recipeId!: string;
  recipeCreator!: string;

  sub$!: Subscription;

  ngOnInit(): void {
    this.recipeId = this.route.snapshot.params['recipeId'];

    this.title.setTitle(`Update Recipe: ${this.recipeId}`);
  }

  ngAfterViewChecked() {
    // setTimeout(() => {
      this.sub$ = this.recipeForm.isInvalidUpdate$.subscribe((isInvalid) => {
        console.log(isInvalid);
        this.isInvalid = isInvalid;
      });
    // });
  }

  ngOnDestroy(): void {
    this.sub$.unsubscribe();
  }

  processForm() {
    const formData = this.recipeForm.recipeData;
    console.info('>>> Submitted form', formData);

    this.recipeSvc
      .updateRecipe(formData)
      .then(() => {
        this.recipeForm.recipeErr = '';
        this.recipeForm.reset = true;

        this.toastSvc.show('Recipe', `Recipe '${formData.recipeName}' updated`);
      })
      .catch((err) => {
        this.recipeForm.recipeError = err.error;
      });
  }

  addIngredient(product: Product) {
    this.ingredientToAdd.next(product);
  }
}
