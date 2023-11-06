import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { NgFor, NgIf } from '@angular/common';
import { CdkAccordionModule } from '@angular/cdk/accordion';
import { SimResult } from '../sim-result';

@Component({
  selector: 'app-hist-text',
  templateUrl: './hist-text.component.html',
  styleUrls: ['./hist-text.component.css'],
  standalone: true,
  imports: [CdkAccordionModule, NgFor, NgIf],
})
export class HistTextComponent implements OnChanges {
  ngOnChanges(changes: SimpleChanges): void {
    //throw new Error('Method not implemented.');
  }//
  @Input() rows: SimResult[] = [];
  @Output() rowsChange = new EventEmitter<SimResult[]>();
  onDownload(hash: string) {
    //fixme magic number for port
    open(location.protocol + '//' + location.hostname + ':8081/download/'+hash, '_blank')?.focus();
  }
}
