import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Product } from '../model/product.model';
import { firstValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ProductDataService {
  constructor(private http: HttpClient) {}

  #API_URL = 'api/';
  #API_PRODUCTS = 'products/';

  getProducts(category: string) {
    const url = this.#API_URL + this.#API_PRODUCTS + category;
    // const params = new HttpParams()

    return firstValueFrom(this.http.get<Product[]>(url));
  }
}
