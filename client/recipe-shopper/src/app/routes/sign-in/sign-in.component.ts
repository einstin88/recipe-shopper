import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthPayLoad } from 'src/app/model/authentication-payload.model';
import { AuthDataService } from 'src/app/services/auth-data.service';

@Component({
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.css'],
})
export class SignInComponent implements OnInit {
  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private svc: AuthDataService
  ) {}

  errorMsg: string = '';
  redirectPath: string = '/';
  signinForm!: FormGroup;

  ngOnInit(): void {
    if (this.route.snapshot.queryParamMap.has('redirect'))
      this.redirectPath = this.route.snapshot.queryParamMap.get('redirect')!;

    console.debug('>>> Redirect path: ', this.redirectPath);

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

    this.svc
      .loginUser(credentials)
      .then(() => {
        this.signinForm.reset();
        this.router.navigate([this.redirectPath]);
      })
      .catch((error: Error) => {
        this.errorMsg = error.message;
        console.debug('Sign in Error! ', this.errorMsg);
      });
  }
}