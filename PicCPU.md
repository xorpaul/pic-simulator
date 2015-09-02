# Einleitung #

Soll eine Nachbildung der PIC-Hardware sein. Hier sind alle Funktionen der CPU
hinterlegt, sowie auch Interrupt-Methoden, und etliche Hilfsmethoden zur Speicherverawaltung.

Zur einfachen Arbeit sind spezielle Registeradressen als Integer-Konstanten definiert.
Im Konstruktor wird der power-on reset status initialisiert.

Besonderheit ist das Status Register. Dies liegt als eigenständiges Bit-Array vor, welches jederzeit mit dem Speicher Synchroisiert wird. Daher sollten auf das Status Register außer BCF und BSF keine anderen Funktionen angewandt werden. Desweiteren muss beachtet werden das JEDES bit im Status Register manipuliert werden kann, was nicht der PIC-Hardware entspricht.

# Klassenattribute #
```
private PICSIMGUI gui;
    public Stack<Integer> CallCount = new Stack<Integer>(); //Stack
    public int linie = 1;
    //Häufig verwendete Adressen und Bitpositionen
    public final int fsr = 4; //Adresse des FSR Registers
    public final int portA = 5; //Adresse von PortA
    public final int portB = 6; //Adresse von PortB
    public final int trisA = 5; //Adresse von TrisA
    public final int trisB = 6; //Adresse von TrisB
    public final int status = 3;//Adresse des StatusRegisters
    public final int OPTION = 1; //Adresse des Option Registers
    public final int INTCON = 11; //Adresse des INTCON Registers
    public final int TMR0 = 1; //Adresse des TMR0 Registers
    public final int PCL = 2;
    public final int EEDATA = 8;
    public final int EEADR = 9;
    public final int EECON1 = 1;
    public final int EECON2 = 1;
    public final int PCLATH = 10;
    public final int zFlag = 2; //Bit# des zFLAG im Statusregister
    public final int cFlag = 0; //Bit# des CarryFLAG im Statusregister
    public final int dcFlag = 1; //Bit# des dcFLAG im Statusregister   
    public final int nPD = 3; // Negiertes Power Down Status Bit
    public final int nTO = 4; // Negiertes Time Out Status Bit
    public final int irp = 7;//
    public final int rp0 = 5;//RP0 gesetzt -> Bank 1 aktiv, RP0 nicht gesetz: Bank 0 aktiv
    public final int rp1 = 6;//
    public int interrupt_aufgetreten = 0;

    //Speicherstruktur 
    public int[] statusReg = new int[8]; //Status Register als Array -> Adr.3
    public int[] memoryBank0; //Gesamte Speichernak 0 des Pic
    public int[] memoryBank1; //Gesamte Speichernak 1 des Pic
    public int akku;   //W-Register des Pic
    public int activeBank = 0;
    public int WDT = 0; //Watchdog Timer
    public int prescaler = 0; //Watchdog Timer Prescaler
    public int Laufzeit = 0; //Variable der Laufzeit
    public boolean interrupt = false;
    public boolean Aflanke = false;
    public boolean Bflanke = false;
    int[] portAlatch = new int[8];
    int[] portBlatch = new int[8];
```