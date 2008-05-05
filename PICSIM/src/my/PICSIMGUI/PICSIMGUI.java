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
import javax.swing.JFileChooser;

/**
 *
 * @author  Mogli BA, PP
 */
public class PICSIMGUI extends javax.swing.JFrame implements Runnable
{

    public static PicCPU pic = new PicCPU();

    public void run()
    {
    }

    /** Creates new form PICSIMGUI */
    public PICSIMGUI()
    {
        super("PIC16F84 - SIMULATOR");
        initComponents();
    }

    static public void e(Object... parameters)
    {
        if (parameters.length == 2)
        {
            System.out.print(parameters[0]);
        }
        else
        {
            System.out.print(parameters[0]);
        }
    }

    /**
     * 
     * @param port 8Bit Array des Wertes in PortA
     * @param portValue  DezimalWert des Wertes in PortA
     */
    @SuppressWarnings("static-access")
    public void refreshGui()
    {

        setPortARadios(pic.getPortA(), pic.memoryBank0[pic.portA]);
        setPortBRadios(pic.getPortB(), pic.memoryBank0[pic.portB]);
        setWRadios(pic.getW(), pic.akku);

        if (pic.statusReg[pic.zFlag] == 1)
        {
            this.RadioButtonZFlag.setSelected(true);
        }
        else
        {
            this.RadioButtonZFlag.setSelected(false);
        }
        if (pic.statusReg[pic.cFlag] == 1)
        {
            this.RadioButtonCarryFlag.setSelected(true);
        }
        else
        {
            this.RadioButtonCarryFlag.setSelected(false);
        }
    }

    public void setWRadios(int[] port, int portValue)
    {
        TextFieldWValue.setText(String.valueOf(portValue));

        if (port[7] == 1)
        {
            RadioButtonA8.setSelected(true);
        }
        else
        {
            RadioButtonA8.setSelected(false);
        }

        if (port[6] == 1)
        {
            RadioButtonA9.setSelected(true);
        }
        else
        {
            RadioButtonA9.setSelected(false);
        }

        if (port[5] == 1)
        {
            RadioButtonA10.setSelected(true);
        }
        else
        {
            RadioButtonA10.setSelected(false);
        }

        if (port[4] == 1)
        {
            RadioButtonA11.setSelected(true);
        }
        else
        {
            RadioButtonA11.setSelected(false);
        }

        if (port[3] == 1)
        {
            RadioButtonA12.setSelected(true);
        }
        else
        {
            RadioButtonA12.setSelected(false);
        }

        if (port[2] == 1)
        {
            RadioButtonA13.setSelected(true);
        }
        else
        {
            RadioButtonA13.setSelected(false);
        }

        if (port[1] == 1)
        {
            RadioButtonA14.setSelected(true);
        }
        else
        {
            RadioButtonA14.setSelected(false);
        }

        if (port[0] == 1)
        {
            RadioButtonA15.setSelected(true);
        }
        else
        {
            RadioButtonA15.setSelected(false);
        }
    }

    public void setPortARadios(int[] port, int portValue)
    {
        TextFieldPortAValue.setText(String.valueOf(portValue));

        if (port[0] == 1)
        {
            RadioButtonA0.setSelected(true);
        }
        else
        {
            RadioButtonA0.setSelected(false);
        }

        if (port[1] == 1)
        {
            RadioButtonA1.setSelected(true);
        }
        else
        {
            RadioButtonA1.setSelected(false);
        }

        if (port[2] == 1)
        {
            RadioButtonA2.setSelected(true);
        }
        else
        {
            RadioButtonA2.setSelected(false);
        }

        if (port[3] == 1)
        {
            RadioButtonA3.setSelected(true);
        }
        else
        {
            RadioButtonA3.setSelected(false);
        }

        if (port[4] == 1)
        {
            RadioButtonA4.setSelected(true);
        }
        else
        {
            RadioButtonA4.setSelected(false);
        }

        if (port[5] == 1)
        {
            RadioButtonA5.setSelected(true);
        }
        else
        {
            RadioButtonA5.setSelected(false);
        }

        if (port[6] == 1)
        {
            RadioButtonA6.setSelected(true);
        }
        else
        {
            RadioButtonA6.setSelected(false);
        }

        if (port[7] == 1)
        {
            RadioButtonA7.setSelected(true);
        }
        else
        {
            RadioButtonA7.setSelected(false);
        }
    }

