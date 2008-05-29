/*
 * PICSIMGUI.java
 *
 * Created on April 7, 2008, 1:50 PM
 */
package my.PICSIMGUI;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.String;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author  Mogli BA, PP
 */
public class PICSIMGUI extends javax.swing.JFrame implements Runnable {

    public PicCPU pic = new PicCPU(); //PIC Prozessor
    public InstructionInterpreter interpret; //Interpreter
    public boolean interpreterSlow; // Speed-Flag
    public boolean running = false; // Start/Stop Flag
    public boolean step = false; //für schrit für schritt ausführung --> TODO!
    private int format = 0; // 0 für DEZ, 1 für HEX, 2 für BIN
    private String[][] contentBank0 = new String[128][2]; // Daten für Bank0 Tabelle
    private String[][] contentBank1 = new String[128][2]; // Daten für Bank1 Tabelle
    private String[] titel = new String[2]; //Überschrift der SpeicherTabelle
    private DefaultTableModel dm0; //TabelModel für Bank0
    private DefaultTableModel dm1; //TableModel für Bank1

    public void run() {
    }

    /** Creates new form PICSIMGUI
     * 
     */
    public PICSIMGUI() {
        super("PIC16F84 - SIMULATOR");
        initComponents();

        titel[0] = "Adresse";
        titel[1] = "Value";

        //Einmalig die Register mit speziellen Namen initilisieren
        // Wird nie mehr überschrieban, auch nicht von RefreshTables

        contentBank0[0][0] = "Register " + String.valueOf(pic.memoryBank0[4]);
        contentBank0[1][0] = "TMR0";
        contentBank0[2][0] = "PCL";
        contentBank0[3][0] = "STATUS";
        contentBank0[4][0] = "FSR";
        contentBank0[5][0] = "PORTA";
        contentBank0[6][0] = "PORTB";
        contentBank0[8][0] = "EEDATA";
        contentBank0[9][0] = "EEADR";
        contentBank0[10][0] = "PCLATH";
        contentBank0[11][0] = "INTCON";

        contentBank1[0][0] = "Register " + String.valueOf(pic.memoryBank1[4]);
        contentBank1[1][0] = "OPTION";
        contentBank1[2][0] = "PCL";
        contentBank1[3][0] = "STATUS";
        contentBank1[4][0] = "FSR";
        contentBank1[5][0] = "TRISA";
        contentBank1[6][0] = "TRISB";
        contentBank1[8][0] = "ECON1";
        contentBank1[9][0] = "ECON2";
        contentBank1[10][0] = "PCLATH";
        contentBank1[11][0] = "INTCON";

        dm0 = new DefaultTableModel(contentBank0, titel);
        dm1 = new DefaultTableModel(contentBank1, titel);

        TableBank0.setModel(dm0);
        TableBank1.setModel(dm1);

        //Um die übrigen Zellen der Tabellen zu füllen
        refreshTables();

        ButtonStop.setEnabled(false);
    }

    static public void e(Object... parameters) {
        if (parameters.length == 2) {
            System.out.print(parameters[0]);
        } else {
            System.out.print(parameters[0]);
        }
    }

    /**
     * Ruft unterschiedliche Funktionen die einen Teil der GUI refreshen auf.
     */
    public void refreshGui() {
        jList1.setSelectedIndex(pic.linie);
        //jList1.

        pic.statusToMemory();
        this.TextFieldWValue.setText(String.valueOf(pic.akku));
        portStatus();
        setPortARadios();
        setPortBRadios();
        setFlagRadios();
        refreshTables();
    }

    /**
     * Liest änderungen an den Inputs des GUI ein
     * wie zB RadioButtons der Ports und setzt entsprechend
     * die Attribute im Pic
     */
    public void readGui() {
        pic.setPortA(readPortA());
        pic.setPortB(readPortB());
    }

