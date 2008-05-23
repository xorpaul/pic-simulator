package my.PICSIMGUI;

import java.util.EmptyStackException;

public class InstructionInterpreter implements Runnable {

    public String[] input;
    private int[] instructions;
    PICSIMGUI gui;
    PicCPU pic;

    public void run() {

        for (int i = 0; i <= (input.length - 1); i++) {
            if (gui.running == true) {
                int ret = translateCodeLine(i);
                if (ret != -2 && ret != -1) {
                    i = ret;
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
                    pic.linie = i;
                    gui.refreshGui();




                    if (gui.interpreterSlow) {
                        Thread.sleep(1000);
                    } else {
                        Thread.sleep(10);
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

    /**
     * @category Konstruktor
     * @param aInput Erwartet als Parameter ein Array, in jedem Feld steht eine Textzeile.
     * @param gui GUI des Programms als Objekt mitgeben
     * @param pic PIC CPU als Objekt mitgeben
     */
    InstructionInterpreter(String[] aInput, PICSIMGUI gui, PicCPU pic) {
        this.input = aInput;
        this.gui = gui;
        this.pic = pic;
        
        int[] newInstructions = new int[input.length + 1];
        int i = 0;
        for (String singleLines : input) {
            // e(this.input[i]);
            String singleLinesTrimmed = singleLines.trim();

            if (singleLinesTrimmed.matches("")) {
                continue;
            }

            String adresseHex = singleLinesTrimmed.substring(0, 4);
            String instructionHex = singleLinesTrimmed.substring(5, 9);
            int adresseInt = Integer.parseInt(adresseHex, 16);
            int instructionInt = Integer.parseInt(instructionHex, 16);

            newInstructions[adresseInt] = instructionInt;

        }
        this.instructions = newInstructions;
    }

    static public void e(Object... parameters) {
        if (parameters.length == 2) {
            System.out.print(parameters[0]);
        } else {
            System.out.print(parameters[0]);
        }
    }

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
        if (instructions[line] >= 128 && instructions[line] <= 255) {
            int f = instructions[line] & 127; //0x00000011111111
            gui.setStatusLabel("MOVWF " + f);
            pic.MOVWF(f);
            System.out.println(line + " ist befehl movwf, f ist " + f);
            return -2;
        } else if (instructions[line] >= 256 && instructions[line] <= 383) {
            gui.setStatusLabel("CLRW");
            pic.CLRW();
            System.out.println(line + " ist befehl clrw, f ist ");
            return -2;
        } else if (instructions[line] >= 14592 && instructions[line] <= 14847) {
            int f = instructions[line] & 255;
            gui.setStatusLabel("ANDLW " + f);
            pic.ANDLW(f);
            System.out.println(line + " ist befehl andlw, f ist " + f);
            return -2;
        } else if (instructions[line] >= 1792 && instructions[line] <= 2047) {
            int f = instructions[line] & 127;
            int d = instructions[line] & 128;
            gui.setStatusLabel("ADDWF, F = " + f + " d ist " + d);
            pic.ADDWF(f, d);
            System.out.println(line + " ist befehl addwf, f ist " + f);
            return -2;
        } else if (instructions[line] >= 1280 && instructions[line] <= 1535) {
            int f = instructions[line] & 127;
            int d = instructions[line] & 128;
            gui.setStatusLabel("ANDWF, F = " + f + " d ist " + d);
            System.out.println(line + " ist befehl andwf, f ist " + f);
            pic.ANDWF(f, d);
            return -2;
        } else if (instructions[line] >= 384 && instructions[line] <= 511) {
            int f = instructions[line] & 127;
            gui.setStatusLabel("CLRF, F = " + f);
            System.out.println(line + " ist befehl clrf, f ist " + f);
            pic.CLRF(f);
            return -2;
        } else if (instructions[line] >= 15872 && instructions[line] <= 16383) {
            int f = instructions[line] & 255;
            gui.setStatusLabel("ADDLW " + f);
            System.out.println(line + " ist befehl addlw, f ist " + f);
            pic.ADDLW(f);
            return -2;
        } else if (instructions[line] >= 2304 && instructions[line] <= 2559) {
            int d = instructions[line] & 128;
            int f = instructions[line] & 172;
            gui.setStatusLabel("COMF " + f + "," + d);
            System.out.println(line + " ist befehl comf, f ist " + f + " d ist " + d);
            pic.COMF(f, d);
            return -2;
        } else if (instructions[line] >= 768 && instructions[line] <= 1023) {
            int d = instructions[line] & 128;
            int f = instructions[line] & 127;
            gui.setStatusLabel("DECF " + f + "," + d);
            System.out.println(line + " ist befehl decf, f ist " + f + " d ist " + d);
            pic.DECF(f, d);
            return -2;
        } else if (instructions[line] >= 2816 && instructions[line] <= 3071) {
            int d = instructions[line] & 128;
            int f = instructions[line] & 127;
            gui.setStatusLabel("DECFSZ " + f + "," + d);
            System.out.println(line + " ist befehl decfsz, f ist " + f + " d ist " + d);
            if (pic.DECFSZ(f, d)) {
                return line + 1;
            } else {
                return -2;
            }
        } else if (instructions[line] >= 3840 && instructions[line] <= 4095) {
            int d = instructions[line] & 128;
            int f = instructions[line] & 127;
            gui.setStatusLabel("INCFSZ " + f + "," + d);
            System.out.println(line + " ist befehl incfsz, f ist " + f + " d ist " + d);
            if (pic.INCFSZ(f, d)) {
                return line + 1;
            } else {
                return -2;
            }
        } else if (instructions[line] >= 1024 && instructions[line] <= 1279) {
            int d = instructions[line] & 128;
            int f = instructions[line] & 127;
            gui.setStatusLabel("IORWF " + f + "," + d);
            System.out.println(line + " ist befehl iorwf, f ist " + f + " d ist " + d);
            pic.IORWF(f, d);
            return -2;
        } else if (instructions[line] >= 2048 && instructions[line] <= 2303) {
            int d = instructions[line] & 128;
            int f = instructions[line] & 127;
            gui.setStatusLabel("MOVF " + f + "," + d);
            System.out.println(line + " ist befehl movf, f ist " + f + "d ist " + d);
            pic.MOVF(f, d);
            return 2;
        } else if (instructions[line] == 0 || instructions[line] == 32 || instructions[line] == 64 || instructions[line] == 96) {
            System.out.println(line + " ist befehl nop");
            gui.setStatusLabel("NOP");
            pic.NOP();
            return -2;
        } else if (instructions[line] >= 3328 && instructions[line] <= 3583) {
            int d = instructions[line] & 128;
            int f = instructions[line] & 127;
            gui.setStatusLabel("RLF " + f + "," + d);
            System.out.println(line + " ist befehl rlf, f ist " + f + " d ist " + d);
            pic.RLF(f, d);
            return -2;
        } else if (instructions[line] >= 3072 && instructions[line] <= 3327) {
            int d = instructions[line] & 128;
            int f = instructions[line] & 127;
            gui.setStatusLabel("RRF " + f + "," + d);
            System.out.println(line + " ist befehl rrf, f ist " + f + " d ist " + d);
            pic.RRF(f, d);
            return -2;
        } else if (instructions[line] >= 512 && instructions[line] <= 767) {
            int d = instructions[line] & 128;
            int f = instructions[line] & 127;
            gui.setStatusLabel("SUBWF " + f + "," + d);
            System.out.println(line + " ist befehl subwf, f ist " + f + " d ist " + d);
            pic.SUBWF(f, d);
            return -2;
        } else if (instructions[line] >= 3584 && instructions[line] <= 3839) {
            int d = instructions[line] & 128;
            int f = instructions[line] & 127;
            gui.setStatusLabel("SWAPF " + f + "," + d);
            System.out.println(line + " ist befehl swapf, f ist " + f + " d ist " + d);
            pic.SWAPF(f, d);
            return -2;
        } else if (instructions[line] >= 1536 && instructions[line] <= 1791) {
            int d = instructions[line] & 128;
            int f = instructions[line] & 127;
            gui.setStatusLabel("XORWF " + f + "," + d);
            System.out.println(line + " ist befehl xorwf, f ist " + f + " d ist " + d);
            pic.XORWF(f, d);
            return -2;
        } else if (instructions[line] >= 4096 && instructions[line] <= 5119) {
            int f = instructions[line] & 127;
            int b = getBitsFromB(instructions[line] & 896);
            gui.setStatusLabel("BCF " + f + "," + b);
            System.out.println(line + " ist befehl bcf, f ist " + f + " b ist " + b);
            pic.BCF(f, b);
            return -2;
        } else if (instructions[line] >= 5120 && instructions[line] <= 6143) {
            int b = getBitsFromB(instructions[line] & 896);
            int f = instructions[line] & 127;
            gui.setStatusLabel("BSF " + f + "," + b);
            System.out.println(line + " ist befehl bsf, f ist " + f + " b ist " + b);
            pic.BSF(f, b);
            return -2;
        } else if (instructions[line] >= 6144 && instructions[line] <= 7167) {
            int f = instructions[line] & 127;
            int b = getBitsFromB(instructions[line] & 896);
            gui.setStatusLabel("BTFSC " + f + "," + b);
            System.out.println(line + " ist befehl btfsc, f ist " + f + " b ist " + b);
            if (pic.BTFSC(f, b)) {
                return line + 1;
            } else {
                return -2;
            }
        } else if (instructions[line] >= 7168 && instructions[line] <= 8191) {
            int b = getBitsFromB(instructions[line] & 896);
            int f = instructions[line] & 127;
            gui.setStatusLabel("BTFSS " + f + "," + b);
            System.out.println(line + " ist befehl btfss, f ist " + f + " b ist " + b);
            if (pic.BTFSS(f, b)) {
                return line + 1;
            } else {
                return -2;
            }
        } else if (instructions[line] >= 15360 && instructions[line] <= 15871) {
            int f = instructions[line] & 255;
            gui.setStatusLabel("SUBLW " + f);
            System.out.println(line + " ist befehl sublw, f ist " + f);
            pic.SUBLW(f);
            return -2;
        } else if (instructions[line] >= 14848 && instructions[line] <= 15103) {
            int f = instructions[line] & 255;
            gui.setStatusLabel("XORLW " + f);
            System.out.println(line + " ist befehl xorlw, f ist " + f);
            pic.XORLW(f);
            return -2;
        } else if (instructions[line] == 100) {
            System.out.println(line + " ist befehl clrwdt");
            gui.setStatusLabel("CLRWDT");
            pic.CLRWDT(); //wo im speicher ist der watchdogtimer?
            return -2;
        } else if (instructions[line] >= 14336 && instructions[line] <= 14591) {
            int f = instructions[line] & 255;
            gui.setStatusLabel("IORLW " + f);
            System.out.println(line + " ist befehl iorlw, f ist " + f);
            pic.IORLW(f);
            return -2;
        } else if (instructions[line] == 9) {
            gui.setStatusLabel("RETFIE");
            System.out.println(line + " ist befehl retfie");
            pic.RETFIE();
            return -2;
        } else if (instructions[line] >= 13312 && instructions[line] <= 14335) {
            int f = instructions[line] & 255;
            int returnTo = pic.CallCount.pop();
            gui.setStatusLabel("RETLW " + f);
            System.out.println(line + " ist befehl retlw, Rücksprungadresse ist " + returnTo + "Literal ist " + f);
            pic.RETLW(f);
            return returnTo;
        } else if (instructions[line] >= 8192 && instructions[line] <= 10239) {
            int f = instructions[line] & 2047;
            gui.setStatusLabel("CALL " + f);
            System.out.println(line + " ist befehl Call, Sprungadresse ist " + f);
            pic.CallCount.push(line + 1);
            return f - 1;
        } else if (instructions[line] >= 12288 && instructions[line] <= 13311) {
            int l = instructions[line] & 255;
            gui.setStatusLabel("MOVLW " + l);
            System.out.println(line + " ist befehl movlw, l ist " + l);
            pic.MOVLW(l);
            return -2;
        } else if (instructions[line] == 8) {
            int returnTo;
            try {
                returnTo = pic.CallCount.pop();
            } catch (EmptyStackException e) {
                System.err.println("Call Stack ist leer!");
                return -1;
            }
            gui.setStatusLabel("RETURN ");
            System.out.println(line + " ist befehl return. Sprungadresse: " + (returnTo + 1));
            return returnTo; /*Rücksprungadresse*/
        } else if (instructions[line] == 99) {
            gui.setStatusLabel("SLEEP");
            System.out.println(line + " ist befehl sleep");
            pic.SLEEP(line, line);
            return -2;
        } else if (instructions[line] >= 10240 && instructions[line] <= 12287) {
            int k = instructions[line] & 2047;
            gui.setStatusLabel("GOTO " + k);
            System.out.println(line + " ist Befehl goto, Sprungadresse: " + k);
            return k - 1;// Minus eins, weil der returnwert in der interpreterschleife hinterher um 1 erhöht wird
        } else if (instructions[line] >= 2560 && instructions[line] <= 2815) {
            int f = instructions[line] & 127;
            gui.setStatusLabel("INCF " + f);
            System.out.println(line + " ist befehl incf, f ist " + f);
            pic.INCF(f);
            return -2;
        } else {
            /*
             * Kein passender Assemblerbefehl gefunden
             */
            gui.setStatusLabel("NO MATCHING INSTRUCTION FOUND!");
            return -1;
        }
    }

    public void testPrint() {
        for (long x : this.instructions) {
            System.out.println(x);
        }
    }
}