    public void setPortBRadios(int[] port, int portValue)
    {
        TextFieldPortBValue.setText(String.valueOf(portValue));

        if (port[0] == 1)
        {
            RadioButtonB0.setSelected(true);
        }
        else
        {
            RadioButtonA0.setSelected(false);
        }

        if (port[1] == 1)
        {
            RadioButtonB1.setSelected(true);
        }
        else
        {
            RadioButtonB1.setSelected(false);
        }

        if (port[2] == 1)
        {
            RadioButtonB2.setSelected(true);
        }
        else
        {
            RadioButtonB2.setSelected(false);
        }

        if (port[3] == 1)
        {
            RadioButtonB3.setSelected(true);
        }
        else
        {
            RadioButtonB3.setSelected(false);
        }

        if (port[4] == 1)
        {
            RadioButtonB4.setSelected(true);
        }
        else
        {
            RadioButtonB4.setSelected(false);
        }

        if (port[5] == 1)
        {
            RadioButtonB5.setSelected(true);
        }
        else
        {
            RadioButtonB5.setSelected(false);
        }

        if (port[6] == 1)
        {
            RadioButtonB6.setSelected(true);
        }
        else
        {
            RadioButtonB6.setSelected(false);
        }

        if (port[7] == 1)
        {
            RadioButtonB7.setSelected(true);
        }
        else
        {
            RadioButtonB7.setSelected(false);
        }
    }

