import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Category } from 'src/app/model/category.model';
import { ProductDataService } from 'src/app/services/product-data.service';
import { Categories } from 'src/app/utils/consts';

/**
 * @description A component associated a router path for displaying the form for submitting a html file for products parsing
 */
@Component({
  templateUrl: './parse-html.component.html',
  styleUrls: ['./parse-html.component.css'],
})
export class ParseHtmlComponent implements OnInit {
  constructor(private fb: FormBuilder, private svc: ProductDataService) {}

  @ViewChild('htmlFile')
  htmlFile!: ElementRef;

  //  Variables
  categories = Categories;
  form!: FormGroup;

  ngOnInit(): void {
    this.initForm();
  }

  /**
   * @description Initializes the file upload {@link form}
   */
  private initForm() {
    this.form = this.fb.group({
      category: ['', [Validators.required]],
      file: ['', [Validators.required]],
    });
  }

  /**
   * @description Takes the {@link form}'s data and transform it into the multipart format for the {@link ProductDataService} to trasmit to the backend for parsing
   */
  processForm() {
    const category = this.form.get('category')?.value as Category;
    const file = (this.htmlFile.nativeElement as HTMLInputElement).files![0];
    // console.info(`>>> Category: ${category} | File: ${file.name}`);

    const payload = new FormData();
    payload.set('category', category);
    payload.set('file', file);

    this.svc.parseHtml(payload);
  }
}
