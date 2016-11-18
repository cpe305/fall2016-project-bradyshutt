'use strict'

$(() => {


   $('#moreAttributes').on('click', () => {
      $('#dataAttributes').append(

        //$('<pre>      "<input class="jsonInput" type="text" name="attribute"><span>" : "</span><input class="jsonInput" type="text" name="value"><span>"</span><input type="button" class="removeData" value="X"><br></pre>'))
        $('<span><pre>      "</pre><input class="jsonInput" type="text" name="attribute" class="attribute"><span>" : "</span><input class="jsonInput" type="text" name="value" class="value"><span>,"</span><img src="images/x-wrong-no-cross.png" class="removeData"><br></span>'))

      console.log('hi')
   }) 


   $('#dataAttributes').on('click', function(event) {
      let target = $(event.target) 
      let parent = target.parent()
      if (target[0].className === "removeData") {
         parent[0].remove()
      }
   })

   $('#sendRequest').on('click', (event) => {
      let route = $('#route').val()
      let data = { }
      let keytemp = null
      let attribs = $('#dataAttributes').children().find('input')
         .each(function (idx) {
            let val = $(this).val()
            console.log($(this).val())
            if (!(idx % 2)) {
               keytemp = val
            } else {
               data[keytemp] = val
            }
      })
      console.log('reqData:', data)
      $.ajax({
         url: '/endpoint',
         method: 'post',
         data: {
            'route': route,
            'data': data
         },
         format: 'json',
         success: function(data) {
            $('#responseDiv').empty()
            $('#responseDiv').append('<pre>' + JSON.stringify(JSON.parse(data), null, 2) + '</pre>')
            console.log('success')
            console.log('resData:', data)
         }

      })
      
   }) 

   
})




