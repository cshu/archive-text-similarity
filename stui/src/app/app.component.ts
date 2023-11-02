import { Component, ElementRef, ViewChild } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  @ViewChild('textFile', { static: false }) public textFile!: ElementRef;
  @ViewChild('directText', { static: false }) public directText!: ElementRef;
  title = 'stui';
  pastedText: string = '';
  submitFile(ev: Event) {
    console.log(ev);
    console.log('submit file');
    if (!this.textFile.nativeElement.reportValidity()){
      console.log('no file');
      return;
    }
  }
  submitText(ev: Event) {
    console.log(ev);
    console.log('submit text');
    if (!this.directText.nativeElement.reportValidity()){
      console.log('text is too short');
      return;
    }
  }
}
