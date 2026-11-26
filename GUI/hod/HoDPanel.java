import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class HoDPanel extends JPanel {
    private static final String TIMETABLE_CARD = "timetable";
    private static final String ROOM_ALLOCATION_CARD = "roomAllocations";

    public HoDPanel() {
        super(new BorderLayout());

        CardLayout cardLayout = new CardLayout();
        JPanel content = new JPanel(cardLayout);
        content.add(new CourseUnitTimetablePanel(), TIMETABLE_CARD);
        content.add(new RoomAllocationPanel(), ROOM_ALLOCATION_CARD);

        JPanel navigation = new JPanel();
        JButton timetableButton = new JButton("Course-Unit Timetables");
        timetableButton.addActionListener(event -> cardLayout.show(content, TIMETABLE_CARD));
        JButton roomAllocationButton = new JButton("Room Allocations");
        roomAllocationButton.addActionListener(event -> cardLayout.show(content, ROOM_ALLOCATION_CARD));

        navigation.add(timetableButton);
        navigation.add(roomAllocationButton);
        add(navigation, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
    }
}
