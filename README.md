# Kjempesprekken statistikk

Lite java-program for enkelt å oppdatere totalstatistikk for antall deltagelser i Kjempesprekken.

Bruker biblioteket opencsv, dette finner du her: http://opencsv.sourceforge.net/

Lagre openCSV i samme mappe som kjempesprekken_statistkk og pakk ut jar fila (jar xf opencsv-x.x.Jar), kompiler og kjør deretter Kjempesprekken.java på vanlig måte.

CSV-fila må være på følgende format (der tekst må skrives slik det er skrevet under):

,Navn,Deltakelser
<any>,<string>,<int>
<any>,<string>,<int>
...
<any>,<string>,<int>
,Totalt antall deltakelser,<int>