    /**
     * setzt die RadioButtons der Flags entsprechend
     * der aktuellen Attribute im Pic
     */
    public void setFlagRadios() {
        if (this.pic.statusReg[pic.zFlag] == 1) {
            this.RadioButtonZFlag.setSelected(true);
        } else {
            this.RadioButtonZFlag.setSelected(false);
        }

        if (this.pic.statusReg[pic.cFlag] == 1) {
            this.RadioButtonCarryFlag.setSelected(true);
        } else {
            this.RadioButtonCarryFlag.setSelected(false);
        }

        if (this.pic.statusReg[pic.rp0] == 1) {
            this.RadioButtonRP0Flag.setSelected(true);
        } else {
            this.RadioButtonRP0Flag.setSelected(false);
        }
        if (this.pic.statusReg[pic.nTO] == 1) {
            this.RadioButtonRP0Flag1.setSelected(true);
        } else {
            this.RadioButtonRP0Flag1.setSelected(false);
        }
        if (this.pic.statusReg[pic.nPD] == 1) {
            this.RadioButtonRP0Flag2.setSelected(true);
        } else {
            this.RadioButtonRP0Flag2.setSelected(false);
        }
    }

    /**
     * setzt die RadioButtons von PortA entsprechend
     * der aktuellen Attribute im Pic
     */
    public void setPortARadios() {
        int[] port = pic.getPortA();
        int portValue = pic.memoryBank0[pic.portA];
        TextFieldPortAValue.setText(String.valueOf(portValue));

        if (port[0] == 1) {
            RadioButtonA0.setSelected(true);
        } else {
            RadioButtonA0.setSelected(false);
        }

        if (port[1] == 1) {
            RadioButtonA1.setSelected(true);
        } else {
            RadioButtonA1.setSelected(false);
        }

        if (port[2] == 1) {
            RadioButtonA2.setSelected(true);
        } else {
            RadioButtonA2.setSelected(false);
        }

        if (port[3] == 1) {
            RadioButtonA3.setSelected(true);
        } else {
            RadioButtonA3.setSelected(false);
        }

        if (port[4] == 1) {
            RadioButtonA4.setSelected(true);
        } else {
            RadioButtonA4.setSelected(false);
        }

    //Delete Comment: PortA is nur 5 Bit. Datasheet Seite 19
    }

    /**
     * setzt die RadioButtons von PortB entsprechend
     * der aktuellen Attribute im Pic
     */
    public void setPortBRadios() {
        int[] port = pic.getPortB();
        int portValue = pic.memoryBank0[pic.portB];
        TextFieldPortBValue.setText(String.valueOf(portValue));

        if (port[0] == 1) {
            RadioButtonB0.setSelected(true);
        } else {
            RadioButtonB0.setSelected(false);
        }

        if (port[1] == 1) {
            RadioButtonB1.setSelected(true);
        } else {
            RadioButtonB1.setSelected(false);
        }

        if (port[2] == 1) {
            RadioButtonB2.setSelected(true);
        } else {
            RadioButtonB2.setSelected(false);
        }

        if (port[3] == 1) {
            RadioButtonB3.setSelected(true);
        } else {
            RadioButtonB3.setSelected(false);
        }

        if (port[4] == 1) {
            RadioButtonB4.setSelected(true);
        } else {
            RadioButtonB4.setSelected(false);
        }

        if (port[5] == 1) {
            RadioButtonB5.setSelected(true);
        } else {
            RadioButtonB5.setSelected(false);
        }

        if (port[6] == 1) {
            RadioButtonB6.setSelected(true);
        } else {
            RadioButtonB6.setSelected(false);
        }

        if (port[7] == 1) {
            RadioButtonB7.setSelected(true);
        } else {
            RadioButtonB7.setSelected(false);
        }
    }

