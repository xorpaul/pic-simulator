package my.PICSIMGUI;

import java.util.Stack;

public class PicCPU {

    Stack<Integer> CallCount = new Stack<Integer>(); //Stack
    public int linie = 0;
    //Häufig verwendete Adressen und Bitpositionen
    public final int fsr = 4; //Adresse des FSR Registers
    public final int portA = 5; //Adresse von PortA
    public final int portB = 6; //Adresse von PortB
    public final int trisA = 5; //Adresse von TrisA
    public final int trisB = 6; //Adresse von TrisB
    public final int status = 3;//Adresse des StatusRegisters
    public final int zFlag = 2; //Bit# des zFLAG im Statusregister
    public final int cFlag = 0; //Bit# des CarryFLAG im Statusregister
    public final int dcFlag = 1; //Bit# des dcFLAG im Statusregister
    public final int rp0 = 5;//RP0 gesetzt -> Bank 1 aktiv, RP0 nicht gesetz: Bank 0 aktiv
    public final int OPTION = 2; //Speicherstelle des Option Registers
    public final int INTCON = 11; //Speichersstelle des INTCON Registers
    public final int TMR0 = 1; //Speichersstelle des TMR0 Registers
    //Speicherstruktur 
    public int[] statusReg = new int[8]; //Status Register als Array -> Adr.3
    public int[] memoryBank0; //Gesamte Speichernak 0 des Pic
    public int[] memoryBank1; //Gesamte Speichernak 1 des Pic
    public int akku;   //W-Register des Pic
    public int activeBank = 0;
    public int WDT = 0; //Watchdog Timer
    public int prescaler = 0; //Watchdog Timer Prescaler
    public int nPD = 1; //Negiertes Power Down Status Bit
    public int nTO = 1; //Negiertes Time Out Status Bit
    public int Laufzeit = 0; //Variable der Laufzeit
    public boolean interrupt = false;

    /**
     * @category Konsruktor. Speicher initilisieren
     */
    PicCPU() {
        memoryBank0 = new int[128];
        memoryBank1 = new int[128];
        //TRIS-Register initialisieren
        memoryBank1[trisA] = 31;
        memoryBank1[trisB] = 255;
        akku = 0;
    }

    static public void e(Object... parameters) {
        if (parameters.length == 2) {
            System.out.print(parameters[0]);
        } else {
            System.out.print(parameters[0]);
        }
    }

    public void setPortA(int value) {
        this.memoryBank0[portA] = value;
    }

    /**
     * Erstellt einen IntegerWert aus dem statusRegister Array
     * und schreibt es an die Adresse 3 der Speicherbänke.
     */
    public void statusToMemory() {
        String statusAsBin = "";
        for (int i = 7; i >= 0; i--) {
            statusAsBin += String.valueOf(statusReg[i]);
        }
        memoryBank0[status] = Integer.parseInt(statusAsBin, 2);
        memoryBank1[status] = Integer.parseInt(statusAsBin, 2);
    }

    public void setBank() {
        if (statusReg[rp0] == 0) {
            activeBank = 0;
        } else {
            activeBank = 1;
        }
    }

    public void fsrMemoryManagement() {
        this.memoryBank0[0] = this.memoryBank0[this.memoryBank0[fsr]];
        this.memoryBank1[0] = this.memoryBank1[this.memoryBank1[fsr]];
    }

    /**
     * @param position Bit welches im Statusregister veränder werden soll. Vrgl final int's am Klassenanfang
     * @param value darf 0 oder 1 sein entsprechend für high oder low
     */
    public void changeStatusReg(int position, int value) {
        statusReg[position] = value;
    }

    //   region Interrups // Implementierung der Interrups
    private boolean Get_INTEDG() { // Liefert den Wert für INTEDG
        return ((this.memoryBank1[OPTION] & 64) == 64);
    }

    private boolean Get_T0CS() { // Liefert den Wert fürT0CS
        return ((this.memoryBank1[OPTION] & 32) == 32);
    }

    private boolean Get_T0SE() { // Liefert den Wert für T0SE
        return ((this.memoryBank1[OPTION] & 16) == 16);
    }

    private boolean Get_PSA() { // Liefert den Wert für PSA
        return ((this.memoryBank1[OPTION] & 8) == 8);
    }

    private boolean Get_GIE() { // Liefert den Wert für GIE
        if (activeBank == 0) {
            return ((this.memoryBank0[INTCON] & 128) == 128);
        } else {
            return ((this.memoryBank1[INTCON] & 128) == 128);
        }
    }

