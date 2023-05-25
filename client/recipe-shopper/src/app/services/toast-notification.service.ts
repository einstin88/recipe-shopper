import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ToastNotificationService {
  toasts: ToastInfo[] = [];

  show(header: string, body: string) {
    this.toasts.push({ header, body });
  }

  remove(toast: ToastInfo) {
    this.toasts = this.toasts.filter((toa) => toa != toast);
  }
}

export type ToastInfo = {
  header: string;
  body: string;
};
