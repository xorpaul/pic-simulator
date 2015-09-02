# Einleitung #

Grafisches Userinterface des Simulators. Beinhaltet Refresh-Funktionen,
kümmert sich um das EventHandling (Drücken von Buttons/Ports etc).

Beim Laden eines Codefiles wird der Interpreter instanziiert.
Beim Drücken des Start Buttons wird der Interprete-Thread gestartet.
Beim Drücken des Stop Buttons wird der Thred beendet und der PIC sowie das
GUI auf WDT-Reset gestellt.

# Klassenattribute #
```
    public PicCPU pic = new PicCPU(this); //PIC Prozessor
    public InstructionInterpreter interpret; //Interpreter
    public boolean interpreterSlow; // Speed-Flag
    public boolean running = false; // Start/Stop Flag
    public boolean step = false; //für schrit für schritt ausführung --> TODO!
    private int format = 0; // 0 für DEZ, 1 für HEX, 2 für BIN
    private String[][] contentBank0 = new String[128][2]; // Daten für Bank0 Tabelle
    private String[][] contentBank1 = new String[128][2]; // Daten für Bank1 Tabelle
    private String[] titel = new String[2]; //Überschrift der SpeicherTabelle
    private DefaultTableModel dm0; //TabelModel für Bank0
    private DefaultTableModel dm1; //TableModel für Bank1
```