    private boolean Get_T0IE() { // Liefert den Wert für T0IE
        if (activeBank == 0) {
            return ((this.memoryBank0[INTCON] & 32) == 32);
        } else {
            return ((this.memoryBank1[INTCON] & 32) == 32);
        }
    }

    private boolean Get_INTE() { // Liefert den Wert für INTE
        if (activeBank == 0) {
            return ((this.memoryBank0[INTCON] & 16) == 16);
        } else {
            return ((this.memoryBank1[INTCON] & 16) == 16);
        }
    }

    private boolean Get_AInt() { // Liefert den Wert des 4. Bits vom A-Register
        return ((this.memoryBank0[portA] & 8) == 8);
    }

    private boolean Get_BInt() { // Liefert den Wert des 8. Bit des B-Registers zurück
        return ((this.memoryBank0[portB] & 1) == 1);
    }

    private double getprescaler(boolean timer) { // Liefert die eingstellte prescaler-Rate abhängig vom WDT/TMR0 zurück
        if (timer) {
// Prescaler des Timer wird abgefragt
            return (2 * Math.pow(2, (this.memoryBank1[OPTION] & 7))); //ACHTUNG DOUBLE
        } else {
// Prescaler des WDT wird abgefragt
            return (Math.pow(2, (this.memoryBank1[OPTION] & 7)));  //ACHTUNG DOUBLE
        }
    }

    private void interrupt() { // Kontrolliert, ob ein Interrupt aufgetreten ist
        //interner Interrupt

        if (!Get_PSA()) { // TMR0 wird benutzt, daher kann der WDT hochzählen
            WDT++;
            if (WDT >= 256) { // WDT hat Überlauf -> Reset
                Reset_WDT();
            }
        }
        if (!Get_T0CS()) { // Ich bin ein Timer -> keine Flanke
            if (!Get_PSA()) { // Ich bin auf RTM0
                if ((Get_GIE()) && (Get_T0IE())) { // Ich darf starten
                    prescaler++;
                    if (prescaler >= getprescaler(true)) { // Ich darf TMR0 erhöhen, da der prescaler einen überlauf hatte
                        this.memoryBank0[TMR0]++;
                        prescaler = 0;
                        if (this.memoryBank0[TMR0] >= 256) { // TMR0 hat Überlauf -> Interrupt
                            this.memoryBank0[TMR0] = 0;
                            if (activeBank == 0) {
                                this.memoryBank0[INTCON] = (this.memoryBank0[INTCON] | 4);
                                this.memoryBank0[INTCON] = (this.memoryBank0[INTCON] & 127);
                            } else {
                                this.memoryBank1[INTCON] = (this.memoryBank0[INTCON] | 4);
                                this.memoryBank1[INTCON] = (this.memoryBank0[INTCON] & 127);
                            }

                            this.CallCount.push(linie);
                            this.interrupt = true;
                            Laufzeit++;
                            this.linie = 4;
                            e("Interner Interrupt ausgelöst");
                        }
                    }
                }
            } else { // Ich bin auf WDT, daher TMR0 sofort erhöhen
                prescaler++;
                if ((Get_GIE()) && (Get_T0IE())) { // Ich darf starten
                    this.memoryBank0[TMR0]++;
                    if (this.memoryBank0[TMR0] >= 256) { // TMR0 hat Überlauf -> Interrupt
                        this.memoryBank0[TMR0] = 0;
                        if (activeBank == 0) {
                            this.memoryBank0[INTCON] = (this.memoryBank0[INTCON] | 4);
                            this.memoryBank0[INTCON] = (this.memoryBank0[INTCON] & 127);
                        } else {
                            this.memoryBank1[INTCON] = (this.memoryBank0[INTCON] | 4);
                            this.memoryBank1[INTCON] = (this.memoryBank0[INTCON] & 127);
                        }
                        //Stack[Stack_Counter] = Reg[PC];
                        this.CallCount.push(linie);
                        this.interrupt = true;
                        Laufzeit++;
                        this.linie = 4;
                        e("int. Interrupt ausgelöst");
                    }
                }
                if (prescaler >= getprescaler(false)) { // Wenn prescaler überlauf hat, dann WDT erhöhen
                    WDT++;
                    prescaler = 0;
                    if (WDT >= 256) { // WDT erzeugt Überlauf -> Reset
                        Reset_WDT();
                    }
                }
            }

        } else { // Ich bin ein Counter, ich brauche eine Flanke
            if (!Get_PSA()) // Ich bin auf RTM0
            {
                if ((Get_AInt() != (Aflanke == 1)) && (Get_AInt() != Get_T0SE())) { // Flanke war da und gültige Flanke war da
                    if ((Get_GIE()) && (Get_T0IE())) { // Ich darf starten
                        prescaler++;
                        if (prescaler >= getprescaler(true)) { // Ich darf TMR0 erhöhen, da der prescaler einen überlauf hatte
                            this.memoryBank0[TMR0]++;
                            prescaler = 0;
                            if (this.memoryBank0[TMR0] >= 256) { // TMR0 hat Überlauf -> Interrupt
                                this.memoryBank0[TMR0] = 0;
                                if (activeBank == 0) {
                                    this.memoryBank0[INTCON] = (this.memoryBank0[INTCON] | 4);
                                    this.memoryBank0[INTCON] = (this.memoryBank0[INTCON] & 127);
                                } else {
                                    this.memoryBank1[INTCON] = (this.memoryBank0[INTCON] | 4);
                                    this.memoryBank1[INTCON] = (this.memoryBank0[INTCON] & 127);
                                }
                                this.CallCount.push(linie);
                                this.interrupt = true;
                                Laufzeit++;
                                this.linie = 4;
                                e("int. Interrupt ausgelöst");
                            }
                        }
                    }
                }
            } else { // Ich bin auf WDT, daher TMR0 sofort erhöhen
                prescaler++;
                if ((Get_AInt() != (Aflanke == 1)) && (Get_AInt() != Get_T0SE())) { // Flanke war da und gültige Flanke war da
                    if ((Get_GIE()) && (Get_T0IE())) { // Ich darf starten
                        if (prescaler >= getprescaler(true)) { // Ich darf TMR0 erhöhen, da der prescaler einen überlauf hatte
                            this.memoryBank0[TMR0]++;
                            if (this.memoryBank0[TMR0] >= 256) { // TMR0 hat Überlauf -> Interrupt
                                this.memoryBank0[TMR0] = 0;
                                if (activeBank == 0) {
                                    this.memoryBank0[INTCON] = (this.memoryBank0[INTCON] | 4);
                                    this.memoryBank0[INTCON] = (this.memoryBank0[INTCON] & 127);
                                } else {
                                    this.memoryBank1[INTCON] = (this.memoryBank0[INTCON] | 4);
                                    this.memoryBank1[INTCON] = (this.memoryBank0[INTCON] & 127);
                                }
                                this.CallCount.push(linie);
                                this.interrupt = true;
                                Laufzeit++;
                                this.linie = 4;
                                e("int. Interrupt ausgelöst");
                            }
                        }
                    }
                }
                if (prescaler >= getprescaler(false)) { // Wenn prescaler überlauf hat, dann WDT erhöhen
                    WDT++;
                    prescaler = 0;
                    if (WDT >= 256) { // WDT hat Überlauf -> Reset
                        Reset_WDT();
                    }
                }
            }
            Aflanke = Convert.ToInt32(Get_AInt());
        }




        //externer Interrupt

        if ((Get_GIE()) && (Get_INTE())) { // Ich darf starten
            if ((Get_BInt() != (Bflanke == 1)) && (Get_BInt() == Get_INTEDG())) { // Flanke da und gültige Flanke war da -> Interrupt
                if (activeBank == 0) {
                    this.memoryBank0[INTCON] = (this.memoryBank0[INTCON] | 4);
                    this.memoryBank0[INTCON] = (this.memoryBank0[INTCON] & 127);
                } else {
                    this.memoryBank1[INTCON] = (this.memoryBank0[INTCON] | 4);
                    this.memoryBank1[INTCON] = (this.memoryBank0[INTCON] & 127);
                }
                this.CallCount.push(linie);
                this.interrupt = true;
                Laufzeit++;
                this.linie = 4;
                e("ext. Interrupt ausgelöst");
            }
            Bflanke = Convert.ToInt32(Get_BInt());
        }


    }
    //#############################################################################
    public int[] getPortA() {
        int a = this.memoryBank0[portA];
        int[] portABits = new int[5];

        for (int i = 0; i < 5; i++) {
            portABits[i] = a % 2;
            a /= 2;
        }
        return portABits;
    }

