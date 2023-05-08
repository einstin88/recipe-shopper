import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { AuthDataService } from 'src/app/services/auth-data.service';
import { User } from 'src/app/model/user.model';

@Component({
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css'],
})
export class RegistrationComponent implements OnInit {
  constructor(private fb: FormBuilder, private svc: AuthDataService) {}

  isPasswordMatched: boolean | null = null;
  registrationForm!: FormGroup;

  confirmPassword = new FormControl('');

  ngOnInit(): void {
    this.initForm();
  }

  private initForm() {
    this.registrationForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(3)]],
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.minLength(5)]],
    });
  }

  processform() {
    const newUser = this.registrationForm.value as User;
    console.log('>>> New User: ', newUser);
  }

  confirmPasswordMatched() {
    const password = this.registrationForm.get('password')!.value as string;
    const confirmPassword = this.confirmPassword.value as string;

    if (password == '' || confirmPassword == '') return;

    console.info('>>> confirmed password: ', confirmPassword);

    if (password != confirmPassword) {
      this.isPasswordMatched = false;
      return;
    }
    this.isPasswordMatched = true;
  }
}
