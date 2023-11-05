import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HistTextComponent } from './hist-text.component';

describe('HistTextComponent', () => {
  let component: HistTextComponent;
  let fixture: ComponentFixture<HistTextComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HistTextComponent]
    });
    fixture = TestBed.createComponent(HistTextComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