    /** Setzt PortA und PortB RadioButtons auf 
     * enabled oder disabled, je nachdem wie die
     * Ports in den TRIS-Registern gesetzt sind.
     */
    public void portStatus() {
        int[] trisA = pic.getTrisA();
        int[] trisB = pic.getTrisB();

        if (trisA[0] == 1) {
            RadioButtonA0.setEnabled(true);
        } else {
            RadioButtonA0.setEnabled(false);
        }
        if (trisA[1] == 1) {
            RadioButtonA1.setEnabled(true);
        } else {
            RadioButtonA1.setEnabled(false);
        }
        if (trisA[2] == 1) {
            RadioButtonA2.setEnabled(true);
        } else {
            RadioButtonA2.setEnabled(false);
        }
        if (trisA[3] == 1) {
            RadioButtonA3.setEnabled(true);
        } else {
            RadioButtonA3.setEnabled(false);
        }
        if (trisA[4] == 1) {
            RadioButtonA4.setEnabled(true);
        } else {
            RadioButtonA4.setEnabled(false);
        }


        if (trisB[0] == 1) {
            RadioButtonB0.setEnabled(true);
        } else {
            RadioButtonB0.setEnabled(false);
        }
        if (trisB[1] == 1) {
            RadioButtonB1.setEnabled(true);
        } else {
            RadioButtonB1.setEnabled(false);
        }
        if (trisB[2] == 1) {
            RadioButtonB2.setEnabled(true);
        } else {
            RadioButtonB2.setEnabled(false);
        }
        if (trisB[3] == 1) {
            RadioButtonB3.setEnabled(true);
        } else {
            RadioButtonB3.setEnabled(false);
        }
        if (trisB[4] == 1) {
            RadioButtonB4.setEnabled(true);
        } else {
            RadioButtonB4.setEnabled(false);
        }
        if (trisB[5] == 1) {
            RadioButtonB5.setEnabled(true);
        } else {
            RadioButtonB5.setEnabled(false);
        }
        if (trisB[6] == 1) {
            RadioButtonB6.setEnabled(true);
        } else {
            RadioButtonB6.setEnabled(false);
        }
        if (trisB[7] == 1) {
            RadioButtonB7.setEnabled(true);
        } else {
            RadioButtonB7.setEnabled(false);
        }
    }

    /**
     * Liest PortB RadioButtons ein und erzeugt ein Array
     * @return 1 für Selected, 0 für nich selektiert
     * -2 wenn der Port nicht eingang sondern ausgang war
     */
    public int[] readPortA() {
        int[] portAReadIn = new int[5];
        if (RadioButtonA0.isEnabled() == true) {
            if (RadioButtonA0.isSelected() == true) {
                portAReadIn[0] = 1;
            } else {
                portAReadIn[0] = 0;
            }
        } else {
            portAReadIn[0] = -2;
        }


        if (RadioButtonA1.isEnabled() == true) {
            if (RadioButtonA1.isSelected() == true) {
                portAReadIn[1] = 1;
            } else {
                portAReadIn[1] = 0;
            }
        } else {
            portAReadIn[1] = -2;
        }

        if (RadioButtonA2.isEnabled() == true) {
            if (RadioButtonA2.isSelected() == true) {
                portAReadIn[2] = 1;
            } else {
                portAReadIn[2] = 0;
            }
        } else {
            portAReadIn[2] = -2;
        }

        if (RadioButtonA3.isEnabled() == true) {
            if (RadioButtonA3.isSelected() == true) {
                portAReadIn[3] = 1;
            } else {
                portAReadIn[3] = 0;
            }
        } else {
            portAReadIn[3] = -2;
        }

        if (RadioButtonA4.isEnabled() == true) {
            if (RadioButtonA4.isSelected() == true) {
                portAReadIn[4] = 1;
            } else {
                portAReadIn[4] = 0;
            }
        } else {
            portAReadIn[4] = -2;
        }

        return portAReadIn;
    }

    /**
     * Liest PortB RadioButtons ein und erzeugt ein Array
     * @return 1 für Selected, 0 für nich selektiert
     * -2 wenn der Port nicht eingang sondern ausgang war
     */
    public int[] readPortB() {
        int[] portBReadIn = new int[8];
        if (RadioButtonB0.isEnabled() == true) {
            if (RadioButtonB0.isSelected() == true) {
                portBReadIn[0] = 1;
            } else {
                portBReadIn[0] = 0;
            }
        } else {
            portBReadIn[0] = -2;
        }


        if (RadioButtonB1.isEnabled() == true) {
            if (RadioButtonB1.isSelected() == true) {
                portBReadIn[1] = 1;
            } else {
                portBReadIn[1] = 0;
            }
        } else {
            portBReadIn[1] = -2;
        }

        if (RadioButtonB2.isEnabled() == true) {
            if (RadioButtonB2.isSelected() == true) {
                portBReadIn[2] = 1;
            } else {
                portBReadIn[2] = 0;
            }
        } else {
            portBReadIn[2] = -2;
        }

        if (RadioButtonB3.isEnabled() == true) {
            if (RadioButtonB3.isSelected() == true) {
                portBReadIn[3] = 1;
            } else {
                portBReadIn[3] = 0;
            }
        } else {
            portBReadIn[3] = -2;
        }

        if (RadioButtonB4.isEnabled() == true) {
            if (RadioButtonB4.isSelected() == true) {
                portBReadIn[4] = 1;
            } else {
                portBReadIn[4] = 0;
            }
        } else {
            portBReadIn[4] = -2;
        }

        if (RadioButtonB5.isEnabled() == true) {
            if (RadioButtonB5.isSelected() == true) {
                portBReadIn[5] = 1;
            } else {
                portBReadIn[5] = 0;
            }
        } else {
            portBReadIn[5] = -2;
        }

        if (RadioButtonB6.isEnabled() == true) {
            if (RadioButtonB6.isSelected() == true) {
                portBReadIn[6] = 1;
            } else {
                portBReadIn[6] = 0;
            }
        } else {
            portBReadIn[6] = -2;
        }

        if (RadioButtonB7.isEnabled() == true) {
            if (RadioButtonB7.isSelected() == true) {
                portBReadIn[7] = 1;
            } else {
                portBReadIn[7] = 0;
            }
        } else {
            portBReadIn[7] = -2;
        }

        return portBReadIn;
    }

