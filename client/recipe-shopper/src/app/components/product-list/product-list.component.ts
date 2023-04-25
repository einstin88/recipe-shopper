import { Component, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { Subject, Subscription } from 'rxjs';
import { Category } from 'src/app/model/category.model';
import { Product } from 'src/app/model/product.model';
import { ProductDataService } from 'src/app/services/product-data.service';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css'],
})
export class ProductListComponent implements OnInit, OnDestroy {
  constructor(private svc: ProductDataService) {}

  @Output()
  addProduct = new Subject<Product>();

  category!: Category | '';
  offset: number = 0;
  limit: number = 20;

  products: Product[] = [];
  sub$!: Subscription;

  ngOnInit(): void {
    this.sub$ = this.svc.products.subscribe(
      (products) => (this.products = products)
    );
  }

  ngOnDestroy(): void {
    this.sub$.unsubscribe();
  }

  addIngredient(product: Product) {
    this.addProduct.next(product);
  }

  updateCategory(category: Category | '') {
    this.category = category;
    // console.info(this.category);
    this.loadProducts();
  }

  updateProductList(offset: number) {
    this.offset = offset < 0 ? 0 : offset;
    // console.info(this.offset);
    this.loadProducts();
  }

  private loadProducts() {
    this.svc.getProducts(this.category, this.limit, this.offset);
  }
}