    /**
     * Schreibt den neuen Wert des PortA in seine Speicheradresse.
     * Ausgelesen werrden die RadioButtons, werte an ein array übergeben     * 
     * @param portAReadIn Enthält Information über geänderte Port Eingänge
     * 0 --> Eingang auf 0 gesetzt
     * 1 --> Eingang auf 1 gesetzt
     * -2 -> Port ist ein Ausgang
     */
    public void setPortA(int[] portAReadIn) {
        String binValue = "";

        for (int i = 4; i >= 0; i--) {
            if (portAReadIn[i] == -2) {
                binValue += String.valueOf(getPortA()[i]);
            } else {
                binValue += String.valueOf(portAReadIn[i]);
            }
        }

        this.memoryBank0[portA] = Integer.parseInt(binValue, 2);
    }

    /**
     * Schreibt den Wert von PortA als Bits in ein array
     * @return Wert von PortA. Jedes Feld entspricht einem Bit
     */
    public int[] getPortB() {
        int a = this.memoryBank0[portB];
        int[] portBBits = new int[8];

        for (int i = 0; i < 8; i++) {
            portBBits[i] = a % 2;
            a /= 2;
        }
        return portBBits;
    }

    /**
     * Schreibt den neuen Wert des PortB in seine Speicheradresse.
     * Ausgelesen werrden die RadioButtons, werte an ein array übergeben     * 
     * @param portAReadIn Enthält Information über geänderte Port Eingänge
     * 0 --> Eingang auf 0 gesetzt
     * 1 --> Eingang auf 1 gesetzt
     * -2 -> Port ist ein Ausgang
     */
    public void setPortB(int[] portBReadIn) {
        String binValue = "";

        for (int i = 7; i >= 0; i--) {
            if (portBReadIn[i] == -2) {
                binValue += String.valueOf(getPortB()[i]);
            } else {
                binValue += String.valueOf(portBReadIn[i]);
            }
        }

        this.memoryBank0[portB] = Integer.parseInt(binValue, 2);
    }

    /**
     * Schreibt den Wert von TrisA als Bits in ein array
     * @return Wert von TrisA. Jedes Feld entspricht einem Bit
     */
    public int[] getTrisA() {
        int a = this.memoryBank1[trisA];
        int[] trisABits = new int[5];

        for (int i = 0; i < 5; i++) {
            trisABits[i] = a % 2;
            a /= 2;
        }
        return trisABits;
    }

    /**
     * Schreibt den Wert von TrisB als Bits in ein array
     * @return Wert von TrisB. Jedes Feld entspricht einem Bit
     */
    public int[] getTrisB() {
        int a = this.memoryBank1[trisB];
        int[] trisBBits = new int[8];

        for (int i = 0; i < 8; i++) {
            trisBBits[i] = a % 2;
            a /= 2;
        }
        return trisBBits;
    }

    public void checkFlags(int result) {
        if (result > 255) {
            changeStatusReg(cFlag, 1);
            if (result - 255 == 0) {
                changeStatusReg(zFlag, 1);
            } else {
                changeStatusReg(zFlag, 0);
            }
        } else {
            changeStatusReg(cFlag, 0);
            if (result == 0) {
                changeStatusReg(zFlag, 1);
            } else {
                changeStatusReg(zFlag, 0);
            }
        }
    }

    public void printPortA() {
        System.err.println("Im Port A steht: " + this.memoryBank0[portA]);
    }

    public void printW() {
        System.err.println("Im Akku steht: " + this.akku);
    }

    public void INCF(int f) {
        if (activeBank == 0) {
            memoryBank0[f]++;
        } else {
            memoryBank1[f]++;
        }
    }

    public void MOVWF(int f) {
        if (activeBank == 0) {
            memoryBank0[f] = this.akku;
        } else {
            memoryBank1[f] = this.akku;
        }
    }

    public void CLRW() {
        this.akku = 0;
        changeStatusReg(zFlag, 1);
    }

    public void ANDLW(int l) {
        this.akku = this.akku & l;
        if (this.akku == 0) {
            changeStatusReg(zFlag, 1);
        } else {
            changeStatusReg(zFlag, 0);
        }
    }

