package my.PICSIMGUI;

/**
 * Interpretiert den eingelesenen Opcode und ruft enstsprechend die PIC-Funktionen
 * in der PicCPU Klasse auf. Realisiert als Thread. Run Methode ruft desweiteren
 * auch GUI-Refresh, GUI-Einlese Methoden und Synchroisations Methoden auf.
 * 
 * Das GUI und die Instanz des PicCPU sind diesr Klasse als Klassenatribute mitgegeben
 * sowie ein Array, welches das eingelesen File enthält. Mittels Kosnstruktor wird
 * hieraus der Hex-Opcod herausgefiltert, zu Integerwerten umgewandelt und der
 * Interpreter Funktion übergeben.
 */
public class InstructionInterpreter implements Runnable {

    public String[] input;
    public int[] instructions;
    public int[] programmCount; //Verknüpfung PC und OpCode
    PICSIMGUI gui;
    PicCPU pic;

    public void run() {

        int i = 0;

        if (gui.running == true) {
            for (i = 0; i <= (input.length - 1); i++) {

                if (gui.running == true) {

                    pic.linie = i;
                    int ret = translateCodeLine(i);
                    pic.interrupt();

                    if (ret != -2 && ret != -1) {

                        i = ret;
                    //e("Neuer Sprung zu: " + i);
                    }

                    if (ret == -1) {
                        System.err.println("Error in line " + i);
                        break;
                    }
                    try {
                        //pic.setPortA(i);
                        pic.setBank();
                        pic.fsrMemoryManagement();
                        pic.statusToMemory();

                        gui.refreshGui();

                        if (pic.interrupt) {
                            i = pic.linie;
                            pic.interrupt = false;
                        } //Wenn sich aufgrund von Interupts was geändert hat


                        if (gui.interpreterSlow) {
                            Thread.sleep(1000);
                        } else {
                            Thread.sleep(35);
                        }

                        gui.readGui();
                    //Read muss nach dem sleep erfolgen, da die 
                    //eingabe sonst überlesen wird
                    } catch (InterruptedException ie) {
                        System.err.println("InteruptedExeption -> " + ie.getClass());
                    }
                } else if (gui.running == false) {
                    System.out.println("Thread beendet");
                    break;
                }
            }
        }

        if (gui.step == true) {
            i = pic.linie;

            int ret = translateCodeLine(i);
            pic.interrupt();
            if (ret != -2 && ret != -1) {
                pic.linie = ret;
            }

            if (ret == -1) {
                System.err.println("Error in line " + i);
            }
            //pic.setPortA(i);
            pic.setBank();
            pic.fsrMemoryManagement();
            pic.statusToMemory();

            gui.refreshGui();

            if (pic.interrupt) {
                i = pic.linie;
                pic.interrupt = false;
            } //Wenn sich aufgrund von Interupts was geändert hat

            gui.readGui();
            //Read muss nach dem sleep erfolgen, da die 
            //eingabe sonst überlesen wird

            if (gui.step == false) {
                e("Thread beendet");
            }
            pic.linie++;
            gui.step = false;
        }
    }

