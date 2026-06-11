import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Demo } from './demo/demo';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, Demo],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {}