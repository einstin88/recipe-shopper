import { Component } from '@angular/core';
import { ToastNotificationService } from 'src/app/services/toast-notification.service';

@Component({
  selector: 'app-toasts',
  templateUrl: './toasts.component.html',
  styleUrls: ['./toasts.component.css']
})
export class ToastsComponent {
 constructor(public toastSvc: ToastNotificationService) {}
}
