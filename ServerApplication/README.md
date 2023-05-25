# Laboratorul nr. 11

## Homework
* Create REST services for:
  * adding a new player, via a HTTP POST request. (solved) (Metoda *addPlayer* ce foloseste *@PostMapping* primeste ca parametru un obiect de tip *Player* si il adauga in baza de date.)
  * modifying the name of a player, via a HTTP PUT request. (solved) (Metoda *updatePlayerName* ce foloseste *@PutMapping("/{id}")* primeste ca parametru id-ul unui player si un nou nume pe care il va actualiza corespunzator id-ului primit.)
  * deleting a player, via a HTTP DELETE request. (solved) (Metoda *deletePlayer* ce foloseste *@DeleteMapping("/{id}")* primeste ca parametru id-ul unui player si acesta sterge player-ul respectiv din baza de date.)
* Create a REST service for obtaining the games that were recorded by the server. (solved) (In clasa *GameHistoryController* am creat metoda *getAllGameHistory()* ce foloseste *@GetMapping* pentru a putea obtine istoricul meciurilor pe care l-am creat folosind metoda *addGameHistory* cu *@PostMapping*.)
* Create a simple client application that invokes the services above, using the support offered by Spring Boot. (solved) (In clasa *HomeworkApplication* am invocat serviciile *POST*, *PUT*, *DELETE* SI *GET* pentru prelucrarea datelor.)
* Document your services using Swagger or a similar tool. (solved) (Am adaugat dependintele necesare in pom.xml pentru a putea utiliza Swagger in scopul documentarii serviciilor *REST*.)
