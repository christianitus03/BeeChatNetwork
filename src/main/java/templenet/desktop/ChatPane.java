package templenet.desktop;

import com.digi.xbee.api.RemoteDigiMeshDevice;
import com.github.cliftonlabs.json_simple.JsonObject;
import templenet.DeviceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class ChatPane extends Panel {
    private JList<String> list = new JList<>();
    private Vector<String> listData = new Vector<>();
    private JTextField textField;
    private JButton fileButton = new JButton("File...");

    public RemoteDigiMeshDevice remoteDevice;

    public ChatPane(RemoteDigiMeshDevice remoteDevice) {
        this.remoteDevice = remoteDevice;

        initializeLayout();

        textField.addActionListener(actionEvent -> {
            addMessage(textField.getText());
            var json = new JsonObject();
            json.put("TEXT", textField.getText());
            DeviceManager.sendData(remoteDevice, json);
            textField.setText("");
        });

    }

    public void addMessage(String text) {
        listData.add(text);
        list.setListData(listData);
    }

    private void initializeLayout() {
        setLayout(new GridBagLayout());
        final JScrollPane scrollPane1 = new JScrollPane();
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 5.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(scrollPane1, gbc);
        scrollPane1.setViewportView(list);
        textField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(textField, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.1;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(fileButton, gbc);
    }

    @Override
    public String getName() {
        return remoteDevice.getNodeID();
    }
}
