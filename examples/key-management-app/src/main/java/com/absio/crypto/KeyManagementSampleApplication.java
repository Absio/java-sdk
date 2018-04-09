package com.absio.crypto;

import com.absio.crypto.cipher.AESKeyStrength;
import com.absio.crypto.cipher.CipherHelper;
import com.absio.crypto.digest.MessageDigestHelper;
import com.absio.crypto.ecc.AbsioIESHelper;
import com.absio.crypto.ecc.ECCHelper;
import com.absio.crypto.ecc.ECKeyDecoder;
import com.absio.crypto.ecc.EllipticCurve;
import com.absio.crypto.encoding.Pem;
import com.absio.crypto.kdf.KDF2Helper;
import com.absio.crypto.kdf.PBKDF2Helper;
import com.absio.crypto.key.IndexedECPrivateKey;
import com.absio.crypto.key.IndexedECPublicKey;
import com.absio.crypto.key.KeyPairHelper;
import com.absio.crypto.key.KeyType;
import com.absio.crypto.keyagreement.KeyAgreementHelper;
import com.absio.crypto.mac.MacHelper;
import com.absio.crypto.signature.SignatureHelper;
import com.absio.util.ByteUtils;
import com.absio.util.FileUtils;
import com.absio.util.StringUtils;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.conscrypt.OpenSSLECPrivateKey;
import org.conscrypt.OpenSSLProvider;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.UUID;

public class KeyManagementSampleApplication extends JFrame {
    private final static Cursor DEFAULT_CURSOR =
            Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    private final static Cursor WAIT_CURSOR =
            Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
    private final static MouseAdapter mouseAdapter =
            new MouseAdapter() {
            };
    private JButton asymmetricGenerateKeysButton;
    private JTextField asymmetricPrivateFile;
    private JButton asymmetricPrivateFileButton;
    private JRadioButton asymmetricPrivateFileRadioButton;
    private JTextArea asymmetricPrivateHere;
    private JRadioButton asymmetricPrivateHereRadioButton;
    private JTextField asymmetricPublicFile;
    private JButton asymmetricPublicFileButton;
    private JRadioButton asymmetricPublicFileRadioButton;
    private JTextArea asymmetricPublicHere;
    private JRadioButton asymmetricPublicHereRadioButton;
    private JCheckBox asymmetricUseECCHelperCheckBox;
    private JButton buttonCancel;
    private JButton buttonExit;
    private CipherHelper cipherHelper;
    private JPanel contentPane;
    private JButton decryptButton;
    private JTextField decryptInputFile;
    private JButton decryptInputFileButton;
    private JRadioButton decryptInputFileRadioButton;
    private JTextField decryptInputHex;
    private JRadioButton decryptInputHexRadioButton;
    private JTextField decryptIvHex;
    private JTextField decryptKeyHex;
    private JRadioButton decryptKeyHexRadioButton;
    private JTextField decryptKeyPem;
    private JRadioButton decryptKeyPemRadioButton;
    private JTextField decryptOutputFile;
    private JButton decryptOutputFileButton;
    private JRadioButton decryptOutputFileRadioButton;
    private JTextArea decryptOutputText;
    private JRadioButton decryptOutputTextRadioButton;
    private JPanel decryptPanel;
    private JRadioButton defaultSizeRadioButton;
    private ECCHelper eccHelper;
    private JTextField ecdhAliceFile;
    private JButton ecdhAliceFileButton;
    private JRadioButton ecdhAliceFileRadioButton;
    private JButton ecdhAliceGenButton;
    private JTextArea ecdhAliceManual;
    private JRadioButton ecdhAliceManualRadioButton;
    private JTextField ecdhBobFile;
    private JButton ecdhBobFileButton;
    private JRadioButton ecdhBobFileRadioButton;
    private JButton ecdhBobGenButton;
    private JTextArea ecdhBobManual;
    private JRadioButton ecdhBobManualRadioButton;
    private JButton ecdhButton;
    private JTextArea ecdhShared;
    private JTextArea ecdhSharedKey;
    private JButton encryptButton;
    private JTextField encryptInputFile;
    private JButton encryptInputFileButton;
    private JRadioButton encryptInputFileRadioButton;
    private JTextField encryptInputText;
    private JRadioButton encryptInputTextRadioButton;
    private JTextField encryptIvHex;
    private JRadioButton encryptIvHexRadioButton;
    private JRadioButton encryptIvRandomRadioButton;
    private JRadioButton encryptKeyHexRadioButton;
    private JRadioButton encryptKeyRandomRadioButton;
    private JTextField encryptOutputFile;
    private JButton encryptOutputFileButton;
    private JRadioButton encryptOutputFileRadioButton;
    private JTextArea encryptOutputHex;
    private JRadioButton encryptOutputHexRadioButton;
    private JPanel encryptPanel;
    private JTextField enryptKeyHexInput;
    private JTextField genKeyHex;
    private JButton genKeyHexButton;
    private JButton hashButton;
    private JTextField hashInput;
    private JTextField hashOutput;
    private JTextArea hmacData;
    private JCheckBox hmacDataHexCheckBox;
    private JTextField hmacDigest;
    private JButton hmacDigestButton;
    private JTextField hmacKey;
    private JButton hmacKeyGenButton;
    private JTextField hmacVerify;
    private JButton hmacVerifyButton;
    private JButton iesDecryptButton;
    private JRadioButton iesDerivationPublicKeyPEMFileRadioButton;
    private JTextField iesDerivationPublicKeyPemFile;
    private JTextArea iesDerivationPublicKeyText;
    private JRadioButton iesDerivationPublicKeyTextRadioButton;
    private JButton iesEncryptButton;
    private AbsioIESHelper iesHelper;
    private JTextField iesInputFile;
    private JButton iesInputFileButton;
    private JRadioButton iesInputFileRadioButton;
    private JTextField iesInputText;
    private JRadioButton iesInputTextRadioButton;
    private JTextField iesObjectId;
    private JButton iesObjectIdGenButton;
    private JPanel iesOutput;
    private JTextField iesOutputFile;
    private JButton iesOutputFileButton;
    private JRadioButton iesOutputFileRadioButton;
    private JTextArea iesOutputText;
    private JRadioButton iesOutputTextRadioButton;
    private JButton iesPrivateKeyFileButton;
    private JButton iesPublicKeyFileButton;
    private JTextField iesSenderId;
    private JButton iesSenderIdGenButton;
    private JRadioButton iesSigningPrivateKeyPEMFileRadioButton;
    private JTextField iesSigningPrivateKeyPemFile;
    private JTextArea iesSigningPrivateKeyText;
    private JRadioButton iesSigningPrivateKeyTextRadioButton;
    private JCheckBox iesUseECCHelperCheckBox;
    private KDF2Helper kdf2Helper;
    private JButton kdfGenKeyButton;
    private JTextField kdfKeySize;
    private JTextField kdfOutput;
    private JTextField kdfSecretHex;
    private JRadioButton kdfSecretHexRadioButton;
    private JTextField kdfSecretText;
    private JRadioButton kdfSecretTextRadioButton;
    private KeyAgreementHelper keyAgreementHelper;
    private ECKeyDecoder keyDecoder;
    private JPanel keyGenPanel;
    private KeyPairHelper keyHelper;
    private JTextField keySize;
    private File lastDirectory;
    private MacHelper macHelper;
    private MessageDigestHelper messageDigestHelper;
    private JButton pbkdf2GenerateKeyButton;
    private JTextField pbkdf2Iterations;
    private JTextField pbkdf2Password;
    private JButton pbkdf2SaltGenButton;
    private JTextField pbkdf2SaltHex;
    private JTextField pbkdf2SaltSize;
    private JTextField pbkdf2SaltText;
    private JButton pdkdf2DecryptButton;
    private JButton pdkdf2EncryptButton;
    private PBKDF2Helper pdkdf2Helper;
    private JTextField pdkdf2InputFile;
    private JButton pdkdf2InputFileButton;
    private JRadioButton pdkdf2InputFileRadioButton;
    private JTextField pdkdf2InputManual;
    private JRadioButton pdkdf2InputManualRadioButton;
    private JTextField pdkdf2OutputFile;
    private JButton pdkdf2OutputFileButton;
    private JRadioButton pdkdf2OutputFileRadioButton;
    private JTextField pdkdf2OutputManual;
    private JRadioButton pdkdf2OutputManualRadioButton;
    private JRadioButton pdkdf2SaltHexRadioButton;
    private JRadioButton pdkdf2SaltTextRadioButton;
    private JTextField sign;
    private JButton signButton;
    private JTextField signData;
    private JCheckBox signDataHexCheckBox;
    private JTextField signPrivateKeyFile;
    private JButton signPrivateKeyFileButton;
    private JRadioButton signPrivateKeyFileRadioButton;
    private JButton signPrivateKeyGenButton;
    private JTextArea signPrivateKeyManual;
    private JRadioButton signPrivateKeyManualRadioButton;
    private JCheckBox signUseECCHelperCheckBox;
    private SignatureHelper signatureHelper;
    private JRadioButton specifiedSizeRadioButton;
    private JTabbedPane tabbedPane1;
    private JPanel thePanel;
    private JCheckBox useECCHelperCheckBox;
    private JTextField verified;
    private JButton verifyButton;

    public KeyManagementSampleApplication() {
        super("Key Management Application");
        Security.insertProviderAt(new OpenSSLProvider(), 1);
        $$$setupUI$$$();
        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonExit);

        buttonExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onExit();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onExit();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onExit();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        try {
            eccHelper = new ECCHelper(EllipticCurve.P384, AESKeyStrength.AES256);
        }
        catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error trying to create ECCHelper.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        setUpSymmetricTab();
        setUpHashTab();
        setUpIESTab();
        setUpKDF2Tab();
        setUpPBKDF2Tab();
        setUpAsymmetricTab();
        setUpEcdhTab();
        setUpHmacTab();
        setUpSignatureTab();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (InstantiationException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        KeyManagementSampleApplication frame = new KeyManagementSampleApplication();
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonExit = new JButton();
        buttonExit.setText("Exit");
        panel2.add(buttonExit, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        tabbedPane1 = new JTabbedPane();
        panel3.add(tabbedPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(700, 600), new Dimension(700, 600), null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Symmetric", panel4);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel5, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        keyGenPanel = new JPanel();
        keyGenPanel.setLayout(new GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(keyGenPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        genKeyHex = new JTextField();
        genKeyHex.setColumns(100);
        keyGenPanel.add(genKeyHex, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        defaultSizeRadioButton = new JRadioButton();
        defaultSizeRadioButton.setText("Default Size");
        keyGenPanel.add(defaultSizeRadioButton, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        specifiedSizeRadioButton = new JRadioButton();
        specifiedSizeRadioButton.setText("Specified Size");
        keyGenPanel.add(specifiedSizeRadioButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        keySize = new JTextField();
        keyGenPanel.add(keySize, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        genKeyHexButton = new JButton();
        genKeyHexButton.setText("Gen Key Hex");
        keyGenPanel.add(genKeyHexButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Generate Key");
        keyGenPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel4.add(spacer2, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        encryptPanel = new JPanel();
        encryptPanel.setLayout(new GridBagLayout());
        panel4.add(encryptPanel, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        encryptKeyHexRadioButton = new JRadioButton();
        encryptKeyHexRadioButton.setText("Hex Input");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        encryptPanel.add(encryptKeyHexRadioButton, gbc);
        enryptKeyHexInput = new JTextField();
        enryptKeyHexInput.setColumns(40);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        encryptPanel.add(enryptKeyHexInput, gbc);
        encryptKeyRandomRadioButton = new JRadioButton();
        encryptKeyRandomRadioButton.setText("Random");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        encryptPanel.add(encryptKeyRandomRadioButton, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Key");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        encryptPanel.add(label2, gbc);
        encryptInputTextRadioButton = new JRadioButton();
        encryptInputTextRadioButton.setText("Text");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        encryptPanel.add(encryptInputTextRadioButton, gbc);
        encryptInputFileRadioButton = new JRadioButton();
        encryptInputFileRadioButton.setText("File");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        encryptPanel.add(encryptInputFileRadioButton, gbc);
        encryptInputFile = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        encryptPanel.add(encryptInputFile, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("Input");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        encryptPanel.add(label3, gbc);
        encryptOutputFileRadioButton = new JRadioButton();
        encryptOutputFileRadioButton.setText("File");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        encryptPanel.add(encryptOutputFileRadioButton, gbc);
        encryptOutputHexRadioButton = new JRadioButton();
        encryptOutputHexRadioButton.setText("Hex");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        encryptPanel.add(encryptOutputHexRadioButton, gbc);
        encryptButton = new JButton();
        encryptButton.setText("Encrypt");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        encryptPanel.add(encryptButton, gbc);
        final JLabel label4 = new JLabel();
        label4.setText("Output");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        encryptPanel.add(label4, gbc);
        encryptOutputHex = new JTextArea();
        encryptOutputHex.setEditable(false);
        encryptOutputHex.setLineWrap(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.gridheight = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        encryptPanel.add(encryptOutputHex, gbc);
        encryptOutputFile = new JTextField();
        encryptOutputFile.setColumns(40);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        encryptPanel.add(encryptOutputFile, gbc);
        encryptOutputFileButton = new JButton();
        encryptOutputFileButton.setText("...");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        encryptPanel.add(encryptOutputFileButton, gbc);
        encryptInputText = new JTextField();
        encryptInputText.setColumns(40);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        encryptPanel.add(encryptInputText, gbc);
        encryptInputFileButton = new JButton();
        encryptInputFileButton.setText("...");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        encryptPanel.add(encryptInputFileButton, gbc);
        final JLabel label5 = new JLabel();
        label5.setText("IV");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        encryptPanel.add(label5, gbc);
        encryptIvRandomRadioButton = new JRadioButton();
        encryptIvRandomRadioButton.setText("Random");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        encryptPanel.add(encryptIvRandomRadioButton, gbc);
        encryptIvHexRadioButton = new JRadioButton();
        encryptIvHexRadioButton.setText("Hex Input");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        encryptPanel.add(encryptIvHexRadioButton, gbc);
        encryptIvHex = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        encryptPanel.add(encryptIvHex, gbc);
        decryptPanel = new JPanel();
        decryptPanel.setLayout(new GridBagLayout());
        panel4.add(decryptPanel, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Key - Hex");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        decryptPanel.add(label6, gbc);
        final JLabel label7 = new JLabel();
        label7.setText("Input");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        decryptPanel.add(label7, gbc);
        decryptInputHexRadioButton = new JRadioButton();
        decryptInputHexRadioButton.setText("Hex");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        decryptPanel.add(decryptInputHexRadioButton, gbc);
        decryptInputFileRadioButton.setText("File");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        decryptPanel.add(decryptInputFileRadioButton, gbc);
        decryptKeyHex = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        decryptPanel.add(decryptKeyHex, gbc);
        decryptInputHex = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        decryptPanel.add(decryptInputHex, gbc);
        decryptInputFile = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        decryptPanel.add(decryptInputFile, gbc);
        decryptInputFileButton = new JButton();
        decryptInputFileButton.setText("...");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        decryptPanel.add(decryptInputFileButton, gbc);
        decryptOutputFileRadioButton = new JRadioButton();
        decryptOutputFileRadioButton.setText("File");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        decryptPanel.add(decryptOutputFileRadioButton, gbc);
        decryptOutputTextRadioButton = new JRadioButton();
        decryptOutputTextRadioButton.setText("Text");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        decryptPanel.add(decryptOutputTextRadioButton, gbc);
        decryptButton = new JButton();
        decryptButton.setText("Decrypt");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        decryptPanel.add(decryptButton, gbc);
        final JLabel label8 = new JLabel();
        label8.setText("Ouput");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        decryptPanel.add(label8, gbc);
        decryptOutputFileButton = new JButton();
        decryptOutputFileButton.setText("...");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        decryptPanel.add(decryptOutputFileButton, gbc);
        decryptOutputFile = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        decryptPanel.add(decryptOutputFile, gbc);
        final JLabel label9 = new JLabel();
        label9.setText("IV - Hex");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        decryptPanel.add(label9, gbc);
        decryptIvHex = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        decryptPanel.add(decryptIvHex, gbc);
        decryptOutputText = new JTextArea();
        decryptOutputText.setEditable(false);
        decryptOutputText.setLineWrap(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        decryptPanel.add(decryptOutputText, gbc);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridBagLayout());
        tabbedPane1.addTab("Hashing", panel6);
        final JLabel label10 = new JLabel();
        label10.setText("Input");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel6.add(label10, gbc);
        hashInput = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel6.add(hashInput, gbc);
        hashOutput = new JTextField();
        hashOutput.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel6.add(hashOutput, gbc);
        hashButton = new JButton();
        hashButton.setText("Hash");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel6.add(hashButton, gbc);
        final JLabel label11 = new JLabel();
        label11.setText("Hash - Hex");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel6.add(label11, gbc);
        iesOutput = new JPanel();
        iesOutput.setLayout(new GridBagLayout());
        tabbedPane1.addTab("IES", iesOutput);
        final JLabel label12 = new JLabel();
        label12.setText("Input - Hex for decrypt");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        iesOutput.add(label12, gbc);
        iesInputTextRadioButton = new JRadioButton();
        iesInputTextRadioButton.setText("Text");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        iesOutput.add(iesInputTextRadioButton, gbc);
        iesInputText = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        iesOutput.add(iesInputText, gbc);
        iesInputFileRadioButton = new JRadioButton();
        iesInputFileRadioButton.setText("File");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        iesOutput.add(iesInputFileRadioButton, gbc);
        iesInputFile = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        iesOutput.add(iesInputFile, gbc);
        iesInputFileButton = new JButton();
        iesInputFileButton.setText("...");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        iesOutput.add(iesInputFileButton, gbc);
        final JLabel label13 = new JLabel();
        label13.setText("Signing Private/Public Key");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        iesOutput.add(label13, gbc);
        iesSigningPrivateKeyTextRadioButton = new JRadioButton();
        iesSigningPrivateKeyTextRadioButton.setText("Text");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        iesOutput.add(iesSigningPrivateKeyTextRadioButton, gbc);
        iesSigningPrivateKeyPEMFileRadioButton = new JRadioButton();
        iesSigningPrivateKeyPEMFileRadioButton.setText("PEM File");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        iesOutput.add(iesSigningPrivateKeyPEMFileRadioButton, gbc);
        iesSigningPrivateKeyPemFile = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        iesOutput.add(iesSigningPrivateKeyPemFile, gbc);
        iesPrivateKeyFileButton = new JButton();
        iesPrivateKeyFileButton.setText("...");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        iesOutput.add(iesPrivateKeyFileButton, gbc);
        final JLabel label14 = new JLabel();
        label14.setText("Derivation Public/Private Key");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        iesOutput.add(label14, gbc);
        iesDerivationPublicKeyTextRadioButton = new JRadioButton();
        iesDerivationPublicKeyTextRadioButton.setText("Text");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        iesOutput.add(iesDerivationPublicKeyTextRadioButton, gbc);
        iesDerivationPublicKeyText = new JTextArea();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.gridheight = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        iesOutput.add(iesDerivationPublicKeyText, gbc);
        iesDerivationPublicKeyPEMFileRadioButton = new JRadioButton();
        iesDerivationPublicKeyPEMFileRadioButton.setText("PEM File");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        iesOutput.add(iesDerivationPublicKeyPEMFileRadioButton, gbc);
        final JLabel label15 = new JLabel();
        label15.setText("Sender ID");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        iesOutput.add(label15, gbc);
        iesDerivationPublicKeyPemFile = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        iesOutput.add(iesDerivationPublicKeyPemFile, gbc);
        iesPublicKeyFileButton = new JButton();
        iesPublicKeyFileButton.setText("...");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        iesOutput.add(iesPublicKeyFileButton, gbc);
        iesSenderId = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        iesOutput.add(iesSenderId, gbc);
        iesSenderIdGenButton = new JButton();
        iesSenderIdGenButton.setText("Gen");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        iesOutput.add(iesSenderIdGenButton, gbc);
        final JLabel label16 = new JLabel();
        label16.setText("Object ID");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.WEST;
        iesOutput.add(label16, gbc);
        iesObjectId = new JTextField();
        iesObjectId.setEditable(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        iesOutput.add(iesObjectId, gbc);
        iesObjectIdGenButton = new JButton();
        iesObjectIdGenButton.setText("Gen");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 9;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        iesOutput.add(iesObjectIdGenButton, gbc);
        final JLabel label17 = new JLabel();
        label17.setText("Output");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.WEST;
        iesOutput.add(label17, gbc);
        iesOutputTextRadioButton = new JRadioButton();
        iesOutputTextRadioButton.setText("Text");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.WEST;
        iesOutput.add(iesOutputTextRadioButton, gbc);
        iesOutputText = new JTextArea();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 10;
        gbc.gridheight = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        iesOutput.add(iesOutputText, gbc);
        iesOutputFileRadioButton = new JRadioButton();
        iesOutputFileRadioButton.setText("File");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 12;
        gbc.anchor = GridBagConstraints.WEST;
        iesOutput.add(iesOutputFileRadioButton, gbc);
        iesOutputFile = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 12;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        iesOutput.add(iesOutputFile, gbc);
        iesOutputFileButton = new JButton();
        iesOutputFileButton.setText("...");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 12;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        iesOutput.add(iesOutputFileButton, gbc);
        iesEncryptButton = new JButton();
        iesEncryptButton.setText("Encrypt");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 13;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        iesOutput.add(iesEncryptButton, gbc);
        iesDecryptButton = new JButton();
        iesDecryptButton.setText("Decrypt");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 13;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        iesOutput.add(iesDecryptButton, gbc);
        iesSigningPrivateKeyText = new JTextArea();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridheight = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        iesOutput.add(iesSigningPrivateKeyText, gbc);
        final JLabel label18 = new JLabel();
        label18.setText(" Private for Encrypt");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        iesOutput.add(label18, gbc);
        final JLabel label19 = new JLabel();
        label19.setText("Public for Encrypt");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        iesOutput.add(label19, gbc);
        iesUseECCHelperCheckBox = new JCheckBox();
        iesUseECCHelperCheckBox.setText("Use ECCHelper");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 13;
        gbc.anchor = GridBagConstraints.WEST;
        iesOutput.add(iesUseECCHelperCheckBox, gbc);
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridBagLayout());
        tabbedPane1.addTab("KDF2", panel7);
        final JLabel label20 = new JLabel();
        label20.setText("Secret");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel7.add(label20, gbc);
        kdfSecretTextRadioButton = new JRadioButton();
        kdfSecretTextRadioButton.setText("Text");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel7.add(kdfSecretTextRadioButton, gbc);
        kdfSecretHexRadioButton = new JRadioButton();
        kdfSecretHexRadioButton.setText("Hex");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel7.add(kdfSecretHexRadioButton, gbc);
        kdfSecretText = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel7.add(kdfSecretText, gbc);
        kdfSecretHex = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel7.add(kdfSecretHex, gbc);
        kdfKeySize = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridheight = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel7.add(kdfKeySize, gbc);
        final JLabel label21 = new JLabel();
        label21.setText("Key size in bytes");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.gridheight = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel7.add(label21, gbc);
        kdfOutput = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel7.add(kdfOutput, gbc);
        final JLabel label22 = new JLabel();
        label22.setText("Output hex");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel7.add(label22, gbc);
        kdfGenKeyButton = new JButton();
        kdfGenKeyButton.setText("Gen Key");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel7.add(kdfGenKeyButton, gbc);
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridBagLayout());
        tabbedPane1.addTab("PBKDF2", panel8);
        final JLabel label23 = new JLabel();
        label23.setText("Password");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel8.add(label23, gbc);
        final JLabel label24 = new JLabel();
        label24.setText("Salt");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel8.add(label24, gbc);
        final JLabel label25 = new JLabel();
        label25.setText("Iterations");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        panel8.add(label25, gbc);
        final JLabel label26 = new JLabel();
        label26.setText("Input");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        panel8.add(label26, gbc);
        pdkdf2SaltTextRadioButton = new JRadioButton();
        pdkdf2SaltTextRadioButton.setText("Text");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel8.add(pdkdf2SaltTextRadioButton, gbc);
        pdkdf2SaltHexRadioButton = new JRadioButton();
        pdkdf2SaltHexRadioButton.setText("Hex");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel8.add(pdkdf2SaltHexRadioButton, gbc);
        pbkdf2SaltText = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel8.add(pbkdf2SaltText, gbc);
        pbkdf2SaltHex = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel8.add(pbkdf2SaltHex, gbc);
        pbkdf2Password = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel8.add(pbkdf2Password, gbc);
        pdkdf2InputManualRadioButton = new JRadioButton();
        pdkdf2InputManualRadioButton.setText("Manual");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        panel8.add(pdkdf2InputManualRadioButton, gbc);
        pdkdf2InputFileRadioButton = new JRadioButton();
        pdkdf2InputFileRadioButton.setText("File");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        panel8.add(pdkdf2InputFileRadioButton, gbc);
        pdkdf2InputManual = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel8.add(pdkdf2InputManual, gbc);
        pdkdf2InputFile = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel8.add(pdkdf2InputFile, gbc);
        pdkdf2OutputManualRadioButton = new JRadioButton();
        pdkdf2OutputManualRadioButton.setText("Text");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        panel8.add(pdkdf2OutputManualRadioButton, gbc);
        pdkdf2OutputFileRadioButton = new JRadioButton();
        pdkdf2OutputFileRadioButton.setText("File");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        panel8.add(pdkdf2OutputFileRadioButton, gbc);
        pdkdf2OutputManual = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 7;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel8.add(pdkdf2OutputManual, gbc);
        pdkdf2OutputFile = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel8.add(pdkdf2OutputFile, gbc);
        final JLabel label27 = new JLabel();
        label27.setText("Output");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        panel8.add(label27, gbc);
        pbkdf2Iterations = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel8.add(pbkdf2Iterations, gbc);
        pdkdf2OutputFileButton = new JButton();
        pdkdf2OutputFileButton.setText("...");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel8.add(pdkdf2OutputFileButton, gbc);
        pdkdf2InputFileButton = new JButton();
        pdkdf2InputFileButton.setText("...");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel8.add(pdkdf2InputFileButton, gbc);
        pdkdf2EncryptButton = new JButton();
        pdkdf2EncryptButton.setText("Encrypt");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel8.add(pdkdf2EncryptButton, gbc);
        pdkdf2DecryptButton = new JButton();
        pdkdf2DecryptButton.setText("Decrypt");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel8.add(pdkdf2DecryptButton, gbc);
        pbkdf2SaltGenButton = new JButton();
        pbkdf2SaltGenButton.setText("Gen");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel8.add(pbkdf2SaltGenButton, gbc);
        final JLabel label28 = new JLabel();
        label28.setText("Size");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel8.add(label28, gbc);
        pbkdf2SaltSize = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel8.add(pbkdf2SaltSize, gbc);
        pbkdf2GenerateKeyButton = new JButton();
        pbkdf2GenerateKeyButton.setText("Generate Key");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel8.add(pbkdf2GenerateKeyButton, gbc);
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridBagLayout());
        tabbedPane1.addTab("Asymmetric", panel9);
        final JLabel label29 = new JLabel();
        label29.setText("Public Key");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel9.add(label29, gbc);
        final JLabel label30 = new JLabel();
        label30.setText("Private Key");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel9.add(label30, gbc);
        asymmetricPublicHereRadioButton = new JRadioButton();
        asymmetricPublicHereRadioButton.setText("Here");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel9.add(asymmetricPublicHereRadioButton, gbc);
        asymmetricPublicFileRadioButton = new JRadioButton();
        asymmetricPublicFileRadioButton.setText("File");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel9.add(asymmetricPublicFileRadioButton, gbc);
        asymmetricPublicFile = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel9.add(asymmetricPublicFile, gbc);
        asymmetricPublicFileButton = new JButton();
        asymmetricPublicFileButton.setText("...");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel9.add(asymmetricPublicFileButton, gbc);
        asymmetricPrivateHereRadioButton = new JRadioButton();
        asymmetricPrivateHereRadioButton.setText("Here");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel9.add(asymmetricPrivateHereRadioButton, gbc);
        asymmetricPrivateFileRadioButton = new JRadioButton();
        asymmetricPrivateFileRadioButton.setText("File");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel9.add(asymmetricPrivateFileRadioButton, gbc);
        asymmetricPrivateFile = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel9.add(asymmetricPrivateFile, gbc);
        asymmetricPrivateFileButton = new JButton();
        asymmetricPrivateFileButton.setText("...");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel9.add(asymmetricPrivateFileButton, gbc);
        asymmetricGenerateKeysButton = new JButton();
        asymmetricGenerateKeysButton.setText("Generate Keys");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel9.add(asymmetricGenerateKeysButton, gbc);
        asymmetricPublicHere = new JTextArea();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel9.add(asymmetricPublicHere, gbc);
        asymmetricPrivateHere = new JTextArea();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel9.add(asymmetricPrivateHere, gbc);
        asymmetricUseECCHelperCheckBox = new JCheckBox();
        asymmetricUseECCHelperCheckBox.setText("Use ECCHelper");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel9.add(asymmetricUseECCHelperCheckBox, gbc);
        thePanel = new JPanel();
        thePanel.setLayout(new GridBagLayout());
        tabbedPane1.addTab("ECDH", thePanel);
        final JLabel label31 = new JLabel();
        label31.setText("Alice Public Key");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        thePanel.add(label31, gbc);
        ecdhAliceManualRadioButton = new JRadioButton();
        ecdhAliceManualRadioButton.setText("Manual");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        thePanel.add(ecdhAliceManualRadioButton, gbc);
        ecdhAliceFileRadioButton = new JRadioButton();
        ecdhAliceFileRadioButton.setText("File");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        thePanel.add(ecdhAliceFileRadioButton, gbc);
        final JLabel label32 = new JLabel();
        label32.setText("Bob Private Key");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        thePanel.add(label32, gbc);
        ecdhBobManualRadioButton = new JRadioButton();
        ecdhBobManualRadioButton.setText("Manual");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        thePanel.add(ecdhBobManualRadioButton, gbc);
        ecdhBobFileRadioButton = new JRadioButton();
        ecdhBobFileRadioButton.setText("File");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        thePanel.add(ecdhBobFileRadioButton, gbc);
        ecdhAliceGenButton = new JButton();
        ecdhAliceGenButton.setText("Gen");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        thePanel.add(ecdhAliceGenButton, gbc);
        ecdhAliceFileButton = new JButton();
        ecdhAliceFileButton.setText("...");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        thePanel.add(ecdhAliceFileButton, gbc);
        ecdhBobGenButton = new JButton();
        ecdhBobGenButton.setText("Gen");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        thePanel.add(ecdhBobGenButton, gbc);
        ecdhBobFileButton = new JButton();
        ecdhBobFileButton.setText("...");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        thePanel.add(ecdhBobFileButton, gbc);
        ecdhButton = new JButton();
        ecdhButton.setText("ECDH");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        thePanel.add(ecdhButton, gbc);
        ecdhAliceFile = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        thePanel.add(ecdhAliceFile, gbc);
        ecdhBobFile = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        thePanel.add(ecdhBobFile, gbc);
        final JLabel label33 = new JLabel();
        label33.setText("Shared Secret");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(5, 0, 0, 0);
        thePanel.add(label33, gbc);
        ecdhAliceManual = new JTextArea();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        thePanel.add(ecdhAliceManual, gbc);
        ecdhBobManual = new JTextArea();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        thePanel.add(ecdhBobManual, gbc);
        ecdhShared = new JTextArea();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.BOTH;
        thePanel.add(ecdhShared, gbc);
        useECCHelperCheckBox = new JCheckBox();
        useECCHelperCheckBox.setText("Use ECCHelper");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        thePanel.add(useECCHelperCheckBox, gbc);
        ecdhSharedKey = new JTextArea();
        ecdhSharedKey.setLineWrap(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        thePanel.add(ecdhSharedKey, gbc);
        final JLabel label34 = new JLabel();
        label34.setText("Shared Key");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        thePanel.add(label34, gbc);
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridBagLayout());
        tabbedPane1.addTab("HMAC", panel10);
        final JLabel label35 = new JLabel();
        label35.setText("Key Hex");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel10.add(label35, gbc);
        hmacKey = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel10.add(hmacKey, gbc);
        final JLabel label36 = new JLabel();
        label36.setText("Data");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel10.add(label36, gbc);
        hmacKeyGenButton = new JButton();
        hmacKeyGenButton.setText("Gen");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel10.add(hmacKeyGenButton, gbc);
        hmacDataHexCheckBox = new JCheckBox();
        hmacDataHexCheckBox.setText("Hex");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel10.add(hmacDataHexCheckBox, gbc);
        hmacVerify = new JTextField();
        hmacVerify.setEditable(false);
        hmacVerify.setEnabled(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel10.add(hmacVerify, gbc);
        hmacData = new JTextArea();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel10.add(hmacData, gbc);
        hmacDigest = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel10.add(hmacDigest, gbc);
        hmacVerifyButton = new JButton();
        hmacVerifyButton.setText("Verify");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel10.add(hmacVerifyButton, gbc);
        hmacDigestButton = new JButton();
        hmacDigestButton.setText("Digest");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel10.add(hmacDigestButton, gbc);
        final JLabel label37 = new JLabel();
        label37.setText("Hex");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel10.add(label37, gbc);
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new GridBagLayout());
        tabbedPane1.addTab("Signature", panel11);
        final JLabel label38 = new JLabel();
        label38.setText("Data");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel11.add(label38, gbc);
        signData = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel11.add(signData, gbc);
        signDataHexCheckBox = new JCheckBox();
        signDataHexCheckBox.setText("Hex");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel11.add(signDataHexCheckBox, gbc);
        final JLabel label39 = new JLabel();
        label39.setText("Signature (Hex)");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel11.add(label39, gbc);
        final JLabel label40 = new JLabel();
        label40.setText("Verified");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        panel11.add(label40, gbc);
        sign = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel11.add(sign, gbc);
        verified = new JTextField();
        verified.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel11.add(verified, gbc);
        signButton = new JButton();
        signButton.setText("Sign");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel11.add(signButton, gbc);
        verifyButton = new JButton();
        verifyButton.setText("Verify");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel11.add(verifyButton, gbc);
        final JLabel label41 = new JLabel();
        label41.setText("Private Key");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel11.add(label41, gbc);
        signPrivateKeyManualRadioButton = new JRadioButton();
        signPrivateKeyManualRadioButton.setText("Manual");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel11.add(signPrivateKeyManualRadioButton, gbc);
        signPrivateKeyFileRadioButton = new JRadioButton();
        signPrivateKeyFileRadioButton.setText("File");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel11.add(signPrivateKeyFileRadioButton, gbc);
        signPrivateKeyFile = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel11.add(signPrivateKeyFile, gbc);
        signPrivateKeyGenButton = new JButton();
        signPrivateKeyGenButton.setText("Gen");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel11.add(signPrivateKeyGenButton, gbc);
        signPrivateKeyFileButton = new JButton();
        signPrivateKeyFileButton.setText("...");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel11.add(signPrivateKeyFileButton, gbc);
        signPrivateKeyManual = new JTextArea();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel11.add(signPrivateKeyManual, gbc);
        signUseECCHelperCheckBox = new JCheckBox();
        signUseECCHelperCheckBox.setText("Use ECCHelper");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        panel11.add(signUseECCHelperCheckBox, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

    private void asymmetricGenerateKeys() {
        try {
            KeyPair keyPair;
            if (asymmetricUseECCHelperCheckBox.isSelected()) {
                keyPair = eccHelper.generateKey();
            }
            else {
                keyPair = keyHelper.generateKeyPair(EllipticCurve.P384);
            }
            String publicPem = Pem.encodePublicKey(keyPair.getPublic());
            String privatePem = Pem.encodePrivateKey(keyPair.getPrivate());
            boolean publicHere = asymmetricPublicHereRadioButton.isSelected();
            boolean privateHere = asymmetricPrivateHereRadioButton.isSelected();
            if (publicHere) {
                asymmetricPublicHere.setText(publicPem);
            }
            else {
                FileUtils.writeToFile(asymmetricPublicFile.getText(), publicPem.getBytes());
            }
            if (privateHere) {
                asymmetricPrivateHere.setText(privatePem);
            }
            else {
                FileUtils.writeToFile(asymmetricPrivateFile.getText(), privatePem.getBytes());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error trying to generate asymmetric keys.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void computeHash() {
        try {
            byte[] hash = messageDigestHelper.digest(hashInput.getText().getBytes());
            hashOutput.setText(DatatypeConverter.printHexBinary(hash));
        }
        catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error hashing.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createUIComponents() {
        // TODO - a hack because IDEA messed up
        decryptInputFileRadioButton = new JRadioButton();
    }

    private void ecdhGenerate() {
        PublicKey aliceKey = null;
        try {
            String aliceKeyPem;
            if (ecdhAliceManualRadioButton.isSelected()) {
                aliceKeyPem = ecdhAliceManual.getText();
            }
            else {
                aliceKeyPem = new String(FileUtils.readBytesFromFile(ecdhAliceFile.getText()));
            }
            aliceKey = keyDecoder.getPublicKeyFromPEM(aliceKeyPem);
        }
        catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error trying to get Alice's public key for ECDH.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        PrivateKey bobKey = null;
        try {
            String bobKeyPem;
            if (ecdhBobManualRadioButton.isSelected()) {
                bobKeyPem = ecdhBobManual.getText();
            }
            else {
                bobKeyPem = new String(FileUtils.readBytesFromFile(ecdhBobFile.getText()));
            }
            bobKey = keyDecoder.getPrivateKeyFromPEM(bobKeyPem);
        }
        catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error trying to get Bob's private key for ECDH.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (useECCHelperCheckBox.isSelected()) {
            try {
                byte[] secret = eccHelper.generateDHSharedSecret(bobKey, aliceKey);
                byte[] key = eccHelper.generateDHSharedKey(bobKey, aliceKey);
                ecdhShared.setText(DatatypeConverter.printHexBinary(secret));
                ecdhSharedKey.setText(DatatypeConverter.printHexBinary(key));
            }
            catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error trying to generate secret and key for ECDH.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        else {
            try {
                byte[] secret = keyAgreementHelper.generateSharedSecret(bobKey, aliceKey);
                ecdhShared.setText(DatatypeConverter.printHexBinary(secret));
            }
            catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error trying to generate secret for ECDH.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void genGuid(JTextField textField) {
        textField.setText(UUID.randomUUID().toString());
    }

    private void genHMacDigest() {
        byte[] data = getHmacData();
        SecretKey key = getHmacKey();
        try {
            byte[] digest = macHelper.digest(key, data);
            hmacDigest.setText(DatatypeConverter.printHexBinary(digest));
        }
        catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error trying to create digest for HMAC.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void genHmacKey() {
        try {
            SecretKey key = macHelper.generateKey();
            hmacKey.setText(DatatypeConverter.printHexBinary(key.getEncoded()));
        }
        catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error trying to create HMAC Key.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generateKeyPem(JTextArea textArea, boolean genPublic) {
        try {
            KeyPair keyPair = keyHelper.generateKeyPair(EllipticCurve.P384);
            String pem;
            if (genPublic) {
                pem = Pem.encodePublicKey(keyPair.getPublic());
            }
            else {
                pem = Pem.encodePrivateKey(keyPair.getPrivate());
            }
            textArea.setText(pem);
        }
        catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error trying to generate asymmetric key for ECDH.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private byte[] getHmacData() {
        return hmacDataHexCheckBox.isSelected() ? DatatypeConverter.parseHexBinary(hmacData.getText()) : hmacData.getText().getBytes();
    }

    private SecretKey getHmacKey() {
        byte[] keyBytes = DatatypeConverter.parseHexBinary(hmacKey.getText());
        return macHelper.generateKey(keyBytes);
    }

    private byte[] getSignData() {
        return signDataHexCheckBox.isSelected() ? DatatypeConverter.parseHexBinary(signData.getText()) : signData.getText().getBytes();
    }

    private PrivateKey getSignaturePrivateKey() {
        PrivateKey key = null;
        try {
            String pem;
            if (signPrivateKeyManualRadioButton.isSelected()) {
                pem = signPrivateKeyManual.getText();
            }
            else {
                pem = new String(FileUtils.readBytesFromFile(signPrivateKeyFile.getText()));
            }
            key = keyDecoder.getPrivateKeyFromPEM(pem);
        }
        catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error trying to get the private key for signature.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return key;
    }

    private void onAsymmetricPrivate(boolean here, boolean file) {
        if (!here) {
            asymmetricPrivateHere.setText("");
        }
        else {
            asymmetricPrivateFile.setText("");
        }
        asymmetricPrivateHere.setEditable(here);
        asymmetricPrivateFile.setEditable(file);
        asymmetricPrivateFileButton.setEnabled(file);
    }

    private void onAsymmetricPublic(boolean here, boolean file) {
        if (!here) {
            asymmetricPublicHere.setText("");
        }
        else {
            asymmetricPublicFile.setText("");
        }
        asymmetricPublicHere.setEditable(here);
        asymmetricPublicFile.setEditable(file);
        asymmetricPublicFileButton.setEnabled(file);
    }

    private void onDecryptInputFile() {
        decryptInputHex.setEditable(false);
        decryptInputHex.setText("");
        decryptInputFile.setEditable(true);
        decryptInputFileButton.setEnabled(true);
    }

    private void onDecryptInputText() {
        decryptInputHex.setEditable(true);
        decryptInputFile.setEditable(false);
        decryptInputFile.setText("");
        decryptInputFileButton.setEnabled(false);
    }

    private void onDecryptOutputFile() {
        decryptOutputText.setText("");
        decryptOutputFile.setEditable(true);
        decryptOutputFileButton.setEnabled(true);
    }

    private void onDecryptOutputText() {
        decryptOutputFile.setText("");
        decryptOutputFile.setEditable(false);
        decryptOutputFileButton.setEnabled(false);
    }

    private void onEcdhAlice(boolean manual, boolean file) {
        if (!manual) {
            ecdhAliceManual.setText("");
        }
        else {
            ecdhAliceFile.setText("");
        }
        ecdhShared.setLineWrap(true);
        ecdhAliceManual.setLineWrap(true);
        ecdhBobManual.setLineWrap(true);
        ecdhAliceManual.setEditable(manual);
        ecdhAliceFile.setEditable(file);
        ecdhAliceFileButton.setEnabled(file);
        ecdhAliceGenButton.setEnabled(manual);
    }

    private void onEcdhBob(boolean manual, boolean file) {
        if (!manual) {
            ecdhBobManual.setText("");
        }
        else {
            ecdhBobFile.setText("");
        }
        ecdhBobManual.setEditable(manual);
        ecdhBobFile.setEditable(file);
        ecdhBobFileButton.setEnabled(file);
        ecdhBobGenButton.setEnabled(manual);
    }

    private void onEncryptHexIv() {
        encryptIvHex.setEditable(true);
        encryptIvHex.setText("");
    }

    private void onEncryptHexKey() {
        enryptKeyHexInput.setEditable(true);
        enryptKeyHexInput.setText("");
    }

    private void onEncryptInputFile() {
        encryptInputText.setEditable(false);
        encryptInputText.setText("");
        encryptInputFile.setEditable(true);
        encryptInputFileButton.setEnabled(true);
    }

    private void onEncryptInputText() {
        encryptInputText.setEditable(true);
        encryptInputFile.setEditable(false);
        encryptInputFile.setText("");
        encryptInputFileButton.setEnabled(false);
    }

    private void onEncryptOutputFile() {
        encryptOutputHex.setText("");
        encryptOutputFile.setEditable(true);
        encryptOutputFileButton.setEnabled(true);
    }

    private void onEncryptOutputText() {
        encryptOutputFile.setText("");
        encryptOutputFile.setEditable(false);
        encryptOutputFileButton.setEnabled(false);
    }

    private void onEncryptRandomIv() {
        encryptIvHex.setEditable(false);
        try {
            encryptIvHex.setText(DatatypeConverter.printHexBinary(cipherHelper.generateIV()));
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void onEncryptRandomKey() {
        enryptKeyHexInput.setEditable(false);
        try {
            enryptKeyHexInput.setText(DatatypeConverter.printHexBinary(cipherHelper.generateKey()));
        }
        catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
    }

    private void onExit() {
        // add your code here
        dispose();
        System.exit(1);
    }

    private void onGenKeyHex() {
        byte[] key;
        try {
            if (defaultSizeRadioButton.isSelected()) {
                key = cipherHelper.generateKey();
            }
            else {
                String sizeString = keySize.getText();
                int keySize = Integer.parseInt(sizeString);
                key = cipherHelper.generateKey(keySize);
            }

            String hex = DatatypeConverter.printHexBinary(key);
            genKeyHex.setText(hex);
        }
        catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating the key.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onHmacVerify() {
        SecretKey key = getHmacKey();
        byte[] data = getHmacData();
        byte[] digest = DatatypeConverter.parseHexBinary(hmacDigest.getText());
        try {
            boolean verify = macHelper.verify(key, data, digest);
            hmacVerify.setText(String.valueOf(verify));
        }
        catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error trying to verify digest for HMAC.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onIesDecrypt() {
        final boolean iesInputTextRadioButtonSelected = iesInputTextRadioButton.isSelected();
        final String iesInputTextText = iesInputText.getText();
        final String iesInputFileText = iesInputFile.getText();
        final boolean iesSigningPrivateKeyTextRadioButtonSelected = iesSigningPrivateKeyTextRadioButton.isSelected();
        final String iesSigningPrivateKeyTextText = iesSigningPrivateKeyText.getText();
        final String iesSigningPrivateKeyPemFileText = iesSigningPrivateKeyPemFile.getText();
        final boolean iesDerivationPublicKeyTextRadioButtonSelected = iesDerivationPublicKeyTextRadioButton.isSelected();
        final String iesDerivationPublicKeyTextText = iesDerivationPublicKeyText.getText();
        final String iesDerivationPublicKeyPemFileText = iesDerivationPublicKeyPemFile.getText();

        SwingWorker worker = new SwingWorker<byte[], Void>() {
            @Override
            protected byte[] doInBackground() {
                // Get Content
                byte[] iesData;
                if (iesInputTextRadioButtonSelected) {
                    iesData = DatatypeConverter.parseHexBinary(iesInputTextText);
                }
                else {
                    try {
                        iesData = FileUtils.readBytesFromFile(iesInputFileText);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error getting input for IES decrypt.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        return null;
                    }
                }

                // Get signing key
                ECPublicKey signingKey;
                try {
                    if (iesSigningPrivateKeyTextRadioButtonSelected) {
                        signingKey = keyDecoder.getPublicKeyFromPEM(iesSigningPrivateKeyTextText);
                    }
                    else {
                        signingKey = keyDecoder.getPublicKeyFromPEM(new String(FileUtils.readBytesFromFile(iesSigningPrivateKeyPemFileText)));
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error getting public signing key for IES decrypt.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }

                // Get derivation key
                ECPrivateKey derivationKey;
                try {
                    if (iesDerivationPublicKeyTextRadioButtonSelected) {
                        derivationKey = keyDecoder.getPrivateKeyFromPEM(iesDerivationPublicKeyTextText);
                    }
                    else {
                        derivationKey = keyDecoder.getPrivateKeyFromPEM(new String(FileUtils.readBytesFromFile(iesDerivationPublicKeyPemFileText)));
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error getting public derivation key for IES decrypt.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }

                // Encrypt
                byte[] plaintext;
                if (iesUseECCHelperCheckBox.isSelected()) {
                    try {
                        plaintext = eccHelper.absioIESDecrypt(iesData, signingKey, derivationKey);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error performing encryption in IES decrypt.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        return null;
                    }
                }
                else {
                    try {
                        plaintext = iesHelper.decrypt(iesData, signingKey, derivationKey);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error performing encryption in IES decrypt.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        return null;
                    }
                }
                return plaintext;
            }

            @Override
            protected void done() {
                // Display output
                try {
                    if (iesOutputTextRadioButton.isSelected()) {
                        iesOutputText.setText(new String(get()));
                    }
                    else {
                        FileUtils.writeToFile(iesOutputFile.getText(), get());
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error writing output for IES decrypt.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

                super.done();
                stopWaitCursor();
            }
        };
        startWaitCursor();
        worker.execute();
    }

    private void onIesDerivationPublicKey(boolean text, boolean file) {
        if (!text) {
            iesDerivationPublicKeyText.setText("");
        }
        else {
            iesDerivationPublicKeyPemFile.setText("");
        }
        iesDerivationPublicKeyText.setEditable(text);
        iesDerivationPublicKeyPemFile.setEditable(file);
        iesPublicKeyFileButton.setEnabled(file);
    }

    private void onIesEncrypt() {
        // Get Content
        final boolean iesInputTextRadioButtonSelected = iesInputTextRadioButton.isSelected();
        final String iesInputTextText = iesInputText.getText();
        final String iesInputFileText = iesInputFile.getText();
        final boolean iesSigningPrivateKeyTextRadioButtonSelected = iesSigningPrivateKeyTextRadioButton.isSelected();
        final String iesSigningPrivateKeyTextText = iesSigningPrivateKeyText.getText();
        final String iesSigningPrivateKeyPemFileText = iesSigningPrivateKeyPemFile.getText();
        final boolean iesDerivationPublicKeyTextRadioButtonSelected = iesDerivationPublicKeyTextRadioButton.isSelected();
        final String iesDerivationPublicKeyTextText = iesDerivationPublicKeyText.getText();
        final String iesDerivationPublicKeyPemFileText = iesDerivationPublicKeyPemFile.getText();

        final UUID senderId = UUID.fromString(iesSenderId.getText());
        final UUID objectId = UUID.fromString(iesObjectId.getText());

        SwingWorker worker = new SwingWorker<byte[], Void>() {
            @Override
            protected byte[] doInBackground() {
                byte[] plaintext;
                if (iesInputTextRadioButtonSelected) {
                    plaintext = iesInputTextText.getBytes();
                }
                else {
                    try {
                        plaintext = FileUtils.readBytesFromFile(iesInputFileText);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error getting input for IES encrypt.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        return null;
                    }
                }

                // Get signing key
                ECPrivateKey signingKey;
                try {
                    if (iesSigningPrivateKeyTextRadioButtonSelected) {
                        signingKey = keyDecoder.getPrivateKeyFromPEM(iesSigningPrivateKeyTextText);
                    }
                    else {
                        signingKey = keyDecoder.getPrivateKeyFromPEM(new String(FileUtils.readBytesFromFile(iesSigningPrivateKeyPemFileText)));
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error getting private signing key for IES encrypt.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }

                // Get derivation key
                ECPublicKey derivationKey;
                try {
                    if (iesDerivationPublicKeyTextRadioButtonSelected) {
                        derivationKey = keyDecoder.getPublicKeyFromPEM(iesDerivationPublicKeyTextText);
                    }
                    else {
                        derivationKey = keyDecoder.getPublicKeyFromPEM(new String(FileUtils.readBytesFromFile(iesDerivationPublicKeyPemFileText)));
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error getting public derivation key for IES encrypt.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }

                // Encrypt
                byte[] data;
                if (iesUseECCHelperCheckBox.isSelected()) {
                    try {
                        data = eccHelper.absioIESEncrypt(plaintext, new IndexedECPrivateKey(signingKey, 0, true, KeyType.Signing), new IndexedECPublicKey(derivationKey, 0, true, KeyType.Derivation), senderId, objectId);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error performing encryption in IES encrypt.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        return null;
                    }
                }
                else {
                    try {
                        data = iesHelper.encrypt(plaintext, new IndexedECPrivateKey(signingKey, 0, true, KeyType.Signing), new IndexedECPublicKey(derivationKey, 0, true, KeyType.Derivation), senderId, objectId);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error performing encryption in IES encrypt.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        return null;
                    }
                }

                return data;
            }

            @Override
            protected void done() {
                // Display output
                try {
                    if (iesOutputTextRadioButton.isSelected()) {
                        iesOutputText.setText(DatatypeConverter.printHexBinary(get()));
                    }
                    else {
                        FileUtils.writeToFile(iesOutputFile.getText(), get());
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error writing output for IES encrypt.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

                super.done();
                stopWaitCursor();
            }
        };
        startWaitCursor();
        worker.execute();
    }

    private void onIesInput(boolean text, boolean file) {
        if (!text) {
            iesInputText.setText("");
        }
        else {
            iesInputFile.setText("");
        }
        iesInputText.setEditable(text);
        iesInputFile.setEditable(file);
        iesInputFileButton.setEnabled(file);
    }

    private void onIesOutput(boolean text, boolean file) {
        if (!text) {
            iesOutputText.setText("");
        }
        else {
            iesOutputFile.setText("");
        }
        iesOutputText.setEditable(text);
        iesOutputFile.setEditable(file);
        iesOutputFileButton.setEnabled(file);
    }

    private void onIesSigningPrivateKey(boolean text, boolean file) {
        if (!text) {
            iesSigningPrivateKeyText.setText("");
        }
        else {
            iesSigningPrivateKeyPemFile.setText("");
        }
        iesSigningPrivateKeyText.setEditable(text);
        iesSigningPrivateKeyPemFile.setEditable(file);
        iesPrivateKeyFileButton.setEnabled(file);
    }

    private void onKdf2GenKey() {
        byte[] secret;
        try {
            if (kdfSecretTextRadioButton.isSelected()) {
                secret = kdfSecretText.getText().getBytes();
            }
            else {
                secret = DatatypeConverter.parseHexBinary(kdfSecretHex.getText());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error trying to get secret for KDF2 Key Gen.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int keySize = 0;
        try {
            keySize = Integer.parseInt(kdfKeySize.getText());
        }
        catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error trying to get key size for KDF2 Key Gen.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            byte[] key = kdf2Helper.deriveKey(secret, keySize);
            kdfOutput.setText(DatatypeConverter.printHexBinary(key));
        }
        catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error trying to gen key for KDF2 Key Gen.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onKdf2Secret(boolean text, boolean hex) {
        if (!text) {
            kdfSecretText.setText("");
        }
        else {
            kdfSecretHex.setText("");
        }
        kdfSecretText.setEditable(text);
        kdfSecretHex.setEditable(hex);
    }

    private void onPbkdf2SaltGen() {
        try {
            int saltLength = Integer.parseInt(pbkdf2SaltSize.getText());
            byte[] salt = ByteUtils.getRandomBytes(saltLength);
            String saltHex = DatatypeConverter.printHexBinary(salt);
            pbkdf2SaltHex.setText(saltHex);
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error trying to generate the salt.  Please enter a valid size.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onPdkdf2Input(boolean manual, boolean file) {
        if (!manual) {
            pdkdf2InputManual.setText("");
        }
        else {
            pdkdf2InputFile.setText("");
        }
        pdkdf2InputManual.setEditable(manual);
        pdkdf2InputFile.setEditable(file);
        pdkdf2InputFileButton.setEnabled(file);
    }

    private void onPdkdf2Output(boolean manual, boolean file) {
        if (!manual) {
            pdkdf2OutputManual.setText("");
        }
        else {
            pdkdf2OutputFile.setText("");
        }
        pdkdf2OutputManual.setEditable(manual);
        pdkdf2OutputFile.setEditable(file);
        pdkdf2OutputFileButton.setEnabled(file);
    }

    private void onPdkdf2Salt(boolean text, boolean hex) {
        if (!text) {
            pbkdf2SaltText.setText("");
        }
        else {
            pbkdf2SaltHex.setText("");
        }
        pbkdf2SaltText.setEditable(text);
        pbkdf2SaltHex.setEditable(hex);
        pbkdf2SaltSize.setEditable(hex);
        pbkdf2SaltGenButton.setEnabled(hex);
    }

    private void onPrivateKeySource(boolean manual, boolean file) {
        if (!manual) {
            signPrivateKeyManual.setText("");
        }
        else {
            signPrivateKeyFile.setText("");
        }
        signPrivateKeyManual.setEditable(manual);
        signPrivateKeyFile.setEditable(file);
        signPrivateKeyFileButton.setEnabled(file);
        signPrivateKeyGenButton.setEnabled(manual);
    }

    private void onSign() {
        byte[] data = getSignData();
        PrivateKey key = getSignaturePrivateKey();
        if (data == null || key == null) {
            return;
        }
        try {
            byte[] signature;
            if (signUseECCHelperCheckBox.isSelected()) {
                signature = eccHelper.sign(key, data);
            }
            else {
                signature = signatureHelper.sign(key, data);
            }
            sign.setText(DatatypeConverter.printHexBinary(signature));
        }
        catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error trying to sign for signature.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onSignPrivateKeyGen() {
        try {
            KeyPair keyPair = keyHelper.generateKeyPair(EllipticCurve.P384);
            String privatePem = Pem.encodePrivateKey(keyPair.getPrivate());
            signPrivateKeyManual.setText(privatePem);
        }
        catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error trying to generate key for Signature.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onSymmetricKeyGenState(boolean defaultSize, boolean specified) {
        if (defaultSize) {
            keySize.setText("");
        }
        keySize.setEditable(specified);
    }

    private void onVerify() {
        byte[] data = getSignData();
        PrivateKey key = getSignaturePrivateKey();
        if (data == null || key == null) {
            return;
        }
        byte[] signature = DatatypeConverter.parseHexBinary(sign.getText());
        try {
            boolean verified;
            if (signUseECCHelperCheckBox.isSelected()) {
                verified = eccHelper.verifySignature(((OpenSSLECPrivateKey)key).getPublicKey(), data, signature);
            }
            else {
                verified = signatureHelper.verify(((OpenSSLECPrivateKey)key).getPublicKey(), data, signature);
            }
            this.verified.setText(String.valueOf(verified));
        }
        catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error trying to verify signature.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openAFile(JTextComponent component) {
        JFileChooser chooser = new JFileChooser();
        setStartingLocation(chooser, component);

        if (JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(this)) {
            File file = chooser.getSelectedFile();
            component.setText(file.toString());
            lastDirectory = file.getParentFile();
        }
    }

    private void pbkdf2Decrypt() {
        final String password = pbkdf2Password.getText();
        final String interationCountString = pbkdf2Iterations.getText();
        final boolean saltText = pdkdf2SaltTextRadioButton.isSelected();
        final String saltTextText = pbkdf2SaltText.getText();
        final String saltHexText = pbkdf2SaltHex.getText();
        final boolean manualInput = pdkdf2InputManualRadioButton.isSelected();
        final boolean manualOutput = pdkdf2OutputManualRadioButton.isSelected();
        final String manualInputText = pdkdf2InputManual.getText();
        final String fileInputText = pdkdf2InputFile.getText();
        final String fileLOutputText = pdkdf2OutputFile.getText();

        SwingWorker worker = new SwingWorker<byte[], Void>() {
            @Override
            protected byte[] doInBackground() throws Exception {
                byte[] input;
                if (manualInput) {
                    input = DatatypeConverter.parseHexBinary(manualInputText);
                }
                else {
                    input = FileUtils.readBytesFromFile(fileInputText);
                }

                int iterations = 0;
                try {
                    iterations = Integer.parseInt(interationCountString);
                }
                catch (NumberFormatException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error trying to get iteration count PBKDF2 decrypt.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }

                try {
                    byte[] output = pdkdf2Helper.decryptFromFormat(input, password, iterations);

                    if (!manualOutput) {
                        FileUtils.writeToFile(fileLOutputText, output);
                    }

                    return output;
                }
                catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error trying to encrypt PBKDF2 decrypt.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

                return null;
            }


            @Override
            protected void done() {
                if (manualOutput) {
                    try {
                        KeyManagementSampleApplication.this.pdkdf2OutputManual.setText(new String(get()));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error trying to print PBKDF2 decrypt.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

                super.done();
                stopWaitCursor();
            }
        };
        startWaitCursor();
        worker.execute();
    }

    private void pbkdf2Encrypt() {
        final String password = pbkdf2Password.getText();
        final String interationCountString = pbkdf2Iterations.getText();
        final boolean saltText = pdkdf2SaltTextRadioButton.isSelected();
        final String saltTextText = pbkdf2SaltText.getText();
        final String saltHexText = pbkdf2SaltHex.getText();
        final boolean manualInput = pdkdf2InputManualRadioButton.isSelected();
        final boolean manualOutput = pdkdf2OutputManualRadioButton.isSelected();
        final String manualInputText = pdkdf2InputManual.getText();
        final String fileInputText = pdkdf2InputFile.getText();
        final String fileLOutputText = pdkdf2OutputFile.getText();

        SwingWorker worker = new SwingWorker<byte[], Void>() {
            @Override
            protected byte[] doInBackground() throws Exception {
                byte[] salt;
                if (StringUtils.isEmpty(saltTextText) && StringUtils.isEmpty(saltHexText)) {
                    salt = new byte[0];
                }
                else if (saltText) {
                    salt = saltTextText.getBytes();
                }
                else {
                    salt = DatatypeConverter.parseHexBinary(saltHexText);
                }

                byte[] input;
                if (manualInput) {
                    input = manualInputText.getBytes();
                }
                else {
                    input = FileUtils.readBytesFromFile(fileInputText);
                }

                int iterations = 0;
                try {
                    iterations = Integer.parseInt(interationCountString);
                }
                catch (NumberFormatException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error trying to get iteration count PBKDF2 encrypt.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }

                try {
                    byte[] output = pdkdf2Helper.encryptToFormat(input, salt, password, iterations);

                    if (!manualOutput) {
                        FileUtils.writeToFile(fileLOutputText, output);
                    }

                    return output;
                }
                catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error trying to encrypt PBKDF2 encrypt.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

                return null;
            }


            @Override
            protected void done() {
                if (manualOutput) {
                    try {
                        KeyManagementSampleApplication.this.pdkdf2OutputManual.setText(DatatypeConverter.printHexBinary(get()));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error trying to print PBKDF2 encrypt.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

                super.done();
                stopWaitCursor();
            }
        };
        startWaitCursor();
        worker.execute();
    }

    private void pbkdf2GenerateKey() {
        final String password = pbkdf2Password.getText();
        final String interationCountString = pbkdf2Iterations.getText();
        final boolean saltText = pdkdf2SaltTextRadioButton.isSelected();
        final String saltTextText = pbkdf2SaltText.getText();
        final String saltHexText = pbkdf2SaltHex.getText();
        final boolean manualOutput = pdkdf2OutputManualRadioButton.isSelected();
        final String fileLOutputText = pdkdf2OutputFile.getText();

        SwingWorker worker = new SwingWorker<byte[], Void>() {
            @Override
            protected byte[] doInBackground() throws Exception {
                int iterations = 0;
                try {
                    iterations = Integer.parseInt(interationCountString);
                }
                catch (NumberFormatException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error trying to get iteration count PBKDF2 encrypt.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }

                byte[] salt;
                if (StringUtils.isEmpty(saltTextText) && StringUtils.isEmpty(saltHexText)) {
                    salt = new byte[0];
                }
                else if (saltText) {
                    salt = saltTextText.getBytes();
                }
                else {
                    salt = DatatypeConverter.parseHexBinary(saltHexText);
                }
                byte[] key = pdkdf2Helper.generateDerivedKey(password, salt, iterations);

                if (!manualOutput) {
                    FileUtils.writeToFile(fileLOutputText, key);
                }

                return key;
            }

            @Override
            protected void done() {
                if (manualOutput) {
                    try {
                        KeyManagementSampleApplication.this.pdkdf2OutputManual.setText(DatatypeConverter.printHexBinary(get()));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error trying to print PBKDF2 gen key.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

                super.done();
                stopWaitCursor();
            }
        };
        startWaitCursor();
        worker.execute();
    }

    private void saveAFile(JTextComponent component) {
        JFileChooser chooser = new JFileChooser();
        setStartingLocation(chooser, component);

        if (JFileChooser.APPROVE_OPTION == chooser.showSaveDialog(this)) {
            File file = chooser.getSelectedFile();
            component.setText(file.toString());
            lastDirectory = file.getParentFile();
        }
    }

    private void setStartingLocation(JFileChooser chooser, JTextComponent component) {
        String fileName = component.getText();
        File startingFileLocation = null;
        if (fileName.length() > 0) {
            try {
                File file = new File(fileName);
                if (file.getParentFile().exists()) {
                    startingFileLocation = file.getParentFile();
                }
            }
            catch (Exception e) {
                startingFileLocation = null;
            }
        }

        if (startingFileLocation == null && lastDirectory != null) {
            startingFileLocation = lastDirectory;
        }

        if (startingFileLocation != null) {
            chooser.setCurrentDirectory(startingFileLocation);
        }
    }

    private void setUpAsymmetricTab() {
        try {
            keyHelper = new KeyPairHelper(KeyPairHelper.KeyAlgorithm.EC);
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error trying to create KeyPairHelper.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        asymmetricPublicHere.setLineWrap(true);
        asymmetricPrivateHere.setLineWrap(true);
        ButtonGroup publicButtonGroup = new ButtonGroup();
        publicButtonGroup.add(asymmetricPublicHereRadioButton);
        publicButtonGroup.add(asymmetricPublicFileRadioButton);
        asymmetricPublicHereRadioButton.setSelected(true);
        onAsymmetricPublic(true, false);
        asymmetricPublicHereRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAsymmetricPublic(true, false);
            }
        });
        asymmetricPublicFileRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAsymmetricPublic(false, true);
            }
        });
        ButtonGroup privateButtonGroup = new ButtonGroup();
        privateButtonGroup.add(asymmetricPrivateHereRadioButton);
        privateButtonGroup.add(asymmetricPrivateFileRadioButton);
        asymmetricPrivateHereRadioButton.setSelected(true);
        onAsymmetricPrivate(true, false);
        asymmetricPrivateHereRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAsymmetricPrivate(true, false);
            }
        });
        asymmetricPrivateFileRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAsymmetricPrivate(false, true);
            }
        });
        asymmetricGenerateKeysButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                asymmetricGenerateKeys();
            }
        });
        asymmetricPrivateFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAFile(asymmetricPrivateFile);
            }
        });
        asymmetricPublicFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAFile(asymmetricPublicFile);
            }
        });
    }

    private void setUpEcdhTab() {
        try {
            keyAgreementHelper = new KeyAgreementHelper(KeyAgreementHelper.KeyAgreementAlgorithm.ECDH);
        }
        catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error trying to create KeyAgreementHelper for ECDH.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        ButtonGroup publicButtonGroup = new ButtonGroup();
        publicButtonGroup.add(ecdhAliceManualRadioButton);
        publicButtonGroup.add(ecdhAliceFileRadioButton);
        ecdhAliceManualRadioButton.setSelected(true);
        onEcdhAlice(true, false);
        ecdhAliceManualRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onEcdhAlice(true, false);
            }
        });
        ecdhAliceFileRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onEcdhAlice(false, true);
            }
        });
        ButtonGroup privateButtonGroup = new ButtonGroup();
        privateButtonGroup.add(ecdhBobManualRadioButton);
        privateButtonGroup.add(ecdhBobFileRadioButton);
        ecdhBobManualRadioButton.setSelected(true);
        onEcdhBob(true, false);
        ecdhBobManualRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onEcdhBob(true, false);
            }
        });
        ecdhBobFileRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onEcdhBob(false, true);
            }
        });
        ecdhButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ecdhGenerate();
            }
        });
        ecdhBobGenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateKeyPem(ecdhBobManual, false);
            }
        });
        ecdhAliceGenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateKeyPem(ecdhAliceManual, true);
            }
        });
        ecdhAliceFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAFile(ecdhAliceFile);
            }
        });
        ecdhBobFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAFile(ecdhBobFile);
            }
        });
    }

    private void setUpHashTab() {
        try {
            messageDigestHelper = new MessageDigestHelper();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        hashButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                computeHash();
            }
        });
    }

