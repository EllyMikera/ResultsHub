import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class HoDPanel extends JPanel {
    private static final String TIMETABLE_CARD = "timetable";
    private static final String ROOM_ALLOCATION_CARD = "roomAllocations";

    public HoDPanel() {
        super(new BorderLayout());
        setPreferredSize(new Dimension(1120, 720));
        setBackground(Color.WHITE);

        CardLayout cardLayout = new CardLayout();
        JPanel content = new JPanel(cardLayout);
        content.setBorder(new EmptyBorder(0, 0, 0, 0));
        content.add(new CourseUnitTimetablePanel(), TIMETABLE_CARD);
        content.add(new RoomAllocationPanel(), ROOM_ALLOCATION_CARD);

        JButton timetableButton = new JButton();
        timetableButton.addActionListener(event -> cardLayout.show(content, TIMETABLE_CARD));
        JButton roomAllocationButton = new JButton();
        roomAllocationButton.addActionListener(event -> cardLayout.show(content, ROOM_ALLOCATION_CARD));

        JPanel shell = new JPanel(new BorderLayout());
        shell.add(HoDShellComponents.createInstitutionHeader(), BorderLayout.NORTH);

        JPanel mainRow = new JPanel(new BorderLayout());
        mainRow.add(HoDShellComponents.createSidebar(timetableButton, roomAllocationButton), BorderLayout.WEST);
        mainRow.add(content, BorderLayout.CENTER);
        shell.add(mainRow, BorderLayout.CENTER);
        shell.add(HoDShellComponents.createStatusBar(), BorderLayout.SOUTH);

        add(shell, BorderLayout.CENTER);
    }
}
