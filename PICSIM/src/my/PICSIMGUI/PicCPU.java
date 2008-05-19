package my.PICSIMGUI;

public class PicCPU
{

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
    //Speicherstruktur 
    public int[] statusReg = new int[8]; //Status Register als Array -> Adr.3
    public int[] memoryBank0; //Gesamte Speichernak 0 des Pic
    public int[] memoryBank1; //Gesamte Speichernak 1 des Pic
    public int akku;   //W-Register des Pic
    public int activeBank = 0;

    /**
     * @category Konsruktor. Speicher initilisieren
     */
    PicCPU()
    {
        memoryBank0 = new int[128];
        memoryBank1 = new int[128];
        akku = 0;
    }

    public void setPortA(int value)
    {
        this.memoryBank0[portA] = value;
    }

    /**
     * Erstellt einen IntegerWert aus dem statusRegister Array
     * und schreibt es an die Adresse 3 der Speicherbänke.
     */
    public void statusToMemory()
    {
        String statusAsBin = "";
        for (int i = 7; i >= 0; i--)
        {
            statusAsBin += String.valueOf(statusReg[i]);
        }
        memoryBank0[status] = Integer.parseInt(statusAsBin, 2);
        memoryBank1[status] = Integer.parseInt(statusAsBin, 2);
    }

    public void setBank()
    {
        if (statusReg[rp0] == 0)
            activeBank = 0;
        else
            activeBank = 1;
    }

    public void fsrMemoryManagement()
    {
        this.memoryBank0[0] = this.memoryBank0[this.memoryBank0[fsr]];
        this.memoryBank1[0] = this.memoryBank1[this.memoryBank1[fsr]];
    }

    /**
     * @param position Bit welches im Statusregister veränder werden soll. Vrgl final int's am Klassenanfang
     * @param value darf 0 oder 1 sein entsprechend für high oder low
     */
    public void changeStatusReg(int position, int value)
    {
        statusReg[position] = value;
    }

    public int[] getPortA()
    {
        int a = this.memoryBank0[portA];
        int[] portABits = new int[8];

        for (int i = 0; i < 8; i++)
        {
            portABits[i] = a % 2;
            a /= 2;
        }
        return portABits;
    }

    public int[] getPortB()
    {
        int a = this.memoryBank0[portB];
        int[] portBBits = new int[8];

        for (int i = 0; i < 8; i++)
        {
            portBBits[i] = a % 2;
            a /= 2;
        }
        return portBBits;
    }

    public int[] getW()
    {
        int a = this.akku;
        int[] WBits = new int[8];

        for (int i = 0; i < 8; i++)
        {
            WBits[i] = a % 2;
            a /= 2;
        }
        return WBits;
    }

    public void checkFlags(int result)
    {
        if (result > 255)
        {
            changeStatusReg(cFlag, 1);
            if (result - 255 == 0)
                changeStatusReg(zFlag, 1);
            else
                changeStatusReg(zFlag, 0);
        }
        else
        {
            changeStatusReg(cFlag, 0);
            if (result == 0)
                changeStatusReg(zFlag, 1);
            else
                changeStatusReg(zFlag, 0);
        }
    }

    public void printPortA()
    {
        System.err.println("Im Port A steht: " + this.memoryBank0[portA]);
    }

    public void printW()
    {
        System.err.println("Im Akku steht: " + this.akku);
    }

//TODO!
    public void INCF(int f)
    {
        if (activeBank == 0)
            memoryBank0[f]++;
        else
            memoryBank1[f]++;
    }

    public void MOFWF(int f)
    {
        if (activeBank == 0)
            memoryBank0[f] = this.akku;
        else
            memoryBank1[f] = this.akku;
    }

    public void CLRW()
    {
        this.akku = 0;
        changeStatusReg(zFlag, 1);
    }

    public void ANDLW(int l)
    {
        this.akku = this.akku & l;
        if (this.akku == 0)
        {
            changeStatusReg(zFlag, 1);
        }
        else
        {
            changeStatusReg(zFlag, 0);
        }
    }

    public void ADDWF(int f, int d)
    {
        if (activeBank == 0)
        {
            int result = this.akku + this.memoryBank0[f];
            if (d == 0)
            {
                if (result > 255)
                {
                    checkFlags(result);
                    result -= 255;
                    this.akku = result;

                }
                else
                {
                    checkFlags(result);
                    this.akku = result;
                }
            }
            else
            {
                if (result > 255)
                {
                    checkFlags(result);
                    result -= 255;
                    this.memoryBank0[f] = result;

                }
                else
                {
                    checkFlags(result);
                    this.memoryBank0[f] = result;
                }
            }
        }
        else
        {
            int result = this.akku + this.memoryBank1[f];
            if (d == 0)
            {
                if (result > 255)
                {
                    checkFlags(result);
                    result -= 255;
                    this.akku = result;

                }
                else
                {
                    checkFlags(result);
                    this.akku = result;
                }
            }
            else
            {
                if (result > 255)
                {
                    checkFlags(result);
                    result -= 255;
                    this.memoryBank1[f] = result;

                }
                else
                {
                    checkFlags(result);
                    this.memoryBank1[f] = result;
                }
            }
        }

    }

    public void ANDWF(int f, int d)
    {
        if (activeBank == 0)
        {
            int result = this.akku & this.memoryBank0[f];
            if (d == 0)
            {
                checkFlags(result);
                this.akku = result;
            }
            else
            {
                checkFlags(result);
                this.memoryBank0[f] = result;
            }
        }
        else
        {
            int result = this.akku & this.memoryBank0[f];
            if (d == 0)
            {
                checkFlags(result);
                this.akku = result;
            }
            else
            {
                checkFlags(result);
                this.memoryBank0[f] = result;
            }
        }
    }