    public void setStatusLabel(String text)
    {
        this.LabelStatus.setText(text);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        ButtonRun = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        InputTextFileButton = new javax.swing.JButton();
        LabelStatus = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        PanelFlags = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        RadioButtonRP0Flag = new javax.swing.JRadioButton();
        RadioButtonCarryFlag = new javax.swing.JRadioButton();
        RadioButtonZFlag = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        PanelPorts = new javax.swing.JPanel();
        LabelPortA = new javax.swing.JLabel();
        LabelPortB = new javax.swing.JLabel();
        RadioButtonA7 = new javax.swing.JRadioButton();
        RadioButtonA6 = new javax.swing.JRadioButton();
        RadioButtonA5 = new javax.swing.JRadioButton();
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
        PanelPorts1 = new javax.swing.JPanel();
        LabelPortA1 = new javax.swing.JLabel();
        RadioButtonA8 = new javax.swing.JRadioButton();
        RadioButtonA9 = new javax.swing.JRadioButton();
        RadioButtonA10 = new javax.swing.JRadioButton();
        RadioButtonA11 = new javax.swing.JRadioButton();
        RadioButtonA12 = new javax.swing.JRadioButton();
        RadioButtonA13 = new javax.swing.JRadioButton();
        RadioButtonA14 = new javax.swing.JRadioButton();
        RadioButtonA15 = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        TextFieldWValue = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBounds(new java.awt.Rectangle(0, 0, 1024, 1024));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        ButtonRun.setText("Start");
        ButtonRun.setToolTipText("Start Compiling");
        ButtonRun.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ButtonRunMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                ButtonRunMouseReleased(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText("0000 2817           00019           goto main           ;Unterprogramme überspringen\n0001 3010           00023           movlw 16            ;Schleifenzähler\n0002 008C           00024           movwf count\n0003 3010           00025           movlw 10h           ;Startzeiger initialisieren\n0004 0084           00026           movwf fsr           ;Zeiger ins FSR\n0005 0100           00027           clrw\n0006 0080           00029           movwf indirect      ;Wert indirekt abspeichern\n0007 0A84           00030           incf fsr            ;Zeiger erhöhen\n0008 3E01           00031           addlw 1             ;W-Register erhöhen (es gibt kein INC W\n0009 0B8C           00032           decfsz count        ;Schleifenzähler erniedrigen\n000A 2806           00033           goto loop1          ;wiederholen bis F08 auf 0 ist\n000B 3400           00034           retlw 0\n000C 3010           00038           movlw 10h           ;Schleifenzähler initialisieren\n000D 008C           00039           movwf count\n000E 0084           00040           movwf fsr           ;Startzeiger initialsieren\n000F 0100           00041           clrw                ;Summenregister löschen\n0010 0700           00043           addwf indirect,w    ;Speicherinhalt zu W addieren\n0011 0A84           00044           incf fsr\n0012 0B8C           00045           decfsz count\n0013 2810           00046           goto loop2\n0014 008F           00047           movwf 0fh           ;Ergebnis abspeichern\n0015 098F           00048           comf 0fh            ;Komplement bilden\n0016 3400           00049           retlw 0             ;Unterprogrammende   \n0017 303F           00052           movlw 3fh           ;zuerst den Vorteiler vom RTCC trennen\n0018 1683           00053           bsf status,5        ;ins Option-Register schreiben\n0019 0081           00054           movwf 1             ;=freg 81h\n001A 1283           00055           bcf status,5        ;zurück auf Bank0\n001B 0100           00056           clrw                ;RTCC-Register löschen\n001C 0081           00057           movwf 1h\n001D 2001           00058           call fillinc        ;Speicherbereich füllen\n001E 200C           00059           call qsumme         ;Quersumme berechnen\n001F 090F           00060           comf 0fh,w          ;Ergebnis holen\n0020 020F           00061           subwf 0fh,w         ;vom Orginalwert abziehen\n0021 008E           00062           movwf 0eh           ;neues Ergebnis abspeichern.\n0022 3010           00063           movlw 10h             \n0023 1683           00064           bsf status,5        ;auf Bank 1 umschalten\n0024 0085           00065           movwf 5             ;=freg 85H  Port A 0-3 auf Ausgang\n0025 1283           00066           bcf status,5        ;zurück auf Bank 0\n0026 0085           00067           movwf ra            ;Signale auf Low  \n0027 1806           00069           btfsc rb,0\n0028 2827           00070           goto main1          ;warten bis RB0 auf 0   \n0029 1C06           00072           btfss rb,0\n002A 2829           00073           goto main2          ;warten bis rb0 wieder 1         \n002B 3020           00074           movlw 20h           ;Option neu setzen, VT=1:2\n002C 1683           00075           bsf status,5        ;Bank 1\n002D 0081           00076           movwf 1             ;hier liegt Option\n002E 1283           00077           bcf status,5        ;wieder Bank 0    \n002F 282F           00081           goto ende           ;Endlosschleife, verhindert Nirwana");
        jScrollPane1.setViewportView(jTextArea1);

        InputTextFileButton.setText("Load File");
        InputTextFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InputTextFileButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("LAST INSTRUCTION: ");

        PanelFlags.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Status Flags", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11)));

        RadioButtonRP0Flag.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        RadioButtonRP0Flag.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        RadioButtonRP0Flag.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadioButtonRP0FlagActionPerformed(evt);
            }
        });

        RadioButtonCarryFlag.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        RadioButtonCarryFlag.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        RadioButtonCarryFlag.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadioButtonCarryFlagActionPerformed(evt);
            }
        });

        RadioButtonZFlag.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        RadioButtonZFlag.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        RadioButtonZFlag.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadioButtonZFlagActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(RadioButtonZFlag)
                        .addContainerGap(12, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(RadioButtonCarryFlag, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(RadioButtonRP0Flag, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(73, 73, 73))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(RadioButtonZFlag)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RadioButtonCarryFlag)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RadioButtonRP0Flag)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setText("Z-Flag");

        jLabel6.setText("Carry");

        jLabel5.setText("RP0");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout PanelFlagsLayout = new javax.swing.GroupLayout(PanelFlags);
        PanelFlags.setLayout(PanelFlagsLayout);
        PanelFlagsLayout.setHorizontalGroup(
            PanelFlagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelFlagsLayout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelFlagsLayout.setVerticalGroup(
            PanelFlagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelFlagsLayout.createSequentialGroup()
                .addGroup(PanelFlagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(101, Short.MAX_VALUE))
        );

        PanelPorts.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ports", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11)));

        LabelPortA.setText("Port A");

        LabelPortB.setText("Port B");

        jLabel3.setText("   7     6     5     4     3     2     1     0");

        TextFieldPortAValue.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        TextFieldPortAValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextFieldPortAValueActionPerformed(evt);
            }
        });

        TextFieldPortBValue.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        TextFieldPortBValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextFieldPortBValueActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PanelPortsLayout = new javax.swing.GroupLayout(PanelPorts);
        PanelPorts.setLayout(PanelPortsLayout);
        PanelPortsLayout.setHorizontalGroup(
            PanelPortsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelPortsLayout.createSequentialGroup()
                .addGroup(PanelPortsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelPortsLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(PanelPortsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(LabelPortA)
                            .addComponent(LabelPortB))
                        .addGap(11, 11, 11)
                        .addGroup(PanelPortsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(PanelPortsLayout.createSequentialGroup()
                                .addComponent(RadioButtonA7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(RadioButtonA6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(RadioButtonA5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                                .addComponent(TextFieldPortAValue, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                .addComponent(TextFieldPortBValue)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 74, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelPortsLayout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
                        .addGap(40, 40, 40)))
                .addContainerGap())
        );
        PanelPortsLayout.setVerticalGroup(
            PanelPortsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelPortsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(6, 6, 6)
                .addGroup(PanelPortsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(RadioButtonA0)
                    .addComponent(RadioButtonA1)
                    .addComponent(RadioButtonA2)
                    .addComponent(RadioButtonA3)
                    .addComponent(RadioButtonA4)
                    .addComponent(RadioButtonA5)
                    .addComponent(RadioButtonA6)
                    .addComponent(RadioButtonA7)
                    .addComponent(LabelPortA)
                    .addComponent(TextFieldPortAValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 6, Short.MAX_VALUE)
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
                .addContainerGap())
        );

        PanelPorts1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Akku", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11)));

        LabelPortA1.setText("Akku");

        jLabel4.setText("   7     6     5     4     3     2     1     0");

        TextFieldWValue.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        javax.swing.GroupLayout PanelPorts1Layout = new javax.swing.GroupLayout(PanelPorts1);
        PanelPorts1.setLayout(PanelPorts1Layout);
        PanelPorts1Layout.setHorizontalGroup(
            PanelPorts1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelPorts1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LabelPortA1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PanelPorts1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
                    .addGroup(PanelPorts1Layout.createSequentialGroup()
                        .addComponent(RadioButtonA8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(RadioButtonA9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(RadioButtonA10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(RadioButtonA11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(RadioButtonA12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(RadioButtonA13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(RadioButtonA14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(RadioButtonA15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TextFieldWValue, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        PanelPorts1Layout.setVerticalGroup(
            PanelPorts1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelPorts1Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(PanelPorts1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(LabelPortA1)
                    .addGroup(PanelPorts1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(RadioButtonA9)
                        .addComponent(RadioButtonA10)
                        .addComponent(RadioButtonA11)
                        .addComponent(RadioButtonA12)
                        .addComponent(RadioButtonA13)
                        .addComponent(RadioButtonA15)
                        .addComponent(RadioButtonA14)
                        .addComponent(RadioButtonA8))
                    .addComponent(TextFieldWValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(jTable1);

        jTabbedPane1.addTab("Bank 0", jScrollPane2);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(jTable2);

        jTabbedPane1.addTab("Bank 1", jScrollPane3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LabelStatus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(PanelPorts, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(PanelPorts1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(PanelFlags, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE))
                                        .addGap(10, 10, 10)
                                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(658, 658, 658))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(ButtonRun)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(InputTextFileButton)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 569, Short.MAX_VALUE)))
                                .addGap(2426, 2426, 2426))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 604, Short.MAX_VALUE)))
                        .addGap(252, 252, 252)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ButtonRun)
                    .addComponent(InputTextFileButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 473, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(PanelPorts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(PanelPorts1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(PanelFlags, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(LabelStatus)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    @SuppressWarnings("static-access")
    private void TextFieldPortAValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextFieldPortAValueActionPerformed
    // TODO add your handling code here:
    }//GEN-LAST:event_TextFieldPortAValueActionPerformed

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

        if (returnVal == chooser.APPROVE_OPTION)
        {
            /* To create a URL for a file on the local file-system, we simply
             * pre-pend the "file" protocol to the absolute path of the file.
             */
            fileURL = chooser.getSelectedFile().getAbsolutePath();
        }

        String record = null;

        try
        {
            jTextArea1.setText("");
            FileReader fr = new FileReader(fileURL);
            BufferedReader br = new BufferedReader(fr);

            record =
                    new String();
            while ((record = br.readLine()) != null)
            {
                jTextArea1.append(record + "\n");
            }

        }
        catch (IOException e)
        {
            // catch possible io errors from readLine()
            System.out.println("Uh oh, got an IOException error!");
            e.printStackTrace();
        }

}//GEN-LAST:event_InputTextFileButtonActionPerformed

    @SuppressWarnings("static-access")
    private void ButtonRunMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonRunMouseReleased


     
    }//GEN-LAST:event_ButtonRunMouseReleased

    /**
     * 
     * @param evt Eventhandler. Kommt von NetBeans IDE 6.0.1
     * @description Startet beim Klick auf den Button einen Thread der die PIC befehle abarbeitet.
     */
    private void ButtonRunMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonRunMousePressed

        String[] Code = jTextArea1.getText().split("\n");
        InstructionInterpreter interpret = new InstructionInterpreter(Code, this, this.pic);
        if (evt.getClickCount() % 2 == 1)
        {
            ButtonRun.setText("Stop");


            Thread interpreterThread = new Thread(interpret);
            interpret.running = true;
            interpreterThread.start();
        }
        else if (evt.getClickCount() % 2 == 0)
        {
            interpret.running = false;
            ButtonRun.setText("Start");
        }
        

        
    }//GEN-LAST:event_ButtonRunMousePressed

    private void RadioButtonZFlagActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioButtonZFlagActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_RadioButtonZFlagActionPerformed

    private void RadioButtonCarryFlagActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioButtonCarryFlagActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_RadioButtonCarryFlagActionPerformed

    private void TextFieldPortBValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextFieldPortBValueActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_TextFieldPortBValueActionPerformed

    private void RadioButtonRP0FlagActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioButtonRP0FlagActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_RadioButtonRP0FlagActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        java.awt.EventQueue.invokeLater(new Runnable()
        {

            public void run()
            {
                new PICSIMGUI().setVisible(true);
            }
            
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButtonRun;
    private javax.swing.JButton InputTextFileButton;
    private javax.swing.JLabel LabelPortA;
    private javax.swing.JLabel LabelPortA1;
    private javax.swing.JLabel LabelPortB;
    private javax.swing.JLabel LabelStatus;
    private javax.swing.JPanel PanelFlags;
    private javax.swing.JPanel PanelPorts;
    private javax.swing.JPanel PanelPorts1;
    private javax.swing.JRadioButton RadioButtonA0;
    private javax.swing.JRadioButton RadioButtonA1;
    private javax.swing.JRadioButton RadioButtonA10;
    private javax.swing.JRadioButton RadioButtonA11;
    private javax.swing.JRadioButton RadioButtonA12;
    private javax.swing.JRadioButton RadioButtonA13;
    private javax.swing.JRadioButton RadioButtonA14;
    private javax.swing.JRadioButton RadioButtonA15;
    private javax.swing.JRadioButton RadioButtonA2;
    private javax.swing.JRadioButton RadioButtonA3;
    private javax.swing.JRadioButton RadioButtonA4;
    private javax.swing.JRadioButton RadioButtonA5;
    private javax.swing.JRadioButton RadioButtonA6;
    private javax.swing.JRadioButton RadioButtonA7;
    private javax.swing.JRadioButton RadioButtonA8;
    private javax.swing.JRadioButton RadioButtonA9;
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
    private javax.swing.JRadioButton RadioButtonZFlag;
    private javax.swing.JTextField TextFieldPortAValue;
    private javax.swing.JTextField TextFieldPortBValue;
    private javax.swing.JTextField TextFieldWValue;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
