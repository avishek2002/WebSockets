# WebSockets
Course : Newtwork and Distributed Computing

Programmer : Avishek

Last Modified : 9/9/2024

## Execution
Make sure Server.java and countries_capitals.csv files are in the same directory.
1. java Server.java <port_number>
2. java Client.java <host_name> <port_number>

## Protocol description
Integers are used to represent server functions to ensure consistency between the server and the client. This allows for minimal data transmission for the client to specify what function it wants to run.
1. Retrieve capital from the server for a user specified country
2. Retrieve estimated population from the server for a user specified country
3. Add a country, capital pair (specified by the user) to the servers memory

-1. Exit the program

Additionally, some HTTP response status codes have been used to provide response back to the client.
1. Response code 201 means the requested has been successfully fulfilled. Eg. when a new country, capital pair is entered, the server adds it to its memory and issues a response code 201.
2. Response code 403 means the client requested a forbidden request. Eg. when a pre-existing country, capital pair is entered, the servers doesn't duplicate the pair in its memory and issues a response code 403.