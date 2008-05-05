package my.PICSIMGUI;

public class PicCPU {

    static int portA = 5; //Adresse von PortA
    static int portB = 6; //Adresse von PortB
    static int trisA = 5; //Adresse von TrisA
    static int trisB = 6; //Adresse von TrisB
    static int zFlag = 2; //Bit# des zFLAG im Statusregister
    static int cFlag = 0; //Bit# des CarryFLAG im Statusregister
    static int dcFlag = 1; //Bit# des dcFLAG im Statusregister
    static int rp0 = 5;//RP0 gesetzt -> Bank 1 aktiv, RP0 nicht gesetz: Bank 0 aktiv
    int[] statusReg = new int[8]; //Status Register als Array
    public int[] memoryBank0; //Gesamte Speichernak 0 des Pic
    public int[] memoryBank1; //Gesamte Speichernak 1 des Pic
    int akku;   //W-Register des Pic

    /**
     * @category Konsruktor. Speicher initilisieren
     */
    PicCPU() {
        memoryBank0 = new int[128];
        memoryBank1 = new int[128];
        akku = 0;
    }
    
    public void setPortA(int value) {
        this.memoryBank0[portA] = value;
    }
    
    /**
     * 
     * @param position Bit welches im Statusregister veränder werden soll. Vrgl static int am Klassenanfang
     * @param value darf 0 oder 1 sein entsprechend für high oder low
     */
    public void changeStatusReg(int position, int value) {
        statusReg[position] = value;
    }

    public int[] getPortA() {
        int a = this.memoryBank0[portA];
        int[] portABits = new int[8];

        for (int i = 0; i < 8; i++) {
            portABits[i] = a % 2;
            a /= 2;
        }
        return portABits;
    }

    public int[] getPortB() {
        int a = this.memoryBank0[portB];
        int[] portBBits = new int[8];

        for (int i = 0; i < 8; i++) {
            portBBits[i] = a % 2;
            a /= 2;
        }
        return portBBits;
    }

    public int[] getW() {
        int a = this.akku;
        int[] WBits = new int[8];

        for (int i = 0; i < 8; i++) {
            WBits[i] = a % 2;
            a /= 2;
        }
        return WBits;
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

//TODO!
    public void INCF(int f) {
        memoryBank0[f]++;
    }

    public void MOFWF(int f) {
        memoryBank0[f] = this.akku;
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

    }

    public void ANDWF(int f, int d) {
        int result = this.akku & this.memoryBank0[f];
        if (d == 0) {
            checkFlags(result);
            this.akku = result;
        } else {
            checkFlags(result);
            this.memoryBank0[f] = result;
        }
    }

    public void CLRF(int f) {
        this.memoryBank0[f] = 0;
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
        int result = this.memoryBank0[f] ^ 255;
        if (d == 0) {
            checkFlags(result);
            this.akku = result;
        } else {
            checkFlags(result);
            this.memoryBank0[f] = result;
        }
    }

    public void DECF(int f, int d) {
        int result = this.memoryBank0[f] - 1;

        if (d == 0) {
            this.akku = result;
        } else {
            this.memoryBank0[f] = result;
        }

        if (result == 0) {
            changeStatusReg(zFlag, 1);
        } else {
            changeStatusReg(zFlag, 0);
        }
    }

    public boolean DECFSZ(int f, int d) {
        int result = this.memoryBank0[f] - 1;
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
    }

    public boolean INCFSZ(int f, int d) {
        int result = this.memoryBank0[f] + 1;
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

    }

    public void IORWF(int f, int d) {
        int result = this.memoryBank0[f] | 255;
        if (d == 0) {
            this.akku = result;
        } else {
            this.memoryBank0[f] = result;
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
    //System.err.println("Im Akku steht: " + this.akku);

    }
}
