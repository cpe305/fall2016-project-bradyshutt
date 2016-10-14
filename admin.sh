#!/bin/bash

CMD=null

echo "$#"
main() {
   check_args $@

   case "$CMD" in
      'start-server')
         if [[ $(can_start_server) ]]; then 
            start_server
         else
            echo "This command can only be run on the server."
         fi 
         ;; 

      'local-server')
         node server.js 8080
         ;;

      '*')
         echo "Command not recognized."
         ;;
   esac
   echo "Exiting"
   exit
}

can_start_server() {
   if [[ $(hostname -s) -eq 'ip-172-31-10-167' && $(whoami) -eq 'root' ]]; then 
      echo "You can start server"
      return true
   else
      echo "You cannot start server"
      return false
   fi
}

start_server() {
   echo "Starting server..."
   node server.js 8080 >> log.txt &
}

check_args() {
   if [ "$#" -lt 1 ]; then 
      echo "No arguments supplied"
      exit
   else 
      CMD=$1 
   fi
}


main $@
