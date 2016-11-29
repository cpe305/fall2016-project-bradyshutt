'use strict'


;(function(app) {

   app.BoardsView = Backbone.View.extend({

      el: '#boards-list',

      template: ctx => Mustache.render($('#boards-list-template').text(), ctx),

      events: {
         'click #new-board-button': 'createNewBoard',
         'blur .edit': 'saveBoardName',
         'dblclick .list-group-item': 'editBoardName',
         'click .list-group-item': 'openBoard',
      },

      initialize() {
         console.log('Creating a new BoardsList')
         this.$list = this.$('.list-group')
         this.$newBoardBtn = this.$('#new-board-button')

         this.listenTo(app.boards, 'add', this.addBoard)
      },

      render() {
         this.$list.html('')
         app.boards.forEach((board) => {
            this.addBoard(board)
         })
      },

      addBoard(board) {
         console.log('adding board to list!')
         //$(`<a class="list-group-item" href="">${board.name}</a>`)
         $(this.template(board.toJSON()))
            .appendTo(this.$list)
      },

      createNewBoard() {
         console.log('start creating a new board')
         let newBoardLine = $($('#boards-list-edit-template').text())
         this.$list.append(newBoardLine)
         newBoardLine.find('input').focus()
         console.log(newBoardLine)
      },

      saveBoardName(evt) {
         let boardName = evt.currentTarget.value
         if (boardName = boardName.trim())
            console.log('Save board name: ' + boardName)
         else {
            let oldName = $(evt.target.parentElement).find('.name').text()
            boardName = (oldName? oldName : 'New Board')
         }

         evt.target.parentElement.remove()
         app.boards.add({name: boardName})

      },

      editBoardName(evt) {
         let initialValue = $(evt.currentTarget).find('.name').text()
         console.log('init val: ' + initialValue)
         let editEl = $(evt.currentTarget)
            .addClass('editing')
            .find('.edit')
            .attr('placeholder', initialValue)
            .focus()
      },

      openBoard(evt) {
         let boardName = $(evt.currentTarget).find('.name').text()
         console.log(evt.currentTarget)
         console.log('bname: ' + boardName)
         let board = app.boards.findWhere({name: boardName})
         console.log(board)
         app.mainBoardView.initBoard(board)
      }
      
   })

})(window.bulletin = window.bulletin || {})

