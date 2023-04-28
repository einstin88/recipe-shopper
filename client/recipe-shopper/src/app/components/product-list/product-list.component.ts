import { Component, OnDestroy, OnInit, Output } from '@angular/core';
import { Subject, Subscription } from 'rxjs';
import { Category } from 'src/app/model/category.model';
import { Product } from 'src/app/model/product.model';
import { ProductDataService } from 'src/app/services/product-data.service';

/**
 * @description A reusable component for displaying the list of products in a chosen category. Works with {@link CategoryChoserComponent}
 */
@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css'],
})
export class ProductListComponent implements OnInit, OnDestroy {
  constructor(private svc: ProductDataService) {}

  @Output()
  addProduct = new Subject<Product>();

  // Variables
  category!: Category | '';
  offset: number = 0;
  limit: number = 20;

  products: Product[] = [];
  private sub$!: Subscription;

  ngOnInit(): void {
    this.sub$ = this.svc.products.subscribe(
      (products) => (this.products = products)
    );
  }

  ngOnDestroy(): void {
    this.sub$.unsubscribe();
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
  updateCategory(category: Category | '') {
    this.category = category;
    // console.info(this.category);
    this.loadProducts();
  }

  /**
   * @description Function to handle the clicking of the Pagination buttons
   * 
   * @param offset The starting index of the product list to display
   */
  updateProductList(offset: number) {
    this.offset = offset < 0 ? 0 : offset;
    // console.info(this.offset);
    this.loadProducts();
  }

  /**
   * @description Function that initiates the API call to fetch the product list
   */
  private loadProducts() {
    this.svc.getProducts(this.category, this.limit, this.offset);
  }
}
