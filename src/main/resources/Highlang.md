# Highlang
## Einleitung
ScheCPUHighLang ist eine höhere Programmiersprache als Schessembler

Die Sprache ist wie folgt aufgebaut:

`<Anweisung> <Argument 1> <Argument 2> <Argument ...>`

## Befehle

| Befehl      | Syntax                   | Beschreibung                                                                                                                                                                                                                                              |
|-------------|--------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| protect     | <variable/option> <ID>   | dieser Befehl schützt eine Variable oder eine Option. Eine geschützte Option (bzw. Variable) kann nicht mehr verändert, aber noch gelesen werden.                                                                                                         |
| ret         |                          | dieser Befehl beendet die aktuelle Methode und springt zu dem Aufruf zurück.                                                                                                                                                                              |
| opt         | <Option> <Wert>          | dieser Befehl setzt die angegebene Option auf den angegebenen Wert                                                                                                                                                                                        |
| reserve     | <Variable> <Größe>       | dieser Befehl reserviert die Variable im RAM der CPU und stellt die angegebene Größe dieser Variable sicher. Eine reservierte Variable bekomme eine Speicheradresse zugewiesen, wird aber nicht davon abgehalten in andere Speicherbereiche zu schreiben. |
| ifbool      | <Variable>               | der Untenstehende Code wird ausgeführt, wenn die Variable true ist                                                                                                                                                                                        |
| endif       |                          | beendet ifbool                                                                                                                                                                                                                                            |
| var         | <Variable> <Wert>        | setzt die angegebene Variable auf den angegebenen Wert. Achtung: dieser Befehl reserviert die Variable falls noch nicht geschehen und schreibt den Wert in diesen Speichereich. Es wird keine Rücksicht auf andere Variablen genommen.                    |
| method      | <Name>                   | deklariert eine Methode                                                                                                                                                                                                                                   |
| endmethod   |                          | beendet die aktuelle Deklaration                                                                                                                                                                                                                          |
| callmethod  | <Methode>                | ruft die angegebene Methode auf                                                                                                                                                                                                                           |
| putsamebool | <Target> <Bool1> <Bool2> | Vergleicht beide booleans. Funktioniert auch mit ints, aber nicht mit Strings. Wenn beide Werte gleich sind, wird <target> auf 1 gesetzt, sonst auf 0                                                                                                     |
| print       | <Variable>               | Gibt die angegebene Variable aus                                                                                                                                                                                                                          |
| println     | <Variable>               | Gibt die angegebene Variable aus und fügt ein \n hinzu                                                                                                                                                                                                    |
| putnot      | <Variable>               | Verneint den angegebene Boolean                                                                                                                                                                                                                           |
| out         | <Variable> <Port>        | Gibt etwas auf dem IO-Port aus                                                                                                                                                                                                                            |
| in          | <Variable> <Port>        | Speichert den Wert auf Port in der Variable                                                                                                                                                                                                               |
| concatvstr  | <Variable1> <Variable2>  | Verbindet den String in Variable1 mit Variable2, das Resultat wird in Variable1 geschrieben                                                                                                                                                               |

Natürlich können Erweiterungen und PreCompiler die Möglichkeiten und Befehle erweitern.

## Funktionsweise
Der Code wird zuerst in Schessembler umgewandelt.
