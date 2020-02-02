package gui;

import SRecProtocol.Server.SRecServer;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;

import javax.swing.DefaultListModel;
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

import Utils.ColorProvider;
import Utils.FontProvider;
import Utils.ImagesLoader;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 * The type Gui frame.
 */
public class GUIFrame extends JFrame {

    private JLabel lblLog, lblQR, lblServerConnections, lblTitle;

    private JLabel btnStart;

    private JTextArea textLogs;

    private JList<String> listConnections;

    private SRecServer sRecServer;
    private DefaultListModel<String> listModel;

    private boolean isServerRunning = false;

    private FontProvider fontProvider;
    private ImagesLoader imagesLoader;
    private ColorProvider colorProvider;

    /**
     * Instantiates a new Gui frame.
     *
     * @param fontProvider  the font provider
     * @param colorProvider the color provider
     * @param imagesLoader  the images loader
     */
    public GUIFrame(FontProvider fontProvider, ColorProvider colorProvider, ImagesLoader imagesLoader) {
        this.fontProvider = fontProvider;
        this.colorProvider = colorProvider;
        this.imagesLoader = imagesLoader;

        this.sRecServer = new SRecServer(0);
        this.listModel = new DefaultListModel<>();

        init();
    }

    private void init() {
        initComponents();
        initStdOutput();
        initListeners();

        applyCustomFonts();
        applyIcons();
    }