    public void ADDWF(int f, int d) {
        if (activeBank == 0) {
            int result = this.akku + this.memoryBank0[f];
            if (d == 0) {
                if (result > 255) {
                    checkFlags(result);
                    result -= 255;
                    this.akku = result;

                } else {
                    checkFlags(result);
                    this.akku = result;
                }
            } else {
                if (result > 255) {
                    checkFlags(result);
                    result -= 255;
                    this.memoryBank0[f] = result;

                } else {
                    checkFlags(result);
                    this.memoryBank0[f] = result;
                }
            }
        } else {
            int result = this.akku + this.memoryBank1[f];
            if (d == 0) {
                if (result > 255) {
                    checkFlags(result);
                    result -= 255;
                    this.akku = result;

                } else {
                    checkFlags(result);
                    this.akku = result;
                }
            } else {
                if (result > 255) {
                    checkFlags(result);
                    result -= 255;
                    this.memoryBank1[f] = result;

                } else {
                    checkFlags(result);
                    this.memoryBank1[f] = result;
                }
            }
        }

    }

    public void ANDWF(int f, int d) {
        if (activeBank == 0) {
            int result = this.akku & this.memoryBank0[f];
            if (d == 0) {
                checkFlags(result);
                this.akku = result;
            } else {
                checkFlags(result);
                this.memoryBank0[f] = result;
            }
        } else {
            int result = this.akku & this.memoryBank0[f];
            if (d == 0) {
                checkFlags(result);
                this.akku = result;
            } else {
                checkFlags(result);
                this.memoryBank0[f] = result;
            }
        }
    }

    public void CLRF(int f) {
        if (activeBank == 0) {
            this.memoryBank0[f] = 0;
        } else {
            this.memoryBank1[f] = 0;
        }

        changeStatusReg(zFlag, 1);
    }

    public void ADDLW(int l) {
        int result = this.akku + l;

        if (result > 255) {
            checkFlags(result);
            result -= 255;
            this.akku = result;

        } else {
            checkFlags(result);
            this.akku = result;
        }
    }

    public void COMF(int f, int d) {
        int result;
        if (this.activeBank == 0) {
            result = this.memoryBank0[f] ^ 255;
        } else {
            result = this.memoryBank1[f] ^ 255;
        }

        if (d == 0) {
            checkFlags(result);
            this.akku = result;
        } else {
            checkFlags(result);
            if (this.activeBank == 0) {
                this.memoryBank0[f] = result;
            } else {
                this.memoryBank1[f] = result;
            }
        }
    }

    public void DECF(int f, int d) {
        int result;
        if (this.activeBank == 0) {
            result = this.memoryBank0[f] - 1;

            if (d == 0) {
                this.akku = result;
            } else {
                this.memoryBank0[f] = result;
            }
        } else {
            result = this.memoryBank1[f] - 1;

            if (d == 0) {
                this.akku = result;
            } else {
                this.memoryBank1[f] = result;
            }
        }
        if (result == 0) {
            changeStatusReg(zFlag, 1);
        } else {
            changeStatusReg(zFlag, 0);
        }
    }

