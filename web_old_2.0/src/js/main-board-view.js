'use strict'


;(function(app) {

   let MainBoardView = Backbone.View.extend({
      el: '#all-pins',

      events: {
         'click #new-pin-button': 'startNewPin'

      },
      
      initialize() {
         console.log('init main boards view ')
         this.$pinsWrapper = this.$('#pin-filter')
         this.$pinsContainer = this.$('#pins-container')
      },

      initBoard(board) {
         this.listenTo(board.pins, 'add', this.newPin)
         this.render(board)
      },

      render(board) {
         this.currentBoard = board
         console.log('Rendering this boards pins')
         console.log(this.$pinsContainer)
         this.$pinsContainer.empty()
         board.pins.forEach((pin) => {
            let pinView = new app.PinView({
               model: pin
            })
            this.$pinsContainer.prepend($(pinView.render().$el.text()))
         })
      },

      startNewPin() {
         let pin = {
            title: 'New Pin!',
            createdOn: new Date(),
            content: 'Pin content',
            color: 'yellow'
         }
         this.currentBoard.pins.add(pin)
      },

      newPin(pin) {
         console.log('new pinnging~')
         let pinView = new app.PinView({model: pin})
         this.$pinsContainer.prepend(
            $(pinView.render().addClass('editing').$el.text()))
         console.log(pinView.$el)//.addClass('editing')
      }
   })

   app.mainBoardView = new MainBoardView()

   
})(window.bulletin = window.bulletin || {})


