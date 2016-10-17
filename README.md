# Kjempesprekken statistikk

Lite java-program for enkelt å oppdatere totalstatistikk for antall deltagelser i Kjempesprekken.

Bruker biblioteket opencsv, dette finner du her: http://opencsv.sourceforge.net/

Lagre openCSV i samme mappe som kjempesprekken_statistkk og pakk ut jar fila (jar xf opencsv-x.x.Jar), deretter kompilerer og kjører du `Kjempesprekken.java` på vanlig måte.

CSV-fila må være semikolonseparert, og med følgende innhold:



empty|Navn|Deltakelser
-----|--------------------|-----------------
int<sub>x</sub>|string<sub>x</sub>|int<sub>y</sub>
int<sub>x</sub>|string<sub>x</sub>|int<sub>y</sub>
...|...|...
int<sub>x</sub>|string<sub>x</sub>|int<sub>y</sub>
int<sub>x</sub>|Totalt antall deltakelser|int<sub>z</sub>

Hvor

Token|Meaning|Read by program|Written by program
-----|--------|---------------|------------------
empty|will (and must) be an empty field|y|y
string<sub>x</sub>|Name of a participant|y|y
int<sub>x</sub>|Which place a participant has on the ranking|n|y
int<sub>y</sub>|Hom many participations a participant has|y|y
int<sub>z</sub>|How many participations in total|n|y
Navn|static|y|y
Deltakelser|static|y|y
Totalt antall deltakelser|static|y|y


## TODO

* Kunne bla blant foreslåtte navn når man har forsøkt å registrere et navn
* Eksportere rett til HTML-tabell
* Importere IOF XML-resultatliste