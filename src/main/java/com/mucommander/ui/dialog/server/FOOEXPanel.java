package com.mucommander.ui.dialog.server;

import com.mucommander.commons.file.Credentials;
import com.mucommander.commons.file.FileURL;
import com.mucommander.commons.file.protocol.FileProtocols;
import com.mucommander.text.Translator;
import com.mucommander.ui.main.MainFrame;

import javax.swing.*;
import java.net.MalformedURLException;

public class FOOEXPanel extends ServerPanel {

    private JTextField urlField;

    private static String lastURL = "http://";

    protected FOOEXPanel(ServerConnectDialog dialog, MainFrame mainFrame) {
        super(dialog, mainFrame);

        urlField = new JTextField(lastURL);
        urlField.selectAll();
        addTextFieldListeners(urlField, true);
        //addRow(Translator.get("server_connect_dialog.http_url"), urlField, 20);
        addRow("WS URL", urlField, 20);
    }

    private void updateValues() {
        lastURL = urlField.getText();
    }

    @Override
    FileURL getServerURL() throws MalformedURLException {
        updateValues();

        if(!(lastURL.toLowerCase().startsWith(FileProtocols.HTTP+"://") || lastURL.toLowerCase().startsWith(FileProtocols.HTTPS+"://")))
            lastURL = FileProtocols.HTTP+"://"+lastURL;

        FileURL fileURL = FileURL.getFileURL(lastURL);

        return fileURL;
    }

    @Override
    boolean usesCredentials() {
        return false;
    }

    @Override
    void dialogValidated() {
        updateValues();
    }
}
