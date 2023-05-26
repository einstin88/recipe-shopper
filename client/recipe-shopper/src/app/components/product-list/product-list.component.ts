import { Component, OnInit, Output } from '@angular/core';
import { Subject } from 'rxjs';
import { Category } from 'src/app/model/category.model';
import { Product } from 'src/app/model/product.model';
import { ProductDataService } from 'src/app/services/product-data.service';
import { Categories } from 'src/app/utils/consts';

/**
 * @description A reusable component for displaying the list of products in a chosen category. Works with {@link CategoryChoserComponent}
 */
@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css'],
})
export class ProductListComponent implements OnInit {
  constructor(private svc: ProductDataService) {}

  @Output()
  addProduct = new Subject<Product>();

  // Variables
  chosenCategory!: Category;
  categories = Categories;
  products: Product[] = [];

  offset: number = 0;
  limit: number = 20;

  errorMsg: string = '';
  isLoading: boolean = false;

  ngOnInit(): void {
    this.chosenCategory = this.categories.at(0)!;
    this.loadProducts();
  }

  /**
   * @description Function to handle the Click event of the 'Add' button beside a product
   * @param product The product to push to the ingredient list
   */
  addIngredient(product: Product) {
    this.addProduct.next(product);
  }

  /**
   * @description Function to handle the category selection from the drop down menu
   *
   * @param category The selected category
   */
  updateCategory(category: Category) {
    this.chosenCategory = category;
    this.loadProducts();
  }

  /**
   * @description Function to handle the clicking of the Pagination buttons
   *
   * @param offset The starting index of the product list to display
   */
  updateProductList(offset: number) {
    this.offset = offset < 0 ? 0 : offset;
    this.loadProducts();
  }

  /**
   * @description Function that initiates the API call to fetch the product list
   */
  private loadProducts() {
    this.isLoading = true;
    this.svc
      .getProducts(this.chosenCategory, this.limit, this.offset)
      .then((products) => {
        this.errorMsg = '';
        this.products = products;
      })
      .catch((error: Error) => {
        this.errorMsg = error.message;
      })
      .finally(() => (this.isLoading = false));
  }
}
