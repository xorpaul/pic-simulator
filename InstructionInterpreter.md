# Einleitung #

> Interpretiert den eingelesenen Opcode und ruft enstsprechend die PIC-Funktionen
> in der PicCPU Klasse auf. Realisiert als Thread. Run Methode ruft desweiteren
> auch GUI-Refresh, GUI-Einlese Methoden und Synchroisations Methoden auf.

> Das GUI und die Instanz des PicCPU sind dieser Klasse als Klassenatribute mitgegeben,
> sowie ein Array, welches das eingelesen File enthält. Mittels Konstruktor wird
> hieraus der Hex-Opcod herausgefiltert, zu Integerwerten umgewandelt und der
> Interpreter Funktion übergeben.


# Details #

### Klassenattribute ###
```
    public String[] input;
    public int[] instructions;
    public int[] programmCount; //Verknüpfung PC und OpCode
    PICSIMGUI gui;
    PicCPU pic;
```

### PAP der run-Methode: ###

![http://pic-simulator.googlecode.com/files/run.png](http://pic-simulator.googlecode.com/files/run.png)

### run-Methode der InstructionInterpreter-Klasse: ###

```
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
                        pic.checkLatch();
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
```