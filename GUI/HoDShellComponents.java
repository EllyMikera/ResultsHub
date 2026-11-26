import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public final class HoDShellComponents {
    static final Color INSTITUTION_BLUE = new Color(31, 78, 121);
    static final Color ACTIVE_BLUE = new Color(218, 230, 242);
    static final Color BORDER = new Color(205, 210, 216);
    static final Color PANEL_BACKGROUND = new Color(248, 249, 251);
    static final Color NOTICE_BLUE = new Color(236, 242, 248);
    static final Color STATUS_GREEN = new Color(39, 139, 92);

    private HoDShellComponents() {
    }

    static JPanel createSidebar(JButton timetableButton, JButton roomAllocationButton) {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(186, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER));
        sidebar.setBackground(Color.WHITE);

        JPanel identity = new JPanel();
        identity.setLayout(new BoxLayout(identity, BoxLayout.X_AXIS));
        identity.setBorder(BorderFactory.createEmptyBorder(9, 10, 9, 10));
        identity.setBackground(Color.WHITE);

        JLabel mark = new JLabel("R");
        mark.setPreferredSize(new Dimension(30, 30));
        mark.setMaximumSize(new Dimension(30, 30));
        mark.setOpaque(true);
        mark.setBackground(INSTITUTION_BLUE);
        mark.setForeground(Color.WHITE);
        mark.setFont(mark.getFont().deriveFont(Font.BOLD, 13f));
        mark.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel title = new JPanel();
        title.setOpaque(false);
        title.setLayout(new BoxLayout(title, BoxLayout.Y_AXIS));
        title.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
        title.add(makeLabel("ResultsHub", Font.BOLD, 12));
        title.add(makeLabel("Academic Portal", Font.PLAIN, 8));

        identity.add(mark);
        identity.add(title);

        JPanel navigation = new JPanel();
        navigation.setOpaque(false);
        navigation.setLayout(new BoxLayout(navigation, BoxLayout.Y_AXIS));
        navigation.setBorder(BorderFactory.createEmptyBorder(7, 8, 0, 8));

        navigation.add(makeSectionLabel("ADMINISTRATOR"));
        navigation.add(makeSideButton("Access Rights"));
        navigation.add(makeSideButton("Master Data Entry"));
        navigation.add(Box.createVerticalStrut(12));
        navigation.add(makeSectionLabel("STUDENT MODULE"));
        navigation.add(makeSideButton("View Own Results"));
        navigation.add(makeSideButton("Test & Exam Timetable"));
        navigation.add(makeSideButton("Submit Complaint"));
        navigation.add(Box.createVerticalStrut(12));
        navigation.add(makeSectionLabel("HEAD OF DEPARTMENT"));

        styleNavigationButton(timetableButton, true);
        styleNavigationButton(roomAllocationButton, false);
        timetableButton.setText("Course-Unit Timetables");
        roomAllocationButton.setText("Room Allocations");
        navigation.add(timetableButton);
        navigation.add(roomAllocationButton);
        navigation.add(Box.createVerticalStrut(12));
        navigation.add(makeSectionLabel("STAFF MODULE"));
        navigation.add(makeSideButton("Results Management"));
        navigation.add(makeSideButton("Faculty Timetables"));
        navigation.add(Box.createVerticalStrut(12));
        navigation.add(makeSectionLabel("SYSTEM ALLOCATION"));
        navigation.add(makeSideButton("Exam Seat Assignment"));

        sidebar.add(identity, BorderLayout.NORTH);
        sidebar.add(navigation, BorderLayout.CENTER);
        return sidebar;
    }

    static JPanel createInstitutionHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(0, 48));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
        header.setBackground(Color.WHITE);

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.X_AXIS));
        left.setBorder(BorderFactory.createEmptyBorder(7, 12, 7, 8));
        JLabel appMark = new JLabel("[R]");
        appMark.setFont(appMark.getFont().deriveFont(Font.BOLD, 12f));
        appMark.setForeground(INSTITUTION_BLUE);
        left.add(appMark);
        left.add(Box.createHorizontalStrut(8));
        JPanel appName = new JPanel();
        appName.setOpaque(false);
        appName.setLayout(new BoxLayout(appName, BoxLayout.Y_AXIS));
        appName.add(makeLabel("ResultsHub", Font.BOLD, 11));
        appName.add(makeLabel("[Kenyan University Institution Name]", Font.PLAIN, 8));
        left.add(appName);

        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setLayout(new BoxLayout(right, BoxLayout.X_AXIS));
        right.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 10));
        right.add(makeLabel("HoD - Computer Science & IT (DEPT-01)", Font.PLAIN, 8));
        right.add(Box.createHorizontalStrut(8));

        JLabel initials = new JLabel("JK");
        initials.setPreferredSize(new Dimension(30, 30));
        initials.setMaximumSize(new Dimension(30, 30));
        initials.setOpaque(true);
        initials.setBackground(new Color(214, 219, 227));
        initials.setHorizontalAlignment(SwingConstants.CENTER);
        initials.setFont(initials.getFont().deriveFont(Font.BOLD, 9f));
        right.add(initials);
        right.add(Box.createHorizontalStrut(8));
        JButton signOut = new JButton("Sign Out");
        signOut.setFocusable(false);
        signOut.setMargin(new java.awt.Insets(4, 7, 4, 7));
        right.add(signOut);

        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);
        return header;
    }

    static JPanel createContextBanner(String title, String scope, String department, String authenticatedUser) {
        JPanel banner = new JPanel(new BorderLayout());
        banner.setBackground(PANEL_BACKGROUND);
        banner.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)));

        JLabel heading = new JLabel("[Calendar]  " + title + "   [" + scope + "]");
        heading.setFont(heading.getFont().deriveFont(Font.BOLD, 12f));
        heading.setForeground(new Color(35, 43, 54));

        JPanel contextRow = new JPanel(new BorderLayout());
        contextRow.setOpaque(false);
        contextRow.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));
        JLabel departmentLabel = new JLabel("Department:  " + department);
        departmentLabel.setFont(departmentLabel.getFont().deriveFont(Font.PLAIN, 9f));
        JLabel userLabel = new JLabel("Authenticated HoD:  " + authenticatedUser);
        userLabel.setFont(userLabel.getFont().deriveFont(Font.PLAIN, 9f));

        contextRow.add(departmentLabel, BorderLayout.WEST);
        contextRow.add(userLabel, BorderLayout.EAST);

        banner.add(heading, BorderLayout.NORTH);
        banner.add(contextRow, BorderLayout.SOUTH);
        return banner;
    }

    static JPanel createReadOnlyNotice(String text) {
        JPanel notice = new JPanel(new BorderLayout());
        notice.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(205, 216, 229)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        notice.setBackground(NOTICE_BLUE);
        JLabel label = new JLabel("[i]  " + text);
        label.setFont(label.getFont().deriveFont(Font.PLAIN, 8f));
        notice.add(label, BorderLayout.WEST);
        return notice;
    }

    static JPanel createStatusBar() {
        JPanel status = new JPanel(new BorderLayout());
        status.setPreferredSize(new Dimension(0, 22));
        status.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER));
        status.setBackground(new Color(249, 250, 252));

        JLabel left = new JLabel("  Ready | 6 records loaded | Active Shililng: 2024/2025 Semester 1");
        left.setFont(left.getFont().deriveFont(Font.PLAIN, 8f));
        left.setForeground(new Color(55, 65, 75));
        JLabel right = new JLabel("Java Swing Desktop View | ResultsHub  ");
        right.setFont(right.getFont().deriveFont(Font.PLAIN, 8f));
        right.setForeground(new Color(95, 105, 115));

        status.add(left, BorderLayout.WEST);
        status.add(right, BorderLayout.EAST);
        return status;
    }

    static JPanel createFooter(String leftText, JButton printButton, JButton exportButton) {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setPreferredSize(new Dimension(0, 50));
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER));
        footer.setBackground(Color.WHITE);

        JPanel left = new JPanel(new BorderLayout());
        left.setOpaque(false);
        left.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 8));
        JLabel count = new JLabel(leftText);
        count.setFont(count.getFont().deriveFont(Font.PLAIN, 8f));
        JLabel department = new JLabel("Home Department: DEPT-01 Computer Science & IT");
        department.setFont(department.getFont().deriveFont(Font.BOLD, 8f));
        left.add(count, BorderLayout.WEST);
        left.add(department, BorderLayout.EAST);

        JPanel actions = new JPanel();
        actions.setOpaque(false);
        actions.setBorder(BorderFactory.createEmptyBorder(7, 8, 7, 8));
        actions.add(printButton);
        actions.add(exportButton);

        footer.add(left, BorderLayout.CENTER);
        footer.add(actions, BorderLayout.EAST);
        return footer;
    }

    private static JLabel makeLabel(String text, int style, float size) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(style, size));
        return label;
    }

    private static JLabel makeSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 7f));
        label.setForeground(new Color(120, 128, 136));
        label.setBorder(BorderFactory.createEmptyBorder(4, 7, 3, 2));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setMaximumSize(new Dimension(Integer.MAX_VALUE, 18));
        return label;
    }

    private static JButton makeSideButton(String text) {
        JButton button = new JButton("  " + text);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(button.getFont().deriveFont(Font.PLAIN, 9f));
        button.setForeground(new Color(70, 78, 88));
        button.setBackground(Color.WHITE);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(5, 7, 5, 7));
        button.setFocusPainted(false);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
        return button;
    }

    private static void styleNavigationButton(JButton button, boolean active) {
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(button.getFont().deriveFont(Font.BOLD, 9f));
        button.setForeground(active ? INSTITUTION_BLUE : new Color(70, 78, 88));
        button.setBackground(active ? ACTIVE_BLUE : Color.WHITE);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, active ? 3 : 0, 0, 0,
                active ? INSTITUTION_BLUE : Color.WHITE),
            BorderFactory.createEmptyBorder(5, 7, 5, 7)));
        button.setFocusPainted(false);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 27));
    }
}
