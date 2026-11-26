import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class CourseUnitTimetablePanel extends JPanel {
    public CourseUnitTimetablePanel() {
        super(new BorderLayout());
        add(new JLabel("Departmental Course-Unit Timetables", SwingConstants.CENTER), BorderLayout.NORTH);
        add(new JLabel("Timetable data will be displayed here.", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}
