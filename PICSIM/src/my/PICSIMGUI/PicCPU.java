package my.PICSIMGUI;

import java.util.HashMap;

public class PicCPU {

    static int portA = 5; //Adresse von PortA
    public int[] memory; //Gesamter Speicher des Pic
    private HashMap<String, Integer> statusReg = new HashMap<String, Integer>();

    PicCPU() {
        statusReg.put("ZFLAG", 0);
        statusReg.put("CARRYFLAG", 0);
        statusReg.put("TIMEOUTFLAG", 0);
        statusReg.put("POWERDOWNFLAG", 0);
        memory = new int[256];
    }

    public void setPortA(int value) {
        this.memory[portA] = value;
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

    public void printPortA() {
        System.out.println(this.memory[portA]);
    }

    public void INCF(int f) {
        memory[portA]++;
    }
}
