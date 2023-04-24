import { Component, Output } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Subject } from 'rxjs';
import { Category } from 'src/app/model/category.model';
import { Categories } from 'src/app/utils/consts';

@Component({
  selector: 'app-category-choser',
  templateUrl: './category-choser.component.html',
  styleUrls: ['./category-choser.component.css'],
})
export class CategoryChoserComponent {
  @Output()
  category = new Subject<Category | ''>();

  categorySelector = new FormControl<Category | null>(null);
  categories = Categories;

  loadProducts() {
    const category = this.categorySelector.value as Category | '';
    // console.info(category);

    this.category.next(category);
  }
}
