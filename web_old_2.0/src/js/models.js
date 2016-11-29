'use strict'


;(function(app) {

   app.Pin = Backbone.Model.extend({
      initialize() {
         console.log('Creating a new pin')
      },
      delete() {
         /* Delete this pin */
      },
      update() {
      }
   })


   app.PinsCollection = Backbone.Collection.extend({
      model: app.Pin,
      initialize() {
         console.log('Creating a new pins collection')
      }
   })
   

   app.Board = Backbone.Model.extend({
      initialize(board) {
         console.log('creating a new board: ' + this.get('name'))
         this.pins = new app.PinsCollection() 
         if (board.pins)
            board.pins.forEach( pin => this.pins.add(pin))
      },
   
   })

   let BoardsCollection = Backbone.Collection.extend({
      model: app.Board,
      initialize() {
         console.log('Creating a new collection of boards')
      }
   })

  app.boards = new BoardsCollection()


})(window.bulletin = window.bulletin || {})


