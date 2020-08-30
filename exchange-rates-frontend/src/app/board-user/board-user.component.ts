import {Component, OnInit} from '@angular/core';
import {UserService} from '../_services/user.service';
import {CurrencyForm} from '../_models/currency-form';

@Component({
  selector: 'app-board-user',
  templateUrl: './board-user.component.html',
  styleUrls: ['./board-user.component.css']
})
export class BoardUserComponent implements OnInit {
  errorMessage = '';
  currency: CurrencyForm[];

  constructor(private userService: UserService) {
  }

  ngOnInit(): void {
    this.userService.getUserBoard().subscribe(
      data => {
        this.currency = data;
      },
      err => {
        this.errorMessage = err.error.message;
      }
    );
  }
}
