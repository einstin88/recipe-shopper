import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { AuthDataService } from 'src/app/services/auth-data.service';
import { User } from 'src/app/model/user.model';
import { Router } from '@angular/router';

@Component({
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css'],
})
export class RegistrationComponent implements OnInit {
  constructor(
    private fb: FormBuilder,
    private router: Router,
    private svc: AuthDataService
  ) {}

  confirmPassword = new FormControl('');
  errorMsg: string = '';
  isPasswordMatched: boolean | null = null;

  registrationForm!: FormGroup;

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
    console.info('>>> New User: ', newUser);

    this.svc
      .registerUser(newUser)
      .then(() => {
        this.registrationForm.reset();
        this.confirmPassword.reset();
        this.router.navigate(['/']);
      })
      .catch((error: Error) => {
        this.errorMsg = error.message;
        console.debug('Registration error! ', this.errorMsg);
      });
  }

  confirmPasswordMatched() {
    const password = this.registrationForm.get('password')!.value as string;
    const confirmPassword = this.confirmPassword.value as string;

    if (password == '' || confirmPassword == '') return;

    console.debug('>>> confirmed password: ', confirmPassword);

    if (password != confirmPassword) {
      this.isPasswordMatched = false;
      return;
    }
    this.isPasswordMatched = true;
  }
}