    private void initListeners(){

        ObservableList<InetAddress> obsListConnections = sRecServer
                .getServerConnectionService().getAliveConnections();

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

        btnStart.addMouseListener(new MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnStartMouseClicked();
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnStart.setBackground(colorProvider.getColor("colorAccentDark"));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnStart.setBackground(colorProvider.getColor("colorAccent"));
            }
        });
    }

    private void btnStartMouseClicked() {
        if(!isServerRunning){
            try {
                textLogs.setText("");
                btnStart.setText("Stop");

                sRecServer.startService();

                lblQR.setIcon(generateQRCode("SRecReceiver:" + sRecServer.getConnectionDetails()));
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

    private ImageIcon generateQRCode(String text) throws WriterException {
        MultiFormatWriter mfw = new MultiFormatWriter();
        BitMatrix bitMatrix = mfw.encode(text, BarcodeFormat.QR_CODE, 200, 200);
        BufferedImage bi = MatrixToImageWriter.toBufferedImage(bitMatrix);

        return new ImageIcon(bi);
    }

    private void applyCustomFonts() {
        lblServerConnections.setFont(fontProvider.getFont("nunito-bold").deriveFont(14f));
        lblLog.setFont(fontProvider.getFont("nunito-bold").deriveFont(14f));
        listConnections.setFont(fontProvider.getFont("nunito").deriveFont(12f));
        textLogs.setFont(fontProvider.getFont("nunito").deriveFont(12f));
        lblTitle.setFont(fontProvider.getFont("nunito-bold").deriveFont(18f));
        btnStart.setFont(fontProvider.getFont("nunito-bold").deriveFont(16f));
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
        rootContainer.setPreferredSize(new Dimension(800, 600));
        rootContainer.setLayout(new BorderLayout());

        /* Header Customization */
        panelHeader.setBackground(colorProvider.getColor("colorAccent"));
        panelHeader.setPreferredSize(new Dimension(600, 70));
        panelHeader.setLayout(new BorderLayout());

        /* Label Title Customization */
        lblTitle.setText("SRec Receiver");
        lblTitle.setFont(fontProvider.getFont("default").deriveFont(Font.PLAIN, 17));
        lblTitle.setForeground(colorProvider.getColor("colorPrimary"));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setPreferredSize(new Dimension(160, 22));
        panelHeader.add(lblTitle, BorderLayout.LINE_START);

        /* Label Title Customization */
        btnStart.setBackground(colorProvider.getColor("colorAccent"));
        btnStart.setFont(fontProvider.getFont("default").deriveFont(Font.PLAIN, 17));
        btnStart.setForeground(colorProvider.getColor("colorPrimary"));
        btnStart.setHorizontalAlignment(SwingConstants.CENTER);
        btnStart.setText("Start");
        btnStart.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnStart.setOpaque(true);
        btnStart.setPreferredSize(new Dimension(125, 70));
        panelHeader.add(btnStart, BorderLayout.LINE_END);

        /* Label Title Customization */
        panelShadowHeader.setBackground(colorProvider.getColor("colorPrimaryDark"));
        panelShadowHeader.setPreferredSize(new Dimension(454, 1));
        panelHeader.add(panelShadowHeader, BorderLayout.PAGE_END);
        rootContainer.add(panelHeader, BorderLayout.PAGE_START);

        /* Panel Body Customization */
        panelBody.setLayout(new BorderLayout());

        /* Splitter Customization */
        serverConsAndLogsSplitter.setOrientation(JSplitPane.VERTICAL_SPLIT);
        serverConsAndLogsSplitter.setDividerLocation(250);

        /* Panel Connections and QR Image Customization */
        panelServerConnectionsAndImageContainer.setLayout(new BorderLayout());

        JPanel panelLblServerConnections = new JPanel();
        panelLblServerConnections.setLayout(new BorderLayout());
        panelLblServerConnections.setBackground(colorProvider.getColor("colorPrimary"));
        panelLblServerConnections.setPreferredSize(new Dimension(155, 50));
        panelServerConnectionsAndImageContainer.add(panelLblServerConnections, BorderLayout.PAGE_START);

        /* Label Server Connections Customization*/
        lblServerConnections.setBackground(colorProvider.getColor("colorPrimary"));
        lblServerConnections.setFont(fontProvider.getFont("default").deriveFont(Font.BOLD, 14));
        lblServerConnections.setText("Server Connections");
        lblServerConnections.setIconTextGap(10);
        lblServerConnections.setOpaque(true);
        lblServerConnections.setHorizontalAlignment(SwingConstants.CENTER);
        lblServerConnections.setPreferredSize(new Dimension(175, 50));
        panelLblServerConnections.add(lblServerConnections, BorderLayout.WEST);

        /* Label Server Connections And Image Customization */
        panelServerConnectionsAndImage.setBackground(colorProvider.getColor("colorPrimary"));
        panelServerConnectionsAndImage.setLayout(new GridLayout(1, 0));

        /* List Connections Customization */
        listConnections.setFont(fontProvider.getFont("default").deriveFont(Font.BOLD, 14));
        listConnections.setModel(listModel);
        listConnections.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listConnections.setFocusable(false);
        listConnections.setRequestFocusEnabled(false);
        listConnections.setSelectionBackground(colorProvider.getColor("colorPrimaryDark"));
        scrollListConnections.setViewportView(listConnections);
        panelServerConnectionsAndImage.add(scrollListConnections);

        /* Label QR Customization */
        lblQR.setBackground(colorProvider.getColor("colorPrimary"));
        lblQR.setHorizontalAlignment(SwingConstants.CENTER);
        lblQR.setFocusable(false);
        lblQR.setPreferredSize(new Dimension(100, 100));
        panelServerConnectionsAndImage.add(lblQR);

        panelServerConnectionsAndImageContainer.add(panelServerConnectionsAndImage, BorderLayout.CENTER);

        /* Space */
        panelSpaceLeft.setBackground(colorProvider.getColor("colorPrimary"));
        panelSpaceLeft.setPreferredSize(new Dimension(10, 10));
        panelServerConnectionsAndImageContainer.add(panelSpaceLeft, BorderLayout.LINE_START);

        /* Space */
        panelSpaceBottom.setBackground(colorProvider.getColor("colorPrimary"));
        panelSpaceBottom.setPreferredSize(new Dimension(10, 10));
        panelServerConnectionsAndImageContainer.add(panelSpaceBottom, BorderLayout.SOUTH);

        serverConsAndLogsSplitter.setTopComponent(panelServerConnectionsAndImageContainer);

        panelLogContainer.setLayout(new BorderLayout());

        JPanel panelLblLog = new JPanel();
        panelLblLog.setLayout(new BorderLayout());
        panelLblLog.setBackground(colorProvider.getColor("colorPrimary"));
        panelLblLog.setPreferredSize(new Dimension(100, 50));
        panelLogContainer.add(panelLblLog, BorderLayout.PAGE_START);

        lblLog.setBackground(colorProvider.getColor("colorPrimary"));
        lblLog.setFont(fontProvider.getFont("default").deriveFont(Font.BOLD, 14));
        lblLog.setText("Log");
        lblLog.setIconTextGap(10);
        lblLog.setOpaque(true);
        lblLog.setHorizontalAlignment(SwingConstants.CENTER);
        lblLog.setPreferredSize(new Dimension(70, 50));
        panelLblLog.add(lblLog, BorderLayout.WEST);

        /* Text Logs Area Customization */
        textLogs.setEditable(false);
        textLogs.setBackground(colorProvider.getColor("colorBodies"));
        textLogs.setColumns(20);
        textLogs.setFont(fontProvider.getFont("default").deriveFont(Font.BOLD, 14));
        textLogs.setForeground(colorProvider.getColor("colorPrimary"));
        textLogs.setRows(5);
        textLogs.setWrapStyleWord(true);
        textLogs.setBorder(null);
        textLogs.setFocusable(false);
        scrollLog.setViewportView(textLogs);

        panelLogContainer.add(scrollLog, BorderLayout.CENTER);

        serverConsAndLogsSplitter.setRightComponent(panelLogContainer);

        panelBody.add(serverConsAndLogsSplitter, BorderLayout.CENTER);

        rootContainer.add(panelBody,BorderLayout.CENTER);

        /* This frame Customization */
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("SRec Receiver");
        setMinimumSize(new Dimension(800, 600));
        getContentPane().add(rootContainer, BorderLayout.CENTER);

        pack();
    }

    private void applyIcons() {
        lblServerConnections.setIcon(imagesLoader.getImageIcon("connections"));
        lblLog.setIcon(imagesLoader.getImageIcon("logs"));
        btnStart.setIcon(imagesLoader.getImageIcon("start"));
    }


}
