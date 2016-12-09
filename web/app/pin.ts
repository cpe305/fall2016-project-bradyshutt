'use strict';

export class Pin {
  id: number;
  title: string;
  content: string;

  constructor(attributes) {
    this.id = attributes.id || null;
    this.title = attributes.title;
    this.content = attributes.content;
  }
}