    private void setUpHmacTab() {
        try {
            macHelper = new MacHelper();
        }
        catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error trying to create MacHelper for HMAC.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        hmacKeyGenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                genHmacKey();
            }
        });
        hmacDigestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                genHMacDigest();
            }
        });
        hmacVerifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onHmacVerify();
            }
        });
    }

    private void setUpIESTab() {
        try {
            keyDecoder = new ECKeyDecoder();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error trying to create ECKeyDecoder.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            iesHelper = new AbsioIESHelper();
        }
        catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error trying to create AbsioIESHelper.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Input
        ButtonGroup inputButtonGroup = new ButtonGroup();
        inputButtonGroup.add(iesInputTextRadioButton);
        inputButtonGroup.add(iesInputFileRadioButton);
        iesInputTextRadioButton.setSelected(true);
        onIesInput(true, false);
        iesInputTextRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onIesInput(true, false);
            }
        });
        iesInputFileRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onIesInput(false, true);
            }
        });
        iesInputFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAFile(iesInputFile);
            }
        });

        // Signing Private Key
        ButtonGroup signingPrivateKeyButtonGroup = new ButtonGroup();
        signingPrivateKeyButtonGroup.add(iesSigningPrivateKeyTextRadioButton);
        signingPrivateKeyButtonGroup.add(iesSigningPrivateKeyPEMFileRadioButton);
        iesSigningPrivateKeyTextRadioButton.setSelected(true);
        onIesSigningPrivateKey(true, false);
        iesSigningPrivateKeyTextRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onIesSigningPrivateKey(true, false);
            }
        });
        iesSigningPrivateKeyPEMFileRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onIesSigningPrivateKey(false, true);
            }
        });
        iesPrivateKeyFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAFile(iesSigningPrivateKeyPemFile);
            }
        });

        // Signing Private Key
        ButtonGroup derivationPublicKeyButtonGroup = new ButtonGroup();
        derivationPublicKeyButtonGroup.add(iesDerivationPublicKeyTextRadioButton);
        derivationPublicKeyButtonGroup.add(iesDerivationPublicKeyPEMFileRadioButton);
        iesDerivationPublicKeyTextRadioButton.setSelected(true);
        onIesDerivationPublicKey(true, false);
        iesDerivationPublicKeyTextRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onIesDerivationPublicKey(true, false);
            }
        });
        iesDerivationPublicKeyPEMFileRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onIesDerivationPublicKey(false, true);
            }
        });
        iesPublicKeyFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAFile(iesDerivationPublicKeyPemFile);
            }
        });

        // IDs
        iesSenderIdGenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                genGuid(iesSenderId);
            }
        });
        iesObjectIdGenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                genGuid(iesObjectId);
            }
        });

        // Output
        ButtonGroup outputButtonGroup = new ButtonGroup();
        outputButtonGroup.add(iesOutputTextRadioButton);
        outputButtonGroup.add(iesOutputFileRadioButton);
        iesOutputTextRadioButton.setSelected(true);
        onIesOutput(true, false);
        iesOutputTextRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onIesOutput(true, false);
            }
        });
        iesOutputFileRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onIesOutput(false, true);
            }
        });
        iesOutputFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAFile(iesOutputFile);
            }
        });

        // Action
        iesEncryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onIesEncrypt();
            }

        });
        iesDecryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onIesDecrypt();
            }
        });
    }

    private void setUpKDF2Tab() {
        try {
            kdf2Helper = new KDF2Helper();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error trying to create KDF2Helper.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        ButtonGroup inputButtonGroup = new ButtonGroup();
        inputButtonGroup.add(kdfSecretTextRadioButton);
        inputButtonGroup.add(kdfSecretHexRadioButton);
        kdfSecretTextRadioButton.setSelected(true);
        onKdf2Secret(true, false);
        kdfSecretTextRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onKdf2Secret(true, false);
            }
        });
        kdfSecretHexRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onKdf2Secret(false, true);
            }
        });
        kdfGenKeyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onKdf2GenKey();
            }
        });
    }

    private void setUpPBKDF2Tab() {
        try {
            pdkdf2Helper = new PBKDF2Helper();
        }
        catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error trying to create PBKDF2Helper.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        ButtonGroup saltButtonGroup = new ButtonGroup();
        saltButtonGroup.add(pdkdf2SaltTextRadioButton);
        saltButtonGroup.add(pdkdf2SaltHexRadioButton);
        pdkdf2SaltTextRadioButton.setSelected(true);
        onPdkdf2Salt(true, false);
        pdkdf2SaltTextRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onPdkdf2Salt(true, false);
            }
        });
        pdkdf2SaltHexRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onPdkdf2Salt(false, true);
            }
        });
        pbkdf2SaltGenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onPbkdf2SaltGen();
            }
        });

        ButtonGroup inputButtonGroup = new ButtonGroup();
        inputButtonGroup.add(pdkdf2InputManualRadioButton);
        inputButtonGroup.add(pdkdf2InputFileRadioButton);
        pdkdf2InputManualRadioButton.setSelected(true);
        onPdkdf2Input(true, false);
        pdkdf2InputManualRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onPdkdf2Input(true, false);
            }
        });
        pdkdf2InputFileRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onPdkdf2Input(false, true);
            }
        });
        pdkdf2InputFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAFile(pdkdf2InputFile);
            }
        });

        ButtonGroup outputButtonGroup = new ButtonGroup();
        outputButtonGroup.add(pdkdf2OutputManualRadioButton);
        outputButtonGroup.add(pdkdf2OutputFileRadioButton);
        pdkdf2OutputManualRadioButton.setSelected(true);
        onPdkdf2Output(true, false);
        pdkdf2OutputManualRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onPdkdf2Output(true, false);
            }
        });
        pdkdf2OutputFileRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onPdkdf2Output(false, true);
            }
        });
        pdkdf2OutputFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAFile(pdkdf2OutputFile);
            }
        });

        pdkdf2EncryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pbkdf2Encrypt();
            }
        });

        pdkdf2DecryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pbkdf2Decrypt();
            }
        });

        pbkdf2GenerateKeyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pbkdf2GenerateKey();
            }
        });
    }

    private void setUpSignatureTab() {
        try {
            signatureHelper = new SignatureHelper();
        }
        catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error trying to create SignatureHelper for Signature.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        ButtonGroup privateKeyButtonGroup = new ButtonGroup();
        privateKeyButtonGroup.add(signPrivateKeyManualRadioButton);
        privateKeyButtonGroup.add(signPrivateKeyFileRadioButton);
        signPrivateKeyManualRadioButton.setSelected(true);
        onPrivateKeySource(true, false);
        signPrivateKeyManual.setLineWrap(true);
        signPrivateKeyManualRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onPrivateKeySource(true, false);
            }
        });
        signPrivateKeyFileRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onPrivateKeySource(false, true);
            }
        });
        signPrivateKeyGenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSignPrivateKeyGen();
            }
        });
        signPrivateKeyFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAFile(signPrivateKeyFile);
            }
        });

        signButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSign();
            }
        });

        verifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onVerify();
            }
        });
    }

    private void setUpSymmetricDecrypt() {
        //  Decrypt Section
        ButtonGroup decryptInputButtonGroup = new ButtonGroup();
        decryptInputButtonGroup.add(decryptInputHexRadioButton);
        decryptInputButtonGroup.add(decryptInputFileRadioButton);
        decryptInputHexRadioButton.setSelected(true);
        onDecryptInputText();

        ButtonGroup decryptOutputButtonGroup = new ButtonGroup();
        decryptOutputButtonGroup.add(decryptOutputFileRadioButton);
        decryptOutputButtonGroup.add(decryptOutputTextRadioButton);
        decryptOutputTextRadioButton.setSelected(true);
        onDecryptOutputText();

        decryptInputHexRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onDecryptInputText();
            }
        });
        decryptInputFileRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onDecryptInputFile();
            }
        });
        decryptOutputTextRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onDecryptOutputText();
            }
        });
        decryptOutputFileRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onDecryptOutputFile();
            }
        });
        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                symmetricDecrypt();
            }
        });
        decryptInputFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAFile(decryptInputFile);
            }
        });
        decryptOutputFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAFile(decryptOutputFile);
            }
        });
    }

    private void setUpSymmetricEncrypt() {
        // Encrypt Section
        ButtonGroup encryptKeyButtonGroup = new ButtonGroup();
        encryptKeyButtonGroup.add(encryptKeyRandomRadioButton);
        encryptKeyButtonGroup.add(encryptKeyHexRadioButton);
        encryptKeyHexRadioButton.setSelected(true);
        onEncryptHexKey();

        ButtonGroup encryptIvButtonGroup = new ButtonGroup();
        encryptIvButtonGroup.add(encryptIvRandomRadioButton);
        encryptIvButtonGroup.add(encryptIvHexRadioButton);
        encryptIvHexRadioButton.setSelected(true);
        onEncryptHexIv();

        ButtonGroup encryptInputButtonGroup = new ButtonGroup();
        encryptInputButtonGroup.add(encryptInputTextRadioButton);
        encryptInputButtonGroup.add(encryptInputFileRadioButton);
        encryptInputTextRadioButton.setSelected(true);
        onEncryptInputText();

        ButtonGroup encryptOutputButtonGroup = new ButtonGroup();
        encryptOutputButtonGroup.add(encryptOutputFileRadioButton);
        encryptOutputButtonGroup.add(encryptOutputHexRadioButton);
        encryptOutputHexRadioButton.setSelected(true);
        onEncryptOutputText();

        encryptKeyRandomRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onEncryptRandomKey();
            }
        });
        encryptKeyHexRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onEncryptHexKey();
            }
        });
        encryptIvRandomRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onEncryptRandomIv();
            }
        });
        encryptIvHexRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onEncryptHexIv();
            }
        });
        encryptInputTextRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onEncryptInputText();
            }
        });
        encryptInputFileRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onEncryptInputFile();
            }
        });
        encryptOutputHexRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onEncryptOutputText();
            }
        });
        encryptOutputFileRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onEncryptOutputFile();
            }
        });
        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                symmetricEncrypt();
            }
        });
        encryptInputFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAFile(encryptInputFile);
            }
        });
        encryptOutputFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAFile(encryptOutputFile);
            }
        });
    }

    private void setUpSymmetricKeyGen() {
        // Gen Key Section
        ButtonGroup genKeyGroup = new ButtonGroup();
        genKeyGroup.add(defaultSizeRadioButton);
        genKeyGroup.add(specifiedSizeRadioButton);
        defaultSizeRadioButton.setSelected(true);
        genKeyHexButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onGenKeyHex();
            }
        });
        defaultSizeRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSymmetricKeyGenState(true, false);
            }
        });
        specifiedSizeRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSymmetricKeyGenState(false, true);
            }
        });
    }

    private void setUpSymmetricTab() {
        keyGenPanel.setBorder(new TitledBorder("Key Generation"));
        encryptPanel.setBorder(new TitledBorder("Encryption"));
        decryptPanel.setBorder(new TitledBorder("Decryption"));
        try {
            cipherHelper = new CipherHelper();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        setUpSymmetricKeyGen();
        setUpSymmetricEncrypt();
        setUpSymmetricDecrypt();
    }

    public void startWaitCursor() {
        RootPaneContainer root = this;
        root.getGlassPane().setCursor(WAIT_CURSOR);
        root.getGlassPane().addMouseListener(mouseAdapter);
        root.getGlassPane().setVisible(true);
    }

    public void stopWaitCursor() {
        RootPaneContainer root = this;
        root.getGlassPane().setCursor(DEFAULT_CURSOR);
        root.getGlassPane().removeMouseListener(mouseAdapter);
        root.getGlassPane().setVisible(false);
    }

    private void symmetricDecrypt() {
        final String decryptKeyHexText = decryptKeyHex.getText();
        final String decryptIvHexText = decryptIvHex.getText();
        final boolean decryptInputHexRadioButtonSelected = decryptInputHexRadioButton.isSelected();
        final String decryptInputHexText = decryptInputHex.getText();
        final String decryptInputFileText = decryptInputFile.getText();
        final boolean decryptOutputFileRadioButtonSelected = decryptOutputFileRadioButton.isSelected();
        final String decryptOutputFileText = decryptOutputFile.getText();
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() {
                try {
                    byte[] key = DatatypeConverter.parseHexBinary(decryptKeyHexText);
                    byte[] iv = DatatypeConverter.parseHexBinary(decryptIvHexText);
                    byte[] ciphertext;
                    if (decryptInputHexRadioButtonSelected) {
                        ciphertext = DatatypeConverter.parseHexBinary(decryptInputHexText);
                    }
                    else {
                        ciphertext = FileUtils.readBytesFromFile(decryptInputFileText);
                    }

                    final byte[] data = cipherHelper.decrypt(key, iv, ciphertext);
                    if (decryptOutputFileRadioButtonSelected) {
                        FileUtils.writeToFile(decryptOutputFileText, data);
                    }
                    else {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                decryptOutputText.setText(new String(data));
                            }
                        });
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error decrypting.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

                return null;
            }

            @Override
            protected void done() {
                super.done();
                stopWaitCursor();
            }
        };
        startWaitCursor();
        worker.execute();
    }

    private void symmetricEncrypt() {
        final String keyText = enryptKeyHexInput.getText();
        final String ivString = encryptIvHex.getText();
        final boolean encryptInputTextRadioButtonSelected = encryptInputTextRadioButton.isSelected();
        final String encryptedInputTextString = encryptInputText.getText();
        final boolean encryptOutputFileRadioButtonSelected = encryptOutputFileRadioButton.isSelected();
        final String encryptOutputFileText = encryptOutputFile.getText();

        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() {
                try {
                    byte[] key = DatatypeConverter.parseHexBinary(keyText);
                    byte[] iv = DatatypeConverter.parseHexBinary(ivString);
                    byte[] data;
                    if (encryptInputTextRadioButtonSelected) {
                        data = encryptedInputTextString.getBytes();
                    }
                    else {
                        data = FileUtils.readBytesFromFile(encryptInputFile.getText());
                    }

                    final byte[] encrypted = cipherHelper.encrypt(key, iv, data);
                    if (encryptOutputFileRadioButtonSelected) {
                        FileUtils.writeToFile(encryptOutputFileText, encrypted);
                    }
                    else {
                        final String printHexBinary = DatatypeConverter.printHexBinary(encrypted);
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                encryptOutputHex.setText(printHexBinary);
                            }
                        });
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(KeyManagementSampleApplication.this, "Error encrypting.\n\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

                return null;
            }

            @Override
            protected void done() {
                super.done();
                stopWaitCursor();
            }
        };
        startWaitCursor();
        worker.execute();
    }

}
