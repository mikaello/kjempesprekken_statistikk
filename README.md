# Kjempesprekken statistikk

Lite java-program for enkelt å oppdatere totalstatistikk for antall deltagelser i Kjempesprekken.

Bruker biblioteket opencsv, dette finner du her: http://opencsv.sourceforge.net/

Lagre openCSV i samme mappe som kjempesprekken_statistkk og pakk ut jar fila (jar xf opencsv-x.x.Jar), kompiler og kjør deretter Kjempesprekken.java på vanlig måte.

CSV-fila må være kommaseparert, og med følgende innhold:


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
int<sub>y</sub>|Hom many particapations a participant has|y|y
int<sub>z</sub>|How many participations in total|n|y
Navn|static|y|y
Deltakelser|static|y|y
Totalt antall deltakelser|static|y|y