    public void setStatusLabel(String text) {
        this.LabelStatus.setText(text);
    }

    /**
     * Refresht die Memory Daten und ändert das anzeigeformat.
     * Das ganze wird über das DefaultTableModel gesteuert
     */
    public void refreshTables() {
        switch (format) {

            case (1)://HEX
                for (int i = 0; i < 128; i++) {
                    if (i != 1 && i != 2 && i != 3 && i != 4 && i != 5 && i != 6 && i != 8 && i != 9 && i != 10 && i != 11) {
                        // Linke Spalte mit Hex-Adressen Füllen, außer die Zeilen mit speziellem namen
                        dm0.setValueAt("Register " + Integer.toHexString(i).toUpperCase(), i, 0);
                        dm1.setValueAt("Register " + Integer.toHexString(i + 128).toUpperCase(), i, 0);
                    }
                    //Rechte Spalte mit Memory-Hex werten füllen, Alle Zeilen
                    dm0.setValueAt(Integer.toHexString(pic.memoryBank0[i]).toUpperCase(), i, 1);
                    dm1.setValueAt(Integer.toHexString(pic.memoryBank1[i]).toUpperCase(), i, 1);
                }
                // Adresse 0 bzw 80hex nach FSR Register setzen
                dm0.setValueAt("Register " + Integer.toHexString(pic.memoryBank0[4]).toUpperCase(), 0, 0);
                dm1.setValueAt("Register " + Integer.toHexString(pic.memoryBank1[4]).toUpperCase(), 0, 0);
                break;

            case (2)://BIN
                for (int i = 0; i < 128; i++) {
                    if (i != 1 && i != 2 && i != 3 && i != 4 && i != 5 && i != 6 && i != 8 && i != 9 && i != 10 && i != 11) {
                        // Linke Spalte mit BIN-Adressen Füllen, außer die Zeilen mit speziellem namen
                        dm0.setValueAt("Register " + Integer.toBinaryString(i), i, 0);
                        dm1.setValueAt("Register " + Integer.toBinaryString(i + 128), i, 0);
                    }
                    //Rechte Spalte mit Memory-BIN werten füllen, Alle Zeilen
                    dm0.setValueAt(Integer.toBinaryString(pic.memoryBank0[i]), i, 1);
                    dm1.setValueAt(Integer.toBinaryString(pic.memoryBank1[i]), i, 1);
                }
                // Adresse 0 bzw 80hex nach FSR Register setzen
                dm0.setValueAt("Register " + Integer.toBinaryString(pic.memoryBank0[4]), 0, 0);
                dm1.setValueAt("Register " + Integer.toBinaryString(pic.memoryBank1[4]), 0, 0);
                break;

            default:// DEZ
                for (int i = 0; i < 128; i++) {
                    if (i != 1 && i != 2 && i != 3 && i != 4 && i != 5 && i != 6 && i != 8 && i != 9 && i != 10 && i != 11) {
                        dm0.setValueAt("Register " + i, i, 0);
                        dm1.setValueAt("Register " + (i + 128), i, 0);
                    }
                    dm0.setValueAt(pic.memoryBank0[i], i, 1);
                    dm1.setValueAt(pic.memoryBank1[i], i, 1);
                }
                dm0.setValueAt("Register " + pic.memoryBank0[4], 0, 0);
                dm1.setValueAt("Register " + pic.memoryBank1[4], 0, 0);
                break;
        }
        //TableModel setzen
        TableBank0.setModel(dm0);
        TableBank1.setModel(dm1);

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ButtonRun = new javax.swing.JButton();
        LabelStatus = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        PanelFlags = new javax.swing.JPanel();
        RadioButtonZFlag = new javax.swing.JRadioButton();
        RadioButtonCarryFlag = new javax.swing.JRadioButton();
        RadioButtonRP0Flag = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        RadioButtonRP0Flag1 = new javax.swing.JRadioButton();
        jLabel7 = new javax.swing.JLabel();
        RadioButtonRP0Flag2 = new javax.swing.JRadioButton();
        PanelPorts = new javax.swing.JPanel();
        LabelPortA = new javax.swing.JLabel();
        LabelPortB = new javax.swing.JLabel();
        RadioButtonA4 = new javax.swing.JRadioButton();
        RadioButtonA3 = new javax.swing.JRadioButton();
        RadioButtonA2 = new javax.swing.JRadioButton();
        RadioButtonA1 = new javax.swing.JRadioButton();
        RadioButtonA0 = new javax.swing.JRadioButton();
        RadioButtonB0 = new javax.swing.JRadioButton();
        RadioButtonB1 = new javax.swing.JRadioButton();
        RadioButtonB2 = new javax.swing.JRadioButton();
        RadioButtonB3 = new javax.swing.JRadioButton();
        RadioButtonB4 = new javax.swing.JRadioButton();
        RadioButtonB5 = new javax.swing.JRadioButton();
        RadioButtonB6 = new javax.swing.JRadioButton();
        RadioButtonB7 = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        TextFieldPortAValue = new javax.swing.JTextField();
        TextFieldPortBValue = new javax.swing.JTextField();
        LabelPortA1 = new javax.swing.JLabel();
        TextFieldWValue = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        TableBank0 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        TableBank1 = new javax.swing.JTable();
        CheckBoxSloMo = new javax.swing.JCheckBox();
        ButtonStop = new javax.swing.JButton();
        ComboBoxChangeFormat = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        ButtonStep = new javax.swing.JButton();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        ButtonRun.setText("Start");
        ButtonRun.setToolTipText("Start Compiling");
        ButtonRun.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ButtonRunMousePressed(evt);
            }
        });

        jLabel1.setText("LAST INSTRUCTION: ");

        PanelFlags.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Status Flags", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11)));

        RadioButtonZFlag.setEnabled(false);
        RadioButtonZFlag.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        RadioButtonZFlag.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        RadioButtonCarryFlag.setEnabled(false);
        RadioButtonCarryFlag.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        RadioButtonCarryFlag.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        RadioButtonRP0Flag.setEnabled(false);
        RadioButtonRP0Flag.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        RadioButtonRP0Flag.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel2.setText("Zero");

        jLabel4.setText("Carry");

        jLabel5.setText("RP0");

        jLabel6.setText("!TO");

        RadioButtonRP0Flag1.setEnabled(false);
        RadioButtonRP0Flag1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        RadioButtonRP0Flag1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel7.setText("!PD");

        RadioButtonRP0Flag2.setEnabled(false);
        RadioButtonRP0Flag2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        RadioButtonRP0Flag2.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout PanelFlagsLayout = new javax.swing.GroupLayout(PanelFlags);
        PanelFlags.setLayout(PanelFlagsLayout);
        PanelFlagsLayout.setHorizontalGroup(
            PanelFlagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelFlagsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelFlagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelFlagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(PanelFlagsLayout.createSequentialGroup()
                            .addGroup(PanelFlagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2)
                                .addComponent(jLabel5))
                            .addGap(23, 23, 23))
                        .addGroup(PanelFlagsLayout.createSequentialGroup()
                            .addComponent(jLabel4)
                            .addGap(18, 18, 18)))
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addGap(49, 49, 49)
                .addGroup(PanelFlagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelFlagsLayout.createSequentialGroup()
                        .addComponent(RadioButtonRP0Flag2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(12, Short.MAX_VALUE))
                    .addGroup(PanelFlagsLayout.createSequentialGroup()
                        .addComponent(RadioButtonRP0Flag1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(PanelFlagsLayout.createSequentialGroup()
                        .addGroup(PanelFlagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(RadioButtonRP0Flag, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(RadioButtonCarryFlag))
                        .addGap(93, 93, 93))
                    .addGroup(PanelFlagsLayout.createSequentialGroup()
                        .addComponent(RadioButtonZFlag, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        PanelFlagsLayout.setVerticalGroup(
            PanelFlagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelFlagsLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(PanelFlagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(RadioButtonZFlag))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelFlagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(RadioButtonCarryFlag))
                .addGap(3, 3, 3)
                .addGroup(PanelFlagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelFlagsLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(9, 9, 9))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelFlagsLayout.createSequentialGroup()
                        .addComponent(RadioButtonRP0Flag)
                        .addGap(7, 7, 7)))
                .addGroup(PanelFlagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(RadioButtonRP0Flag1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(18, 18, 18)
                .addGroup(PanelFlagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(RadioButtonRP0Flag2))
                .addContainerGap(80, Short.MAX_VALUE))
        );

        PanelPorts.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ports", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11)));

        LabelPortA.setText("Port A");

        LabelPortB.setText("Port B");

        jLabel3.setText("   7     6     5     4     3     2     1     0");

        TextFieldPortAValue.setEditable(false);
        TextFieldPortAValue.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        TextFieldPortBValue.setEditable(false);
        TextFieldPortBValue.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        LabelPortA1.setText("Akku");

        TextFieldWValue.setEditable(false);
        TextFieldWValue.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        javax.swing.GroupLayout PanelPortsLayout = new javax.swing.GroupLayout(PanelPorts);
        PanelPorts.setLayout(PanelPortsLayout);
        PanelPortsLayout.setHorizontalGroup(
            PanelPortsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelPortsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelPortsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelPortsLayout.createSequentialGroup()
                        .addGroup(PanelPortsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(LabelPortA)
                            .addComponent(LabelPortB))
                        .addGap(11, 11, 11)
                        .addGroup(PanelPortsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(PanelPortsLayout.createSequentialGroup()
                                .addGap(63, 63, 63)
                                .addComponent(RadioButtonA4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(RadioButtonA3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(RadioButtonA2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(RadioButtonA1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(RadioButtonA0)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TextFieldPortAValue))
                            .addGroup(PanelPortsLayout.createSequentialGroup()
                                .addComponent(RadioButtonB7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(RadioButtonB6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(RadioButtonB5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(RadioButtonB4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(RadioButtonB3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(RadioButtonB2, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(RadioButtonB1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(RadioButtonB0)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(TextFieldPortBValue, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(192, 192, 192))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelPortsLayout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE))
                    .addGroup(PanelPortsLayout.createSequentialGroup()
                        .addComponent(LabelPortA1)
                        .addGap(18, 18, 18)
                        .addComponent(TextFieldWValue, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        PanelPortsLayout.setVerticalGroup(
            PanelPortsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelPortsLayout.createSequentialGroup()
                .addComponent(jLabel3)
                .addGap(6, 6, 6)
                .addGroup(PanelPortsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(RadioButtonA0)
                    .addComponent(RadioButtonA1)
                    .addComponent(RadioButtonA2)
                    .addComponent(RadioButtonA3)
                    .addComponent(RadioButtonA4)
                    .addComponent(LabelPortA)
                    .addComponent(TextFieldPortAValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(PanelPortsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(LabelPortB)
                    .addComponent(RadioButtonB0)
                    .addComponent(RadioButtonB1)
                    .addComponent(RadioButtonB3)
                    .addComponent(RadioButtonB4)
                    .addComponent(RadioButtonB5)
                    .addComponent(RadioButtonB6)
                    .addComponent(RadioButtonB7)
                    .addComponent(RadioButtonB2)
                    .addComponent(TextFieldPortBValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelPortsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(LabelPortA1)
                    .addComponent(TextFieldWValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(127, 127, 127))
        );

        TableBank0.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Adresse", "Value"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(TableBank0);

        jTabbedPane1.addTab("Bank 0", jScrollPane3);

        TableBank1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Adresse", "Value"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(TableBank1);

        jTabbedPane1.addTab("Bank 1", jScrollPane2);

        CheckBoxSloMo.setText("Run in slow motion");
        CheckBoxSloMo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckBoxSloMoActionPerformed(evt);
            }
        });

        ButtonStop.setText("Stop");
        ButtonStop.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ButtonStopMousePressed(evt);
            }
        });

        ComboBoxChangeFormat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "DEZ", "HEX", "BIN" }));
        ComboBoxChangeFormat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboBoxChangeFormatActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(jList1);

        jMenu1.setText("File");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Load File");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InputTextFileButtonActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        ButtonStep.setText("Step-by-Step LAlalalalalaal!");
        ButtonStep.setToolTipText("Step-by-Step Compiling");
        ButtonStep.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ButtonStepMousePressed(evt);
            }
        });
        ButtonStep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonStepActionPerformed(evt);
            }
        });

        jMenu3.setText("File");

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("Load File");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InputTextFileButtonActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);

        jMenuBar2.add(jMenu3);

        jMenu4.setText("Edit");
        jMenuBar2.add(jMenu4);

        setJMenuBar(jMenuBar2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LabelStatus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(ButtonRun)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ButtonStop)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(CheckBoxSloMo)
                        .addGap(49, 49, 49)
                        .addComponent(ButtonStep))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(PanelPorts, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(15, 15, 15)
                            .addComponent(PanelFlags, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                    .addComponent(ComboBoxChangeFormat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(ComboBoxChangeFormat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ButtonRun)
                            .addComponent(ButtonStop)
                            .addComponent(CheckBoxSloMo)
                            .addComponent(ButtonStep))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 493, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(PanelFlags, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(PanelPorts, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(LabelStatus)
                    .addComponent(jLabel1))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    @SuppressWarnings("static-access")
    private void InputTextFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InputTextFileButtonActionPerformed

        String initialDirectory = "";
        String fileURL = null;
        JFileChooser chooser;

        int returnVal;

        chooser =
                new JFileChooser(initialDirectory);
        returnVal =
                chooser.showOpenDialog((Component) evt.getSource());

        if (returnVal == chooser.APPROVE_OPTION) {
            /* To create a URL for a file on the local file-system, we simply
             * pre-pend the "file" protocol to the absolute path of the file.
             */
            fileURL = chooser.getSelectedFile().getAbsolutePath();
        }



        try {
            //jTextArea1.setText("");

            int c = 0;

            String record = null;
            int lineCount = 0;
            String[] Code;

            jList1.removeAll();
            FileReader fr = new FileReader(fileURL);
            BufferedReader br = new BufferedReader(fr);
            //Zweiter Bufferd Reader nit dem selben file
            //benötigt um die anzahl der zeilen rauszufinden
            BufferedReader br2 = new BufferedReader(new FileReader(fileURL));

            //Anzahl der Zeilen der JList rausfinden
            while (br2.readLine() != null) {
                lineCount++;
            }
            //System.err.println(lineCount);

            Code = new String[lineCount];
            record = new String();
            while ((record = br.readLine()) != null) {
                // jTextArea1.append(record + "\n");
                Vector<Object> vector = new Vector<Object>();
                // alle vorhandene Elemente einfügen
                for (int i = 0; i < jList1.getModel().getSize(); i++) {
                    vector.add(jList1.getModel().getElementAt(i));
                }
                Code[c] = record;
                // den eigenen Text eingeben
                vector.add(record + "\n");
                jList1.setListData(vector);
                c++;
            }
            interpret = new InstructionInterpreter(Code, this, this.pic);
        } catch (IOException e) {
            // catch possible io errors from readLine()
            System.out.println("Uh oh, got an IOException error!");
            e.printStackTrace();
        }

}//GEN-LAST:event_InputTextFileButtonActionPerformed

    /**
     * @param evt Eventhandler. Kommt von NetBeans IDE 6.0.1
     * @description Startet beim Klick auf den Button einen Thread der die PIC befehle abarbeitet.
     */
    private void ButtonRunMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonRunMousePressed

        //  String[] Code = jTextArea1.getText().split("\n");

        /* int i = 0;
        int iSize = jList1.getModel().getSize();
        //  e(iSize);
        String[] Code = null;
        for (i = 0; i < iSize; i++) {
        Code[i] = jList1.getModel().getElementAt(i).toString();
        //e(Code[i]);
        }
        interpret = new InstructionInterpreter(Code, this, this.pic);
         */
        if (this.CheckBoxSloMo.isSelected()) {
            this.interpreterSlow = true;
        } else {
            this.interpreterSlow = false;
        }

        ButtonStop.setEnabled(true);
        ButtonRun.setEnabled(false);
        Thread interpreterThread = new Thread(interpret);
        this.running = true;
        interpreterThread.start();  
        
    }//GEN-LAST:event_ButtonRunMousePressed

    private void CheckBoxSloMoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckBoxSloMoActionPerformed
        if (this.CheckBoxSloMo.isSelected()) {
            this.interpreterSlow = true;
        } else {
            this.interpreterSlow = false;
        }
           
    }//GEN-LAST:event_CheckBoxSloMoActionPerformed

    private void ButtonStopMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonStopMousePressed
        ButtonStop.setEnabled(false);
        ButtonRun.setEnabled(true);
        this.running = false;
        this.step = false;
        pic.linie = 0;//Weis net warum, aber nachm rest is linie -1 --> error
        pic.Reset_WDT();
        pic.linie = 0; //Weis net warum, aber nachm rest is linie -1 --> error
        pic.akku = 0;
        refreshGui();
    }//GEN-LAST:event_ButtonStopMousePressed

    private void ComboBoxChangeFormatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboBoxChangeFormatActionPerformed
        if (ComboBoxChangeFormat.getSelectedItem().equals("DEZ")) {
            this.format = 0;
        } else if (ComboBoxChangeFormat.getSelectedItem().equals("HEX")) {
            this.format = 1;
        } else if (ComboBoxChangeFormat.getSelectedItem().equals("BIN")) {
            this.format = 2;
        }

        refreshTables();
    }//GEN-LAST:event_ComboBoxChangeFormatActionPerformed

    private void ButtonStepMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonStepMousePressed
        if (this.CheckBoxSloMo.isSelected()) {
            this.interpreterSlow = true;
        } else {
            this.interpreterSlow = false;
        }

        ButtonStop.setEnabled(true);
        ButtonRun.setEnabled(false);
        Thread interpreterThread = new Thread(interpret);
        this.step = true;
        //pic.linie++;
        interpreterThread.start();  
}//GEN-LAST:event_ButtonStepMousePressed

    private void ButtonStepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonStepActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_ButtonStepActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new PICSIMGUI().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButtonRun;
    private javax.swing.JButton ButtonStep;
    private javax.swing.JButton ButtonStop;
    private javax.swing.JCheckBox CheckBoxSloMo;
    private javax.swing.JComboBox ComboBoxChangeFormat;
    private javax.swing.JLabel LabelPortA;
    private javax.swing.JLabel LabelPortA1;
    private javax.swing.JLabel LabelPortB;
    private javax.swing.JLabel LabelStatus;
    private javax.swing.JPanel PanelFlags;
    private javax.swing.JPanel PanelPorts;
    private javax.swing.JRadioButton RadioButtonA0;
    private javax.swing.JRadioButton RadioButtonA1;
    private javax.swing.JRadioButton RadioButtonA2;
    private javax.swing.JRadioButton RadioButtonA3;
    private javax.swing.JRadioButton RadioButtonA4;
    private javax.swing.JRadioButton RadioButtonB0;
    private javax.swing.JRadioButton RadioButtonB1;
    private javax.swing.JRadioButton RadioButtonB2;
    private javax.swing.JRadioButton RadioButtonB3;
    private javax.swing.JRadioButton RadioButtonB4;
    private javax.swing.JRadioButton RadioButtonB5;
    private javax.swing.JRadioButton RadioButtonB6;
    private javax.swing.JRadioButton RadioButtonB7;
    private javax.swing.JRadioButton RadioButtonCarryFlag;
    private javax.swing.JRadioButton RadioButtonRP0Flag;
    private javax.swing.JRadioButton RadioButtonRP0Flag1;
    private javax.swing.JRadioButton RadioButtonRP0Flag2;
    private javax.swing.JRadioButton RadioButtonZFlag;
    private javax.swing.JTable TableBank0;
    private javax.swing.JTable TableBank1;
    private javax.swing.JTextField TextFieldPortAValue;
    private javax.swing.JTextField TextFieldPortBValue;
    private javax.swing.JTextField TextFieldWValue;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JList jList1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
