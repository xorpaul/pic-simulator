package my.PICSIMGUI;

public class PicCPU {

    static int portA = 5; //Adresse von PortA
    static int portB = 6; //Adresse von PortA
    static int zFlag = 2;
    static int cFlag = 0;
    static int dcFlag = 1;
    int[] statusReg = new int[8];
    public int[] memory; //Gesamter Speicher des Pic
    int akku;

    PicCPU() {
        memory = new int[256];
        akku = 0;
    }

    public void setPortA(int value) {
        this.memory[portA] = value;
    }

    public void changeStatusReg(int position, int value) {
        statusReg[position] = value;
    }

    public int[] getPortA() {
        int a = this.memory[portA];
        int[] portABits = new int[8];

        for (int i = 0; i < 8; i++) {
            portABits[i] = a % 2;
            a /= 2;
        }
        return portABits;
    }

    public int[] getPortB() {
        int a = this.memory[portB];
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
        System.err.println("Im Port A steht: " + this.memory[portA]);
    }

    public void printW() {
        System.err.println("Im Akku steht: " + this.akku);
    }

//TODO!
    public void INCF(int f) {
        memory[f]++;
    }

    public void MOFWF(int f) {
        memory[f] = this.akku;
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
        int result = this.akku + this.memory[f];
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
                this.memory[f] = result;

            } else {
                checkFlags(result);
                this.memory[f] = result;
            }
        }

    }

    public void ANDWF(int f, int d) {
        int result = this.akku & this.memory[f];
        if (d == 0) {
            checkFlags(result);
            this.akku = result;
        } else {
            checkFlags(result);
            this.memory[f] = result;
        }
    }

    public void CLRF(int f) {
        this.memory[f] = 0;
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
        int result = this.memory[f] ^ 255;
        if (d == 0) {
            checkFlags(result);
            this.akku = result;
        } else {
            checkFlags(result);
            this.memory[f] = result;
        }
    }

    public void DECF(int f, int d) {
        int result = this.memory[f] - 1;

        if (d == 0) {
            this.akku = result;
        } else {
            this.memory[f] = result;
        }

        if (result == 0) {
            changeStatusReg(zFlag, 1);
        } else {
            changeStatusReg(zFlag, 0);
        }
    }

    public boolean DECFSZ(int f, int d) {
        int result = this.memory[f] - 1;
        if (d != 0) {
            this.memory[f] = result;
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
        int result = this.memory[f] + 1;
        if (d != 0) {
            this.memory[f] = result;
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
        int result = this.memory[f] | 255;
        if (d == 0) {
            this.akku = result;
        } else {
            this.memory[f] = result;
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