    /**
     * @param aInput Erwartet als Parameter ein Array, in jedem Feld steht eine Textzeile.
     * @param gui GUI des Programms als Objekt mitgeben
     * @param pic PIC CPU als Objekt mitgeben
     */
    public InstructionInterpreter(String[] aInput,
            PICSIMGUI gui, PicCPU pic) {
        this.input = aInput;
        this.gui = gui;
        this.pic = pic;

        int CodeCount = 0; //Da wie bzw. die Testprogramme mit PC 0001 anfangen

        int[] newInstructions = new int[this.input.length + 1];
        int[] newProgrammCount = new int[this.input.length + 2];

        for (String singleLines : input) {

            // e("Das steht in dem Array input an stelle [" + i + "]: " + this.input[i] + "\n");
            int adresseInt;
            int instructionInt;
            int ProgrammCounterInt;

            String adresseHex = singleLines.substring(0, 4);
            String instructionHex = singleLines.substring(5, 9);
            String ProgrammCounterString = singleLines.substring(20, 25);

            //e("OpCode: " + instructionHex + " PCCount:" + ProgrammCounterHex);

            if (singleLines.matches("")) {
                continue;
            }

            if (!adresseHex.equalsIgnoreCase("    ")) {
                //Wenn eine Zeile MIT OpCode eingelesen wurde
                adresseInt = Integer.parseInt(adresseHex, 16);
            } else {
                adresseInt = this.input.length + 1; //Da SOLLTE er NIE hinkommen!
            }

            if (!instructionHex.equalsIgnoreCase("    ")) {
                //Wenn eine Zeile MIT OpCode eingelesen wurde
                instructionInt = Integer.parseInt(instructionHex, 16);
            } else {
                instructionInt = -1337;
            }

            ProgrammCounterInt = Integer.parseInt(ProgrammCounterString);

            newInstructions[CodeCount] = instructionInt;
            newProgrammCount[adresseInt] = ProgrammCounterInt;

            //e(" ### Parsed-OpCode @ " + CodeCount + " -----  " + newInstructions[CodeCount] + " Parsed-PCCount: " + ProgrammCounterInt + "\n");
            //e(" ### Zum PC " + adresseInt + " gehoert " + newProgrammCount[adresseInt] + "\n");
            CodeCount++;

        }
        //e(" ### Parsed-OpCode @ " + 0 + " -----  "+ newInstructions[0][0] + " Parsed-PCCount: " + newInstructions[0][1] + "\n");

        this.instructions = newInstructions;
        this.programmCount = newProgrammCount;
    }

    static public void e(Object... parameters) {
        if (parameters.length == 2) {
            System.out.print(parameters[0]);
        } else {
            System.out.print(parameters[0]);
        }

    }

    /**Hilfsfunktion für Bitmanipulations-Funktionen des PIC
     * Filter die absolute Zahl des Bits welches manipuliert werde soll aus der 
     * verundung des 14-Bit Opcode mit 11 1000 0000 (stellen im Opcode wo b codiert ist)
     * 
     * @param b aud dem Opcode gefilterte Binärzahl
     * @return absolute Stelle des Bits welche manipuliert werden soll
     */
    public int getBitsFromB(int b) {
        switch (b) {
            case 0:
                return 0;
            case 128:
                return 1;
            case 256:
                return 2;
            case 384:
                return 3;
            case 512:
                return 4;
            case 640:
                return 5;
            case 768:
                return 6;
            case 896:
                return 7;
            default:
                System.err.println("Fehler beim lesen der bits.");
                return -1;
        }

    }

