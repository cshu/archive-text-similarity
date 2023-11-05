import { Component, ElementRef, ViewChild } from '@angular/core';
import { DefService } from './def.service';
import { Modal } from 'bootstrap';
import { RandomToken } from './random-token';
import { SimResp } from './sim-resp';
import { SimPair } from './sim-pair';
import { SimResult } from './sim-result';
import { HistTextComponent } from './hist-text/hist-text.component';

//note binding html elements with fields below means Proxy Pattern

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
  //@ViewChild('ht') ht:HistTextComponent;

  readonly minSize: number = 100;
  //readonly recvLog = (msgev: MessageEvent) => { console.log('recv', msgev); };

  es: EventSource | undefined;
  sock: WebSocket | undefined;
  title = 'stui';
  pastedText: string = '';
  //sid: string = '';
  sseid: string = '';
  allowUpload: boolean = false;
  defdialog: Modal | undefined;
  //recv: (msgev: MessageEvent) => void = this.recvLog;
  //filesInTransfer: string[] = [];
  fileInTransfer: string = '';
  htrows: SimResult[] = [];
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
    //this.defdialog.show();
    console.log(ev);
    console.log('submit text');
    if (!this.directText.nativeElement.reportValidity()) {
      console.log('text is too short');
      return;
    }
    console.log(this.pastedText);
    this.upload('Untitled', new Blob([this.pastedText]));
  }
  upload(filename: string, text: Blob) {
      this.allowUpload = false;
    let sock = this.sock!;
    sock.send(text);
    //this.filesInTransfer.push(filename);
    this.fileInTransfer = filename;
    this.htrows.unshift({ error: '', hash: '', name: filename, others: ([] as SimPair[]), type: 'newreq' } as SimResult);

    //undone add row to the loading area
    //
    ////this.allowUpload = false;
    ////this.recv = (msgev: MessageEvent) => {
    ////  sock.send(JSON.stringify({ action: 'similarity', name: filename, hash: msgev.data }));
    ////  this.allowUpload = true;
    ////  this.recv = this.recvLog;
    ////  //undone add pending task to list
    ////};
    //let resp = this.def.send(this.sid, filename, text);
    //resp.subscribe((data: string) => {
    //  console.log('RESP', data);
    //});
  }
  ngOnInit() {
    console.log('oninit');
    this.defdialog = new Modal('#defdialog', { keyboard: true });
    this.def.token().subscribe((data: RandomToken) => {
      this.sseid = data.token;
      this.allowUpload = true;
      this.es = new EventSource(location.protocol + '//' + location.hostname + ':8081/sse?id=' + encodeURIComponent(this.sseid));
      this.es.onmessage = (ev) => {
        console.log('onmsg', ev.data);
        let msg = JSON.parse(ev.data);
        switch (msg.type) {
          case 'established':
            console.log('est');
            break;
          case 'sim':
            console.log('sim');
            console.log(msg);
            let simresult = msg as SimResult;
            let idx = this.htrows.findIndex((elem) => elem.hash === simresult.hash);
            if (-1 === idx) this.htrows.push(simresult);
            else if (this.htrows[idx].others.length !== simresult.others.length) this.htrows[idx] = simresult;
            break;
        }
      };
    });
    this.sock = new WebSocket('ws://' + location.hostname + ':8080/defHandler');
    this.sock.onopen = (ev) => { console.log('sock open'); };
    this.sock.onclose = (ev) => { console.log('sock close'); };
    this.sock.onerror = (ev) => { console.log('sock error', ev); };
    this.sock.onmessage = (ev) => {
      console.log('sock message', ev.data);
      let newfilename = this.fileInTransfer;
      this.fileInTransfer = '';
      this.allowUpload = true;
      let idx = this.htrows.findIndex((elem) => elem.hash === ev.data);
      if (-1 !== idx) this.htrows.splice(idx, 1);
      this.htrows[0].hash = ev.data;
      this.def.sim(this.sseid, ev.data, newfilename).subscribe((data: SimResp) => {
        console.log('simresp', data);
        switch (data.result) {
          case 'OK':
            break;
          case 'END':
            //todo remove spinning icon at bottom of page because nothing more to load
            break;
          default:
            //todo show some error message
            break;
        }
      });
      //if (!this.sid) this.sid = ev.data;
      //this.recv(ev);
    };
  }
}
