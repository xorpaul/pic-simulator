package my.PICSIMGUI;

public class PicCPU
{

    static int portA = 5; //Adresse von PortA
    static int zFlag = 2;
    static int cFlag = 0;
    static int dcFlag = 1;
    int[] statusReg = new int[8];
    public int[] memory; //Gesamter Speicher des Pic
    int akku;

    PicCPU()
    {
        memory = new int[256];
        akku = 0;
    }

    public void setPortA(int value)
    {
        this.memory[portA] = value;
    }

    public void changeStatusReg(int position, int value)
    {
        statusReg[position] = value;
    }

    public int[] getPortA()
    {
        int a = this.memory[portA];
        int[] portABits = new int[8];

        for (int i = 0; i < 8; i++)
        {
            portABits[i] = a % 2;
            a /= 2;
        }
        return portABits;
    }

    public void checkFlags(int result)
    {
        if (result > 255)
        {
            changeStatusReg(cFlag, 1);
            if (result - 255 == 0)
            {
                changeStatusReg(zFlag, 1);
            } else
            {
                changeStatusReg(zFlag, 0);
            }
        } else
        {
            changeStatusReg(cFlag, 0);
            if (result == 0)
            {
                changeStatusReg(zFlag, 1);
            } else
            {
                changeStatusReg(zFlag, 0);
            }
        }
    }

    public void printPortA()
    {
        System.out.println(this.memory[portA]);
    }
//TODO!
    public void INCF(int f)
    {
        memory[portA]++;
    }

    public void MOFWF(int f)
    {
        memory[f] = this.akku;
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
        } else
        {
            changeStatusReg(zFlag, 0);
        }
    }

    public void ADDWF(int f, int d)
    {
        int result = this.akku + this.memory[f];
        if (d == 0)
        {
            if (result > 255)
            {
                checkFlags(result);
                result -= 255;
                this.akku = result;

            } else
            {
                checkFlags(result);
                this.akku = result;
            }
        } else
        {
            if (result > 255)
            {
                checkFlags(result);
                result -= 255;
                this.memory[f] = result;

            } else
            {
                checkFlags(result);
                this.memory[f] = result;
            }
        }

    }

    public void ANDWF(int f, int d)
    {
        int result = this.akku & this.memory[f];
        if (d == 0)
        {
            checkFlags(result);
            this.akku = result;
        } else
        {
            checkFlags(result);
            this.memory[f] = result;
        }
    }

    public void CLRF(int f)
    {
        this.memory[f] = 0;
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

        } else
        {
            checkFlags(result);
            this.akku = result;
        }
    }

    public void COMF(int f, int d)
    {
        int result = this.memory[f] ^ 255;
        if (d == 0)
        {
            checkFlags(result);
            this.akku = result;
        } else
        {
            checkFlags(result);
            this.memory[f] = result;
        }
    }

    public void DECF(int f, int d)
    {
        int result = this.memory[f] - 1;
        
        if (d == 0)
            this.akku = result;
        else
            this.memory[f] = result;

        if (result == 0)
            changeStatusReg(zFlag, 1);
        else
            changeStatusReg(zFlag, 0);
    }
}
