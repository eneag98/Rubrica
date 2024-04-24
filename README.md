Tutte le richieste della traccia sono state implementate.</br>
Lascio il progetto aperto per eventuali revisioni e sviluppi futuri.

Per come è aggiornato al commit -a527bc1- l'eseguibile è creabile a patto di inserire nel Classpath del progetto eclipse/idea la libreria esterna connector\j di MySQL (cartella "lib" del progetto).
Inoltre, è importante copiare la cartella "icons" e incollarla tal quale nella stessa cartella dove viene creato l'eseguibile .jar .


L’utente che dovrà provare l’applicazione dovrà	in sequenza:
1. aprire e sostituire le credenziali del proprio sistema MySQL dentro "credenziali_database.properties"
3. lanciare sul proprio sistema MySQL lo script di costruzione database	"schema_database.sql"
4. eseguire il file .jar

NOTA: Nell'applicazione è stata implementata la funzione di Login. Come suggerito dalla traccia del progetto, vengono richiesti <username> e <password> per accedere alla rubrica vera e propria. A seconda della tipologia di utente che accede all'applicazione, sono rese disponibili o bloccate le funzionalità richieste della traccia del progetto (aggiunta, modifica e cancellazione dei contatti).

Una volta avviata l'applicazione .jar, inserire delle credenziali a scelta tra quelle elencate di seguito:
- username: admin	password: pwdAdmin24!	-> privilegi: sì
- username: turing	password: pwdTuring24!	-> privilegi: sì
- username: mrossi	password: pwdMario24!	-> privilegi: no
- username: dverdi	password: pwdDebora24!	-> privilegi: no
