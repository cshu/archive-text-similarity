import { Component,EventEmitter,Input, Output } from '@angular/core';
import { SimResult } from '../sim-result';

@Component({
  selector: 'app-hist-text',
  templateUrl: './hist-text.component.html',
  styleUrls: ['./hist-text.component.css']
})
export class HistTextComponent {

  @Input() rows: SimResult[] = [];
  @Output() rowsChange = new EventEmitter<SimResult[]>();
}
