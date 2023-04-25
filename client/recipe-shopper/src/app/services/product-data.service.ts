import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Product } from '../model/product.model';
import { Subject, firstValueFrom, tap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ProductDataService {
  constructor(private http: HttpClient) {}

  #API_URL = 'api/';
  #API_PRODUCTS = 'products/';

  products = new Subject<Product[]>();

  getProducts(category: string, limit: number, offset: number) {
    if (!category) {
      this.products.next([]);
      return;
    }

    const url = this.#API_URL + this.#API_PRODUCTS + category;
    const params = new HttpParams().appendAll({ limit, offset });

    return firstValueFrom(
      this.http
        .get<Product[]>(url, { params })
        .pipe(tap((products) => this.products.next(products)))
    );
  }
}
