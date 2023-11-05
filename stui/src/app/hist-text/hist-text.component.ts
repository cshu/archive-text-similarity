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
  items = ['Item 1', 'Item 2', 'Item 3', 'Item 4', 'Item 5'];
  expandedIndex = 0;
  @Input() rows: SimResult[] = [];
  @Output() rowsChange = new EventEmitter<SimResult[]>();
}
