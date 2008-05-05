package my.PICSIMGUI;

import java.util.EmptyStackException;
import java.util.Stack;

public class InstructionInterpreter implements Runnable{

    public  String[] input;
    private int[] instructions;
    PICSIMGUI gui;
    PicCPU pic;
    public boolean running;
    Stack<Integer> CallCount = new Stack<Integer>();



    public void run()
    {     
        
        for (int i = 0; i <= (input.length - 1); i++) 
        {
            if(running ==true)
            {
                int ret = translateCodeLine(i);
                if (ret != -2 && ret != -1) 
                {
                    i = ret;
                }

                if (ret == -1) 
                {
                    System.err.println("Error in line " + i);
                    break;

                }
                
                try
                { 
                    PICSIMGUI.pic.setPortA(i);
                    gui.setPortARadios(pic.getPortA(), pic.memory[5]);
                    gui.refreshGui();
                    Thread.sleep(70);
                }
                catch(InterruptedException ie)
                {
                    System.err.println("InteruptedExeption -> " + ie.getClass());
                }
            }
            else if (running == false)
            {
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

        for (String singleLines : input) {
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

    /**
     * 
     * @param line Zeilennummer des eingelesenen Codes (spalte gant links)
     * @return Returnwert ist entweder die Zeilennummer an die gesprungen wird
     *         oder -1 wenn ein Opcode nicht erkannt wurde, oder -2 als defaut.
     */
    int translateCodeLine(int line) {
        /**
         * Aktueller Befehl mit Oppcode Bitweise verundet, wenn hinterher der
         * Oppcode wieder rauskommt, ist das der befehl der inter dem oppcode steckt
         */
        if (instructions[line] >= 128 && instructions[line] <= 255) {
            int f = instructions[line] & 127; //0x00000011111111
            gui.setStatusLabel("MOFWF " + f);
            pic.MOFWF(f);
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
            gui.setStatusLabel("ANDWF, F = " + f +" d ist " + d);
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
            System.out.println(line + " ist befehl addlw, f ist " + f);
            pic.ADDLW(f);
            return -2;
        } else if (instructions[line] >= 2304 && instructions[line] <= 2559) {
            int d = instructions[line] & 128;
            int f = instructions[line] & 172;
            System.out.println(line + " ist befehl comf, f ist " + f + "d ist " + d);
            pic.COMF(f, d);
            return -2;
        } else if (instructions[line] >= 768 && instructions[line] <= 1023) {
            int d = instructions[line] & 128;
            int f = instructions[line] & 127;
            System.out.println(line + " ist befehl decf, f ist " + f + "d ist " + d);
            pic.DECF(f, d);
            return -2;
        } else if (instructions[line] >= 2816 && instructions[line] <= 3071) {
            //00 1011 dfff ffff -> Destination Bit prüfen
            int f = instructions[line] & 127;
            System.out.println(line + " ist befehl decfsz, f ist " + f);
            return -2;
        } else if (instructions[line] >= 3840 && instructions[line] <= 4095) {
            //00 1111 dfff ffff -> Destination Bit prüfen
            int f = instructions[line] & 127;
            System.out.println(line + " ist befehl incfsz, f ist " + f);
            return -2;
        } else if (instructions[line] >= 1024 && instructions[line] <= 1279) {
            //00 0100 dfff ffff -> Destination Bit prüfen
            int f = instructions[line] & 127;
            System.out.println(line + " ist befehl iorwf, f ist " + f);
            return -2;
        } else if (instructions[line] >= 2048 && instructions[line] <= 2303) {
            //00 1000 dfff ffff -> Destination Bit prüfen
            int f = instructions[line] & 127;
            System.out.println(line + " ist befehl movf, f ist " + f);
            return -2;
        } else if (instructions[line] == 0 || instructions[line] == 32 || instructions[line] == 64 || instructions[line] == 96) {
            System.out.println(line + " ist befehl nop");
            return -2;
        } else if (instructions[line] >= 3328 && instructions[line] <= 3583) {
            //00 1101 dfff ffff -> Destination Bit prüfen
            int f = instructions[line] & 127;
            System.out.println(line + " ist befehl rlf, f ist " + f);
            return -2;
        } else if (instructions[line] >= 3072 && instructions[line] <= 3327) {
            //00 1100 dfff ffff -> Destination Bit prüfen
            int f = instructions[line] & 127;
            System.out.println(line + " ist befehl rrf, f ist " + f);
            return -2;
        } else if (instructions[line] >= 512 && instructions[line] <= 767) {
            //00 0010 dfff ffff -> Destination Bit prüfen
            int f = instructions[line] & 127;
            System.out.println(line + " ist befehl subwf, f ist " + f);
            return -2;
        } else if (instructions[line] >= 3584 && instructions[line] <= 3839) {
           //00 1110 dfff ffff -> Destination Bit prüfen
            int f = instructions[line] & 127;
            System.out.println(line + " ist befehl swapf, f ist " + f);
            return -2;
        } else if (instructions[line] >= 1536 && instructions[line] <= 1791) {
            //00 0110 dfff ffff -> Destination Bit prüfen
            int f = instructions[line] & 127;
            System.out.println(line + " ist befehl xorwf, f ist " + f);
            return -2;
        } else if (instructions[line] >= 4096 && instructions[line] <= 5119) {
            //01 00bb bfff ffff -> Bits b prüfen
            int f = instructions[line] & 127;
            System.out.println(line + " ist befehl bcf, f ist " + f);
            return -2;
        } else if (instructions[line] >= 5120 && instructions[line] <= 6143) {
            //01 01bb bfff ffff -> Bits b prüfen
            int f = instructions[line] & 127;
            System.out.println(line + " ist befehl bsf, f ist " + f);
            return -2;
        } else if (instructions[line] >= 6144 && instructions[line] <= 7167) {
            //01 10bb bfff ffff -> Bits b prüfen
            int f = instructions[line] & 127;
            int b = instructions[line] & 896;
            System.out.println(line + " ist befehl btfsc, f ist " + f + "b ist " + b);
            return -2;
        } else if (instructions[line] >= 7168 && instructions[line] <= 8191) {
            //01 11bb bfff ffff -> Bits b prüfen
            int f = instructions[line] & 127;
            System.out.println(line + " ist befehl btfss, f ist " + f);
            return -2;
        } else if (instructions[line] >= 15360 && instructions[line] <= 15871) {
            int f = instructions[line] & 255;
            System.out.println(line + " ist befehl sublw, f ist " + f);
            return -2;
        } else if (instructions[line] >= 14848 && instructions[line] <= 15103) {
            int f = instructions[line] & 255;
            System.out.println(line + " ist befehl xorlw, f ist " + f);
            return -2;
        } else if (instructions[line] == 100) {
            System.out.println(line + " ist befehl clrwdt");
            return -2;
        } else if (instructions[line] >= 14336 && instructions[line] <= 14591) {
            int f = instructions[line] & 255;
            System.out.println(line + " ist befehl iorlw, f ist " + f);
            return -2;
        } else if (instructions[line] == 9) {
            System.out.println(line + " ist befehl retfie");
            return -2;
        } else if (instructions[line] >= 13312 && instructions[line] <= 14335) {
            int f = instructions[line] & 255;
            int returnTo = CallCount.pop();
            System.out.println(line + " ist befehl retlw, Rücksprungadresse ist " + returnTo + "Literal ist " + f);
            return returnTo;
        } else if (instructions[line] >= 8192 && instructions[line] <= 10239) {
            int f = instructions[line] & 2047;
            System.out.println(line + " ist befehl Call, Sprungadresse ist " + f);
            CallCount.push(line + 1);
            return f - 1;
        } else if (instructions[line] >= 12288 && instructions[line] <= 13311) {
            int k = instructions[line] & 255;
            System.out.println(line + " ist befehl movlw, k ist " + k);
            return -2;
        } else if (instructions[line] == 8) {
            int returnTo;
            try {
                returnTo = CallCount.pop();
            } catch (EmptyStackException e) {
                System.err.println("Call Stack ist leer!");
                return -1;
            }
            System.out.println(line + " ist befehl return. Sprungadresse: " + (returnTo + 1));
            return returnTo; /*Rücksprungadresse*/
        } else if (instructions[line] == 99) {
            System.out.println(line + " ist befehl sleep");
            return -2;
        }else if (instructions[line] >= 10240 && instructions[line] <= 12287) {
            int k = instructions[line] & 2047;
            System.out.println(line + " ist Befehl goto, Sprungadresse: " + k);
            return k-1;// Minus eins, weil der returnwert in der interpreterschleife hinterher um 1 erhöht wird
        } else if (instructions[line] >= 2560 && instructions[line] <= 2815) {
            int f = instructions[line] & 127;
            System.out.println(line + " ist befehl incf, f ist " + f);
            pic.INCF(f);
            return -2;
        } else {
            /*
             * Kein passender Assemblerbefehl gefunden
             */
            return -1;
        }
    }

    public void testPrint() {
        for (long x : this.instructions) {
            System.out.println(x);
        }
    }
}
