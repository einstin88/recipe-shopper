import {
  HttpClient,
  HttpErrorResponse,
  HttpParams,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Product } from '../model/product.model';
import { Subject, firstValueFrom } from 'rxjs';
import { Category } from '../model/category.model';
import { EP_GET_PRODUCTS, EP_PARSE_HTML } from '../utils/urls';

/**
 * @description Service to handle API calls related to {@link Product} transactions
 */
@Injectable({
  providedIn: 'root',
})
export class ProductDataService {
  constructor(private http: HttpClient) {}

  /**
   * @description Produces stream of Products when {@link getProducts} is called
   */
  products = new Subject<Product[]>();

  /**
   * @description API call to retrieve the list of products from a chosen {@link Category}
   *
   * @param category The chosen category of products
   * @param limit Max number of products to retrieve
   * @param offset The starting index of the results to retrieve
   *
   * @returns Promise with a list of products in the chosen {@link category}
   */
  getProducts(category: Category, limit: number, offset: number) {
    const url = EP_GET_PRODUCTS + category;
    const params = new HttpParams().appendAll({ limit, offset });

    return firstValueFrom(this.http.get<Product[]>(url, { params })).catch(
      (error: HttpErrorResponse) => {
        console.error(`Error response: ${error.message}`);
        throw new Error(`${error.status}: ${error.statusText}`);
      }
    );
  }

  /**
   * @description API call to backend for parsing products from the submitted Html
   *
   * @param payload - the multi-part category and file to send to backend for parsing
   * @returns A void promise. No callback handling required.
   */
  parseHtml(payload: FormData) {
    const url = EP_PARSE_HTML;

    return firstValueFrom(this.http.post<void>(url, payload));
  }
}
