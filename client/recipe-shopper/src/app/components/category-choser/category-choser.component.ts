import { Component, Output } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Subject } from 'rxjs';
import { Category } from 'src/app/model/category.model';
import { Categories } from 'src/app/utils/consts';

/**
 * @description A reusable component for controlling the category of products to display
 */
@Component({
  selector: 'app-category-choser',
  templateUrl: './category-choser.component.html',
  styleUrls: ['./category-choser.component.css'],
})
export class CategoryChoserComponent {
  @Output()
  category = new Subject<Category | ''>();

  // Variables
  categorySelector = new FormControl<Category | null>(null);
  categories = Categories;

  /**
   * @description Function to handle the change event of the drop down list
   */
  loadProducts() {
    const category = this.categorySelector.value as Category | '';
    // console.info(category);

    this.category.next(category);
  }
}
