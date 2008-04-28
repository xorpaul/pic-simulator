package my.PICSIMGUI;

import java.util.Stack;

public class InstructionInterpreter {

    String[] input;
    private int[] instructions;
    PicCPU pic;
    Stack<Integer> CallCount = new Stack<Integer>();

    /**
     * @category Konstruktor
     * @param aInput Erwartet als Parameter ein Array, in jedem Feld steht eine Textzeile.
     */
    InstructionInterpreter(String[] aInput) {
        this.input = aInput;

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

            pic = new PicCPU();
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
            System.out.println(line + " ist befehl movwf, f ist " + f);
            return -2;
        } else if (instructions[line] >= 256 && instructions[line] <= 383) {
            System.out.println(line + " ist befehl clrw, f ist ");
            return -2;
        } else if (instructions[line] >= 14592 && instructions[line] <= 14847) {
            int f = instructions[line] & 255;
            System.out.println(line + " ist befehl andlw, f ist " + f);
            return -2;
        } else if (instructions[line] >= 1792 && instructions[line] <= 2047) {
            //F NOCH MACHEN!!!! ###########
            int f = instructions[line] & 255;
            System.out.println(line + " ist befehl addwf, f ist " + f);
            return -2;
        } else if (instructions[line] >= 1280 && instructions[line] <= 1535) {
            //F NOCH MACHEN!!!! ###########
            int f = instructions[line] & 255;
            System.out.println(line + " ist befehl andwf, f ist " + f);
            return -2;
        } else if (instructions[line] >= 384 && instructions[line] <= 511) {
            //F NOCH MACHEN!!!! ###########
            int f = instructions[line] & 255;
            System.out.println(line + " ist befehl clrf, f ist " + f);
            return -2;
        } else if (instructions[line] >= 15872 && instructions[line] <= 16383) {
            //F NOCH MACHEN!!!! ###########
            int f = instructions[line] & 255;
            System.out.println(line + " ist befehl addlw, f ist " + f);
            return -2;
        } else if (instructions[line] >= 256 && instructions[line] <= 383) {
            //F NOCH MACHEN!!!! ###########
            int f = instructions[line] & 255;
            System.out.println(line + " ist befehl clrw, f ist " + f);
            return -2;
        } else if (instructions[line] >= 2304 && instructions[line] <= 2559) {
            //F NOCH MACHEN!!!! ###########
            int f = instructions[line] & 255;
            System.out.println(line + " ist befehl comf, f ist " + f);
            return -2;
        } else if (instructions[line] >= 768 && instructions[line] <= 1023) {
            //F NOCH MACHEN!!!! ###########
            int f = instructions[line] & 255;
            System.out.println(line + " ist befehl decf, f ist " + f);
            return -2;
        } else if (instructions[line] >= 2816 && instructions[line] <= 3071) {
            //F NOCH MACHEN!!!! ###########
            int f = instructions[line] & 255;
            System.out.println(line + " ist befehl decfsz, f ist " + f);
            return -2;
        } else if (instructions[line] >= 3840 && instructions[line] <= 4095) {
            //F NOCH MACHEN!!!! ###########
            int f = instructions[line] & 255;
            System.out.println(line + " ist befehl incfsz, f ist " + f);
            return -2;
        } else if (instructions[line] >= 1024 && instructions[line] <= 1279) {
            //F NOCH MACHEN!!!! ###########
            int f = instructions[line] & 255;
            System.out.println(line + " ist befehl iorwf, f ist " + f);
            return -2;
        } else if (instructions[line] >= 2048 && instructions[line] <= 2303) {
            //F NOCH MACHEN!!!! ###########
            int f = instructions[line] & 255;
            System.out.println(line + " ist befehl movf, f ist " + f);
            return -2;
        } else if (instructions[line] == 0 || instructions[line] == 32 || instructions[line] == 64 || instructions[line] == 96) {
            //F NOCH MACHEN!!!! ###########
            int f = instructions[line] & 255;
            System.out.println(line + " ist befehl nop, f ist " + f);
            return -2;
        } else if (instructions[line] >= 3328 && instructions[line] <= 3583) {
            //F NOCH MACHEN!!!! ###########
            int f = instructions[line] & 255;
            System.out.println(line + " ist befehl rlf, f ist " + f);
            return -2;
        } else if (instructions[line] >= 3072 && instructions[line] <= 3327) {
            //F NOCH MACHEN!!!! ###########
            int f = instructions[line] & 255;
            System.out.println(line + " ist befehl rrf, f ist " + f);
            return -2;
        } else if (instructions[line] >= 512 && instructions[line] <= 767) {
            //F NOCH MACHEN!!!! ###########
            int f = instructions[line] & 255;
            System.out.println(line + " ist befehl subwf, f ist " + f);
            return -2;
        } else if (instructions[line] >= 3584 && instructions[line] <= 3839) {
            //F NOCH MACHEN!!!! ###########
            int f = instructions[line] & 255;
            System.out.println(line + " ist befehl swapf, f ist " + f);
            return -2;
        } else if (instructions[line] >= 1536 && instructions[line] <= 1791) {
            //F NOCH MACHEN!!!! ###########
            int f = instructions[line] & 255;
            System.out.println(line + " ist befehl xorwf, f ist " + f);
            return -2;
        } else if (instructions[line] >= 4096 && instructions[line] <= 5119) {
            //F NOCH MACHEN!!!! ###########
            int f = instructions[line] & 255;
            System.out.println(line + " ist befehl bcf, f ist " + f);
            return -2;
        } else if (instructions[line] >= 5120 && instructions[line] <= 6143) {
            //F NOCH MACHEN!!!! ###########
            int f = instructions[line] & 255;
            System.out.println(line + " ist befehl bsf, f ist " + f);
            return -2;
        } else if (instructions[line] >= 6144 && instructions[line] <= 7167) {
            //F NOCH MACHEN!!!! ###########
            int f = instructions[line] & 255;
            System.out.println(line + " ist befehl btfsc, f ist " + f);
            return -2;
        } else if (instructions[line] >= 7168 && instructions[line] <= 8191) {
            //F NOCH MACHEN!!!! ###########
            int f = instructions[line] & 255;
            System.out.println(line + " ist befehl bfss, f ist " + f);
            return -2;
        } else if (instructions[line] >= 15360 && instructions[line] <= 15871) {
            //F NOCH MACHEN!!!! ###########
            int f = instructions[line] & 255;
            System.out.println(line + " ist befehl sublw, f ist " + f);
            return -2;
        } else if (instructions[line] >= 14848 && instructions[line] <= 15103) {
            //F NOCH MACHEN!!!! ###########
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
            int f = instructions[line] & 1023;
            // int returnTo = CallCount.pop();
            //System.out.println(line + " ist befehl retlw, Rücksprungadresse ist " + returnTo + "Literal ist " + f);
            System.out.println(line + " ist befehl retlw");
            //return returnTo;
            return -2;
        } else if (instructions[line] >= 8192 && instructions[line] <= 10239) {
            int f = instructions[line] & 2047;
            System.out.println(line + " ist befehl Call, Sprungadresse ist " + f);
            //CallCount.push(line + 1);
            //return f - 1;
            return -2;
        } else if (instructions[line] >= 12288 && instructions[line] <= 13311) {
            int k = instructions[line] & 255;
            System.out.println(line + " ist befehl movlw, k ist " + k);
            return -2;
        } else if (instructions[line] == 8) {
            int returnTo;
            /* try {
            returnTo = CallCount.pop();
            } catch (EmptyStackException e) {
            System.err.println("Call Stack ist leer!");
            return -1;
            }*/
            //  System.out.println(line + " ist befehl return. Sprungadresse: " + (returnTo + 1));
            System.out.println(line + " ist befehl return.");
            //return returnTo; /*Rücksprungadresse*/
            return -2;
        } else if (instructions[line] == 99) {
            System.out.println(line + " ist befehl sleep, f gibt es nicht ");
            return -2;
        } else if (instructions[line] >= 15360 && instructions[line] <= 15871) {
            int f = instructions[line] & 511;
            System.out.println(line + " ist befehl sublw, f ist " + f);
            return -2;
        } else if (instructions[line] >= 14848 && instructions[line] <= 15103) {
            int f = instructions[line] & 255;
            System.out.println(line + " ist befehl xorlw, f ist " + f);
            return -2;
        } else if (instructions[line] >= 10240 && instructions[line] <= 12287) {
            int k = instructions[line] & 0x7ff;
            System.out.println(line + " ist Befehl goto, Sprungadresse: " + k);
            //return k;
            return -2;
        } else if (instructions[line] >= 2560 && instructions[line] <= 2815) {
            int f = instructions[line] & 127;
            System.out.println(line + " ist befehl incf, f ist " + f);

            pic.setPortA(22);
            pic.INCF(f);
            return -2;
        } else { /*
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
