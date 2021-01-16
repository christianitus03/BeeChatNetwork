package templenet.desktop;

import com.digi.xbee.api.DigiMeshDevice;
import com.digi.xbee.api.DigiMeshNetwork;
import com.digi.xbee.api.RemoteDigiMeshDevice;
import templenet.DeviceHandler;
import templenet.DeviceManager;
import templenet.MainKt;

import javax.swing.*;
import java.util.HashMap;

public class MainMenu {
    private JPanel panel1;
    public JTabbedPane tabbedPane;
    private HashMap<RemoteDigiMeshDevice, ChatPane> devicePaneHashMap = new HashMap<>();
    public JFrame frame = new JFrame("TempleNet");



    DigiMeshDevice localDevice;
    DigiMeshNetwork network;

    public MainMenu(DigiMeshDevice localDevice, DigiMeshNetwork network) {
        this.localDevice = localDevice;
        this.network = network;

        DeviceManager.initialize(localDevice, network);

        for (var device: MainKt.toDigiMesh(network.getDevices())) { // TODO: 1/16/21 Maybe put this in MainKT or something?
            DeviceManager.getDeviceHandlerHashMap().put(device, new DeviceHandler(device, json -> {
                var pane = devicePaneHashMap.get(device);
                System.out.println(json.toJson());
                if (json.containsKey("TEXT")) {
                    pane.addMessage(device.getNodeID() + ": " + json.get("TEXT"));
                }
            }
            ));
        }

        updateTabbedPane();

        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    void updateTabbedPane() {
        var devices = MainKt.toDigiMesh(network.getDevices());

        for (var device: devices) {
            if (!devicePaneHashMap.containsKey(device)) {
                devicePaneHashMap.put(device, new ChatPane(device));
                tabbedPane.add(devicePaneHashMap.get(device));
            }
        }
    }
}
