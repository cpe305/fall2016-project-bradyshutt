'use strict'


;(function(app) {
   
   $(function pageReady() {

      let boardsView = new app.BoardsView()

      boardsView.render()

      let board = {
         "owner": "braaady",
         "name": "My Second Board",
         "boardID": "57e0abbf8b8c8c51ad861138",
         "pins": [
            {
               "title": "lit",
               "boardID": "57e0abbf8b8c8c51ad861138",
               "createdOn": 1474343892515,
               "content": "Wow this app is lit",
               "color": "yellow"
            },
            {
               "title": "Second Pin!",
               "boardID": "57e0abbf8b8c8c51ad861138",
               "createdOn": 1474343972988,
               "content": "yer mum",
               "color": "blue"
            }
         ]
      }

      app.boards.add(board)

      
   })


})(window.bulletin = window.bulletin || {})
