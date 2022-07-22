# GoCarrot
## Progetto scolastico

GoCarrot è un gioco inventato per un progetto scolastico.

## Features
- Multipiattaforma (gioca da qualsiasi computer: osx, windows, linux)
- Modalità online (grazie all'utilizzo dei socket)
- Creazione delle mappe direttamente da un semplice file
- Suoni e musica di sottofondo

## Come giocare
Nessuna libreria aggiuntiva è richiesta

Per giocare basta compilare ed eseguire attraverso un terminale o se preferite un IDE

Prima di avviare il Client bisogna avviare il server.
Prima di tutto però spostati nella directory /src/it/fdb/gocarrot/server, quindi esegui
```sh
javac Server.java
```
e per eseguire
```sh
java Server
```

Per eseguire il gioco vero e proprio, spostati dentro /src/it/fdb/gocarrot e scrivi
```sh
javac App.java
```
e per eseguire
```sh
java App
```

## Note
Il client è in ascolto nella porta 5050 della rete locale
```sh
127.0.0.1:5050
```
Se si desidera cambiare il comportamento, per ora, è possibile farlo solo tramite codice,
aprire il file Client.java (situato in /src/it/fdb/gocarrot) e modificare l'indirizzo a riga 20
Esempio:
``` socket = new Socket("192.123.34.112", 4020);```

## Cose importanti
Da una celebre citazione di Dante Alighieri mentre si trovava in paradiso dinanzi ai Develooopers Horse

> Si certo, GoCarrot è un ottimo gioco
> ma vi consiglio di provare anche l'amatissimo,
> ma proprio amatissimo [Navicella Cavallo](https://github.com/favmatteo/navicella-cavallo)

Questo progetto non è un buon esempio di corretto stile di programmazione, è stato creato solamente come progetto scolastico.
