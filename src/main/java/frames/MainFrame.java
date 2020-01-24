package frames;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import SRecProtocol.Server.SRecServer;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class MainFrame extends JFrame {

    /*** Labels ***/
    private JLabel lblLog, lblQR, lblServerConnections, lblTitle;

    /*** Labels used as buttons ***/
    private JLabel btnStart;

    /*** Text area ***/
    private JTextArea textLogs;

    /*** List views ***/
    private JList<String> listConnections;

    private SRecServer sRecServer;
    private DefaultListModel<String> listModel;

    private HashMap<String, Color> colors;
    private HashMap<String, Font> fonts;

    private boolean isServerRunning = false;

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initServerAndTriggers();
        initColors();
        initComponents();
        initStdOutput();
        initFonts();

        if(!this.fonts.isEmpty())
            this.applyFonts();
    }


    private void initServerAndTriggers(){

        this.sRecServer = new SRecServer(55555);

        ObservableList<InetAddress> obsListConnections = sRecServer.getServerConnectionService().getAliveConnections();

        this.listModel = new DefaultListModel<>();

        obsListConnections.addListener((ListChangeListener<InetAddress>)(change) -> {
            while(change.next()) {
                if(change.wasAdded())
                    change.getAddedSubList().forEach((item) ->
                            listModel.addElement(item.toString()));

                if(change.wasRemoved())
                    change.getRemoved().forEach((item) ->
                            listModel.removeElement(item.toString()));
            }
        });
    }

    private ImageIcon generateQRCode(String text) throws WriterException {
        MultiFormatWriter mfw = new MultiFormatWriter();
        BitMatrix bitMatriz = mfw.encode(text, BarcodeFormat.QR_CODE, 200, 200);
        BufferedImage bi = MatrixToImageWriter.toBufferedImage(bitMatriz);

        return new ImageIcon(bi);
    }

    /** Triggers **/
    private void btnStartMouseClicked() {
        if(!isServerRunning){
            try {
                textLogs.setText("");
                btnStart.setText("Stop");

                sRecServer.startService();

                lblQR.setIcon(generateQRCode(sRecServer.getConnectionDetails()));
                lblQR.setVisible(true);

                isServerRunning = !isServerRunning;

            } catch (WriterException ex) {
                ex.printStackTrace();
            }
        }
        else {
            sRecServer.stopService();
            btnStart.setText("Start");
            lblQR.setVisible(false);
            isServerRunning = !isServerRunning;
        }
    }

    private void btnStartMouseEntered() {
        btnStart.setBackground(this.colors.get("colorAccentDark"));
    }

    private void btnStartMouseExited() {
        btnStart.setBackground(this.colors.get("colorAccent"));
    }

    private void initStdOutput() {
        PrintStream printStream = new PrintStream(new OutputStream(){
            @Override
            public void write(int i) {
                textLogs.append((char)i + "");
            }
        });

        System.setOut(printStream);
    }

    private void initColors() {
        this.colors = new HashMap<>();

        this.colors.put("colorAccent", new Color(203, 0, 23));
        this.colors.put("colorPrimary", new Color(255,255,255));
        this.colors.put("colorPrimaryDark", new Color(199,199,199));
        this.colors.put("colorTitles", new Color(40,41,48));
        this.colors.put("colorBodies", new Color(66,66,66));
        this.colors.put("colorAccentDark", new Color(152, 1, 18));
    }

    private void initFonts() {

        this.fonts = new HashMap<>();

        try {

            String a = "fonts/";

            InputStream in;

            in = new BufferedInputStream(
                    new FileInputStream(a + "Nunito-Regular.ttf")
            );
            this.fonts.put("nunito", Font.createFont(Font.TRUETYPE_FONT, in ));


            in = new BufferedInputStream(
                    new FileInputStream(a + "Nunito-Bold.ttf")
            );
            this.fonts.put("nunito-bold", Font.createFont(Font.TRUETYPE_FONT, in));

        } catch (FontFormatException | IOException ex) {
            ex.printStackTrace();
        }
    }

    private void applyFonts() {
        Font nunitoBold = this.fonts.get("nunito-bold");
        Font nunito = this.fonts.get("nunito");

        lblServerConnections.setFont(nunitoBold.deriveFont(14f));
        lblLog.setFont(nunitoBold.deriveFont(14f));
        listConnections.setFont(nunito.deriveFont(12f));
        textLogs.setFont(nunito.deriveFont(12f));

        lblTitle.setFont(nunitoBold.deriveFont(18f));
        btnStart.setFont(nunitoBold.deriveFont(16f));
    }

    private void initComponents() {

        /* Containers */
        JPanel rootContainer = new JPanel();
        JPanel panelHeader = new JPanel();

        /* Components */
        JPanel panelShadowHeader = new JPanel();

        /* Containers */
        JPanel panelBody = new JPanel();
        JPanel panelServerConnectionsAndImageContainer = new JPanel();
        JPanel panelSpaceLeft = new JPanel();
        JPanel panelSpaceBottom = new JPanel();
        JPanel panelLogContainer = new JPanel();
        JPanel panelServerConnectionsAndImage = new JPanel();

        /* Labels */
        btnStart = new JLabel();
        lblTitle = new JLabel();
        lblServerConnections = new JLabel();
        lblQR = new JLabel();
        lblLog = new JLabel();

        /* Split Panels */
        JSplitPane serverConsAndLogsSplitter = new JSplitPane();

        /* ScrollPanels */
        JScrollPane scrollLog = new JScrollPane();
        JScrollPane scrollListConnections = new JScrollPane();

        /* List */
        listConnections = new JList<>();

        /* TextArea */
        textLogs = new JTextArea();

        /* Root Container Customization */
        rootContainer.setPreferredSize(new java.awt.Dimension(600, 400));
        rootContainer.setLayout(new java.awt.BorderLayout());

        /* Header Customization */
        panelHeader.setBackground(new Color(203, 0, 23));
        panelHeader.setPreferredSize(new Dimension(600, 70));
        panelHeader.setLayout(new BorderLayout());

        /* Label Title Customization */
        lblTitle.setText("SRec Receiver");
        lblTitle.setBackground(new Color(255, 255, 255));
        lblTitle.setFont(new Font("DejaVu Sans Condensed", Font.PLAIN, 17)); // NOI18N
        lblTitle.setForeground(Color.white);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setPreferredSize(new Dimension(160, 22));
        panelHeader.add(lblTitle, BorderLayout.LINE_START);

        /* Label Title Customization */
        btnStart.setBackground(new Color(203, 0, 23));
        btnStart.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 18)); // NOI18N
        btnStart.setForeground(Color.white);
        btnStart.setHorizontalAlignment(SwingConstants.CENTER);
        btnStart.setText("Start");
        btnStart.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnStart.setOpaque(true);
        btnStart.setPreferredSize(new Dimension(70, 70));
        btnStart.addMouseListener(new MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnStartMouseClicked();
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnStartMouseEntered();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnStartMouseExited();
            }
        });
        panelHeader.add(btnStart, BorderLayout.LINE_END);

        /* Label Title Customization */
        panelShadowHeader.setBackground(new Color(199, 199, 199));
        panelShadowHeader.setPreferredSize(new Dimension(454, 1));
        panelHeader.add(panelShadowHeader, BorderLayout.PAGE_END);
        rootContainer.add(panelHeader, BorderLayout.PAGE_START);

        /* Panel Body Customization */
        panelBody.setLayout(new BorderLayout());

        /* Splitter Customization */
        serverConsAndLogsSplitter.setOrientation(JSplitPane.VERTICAL_SPLIT);

        /* Panel Connections and QR Image Customization */
        panelServerConnectionsAndImageContainer.setLayout(new BorderLayout());

        /* Label Server Connections Customization*/
        lblServerConnections.setBackground(Color.white);
        lblServerConnections.setFont(new Font("DejaVu Sans Light", Font.BOLD, 14)); // NOI18N
        lblServerConnections.setText("     Server Connections");
        lblServerConnections.setOpaque(true);
        lblServerConnections.setPreferredSize(new Dimension(133, 30));
        panelServerConnectionsAndImageContainer.add(lblServerConnections, BorderLayout.PAGE_START);

        /* Label Server Connections And Image Customization */
        panelServerConnectionsAndImage.setBackground(Color.white);
        panelServerConnectionsAndImage.setLayout(new GridLayout(1, 0));

        /* List Connections Customization */
        listConnections.setFont(new Font("DejaVu Sans Light", Font.BOLD, 14)); // NOI18N
        listConnections.setModel(listModel);
        listConnections.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listConnections.setFocusable(false);
        listConnections.setRequestFocusEnabled(false);
        listConnections.setSelectionBackground(new Color(199, 199, 199));
        scrollListConnections.setViewportView(listConnections);
        panelServerConnectionsAndImage.add(scrollListConnections);

        /* Label QR Customization */
        lblQR.setBackground(Color.white);
        lblQR.setHorizontalAlignment(SwingConstants.CENTER);
        lblQR.setFocusable(false);
        lblQR.setPreferredSize(new Dimension(100, 100));
        panelServerConnectionsAndImage.add(lblQR);

        panelServerConnectionsAndImageContainer.add(panelServerConnectionsAndImage, BorderLayout.CENTER);

        /* Space */
        panelSpaceLeft.setBackground(new Color(255, 255, 255));
        panelSpaceLeft.setPreferredSize(new Dimension(10, 10));
        panelServerConnectionsAndImageContainer.add(panelSpaceLeft, BorderLayout.LINE_START);

        /* Space */
        panelSpaceBottom.setBackground(new Color(255, 255, 255));
        panelSpaceBottom.setPreferredSize(new Dimension(10, 10));
        panelServerConnectionsAndImageContainer.add(panelSpaceBottom, BorderLayout.SOUTH);

        serverConsAndLogsSplitter.setTopComponent(panelServerConnectionsAndImageContainer);

        panelLogContainer.setLayout(new BorderLayout());

        lblLog.setBackground(Color.white);
        lblLog.setFont(new Font("DejaVu Sans Light", Font.BOLD, 14)); // NOI18N
        lblLog.setText("     Log");
        lblLog.setOpaque(true);
        lblLog.setPreferredSize(new Dimension(26, 30));
        panelLogContainer.add(lblLog, BorderLayout.PAGE_START);

        /* Text Logs Area Customization */
        textLogs.setEditable(false);
        textLogs.setBackground(new Color(66, 66, 66));
        textLogs.setColumns(20);
        textLogs.setFont(new Font("DejaVu Sans Light", Font.BOLD, 14)); // NOI18N
        textLogs.setForeground(Color.white);
        textLogs.setRows(5);
        textLogs.setWrapStyleWord(true);
        textLogs.setBorder(null);
        textLogs.setFocusable(false);
        scrollLog.setViewportView(textLogs);

        panelLogContainer.add(scrollLog, BorderLayout.CENTER);

        serverConsAndLogsSplitter.setRightComponent(panelLogContainer);

        panelBody.add(serverConsAndLogsSplitter, BorderLayout.CENTER);

        rootContainer.add(panelBody,BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("SRec Receiver");
        setMinimumSize(new Dimension(600, 400));;
        getContentPane().add(rootContainer, BorderLayout.CENTER);

        pack();
    }

}
