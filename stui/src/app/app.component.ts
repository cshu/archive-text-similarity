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
    if (!this.textFile.nativeElement.files.length) {
      this.textFile.nativeElement.setCustomValidity('Please choose a file for archiving.');
      this.textFile.nativeElement.reportValidity();
      return;
    }
    if (this.textFile.nativeElement.files[0].size < 100) {
      this.textFile.nativeElement.setCustomValidity('Small files are not allowed.');
      this.textFile.nativeElement.reportValidity();
      return;
    }
    this.textFile.nativeElement.setCustomValidity('');
    //if (!this.textFile.nativeElement.reportValidity()){
    //  console.log('no file');
    //  return;
    //}
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
