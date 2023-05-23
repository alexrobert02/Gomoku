# Laboratorul nr. 10

## Homework
* Implement functionalities of the game, using the classes Game, Board, Player, etc. (solved) ("Am creat clasele *Board*, *Player* si *Game* care se ocupa de gestionarea jocului. Fiecarui player is se ataseaza un socket pentru a se putea conecta la joc.)
* The clients will send to the server commands such as: create game, join game, submit move, etc. (solved) (Comanda create *name* creeaza un joc si player-ului i se ataseaza acel nume. Cel de-al doilea player trebuie sa dea join folosind join *altNume*. Apoi, ambii jucatori au acces la comanda move *pozitieX* *pozitieY*)
* The server is responsible with the game management and mediating the players. (solved) (Clasa *GameServer* gestioneaza celelate clase pentru efectuarea unui joc.)
* The games will be played under time control (blitz) with each player having the same amount of time at the beginning of the game. If a player's time runs out, the game is lost. (solved) (Folosind un obiect din clasa *Timer*, pastrez evidenta fiecarei ture in parte, astfel incat ca timpul sa se opreasca cand se termina tura jucatorului 1 si incepe tura celui de-al doilea jucator, acesta reluandu-se.)