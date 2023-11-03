import { Component, ElementRef, ViewChild } from '@angular/core';
import { DefService } from './def.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  constructor(private def: DefService) {
  }
  @ViewChild('textFile', { static: false }) public textFile!: ElementRef;
  @ViewChild('directText', { static: false }) public directText!: ElementRef;

  readonly minSize: number = 100;

  sock: WebSocket | undefined;
  title = 'stui';
  pastedText: string = '';
  sid: string = '';
  submitFile(ev: Event) {
    console.log(ev);
    console.log('submit file');
    if (!this.textFile.nativeElement.files.length) {
      this.textFile.nativeElement.setCustomValidity('Please choose a file for archiving.');
      this.textFile.nativeElement.reportValidity();
      return;
    }
    let fileobj = this.textFile.nativeElement.files[0];
    if (fileobj.size < this.minSize) {
      this.textFile.nativeElement.setCustomValidity('Small files are not allowed.');
      this.textFile.nativeElement.reportValidity();
      return;
    }
    this.textFile.nativeElement.setCustomValidity('');
    //if (!this.textFile.nativeElement.reportValidity()){
    //  console.log('no file');
    //  return;
    //}
    this.upload(fileobj.name, fileobj);
  }
  submitText(ev: Event) {
    console.log(ev);
    console.log('submit text');
    if (!this.directText.nativeElement.reportValidity()){
      console.log('text is too short');
      return;
    }
    console.log(this.pastedText);
    this.upload('Untitled', new Blob([this.pastedText]));
  }
  upload(filename: string, text: Blob) {
    //console.log(location);
    //sock.send()
    let resp = this.def.send(this.sid, filename, text);
    resp.subscribe((data: string) => {
      console.log('RESP', data);
    });
  }
  ngOnInit() {
    console.log('oninit');
    this.sock = new WebSocket('ws://'+location.hostname+':8080/defHandler');
    this.sock.onopen = (ev) => { console.log('sock open'); };
    this.sock.onclose = (ev) => { console.log('sock close'); };
    this.sock.onerror = (ev) => { console.log('sock error', ev); };
    this.sock.onmessage = (ev) => {
      console.log('sock message', ev.data);
      if (!this.sid) this.sid = ev.data;
    };
  }
}
