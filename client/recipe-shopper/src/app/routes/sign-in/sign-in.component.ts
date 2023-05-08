import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthPayLoad } from 'src/app/model/authentication-payload.model';
import { AuthDataService } from 'src/app/services/auth-data.service';

@Component({
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.css'],
})
export class SignInComponent implements OnInit {
  constructor(private fb: FormBuilder, private svc: AuthDataService) {}

  signinForm!: FormGroup;

  ngOnInit(): void {
    this.initForm();
  }

  private initForm() {
    this.signinForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(3)]],
    });
  }

  processForm() {
    const credentials = this.signinForm.value as AuthPayLoad;
    console.debug('>>> Credentials: ', credentials);

    this.svc.loginUser(credentials);
  }
}
