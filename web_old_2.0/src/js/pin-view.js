'use strict'


;(function(app) {

   app.PinView = Backbone.View.extend({
      tagName: 'div',
      className: 'xxxxxxxxxxxxxxxxx',
      //className: 'pin panel panel-warning',

      template: ctx => Mustache.render($('#panel-template').text(), ctx),

      events: {
         'dblclick .title': 'editTitle',
         'dblclick .content': 'editContent',
         'click .deletePin': 'deletePin'
      },

      initialize() {
         console.log('initializing new pin view')
      },

      render() {
         console.log('Rendering pin')
         let pin = this.template({
            title: this.model.get('title'),
            content: this.model.get('content'),
            creationDate: this.model.get('createdOn'),
            color: this.model.get('color')
         })
         //pin = $('' + pin)
         console.log(pin)
         this.el.innerHtml = pin
         return this
      },

      deletePin() {

      },

      editTitle(evt) {

      },

      editContent() {

      },



   })




})(window.bulletin = window.bulletin || {})