    public boolean DECFSZ(int f, int d) {
        int result;
        if (this.activeBank == 0) {
            result = this.memoryBank0[f] - 1;
            if (d != 0) {
                this.memoryBank0[f] = result;
            } else {
                this.akku = result;
            }
            if (result == 0) {
                return true;
            } else {
                return false;
            }
        } else {
            result = this.memoryBank1[f] - 1;

            if (d != 0) {
                this.memoryBank1[f] = result;
            } else {
                this.akku = result;
            }
            if (result == 0) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean INCFSZ(int f, int d) {
        int[] currentBank;

        if (this.activeBank == 0) {
            currentBank = memoryBank0;
        } else {
            currentBank = memoryBank1;
        }

        int result = currentBank[f] + 1;
        if (d != 0) {
            currentBank[f] = result;
        } else {
            this.akku = result;
        }
        if (result == 0) {
            if (this.activeBank == 0) {
                memoryBank0 = currentBank;
            } else {
                memoryBank1 = currentBank;
            }
            return true;
        } else {
            if (this.activeBank == 0) {
                memoryBank0 = currentBank;
            } else {
                memoryBank1 = currentBank;
            }
            return false;
        }
    }

    public void IORWF(int f, int d) {
        int result;
        if (this.activeBank == 0) {
            result = this.memoryBank0[f] | 255;
            if (d == 0) {
                this.akku = result;
            } else {
                this.memoryBank0[f] = result;
            }
        } else {
            result = this.memoryBank1[f] | 255;
            if (d == 0) {
                this.akku = result;
            } else {
                this.memoryBank1[f] = result;
            }
        }

        if (result == 0) {
            changeStatusReg(zFlag, 1);
        } else {
            changeStatusReg(zFlag, 0);
        }
    }

    public void MOVLW(int l) {
        this.akku = l;
    }

    public void IORLW(int l) {
        int result = l | 255;
        this.akku = result;

        if (result == 0) {
            changeStatusReg(zFlag, 1);
        } else {
            changeStatusReg(zFlag, 0);
        }
    }

    public void XORLW(int l) {
        this.akku = this.akku | l;
        if (this.akku == 0) {
            changeStatusReg(zFlag, 1);
        } else {
            changeStatusReg(zFlag, 0);
        }
    }

    public void SUBLW(int l) {
        int result = this.akku - l;

        if (this.akku < l) { // Wenn als ERG was negatives rauskommt
            changeStatusReg(cFlag, l);
            //result -= 255; Muss man hier was machen? Um bspw -3 darzustellen?
            changeStatusReg(zFlag, 0);
            this.akku = result;

        } else {
            changeStatusReg(cFlag, 0);
            if (result == 0) {
                changeStatusReg(zFlag, 1);
            } else {
                changeStatusReg(zFlag, 0);
            }
            this.akku = result;
        }
    }

    public void BCF(int f, int b) {
        int mask;

        //Bitmaske aus der Zahl b erzeugen
        switch (b) {
            case 0:
                mask = 1;
                break;
            case 1:
                mask = 2;
                break;
            case 2:
                mask = 4;
                break;
            case 3:
                mask = 8;
                break;
            case 4:
                mask = 16;
                break;
            case 5:
                mask = 32;
                break;
            case 6:
                mask = 64;
                break;
            case 7:
                mask = 128;
                break;
            default:
                System.err.println("Error beim setzen des Bits!");
                return;
        }

        if (this.activeBank == 0) {
            if (((memoryBank0[f] & mask) > 0)) {
                //je nachdem welche Bank aktiv ist
                memoryBank0[f] = memoryBank0[f] ^ mask;
            }//Xor mit bitmaske
        } else {
            if (((memoryBank1[f] & mask) > 0)) {
                //je nachdem welche Bank aktiv ist
                memoryBank1[f] = memoryBank1[f] ^ mask;
            }//Xor mit bitmaske
        }

        if (f == 3) {
            statusReg[b] = 0;
        }
    }

    public void BSF(int f, int b) {
        int mask;

        //Bitmaske aus der Zahl b erzeugen
        switch (b) {
            case 0:
                mask = 1;
                break;
            case 1:
                mask = 2;
                break;
            case 2:
                mask = 4;
                break;
            case 3:
                mask = 8;
                break;
            case 4:
                mask = 16;
                break;
            case 5:
                mask = 32;
                break;
            case 6:
                mask = 64;
                break;
            case 7:
                mask = 128;
                break;
            default:
                System.err.println("Error beim setzen des Bits!");
                return;
        }


        if (this.activeBank == 0) {
            if (!((memoryBank0[f] & mask) > 0)) {
                //je nachdem welche Bank aktiv ist
                memoryBank0[f] = memoryBank0[f] ^ mask;
            }//Xor mit bitmaske
        } else {
            if (!((memoryBank1[f] & mask) > 0)) {
                //je nachdem welche Bank aktiv ist
                memoryBank1[f] = memoryBank1[f] ^ mask;
            }//Xor mit bitmaske
        }
        if (f == status) {
            statusReg[b] = 1;
        }
    }

    public void MOVF(int f, int d) {
        if (this.activeBank == 0) {
            if (d == 0) {
                this.akku = memoryBank0[f];
                if (this.akku == 0) {
                    this.statusReg[zFlag] = 1;
                } else {
                    this.statusReg[zFlag] = 0;
                }
            } else {
                int swap = memoryBank0[f];
                memoryBank0[f] = 0;
                memoryBank0[f] = swap;
                if (memoryBank0[f] == 0) {
                    this.statusReg[zFlag] = 1;
                } else {
                    this.statusReg[zFlag] = 0;
                }
            }
        } else {
            if (d == 0) {
                this.akku = memoryBank1[f];
                if (this.akku == 0) {
                    this.statusReg[zFlag] = 1;
                } else {
                    this.statusReg[zFlag] = 0;
                }
            } else {
                int swap = memoryBank1[f];
                memoryBank1[f] = 0;
                memoryBank1[f] = swap;
                if (memoryBank1[f] == 0) {
                    this.statusReg[zFlag] = 1;
                } else {
                    this.statusReg[zFlag] = 0;
                }
            }
        }
    }

    public void NOP() {
    //TODO:
    // Cycel ++;
    }

    public void RRF(int f, int d) {
        int result;
        int oldLsb;
        int carry = statusReg[cFlag];

        if (this.activeBank == 0) {
            //LSB herausfiltern (0 oder 1 ? )
            oldLsb = memoryBank0[f] & 1;
            //Um 1 Bit nach rechts verschieben
            result = memoryBank0[f] >>> 1;
        } else {
            oldLsb = memoryBank1[f] & 1;
            result = memoryBank1[f] >>> 1;
        }
        //MSB nach >>> ist immer 0
        //Wenn im Carry ne 1 ist MSB auf 1 setzen (+128)
        if (carry != 0) {
            result += 128;
        }
        carry = oldLsb; //imm Carry steht jetzt das alte LSB
        statusReg[cFlag] = carry;

        if (d == 0) {
            this.akku = result;
        } else {
            if (this.activeBank == 0) {
                this.memoryBank0[f] = result;
            } else {
                this.memoryBank1[f] = result;
            }
        }

    }

    public void RLF(int f, int d) {
        int result;
        int oldMsb;
        int carry = statusReg[cFlag];

        if (this.activeBank == 0) {
            //LSB herausfiltern (0 oder 1 ? )
            oldMsb = memoryBank0[f] & 128;
            //Um 1 Bit nach links verschieben
            result = memoryBank0[f] << 1;
        } else {
            oldMsb = memoryBank1[f] & 128;
            result = memoryBank1[f] << 1;
        }

        if (oldMsb == 128) {
            oldMsb = 1;
        }

        //LSB nach << ist immer 0
        //Wenn im Carry ne 1 ist MSB auf 1 setzen (+1)
        if (carry != 0) {
            result += 1;
        }

        carry = oldMsb; //imm Carry steht jetzt das alte LSB
        statusReg[cFlag] = carry;

        if (d == 0) {
            this.akku = result;
        } else {
            if (this.activeBank == 0) {
                this.memoryBank0[f] = result;
            } else {
                this.memoryBank1[f] = result;
            }
        }
    }

    public void SWAPF(int f, int d) {
    // vertauscht das vordere halbe byte mit dem hintern            
    }

    public void XORWF(int f, int d) {
        int result;
        if (this.activeBank == 0) {
            result = this.akku ^ this.memoryBank0[f];
            if (d == 0) {
                this.akku = result;
            } else {
                this.memoryBank0[f] = result;
            }
        } else {
            result = this.akku ^ this.memoryBank1[f];
            if (d == 0) {
                this.akku = result;
            } else {
                this.memoryBank1[f] = result;
            }
        }

        if (result == 0) //Bei XOR kann nichts negatives rauskommen!
        {
            this.statusReg[zFlag] = 1;
        } else {
            this.statusReg[zFlag] = 0;
        }
    }

    //Macht Paul
    public boolean BTFSC(int f, int b) {

        int mask = 0;

        //Bitmaske aus der Zahl b erzeugen
        switch (b) {
            case 0:
                mask = 1;
                break;
            case 1:
                mask = 2;
                break;
            case 2:
                mask = 4;
                break;
            case 3:
                mask = 8;
                break;
            case 4:
                mask = 16;
                break;
            case 5:
                mask = 32;
                break;
            case 6:
                mask = 64;
                break;
            case 7:
                mask = 128;
                break;
            default:
                System.err.println("Error beim Setzen des Bits!");
            }
        if (this.activeBank == 0) {
            if ((memoryBank0[f] & mask) > 0) // Wenn der Wert kleiner is als das gesuchte Bit ist es auf jeden Fall nicht gesetzt
            //Bsp.: gsuchtes Bitstelle 4 wenn 13 in f steht kann bitstelle 4(=16) nicht gesetzt sein
            {
                NOP();
                return true;
            } else {
                return false;
            }
        } else {
            if ((memoryBank1[f] & mask) > 0) // Wenn der Wert kleiner is als das gesuchte Bit ist es auf jeden Fall nicht gesetzt
            //Bsp.: gsuchtes Bitstelle 4 wenn 13 in f steht kann bitstelle 4(=16) nicht gesetzt sein
            {
                NOP();
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean BTFSS(int f, int b) {


        int mask = 0;

        //Bitmaske aus der Zahl b erzeugen
        switch (b) {
            case 0:
                mask = 1;
                break;
            case 1:
                mask = 2;
                break;
            case 2:
                mask = 4;
                break;
            case 3:
                mask = 8;
                break;
            case 4:
                mask = 16;
                break;
            case 5:
                mask = 32;
                break;
            case 6:
                mask = 64;
                break;
            case 7:
                mask = 128;
                break;
            default:
                System.err.println("Error beim Setzen des Bits!");
            }
        if (this.activeBank == 0) {
            if (!((memoryBank0[f] & mask) > 0)) // Wenn der Wert kleiner is als das gesuchte Bit ist es auf jeden Fall nicht gesetzt
            //Bsp.: gsuchtes Bitstelle 4 wenn 13 in f steht kann bitstelle 4(=16) nicht gesetzt sein
            {

                return false;
            } else {
                NOP();
                return true;
            }
        } else {
            if (!((memoryBank1[f] & mask) > 0)) // Wenn der Wert kleiner is als das gesuchte Bit ist es auf jeden Fall nicht gesetzt
            //Bsp.: gsuchtes Bitstelle 4 wenn 13 in f steht kann bitstelle 4(=16) nicht gesetzt sein
            {
                return false;
            } else {
                NOP();
                return true;
            }
        }
    }

    public void CLRWDT() {
    }

    public void RETFIE() {
    }

    public void RETLW(int f) {
    }

    public void SLEEP(int f, int b) {
        this.WDT = 0;
        this.prescaler++;
    }

    public void SUBWF(int f, int d) {

        if (this.activeBank == 0) {

            int result = this.akku - this.memoryBank0[f];
            if (d == 0) {
                if (this.akku < this.memoryBank0[f]) { // Wenn als ERG was negatives rauskommt
                    changeStatusReg(cFlag, this.memoryBank0[f]);
                    //result -= 255; Muss man hier was machen? Um bspw -3 darzustellen?
                    changeStatusReg(zFlag, 0);
                    this.akku = result;

                } else {
                    changeStatusReg(cFlag, 0);
                    if (result == 0) {
                        changeStatusReg(zFlag, 1);
                    } else {
                        changeStatusReg(zFlag, 0);
                    }
                    this.akku = result;
                }
            } else if (d == 1) {
                if (this.akku < this.memoryBank0[f]) { // Wenn als ERG was negatives rauskommt
                    changeStatusReg(cFlag, this.memoryBank0[f]);
                    //result -= 255; Muss man hier was machen? Um bspw -3 darzustellen?
                    changeStatusReg(zFlag, 0);
                    this.memoryBank0[f] = result;

                } else {
                    changeStatusReg(cFlag, 0);
                    if (result == 0) {
                        changeStatusReg(zFlag, 1);
                    } else {
                        changeStatusReg(zFlag, 0);
                    }
                    this.memoryBank0[f] = result;
                }
            }

        } else if (this.activeBank == 1) {
            if (d == 0) {
                int result = this.akku - this.memoryBank1[f];

                if (this.akku < this.memoryBank1[f]) { // Wenn als ERG was negatives rauskommt
                    changeStatusReg(cFlag, this.memoryBank1[f]);
                    //result -= 255; Muss man hier was machen? Um bspw -3 darzustellen?
                    changeStatusReg(zFlag, 0);
                    this.akku = result;

                } else {
                    changeStatusReg(cFlag, 0);
                    if (result == 0) {
                        changeStatusReg(zFlag, 1);
                    } else {
                        changeStatusReg(zFlag, 0);
                    }
                    this.akku = result;
                }
            } else if (d == 1) {
                int result = this.akku - this.memoryBank1[f];

                if (this.akku < this.memoryBank1[f]) { // Wenn als ERG was negatives rauskommt
                    changeStatusReg(cFlag, this.memoryBank1[f]);
                    //result -= 255; Muss man hier was machen? Um bspw -3 darzustellen?
                    changeStatusReg(zFlag, 0);
                    this.akku = result;

                } else {
                    changeStatusReg(cFlag, 0);
                    if (result == 0) {
                        changeStatusReg(zFlag, 1);
                    } else {
                        changeStatusReg(zFlag, 0);
                    }
                    this.akku = result;
                }
            }
        }
    }
}
