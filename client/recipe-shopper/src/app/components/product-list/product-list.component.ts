import { Component, Input, Output } from '@angular/core';
import { Subject } from 'rxjs';
import { Product } from 'src/app/model/product.model';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css'],
})
export class ProductListComponent {
  constructor() {}

  @Input()
  products!: Product[];
  @Input()
  offset!: number;

  @Output()
  addProduct = new Subject<Product>();

  addIngredient(product: Product) {
    this.addProduct.next(product);
  }
}
