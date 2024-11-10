# ScheCPUEmulator

URL: https://github.com/schemil053/ScheCPUEmulator

## Sprachen
- [Englisch](README.md)
- Deutsch (#deutsch)

## Deutsch

## Inhaltsverzeichnis
1. [Einleitung](#einleitung)
2. [Projektziele](#projektziele)
3. [Emulator-Design](#emulator-design)
   - [CPU-Architektur](#cpu-architektur)
   - [Befehlssatz](#befehlssatz)
   - [Register](#register)
   - [Speicherverwaltung](#speicherverwaltung)
4. [Verwendung](#verwendung)
5. [Beispiele](#beispiele)
6. [Zukünftige Verbesserungen](#zukünftige-verbesserungen)

## Einleitung
Dieses Projekt ist ein Java-basierter CPU-Emulator, der grundlegende CPU-Operationen zu Lernzwecken simuliert. Der Emulator wurde entwickelt, um das Verständnis dafür zu erleichtern, wie eine CPU funktioniert und für Einsteiger leicht zugänglich und verständlich zu machen.

Dieser Emulator läuft im "locked-down"-Modus: Programme werden zur Compile-Zeit geladen, d. h. Speicher und Befehle können zur Laufzeit nicht verändert werden. Diese Einschränkung ermöglicht Anfängern, sich auf die grundlegende CPU-Mechanik zu konzentrieren, ohne zusätzliche Komplexität.

## Projektziele
- Die Grundlagen des CPU-Designs vermitteln: Der Hauptzweck dieses Emulators ist es, den Benutzern zu helfen, zu verstehen, wie eine CPU funktioniert.
- Lernen durch eine einfache, verständliche Architektur.
- Einführung in Maschinensprache und Assembler-Konzepte.
- Ein interaktives Lernerlebnis bieten.

## Emulator-Design
### CPU-Architektur

**1. Wortgröße**:
- Die CPU in diesem Emulator verwendet eine 32-Bit-Wortgröße, da ein Integer in Java normalerweise eine 32-Bit-Variable (4 Bytes) ist. Das bedeutet, dass die maximale Anzahl von Speicheradressen, Speicherwerten oder Registerwerten 2^31-1 (2147483647) beträgt.

**2. Register**
- Die CPU enthält eine Reihe von Registern, die als kleiner, schneller Speicher dienen und während der Ausführung Daten und Befehle speichern.

**3. Speicher**
- Der Emulator verwendet einen Speicherblock fester Größe, in dem Daten gespeichert werden. Die CPU kann auf diesen Speicher direkt über Adressen zugreifen.
- Ein Wert im Speicher ist ein 32-Bit-Integer. 2^31-1 ist das Maximum und -2^31 das Minimum.
- Diese Konfiguration vermeidet Komplexitäten wie Speichersegmentierung und Paging und konzentriert sich auf direkten Speicherzugriff.

**4. Befehlssatzarchitektur (ISA)**
- Die CPU arbeitet mit einem einfachen Befehlssatz (z. B. MOV, ADD, SUB, JMP), der Datenbewegung, arithmetische und logische Operationen sowie den Programmfluss steuert.

**5. Einzel-Threaded**
- Diese CPU ist single-threaded für besseres Verständnis.

### Befehlssatz
- Die vollständige Befehlsübersicht finden Sie in der Datei [src/main/resources/Instructions.md](src/main/resources/Instructions.md)

### Register
- Die CPU hat 5 Register:
   - A, B, C, D: derzeit unbenutzt (highlang verwendet sie, aber ich möchte in Zukunft weitere Funktionen hinzufügen, die auf ihnen basieren).
   - BOOL: Das BOOL-Register speichert das Ergebnis der letzten Operation.

### Speicherverwaltung
- Die Speicherverwaltung in diesem Emulator ist vereinfacht und umfasst eine klare Trennung zwischen Programmcode und Daten.
- Diese Konfiguration ermöglicht es den Benutzern, den gesamten Speicher für Daten zu nutzen, ohne das Risiko, Programmbefehle zur Laufzeit zu verändern, was die Arbeit mit Speicheroperationen erleichtert.
- Ein Wert im Speicher ist ein 32-Bit-Integer. 2^31-1 ist das Maximum und -2^31 das Minimum (Java-Integer-Limit).

## Verwendung
Es gibt mehrere Möglichkeiten, dieses Projekt in Ihr eigenes Projekt zu integrieren.
Hier sind einige empfohlene Methoden:

- [Jitpack](#jitpack)
- [In eigenes Repository pushen](#in-eigenes-repository-pushen)
- [Lokal installieren](#lokal-installieren)

### Hinzufügen als Abhängigkeit
#### Jitpack
1. Holen Sie sich die neueste Version bei Jitpack: [jitpack.io/#schemil053/ScheCPUEmulator/](https://jitpack.io/#schemil053/ScheCPUEmulator/)
2. Fügen Sie das Jitpack-Repository hinzu:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
3. Fügen Sie dieses Repository hinzu (ersetzen Sie VERSION durch die Version von Jitpack):
```xml
<dependency>
    <groupId>com.github.schemil053</groupId>
    <artifactId>ScheCPUEmulator</artifactId>
    <version>VERSION</version>
    <scope>compile</scope>
</dependency>
```

#### In eigenes Repository pushen
1. Fügen Sie Ihr eigenes Repository in die pom.xml-Datei ein:
```xml
    <profiles>
        <profile>
            <id>beispiel-repo</id>
            <distributionManagement>
                <repository>
                    <id>beispiel-repo</id>
                    <url>https://maven.beispiel.com/repo</url>
                </repository>
            </distributionManagement>
        </profile>
    </profiles>
```
2. Bauen und bereitstellen:
```bash
mvn deploy -P beispiel-repo
```

3. Fügen Sie es als Abhängigkeit hinzu:
```xml
    <repository>
        <id>jitpack.io</id>
        <url>https://maven.beispiel.com/repo</url>
    </repository>
```

```xml
<dependency>
    <groupId>de.emilschlampp</groupId>
    <artifactId>ScheCPUEmulator</artifactId>
    <version>1.0-SNAPSHOT</version>
    <scope>compile</scope>
</dependency>
```

### Lokal installieren
1. Bauen und installieren:
```bash
mvn install
```

2. Fügen Sie es als Abhängigkeit hinzu:
```xml
<dependency>
    <groupId>de.emilschlampp</groupId>
    <artifactId>ScheCPUEmulator</artifactId>
    <version>1.0-SNAPSHOT</version>
    <scope>compile</scope>
</dependency>
```

## Beispiele
### Schessembler-Beispiele
- [Hello world (Zeichen für Zeichen)](src/test/resources/hello-world.sasm)
- [Hello world (loadstrm)](src/test/resources/simple-loadstrm.sasm)

### Implementierung
- [Einfacher Compiler (Schessembler)](src/test/java/de/emilschlampp/scheCPU/compile/CompilerTest.java)
- [Einfacher Compiler (Highlang)](src/test/java/de/emilschlampp/scheCPU/high/HighCompilerTest.java)
- [Ausführung (Bytecode)](src/test/java/de/emilschlampp/scheCPU/emulator/CPUEmulatorTest.java)

## Zukünftige Verbesserungen
- [ ] Eine Minecraft-Version des Emulators erstellen, die innerhalb von Minecraft läuft und Mechaniken hinzufügt, um Redstone über eine CPU zu steuern.
- [ ] Eine höhere Programmiersprache als Schessembler entwickeln (angefangen, aber noch nicht abgeschlossen).
- [ ] Einen Debugger hinzufügen.
- [ ] Eine Möglichkeit hinzufügen, das Programm zur Laufzeit zu ändern.
- [ ] Weitere Einschränkungen hinzufügen, um die CPU-Nutzung sicherer für den Einsatz in Produktionsumgebungen zu gestalten.
- [ ] Mehr Möglichkeiten zur Verwendung von Registern (Funktionen/Instruktionen implementieren?).