    public void CLRF(int f)
    {
        if (activeBank == 0)
            this.memoryBank0[f] = 0;
        else
            this.memoryBank1[f] = 0;

        changeStatusReg(zFlag, 1);
    }

    public void ADDLW(int l)
    {
        int result = this.akku + l;

        if (result > 255)
        {
            checkFlags(result);
            result -= 255;
            this.akku = result;

        }
        else
        {
            checkFlags(result);
            this.akku = result;
        }
    }

    public void COMF(int f, int d)
    {
        int result;
        if (this.activeBank == 0)
            result = this.memoryBank0[f] ^ 255;
        else
            result = this.memoryBank1[f] ^ 255;

        if (d == 0)
        {
            checkFlags(result);
            this.akku = result;
        }
        else
        {
            checkFlags(result);
            if (this.activeBank == 0)
                this.memoryBank0[f] = result;
            else
                this.memoryBank1[f] = result;
        }
    }

    public void DECF(int f, int d)
    {
        int result;
        if (this.activeBank == 0)
        {
            result = this.memoryBank0[f] - 1;

            if (d == 0)
            {
                this.akku = result;
            }
            else
            {
                this.memoryBank0[f] = result;
            }
        }
        else
        {
            result = this.memoryBank1[f] - 1;

            if (d == 0)
            {
                this.akku = result;
            }
            else
            {
                this.memoryBank1[f] = result;
            }
        }
        if (result == 0)
        {
            changeStatusReg(zFlag, 1);
        }
        else
        {
            changeStatusReg(zFlag, 0);
        }
    }

    public boolean DECFSZ(int f, int d)
    {
        int result;
        if (this.activeBank == 0)
        {
            result = this.memoryBank0[f] - 1;
            if (d != 0)
            {
                this.memoryBank0[f] = result;
            }
            else
            {
                this.akku = result;
            }
            if (result == 0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            result = this.memoryBank1[f] - 1;

            if (d != 0)
            {
                this.memoryBank1[f] = result;
            }
            else
            {
                this.akku = result;
            }
            if (result == 0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    public boolean INCFSZ(int f, int d)
    {
        int [] currentBank;
        
        if(this.activeBank ==  0)
         currentBank = memoryBank0;
        else
            currentBank = memoryBank1;
        
        int result = currentBank[f] + 1;
        if (d != 0)
        {
            currentBank[f] = result;
        }
        else
        {
            this.akku = result;
        }
        if (result == 0)
        {
            if(this.activeBank ==  0)
                memoryBank0 = currentBank;
            else
                memoryBank1 = currentBank;
            return true;
        }
        else
        {
            if(this.activeBank ==  0)
                memoryBank0 = currentBank;
            else
                memoryBank1 = currentBank;
            return false;
        }
    }

    public void IORWF(int f, int d)
    {
        int result = this.memoryBank0[f] | 255;
        if (d == 0)
        {
            this.akku = result;
        }
        else
        {
            this.memoryBank0[f] = result;
        }

        if (result == 0)
        {
            changeStatusReg(zFlag, 1);
        }
        else
        {
            changeStatusReg(zFlag, 0);
        }
    }

    public void MOVLW(int l)
    {
        this.akku = l;
    }

    public void IORLW(int l)
    {
        int result = l | 255;
        this.akku = result;

        if (result == 0)
        {
            changeStatusReg(zFlag, 1);
        }
        else
        {
            changeStatusReg(zFlag, 0);
        }
    }

    public void XORLW(int l)
    {
        this.akku = this.akku | l;
        if (this.akku == 0)
        {
            changeStatusReg(zFlag, 1);
        }
        else
        {
            changeStatusReg(zFlag, 0);
        }
    }

    public void SUBLW(int l)
    {
        int result = this.akku - l;

        if (this.akku < l)
        { // Wenn als ERG was negatives rauskommt
            changeStatusReg(cFlag, l);
            //result -= 255; Muss man hier was machen? Um bspw -3 darzustellen?
            changeStatusReg(zFlag, 0);
            this.akku = result;

        }
        else
        {
            changeStatusReg(cFlag, 0);
            if (result == 0)
            {
                changeStatusReg(zFlag, 1);
            }
            else
            {
                changeStatusReg(zFlag, 0);
            }
            this.akku = result;
        }
    }

    public void BCF(int f, int b)
    {
        int mask;

        //Bitmaske aus der Zahl b erzeugen
        switch (b)
        {
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

        //Prüft ob Bit vorher schon geCleared ist.
        if ((f & mask) > 0)
        {
            //je nachdem welche Bank aktiv ist
            if (this.activeBank == 0)
                memoryBank0[f] = memoryBank0[f] ^ mask; //Xor mit bitmaske
            else
                memoryBank1[f] = memoryBank1[f] ^ mask; //Xor mit bitmaske
        }

        if (f == 3)
            statusReg[b] = 0;
    }

    public void BSF(int f, int b)
    {
        int mask;

        //Bitmaske aus der Zahl b erzeugen
        switch (b)
        {
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

        //Prüft ob Bit vorher schon geCleared ist.
        if (!((f & mask) > 0))
        {
            //je nachdem welche Bank aktiv ist
            if (this.activeBank == 0)
                memoryBank0[f] = memoryBank0[f] ^ mask; //Xor mit bitmaske
            else
                memoryBank1[f] = memoryBank1[f] ^ mask; //Xor mit bitmaske
        }

        if (f == status)
            statusReg[b] = 1;
    }
}