    /**
     * 
     * @param line Zeilennummer des eingelesenen Codes (spalte gant links)
     * @return Returnwert ist entweder die Zeilennummer an die gesprungen wird
     *         oder -1 wenn ein Opcode nicht erkannt wurde, oder -2 als defaut.
     */
    int translateCodeLine(int line) {
        /**
         * Befehle liegen zwischen einem bestimmten wert
         * 
         */
        //e("\n" + instructions[line] + "\n");
        if (instructions[line] >= 128 && instructions[line] <= 255) {
            int f = instructions[line] & 127; //0x00000011111111
            gui.setStatusLabel("MOVWF " + f);
            pic.MOVWF(f);
            System.out.println(programmCount[pic.linie] + " ist befehl movwf, f ist " + f);
            return -2;
        } else if (instructions[line] >= 256 && instructions[line] <= 383) {
            gui.setStatusLabel("CLRW");
            pic.CLRW();
            System.out.println(programmCount[pic.linie] + " ist befehl clrw, f ist ");
            return -2;
        } else if (instructions[line] >= 14592 && instructions[line] <= 14847) {
            int f = instructions[line] & 255;
            gui.setStatusLabel("ANDLW " + f);
            pic.ANDLW(f);
            System.out.println(programmCount[pic.linie] + " ist befehl andlw, f ist " + f);
            return -2;
        } else if (instructions[line] >= 1792 && instructions[line] <= 2047) {
            int f = instructions[line] & 127;
            int d = instructions[line] & 128;
            gui.setStatusLabel("ADDWF, F = " + f + " d ist " + d);
            pic.ADDWF(f, d);
            System.out.println(programmCount[pic.linie] + " ist befehl addwf, f ist " + f);
            return -2;
        } else if (instructions[line] >= 1280 && instructions[line] <= 1535) {
            int f = instructions[line] & 127;
            int d = instructions[line] & 128;
            gui.setStatusLabel("ANDWF, F = " + f + " d ist " + d);
            System.out.println(programmCount[pic.linie] + " ist befehl andwf, f ist " + f);
            pic.ANDWF(f, d);
            return -2;
        } else if (instructions[line] >= 384 && instructions[line] <= 511) {
            int f = instructions[line] & 127;
            gui.setStatusLabel("CLRF, F = " + f);
            System.out.println(programmCount[pic.linie] + " ist befehl clrf, f ist " + f);
            pic.CLRF(f);
            return -2;
        } else if (instructions[line] >= 15872 && instructions[line] <= 16383) {
            int f = instructions[line] & 255;
            gui.setStatusLabel("ADDLW " + f);
            System.out.println(programmCount[pic.linie] + " ist befehl addlw, f ist " + f);
            pic.ADDLW(f);
            return -2;
        } else if (instructions[line] >= 2304 && instructions[line] <= 2559) {
            int d = instructions[line] & 128;
            int f = instructions[line] & 127;
            gui.setStatusLabel("COMF " + f + "," + d);
            System.out.println(programmCount[pic.linie] + " ist befehl comf, f ist " + f + " d ist " + d);
            pic.COMF(f, d);
            return -2;
        } else if (instructions[line] >= 768 && instructions[line] <= 1023) {
            int d = instructions[line] & 128;
            int f = instructions[line] & 127;
            gui.setStatusLabel("DECF " + f + "," + d);
            System.out.println(programmCount[pic.linie] + " ist befehl decf, f ist " + f + " d ist " + d);
            pic.DECF(f, d);
            return -2;
        } else if (instructions[line] >= 2816 && instructions[line] <= 3071) {
            int d = instructions[line] & 128;
            int f = instructions[line] & 127;
            gui.setStatusLabel("DECFSZ " + f + "," + d);
            System.out.println(programmCount[pic.linie] + " ist befehl decfsz, f ist " + f + " d ist " + d);
            if (pic.DECFSZ(f, d)) {
                return line + 1;
            } else {
                return -2;
            }

        } else if (instructions[line] >= 3840 && instructions[line] <= 4095) {
            int d = instructions[line] & 128;
            int f = instructions[line] & 127;
            gui.setStatusLabel("INCFSZ " + f + "," + d);
            System.out.println(programmCount[pic.linie] + " ist befehl incfsz, f ist " + f + " d ist " + d);
            if (pic.INCFSZ(f, d)) {
                return line + 1;
            } else {
                return -2;
            }

        } else if (instructions[line] >= 1024 && instructions[line] <= 1279) {
            int d = instructions[line] & 128;
            int f = instructions[line] & 127;
            gui.setStatusLabel("IORWF " + f + "," + d);
            System.out.println(programmCount[pic.linie] + " ist befehl iorwf, f ist " + f + " d ist " + d);
            pic.IORWF(f, d);
            return -2;
        } else if (instructions[line] >= 2048 && instructions[line] <= 2303) {
            int d = instructions[line] & 128;
            int f = instructions[line] & 127;
            gui.setStatusLabel("MOVF " + f + "," + d);
            System.out.println(programmCount[pic.linie] + " ist befehl movf, f ist " + f + "d ist " + d);
            pic.MOVF(f, d);
            return -2;
        } else if (instructions[line] == 0 || instructions[line] == 32 || instructions[line] == 64 || instructions[line] == 96) {
            System.out.println(programmCount[pic.linie] + " ist befehl nop");
            gui.setStatusLabel("NOP");
            pic.NOP();
            return -2;
        } else if (instructions[line] >= 3328 && instructions[line] <= 3583) {
            int d = instructions[line] & 128;
            int f = instructions[line] & 127;
            gui.setStatusLabel("RLF " + f + "," + d);
            System.out.println(programmCount[pic.linie] + " ist befehl rlf, f ist " + f + " d ist " + d);
            pic.RLF(f, d);
            return -2;
        } else if (instructions[line] >= 3072 && instructions[line] <= 3327) {
            int d = instructions[line] & 128;
            int f = instructions[line] & 127;
            gui.setStatusLabel("RRF " + f + "," + d);
            System.out.println(programmCount[pic.linie] + " ist befehl rrf, f ist " + f + " d ist " + d);
            pic.RRF(f, d);
            return -2;
        } else if (instructions[line] >= 512 && instructions[line] <= 767) {
            int d = instructions[line] & 128;
            int f = instructions[line] & 127;
            gui.setStatusLabel("SUBWF " + f + "," + d);
            System.out.println(programmCount[pic.linie] + " ist befehl subwf, f ist " + f + " d ist " + d);
            pic.SUBWF(f, d);
            return -2;
        } else if (instructions[line] >= 3584 && instructions[line] <= 3839) {
            int d = instructions[line] & 128;
            int f = instructions[line] & 127;
            gui.setStatusLabel("SWAPF " + f + "," + d);
            System.out.println(programmCount[pic.linie] + " ist befehl swapf, f ist " + f + " d ist " + d);
            pic.SWAPF(f, d);
            return -2;
        } else if (instructions[line] >= 1536 && instructions[line] <= 1791) {
            int d = instructions[line] & 128;
            int f = instructions[line] & 127;
            gui.setStatusLabel("XORWF " + f + "," + d);
            System.out.println(programmCount[pic.linie] + " ist befehl xorwf, f ist " + f + " d ist " + d);
            pic.XORWF(f, d);
            return -2;
        } else if (instructions[line] >= 4096 && instructions[line] <= 5119) {
            int f = instructions[line] & 127;
            int b = getBitsFromB(instructions[line] & 896);
            gui.setStatusLabel("BCF " + f + "," + b);
            System.out.println(programmCount[pic.linie] + " ist befehl bcf, f ist " + f + " b ist " + b);
            pic.BCF(f, b);
            return -2;
        } else if (instructions[line] >= 5120 && instructions[line] <= 6143) {
            int b = getBitsFromB(instructions[line] & 896);
            int f = instructions[line] & 127;
            gui.setStatusLabel("BSF " + f + "," + b);
            System.out.println(programmCount[pic.linie] + " ist befehl bsf, f ist " + f + " b ist " + b);
            pic.BSF(f, b);
            return -2;
        } else if (instructions[line] >= 6144 && instructions[line] <= 7167) {
            int f = instructions[line] & 127;
            int b = getBitsFromB(instructions[line] & 896);
            gui.setStatusLabel("BTFSC " + f + "," + b);
            System.out.println(programmCount[pic.linie] + " ist befehl btfsc, f ist " + f + " b ist " + b);
            if (pic.BTFSC(f, b)) {
                return line + 1;
            } else {
                return -2;
            }

        } else if (instructions[line] >= 7168 && instructions[line] <= 8191) {
            int b = getBitsFromB(instructions[line] & 896);
            int f = instructions[line] & 127;
            gui.setStatusLabel("BTFSS " + f + "," + b);
            System.out.println(programmCount[pic.linie] + " ist befehl btfss, f ist " + f + " b ist " + b);
            if (pic.BTFSS(f, b)) {
                return line + 1;
            } else {
                return -2;
            }

        } else if (instructions[line] >= 15360 && instructions[line] <= 15871) {
            int f = instructions[line] & 255;
            gui.setStatusLabel("SUBLW " + f);
            System.out.println(programmCount[pic.linie] + " ist befehl sublw, f ist " + f);
            pic.SUBLW(f);
            return -2;
        } else if (instructions[line] >= 14848 && instructions[line] <= 15103) {
            int f = instructions[line] & 255;
            gui.setStatusLabel("XORLW " + f);
            System.out.println(programmCount[pic.linie] + " ist befehl xorlw, f ist " + f);
            pic.XORLW(f);
            return -2;
        } else if (instructions[line] == 100) {
            System.out.println(programmCount[pic.linie] + " ist befehl clrwdt");
            gui.setStatusLabel("CLRWDT");
            pic.CLRWDT(); //wo im speicher ist der watchdogtimer?
            return -2;
        } else if (instructions[line] >= 14336 && instructions[line] <= 14591) {
            int f = instructions[line] & 255;
            gui.setStatusLabel("IORLW " + f);
            System.out.println(programmCount[pic.linie] + " ist befehl iorlw, f ist " + f);
            pic.IORLW(f);
            return -2;
        } else if (instructions[line] == 9) {
            gui.setStatusLabel("RETFIE");
            System.out.println(programmCount[pic.linie] + " ist befehl retfie");
            return (pic.RETFIE());
        } else if (instructions[line] >= 13312 && instructions[line] <= 14335) {
            int returnTo;
            int l = instructions[line] & 255;

            returnTo =
                    pic.CallStackPop();
            gui.setStatusLabel("RETLW " + l);
            System.out.println(programmCount[pic.linie] + " ist befehl retlw, Rücksprungadresse ist " + programmCount[returnTo] + "Literal ist " + l);
            pic.RETLW(l);
            return returnTo - 1;
        } else if (instructions[line] >= 8192 && instructions[line] <= 10239) {
            int f = instructions[line] & 2047;
            gui.setStatusLabel("CALL " + f);
            System.out.println(programmCount[pic.linie] + " ist befehl Call, Sprungadresse ist " + programmCount[f]);
            pic.CallStackPush(line + 1);
//            try{
//            pic.CallCount.push(line + 1);}
//            catch(Exception e){System.err.println("Konnte nicht auf Stack schreiben");}
            return f - 1;
        } else if (instructions[line] >= 12288 && instructions[line] <= 13311) {
            int l = instructions[line] & 255;
            gui.setStatusLabel("MOVLW " + l);
            System.out.println(programmCount[pic.linie] + " ist befehl movlw, l ist " + l);
            pic.MOVLW(l);
            return -2;
        } else if (instructions[line] == 8) {
            int returnTo;
            returnTo =
                    pic.CallStackPop();
//            try
//            {
//                returnTo = pic.CallCount.pop();
//            }
//            catch (EmptyStackException e)
//            {
//                System.err.println(line + " ist befehl return. Sprungadresse:  ERROR\nCall Stack ist leer!");
//                return -1;
//            }

            gui.setStatusLabel("RETURN ");
            System.out.println(programmCount[pic.linie] + " ist befehl return. Sprungadresse: " + (programmCount[returnTo]));
            return returnTo; /*Rücksprungadresse*/
        } else if (instructions[line] == 99) {
            gui.setStatusLabel("SLEEP");
            System.out.println(programmCount[pic.linie] + " ist befehl sleep");
            pic.SLEEP();
            return -2;
        } else if (instructions[line] >= 10240 && instructions[line] <= 12287) {
            int k = instructions[line] & 2047;
            gui.setStatusLabel("GOTO " + k);
            System.out.println(programmCount[pic.linie] + " ist Befehl goto, Sprungadresse: " + programmCount[k]);
            return k - 1;// Minus eins, weil der returnwert in der interpreterschleife hinterher um 1 erhöht wird
        } else if (instructions[line] >= 2560 && instructions[line] <= 2815) {
            int f = instructions[line] & 127;
            gui.setStatusLabel("INCF " + f);
            System.out.println(programmCount[pic.linie] + " ist befehl incf, f ist " + f);
            pic.INCF(f);
            return -2;
        } else if (instructions[line] == -1337) {
            return -2;
        } else {
            /*
             * Kein passender Assemblerbefehl gefunden
             */
            gui.setStatusLabel("NO MATCHING INSTRUCTION FOUND!");
            return -1;
        }

    }
}
