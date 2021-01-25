import com.rapidminer.gui.RapidMinerGUI;
import com.rapidminer.gui.ToolbarGUIStartupListener;
import com.rapidminer.tools.PlatformUtilities;

import java.nio.file.Paths;

class GuiLauncher {
    public static void main(String args[]) throws Exception {
        System.setProperty(PlatformUtilities.PROPERTY_RAPIDMINER_HOME, Paths.get("").toAbsolutePath().toString());
        RapidMinerGUI.registerStartupListener(new ToolbarGUIStartupListener());
        RapidMinerGUI.main(args);
    }
}
