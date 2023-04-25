import { Component } from '@angular/core';
import { Subject } from 'rxjs';
import { Category } from 'src/app/model/category.model';
import { Product } from 'src/app/model/product.model';
import { ProductDataService } from 'src/app/services/product-data.service';

@Component({
  templateUrl: './new-recipe.component.html',
  styleUrls: ['./new-recipe.component.css']
})
export class NewRecipeComponent {
  constructor(private productSvc: ProductDataService) {}

  category!: Category | '';
  ingredientToAdd = new Subject<Product>();

  offset: number = 0;
  limit: number = 20;

  addIngredient(product: Product) {
    this.ingredientToAdd.next(product);
  }

  updateCategory(category: Category | '') {
    this.category = category;
    // console.info(this.category);
    this.loadProducts();
  }

  updateProductList(offset: number) {
    this.offset = offset < 0 ? 0 : offset;
    // console.log(this.offset);
    this.loadProducts();
  }

  loadProducts() {
    this.productSvc.getProducts(this.category, this.limit, this.offset);
  }

}
