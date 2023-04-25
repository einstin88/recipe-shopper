import { Component, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { Subject, Subscription } from 'rxjs';
import { Product } from 'src/app/model/product.model';
import { ProductDataService } from 'src/app/services/product-data.service';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css'],
})
export class ProductListComponent implements OnInit, OnDestroy {
  constructor(private svc: ProductDataService) {}

  @Input()
  offset!: number;

  @Output()
  addProduct = new Subject<Product>();

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
}
