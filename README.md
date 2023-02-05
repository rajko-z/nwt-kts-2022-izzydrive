# nwt-kts-2022-izzydrive



Članovi tima:
- Nevena Prokić
- Nataša Laković
- Rajko Zagorac

## Pokretanje

### Poreban softver

- Verzija jave >= 11
- PostgreSQL baza podataka
- npm
- node.js >= 16
- maven

### Server

##### Baza podataka:

- url=jdbc:postgresql://localhost:5432/izzydrive
- username=postgres
- password=admin

Učitati sve potrebne biblioteke iz pom.xml fajla i pokrenuti server.

### Klijent

Pozicionirati se u frontend folder i pokrenuti komande redom:
- npm install
- ng serve (ili npm start)

### Testni podaci
Svi testni podaci se mogu pronaći u backend/src/main/resources/data-postgres.sql

Npr:
- admin -> admin0@gmail.com
- vozač -> mika@gmail.com
- putnik -> john@gmail.com
- lozinka za sve je 12345678(ili 123 ako prvo ne prodje)


Nakon što se pokrene aplikacija neće biti ulogovanih vozača. Potrebno je ulogovati vozače u posebnim tabovima kako bi postali slobodni za vožnju. Npr ulogovati:
- mika@gmail.com
- milan@gmail.com
- marko@gmail.com
- petar@gmail.com

Za korišćenje aplikacije je potrebna internet konekcija. Plaćanje vožnje se vrši preko <b>sepolia eth test mreže</b>

Ukoliko nekom korisniku zafali tokena potrebno ih je uplatiti na njegovu eth adresu. Preporučen način za nabavljanje tokena https://www.allthatnode.com/faucet/ethereum.dsrv (sa nalogom ide i do 0.075 eth na dan)
