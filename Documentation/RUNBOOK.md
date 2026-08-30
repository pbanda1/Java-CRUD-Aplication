 # O APLIKACIJI
 Sustav izradio Pino Banda, 
 student 2. godine smijera Programsko Inženjerstvo
 Kolovoz 2026.
 
# TEHNOLOGIJE 
Ova aplikacija koristi tehnologije H2 za bazu podataka, 
SLF4J + LOGBACK za logiranje,
JACKSON (JSON parsiranje vanjskog API-ja)
DOM, StAX(XML uvoz/izvoz organizacija, XML log korisničkih akcija, XML konfiguracije sustava)

# PRIJAVA

Sustav dolazi  sa dva unaprijed pripremljena korisnika Admin i User
Administrator ima pristup svim funkcijama aplikacije (CRUD, uvoz/izvoz, dodjela volonetra itd.)
Korisnik ima privilegiju samo pretraživati Organizacije po predefiniranim aspektima.

# FUNKCIONALNOSTI 

Prijava i odjava korisnika, registracija korisnika.

Pregled i pretraga organizacija (po naslovu, misiji, cilju i državi)

Admin ima mogućnost dodavanja, ažuriranja i brisanja organizacije.

Omogućena je dodjela Volontera Organizacijama preko zasebnog prozora čija se 
funkcionalnost odvija u Assign Controller-u.

Uvoz država preko vanjskog REST API-ja (CountriesNOW) odvija se na pozadinskoj 
dretvi preko  Task funkcionalnosti kako nebi došlo do zamrzivanja aplikacije, a uvoz i izvoz
organizacija se isto tako odvija po ovome principu

Uvoz organizacija iz XML datoteke se odvija preko DOM pristupa a izvoz preko StAX pristupa

Konfiguracija aplikacije (spajanje na bazu, dimenzije prozora) učitava se iz XML datoteke
preko ConfigParsera i DatabaseUtil funkcije loadConfig(), i to sve prije static blocka unutar DBUtila kako
bi se config valjano mogao povući i proslijediti static blocku.

Omogućeno je i informacijsko logiranje i error logiranje unutar aplikacije unutar catch 
blokova iznimaka a isto tako i logiranje korisničkih akcija unutar aplikacije.

Admin je u mogućnosti na klik MenuItema clear-ati cijelu bazu stoga je potrebno izaći iz aplikacije
kako bi se ona opet instancirala i popunila vrijednostima koje smo zadali prilikom insert naredbi u init.SQL

