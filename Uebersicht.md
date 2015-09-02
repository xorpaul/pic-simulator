# Einleitung #

**Programmable Integrated Circuit - Simulator**

Dieses Projekt wird in der Vorlesung **Rechnertechnik II** im Kurs **TIT06GR1** der [BA-Karlsruhe](http://www.ba-karlsruhe.de) bearbeitet.

Dieses Projekt kann in einer beliebigen Hochsprache geschrieben werden.
Die hier verwendete Implementierungsprogrammiersprache ist _Java_.

In der vorherigen Vorlesung des 3. Semesters Rechnertechnik I wurden die Grundlagen der Assembler Programmiersprache beigebracht.

Mit diesem Projekt soll ein PIC 16C83 Prozessor per Software simuliert werden. Als Eingabe benötigt das Programm ein bereits ausgearbeitete Assemblerprogrammdatei.
Der Simulator wurde in 3 Javaklassen unterteilt.
  * PICSIMGUI stellt die grafische Benutzeroberfläche bereit
  * InstructionInterpreter stellt die Verarbeitung der durch die Datei bereitgestellte Informationen bereit. Diese Informationen werden ausgewertet und für die Endbearbeitung an die Klasse PicCPU weitergegeben.
  * PicCPU beinhaltet die einzelnen Assemblerbefehle (GOTO, INC, ...) und stellt die virtuelle Speicherregister und Speicherbanksimulation dar.


# Inhalt #

  1. **Einleitung**
  1. **Beschreibung der einzelnen Klassen**
    * [Klasse PicCPU](http://code.google.com/p/pic-simulator/wiki/PicCPU)
    * [Klasse InstructionInterpreter](http://code.google.com/p/pic-simulator/wiki/InstructionInterpreter)
    * [Klasse PICSIMGUI](http://code.google.com/p/pic-simulator/wiki/PICSIMGUI)
  1. **Programmablaufpläne (PAP)**
    * [getIndirectAdress PAP](http://code.google.com/p/pic-simulator/wiki/gIAPAP)
    * [BTFSC PAP](http://code.google.com/p/pic-simulator/wiki/BTFSCPAP)
    * [COMF PAP](http://code.google.com/p/pic-simulator/wiki/COMFPAP)
    * [XORLW PAP](http://code.google.com/p/pic-simulator/wiki/xorlw)
    * [DECFSZ PAP](http://code.google.com/p/pic-simulator/wiki/decfsz)
    * [SUBWF PAP](http://code.google.com/p/pic-simulator/wiki/subwf)
    * [RRF PAP](http://code.google.com/p/pic-simulator/wiki/rrf)
    * [MOVF PAP](http://code.google.com/p/pic-simulator/wiki/movf)
    * [CALL PAP](http://code.google.com/p/pic-simulator/wiki/call)
    * [PAP run-Methode der Klasse InstructionInterpreter](http://code.google.com/p/pic-simulator/wiki/IIrun)
  1. **Grafische-Benutzer-Oberfläche**
    * [Screenshot der GUI](http://code.google.com/p/pic-simulator/wiki/screenshotgui)
  1. **Anhang**
    * [JavaDoc](http://home.arcor.de/pp1986/picsim/javadoc/index.html)
    * [Beantwortung der Fragen](http://code.google.com/p/pic-simulator/wiki/Fragen)