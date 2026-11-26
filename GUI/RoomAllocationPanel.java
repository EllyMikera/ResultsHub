import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class RoomAllocationPanel extends JPanel {
    public RoomAllocationPanel() {
        super(new BorderLayout());
        add(new JLabel("Departmental Room Allocations", SwingConstants.CENTER), BorderLayout.NORTH);
        add(new JLabel("Room allocation data will be displayed here.", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}
