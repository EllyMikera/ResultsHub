import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

public class CourseUnitTimetablePanel extends JPanel {
    private static final String SEARCH_PLACEHOLDER = "Filter code or name";

    private final JComboBox<String> courseFilter = new JComboBox<>();
    private final JComboBox<String> dateFilter = new JComboBox<>();
    private final JComboBox<String> venueFilter = new JComboBox<>();
    private final JTextField searchField = new JTextField();
    private final JButton applyButton = new JButton("Apply Filter");
    private final JButton resetButton = new JButton("Reset");
    private final JButton refreshButton = makeIconButton("Refresh");
    private final JButton printButton = new JButton("Print Timetable");
    private final JButton exportButton = new JButton("Export Schedule (Text)");
    private final JLabel countLabel = new JLabel();
    private final JLabel footerDepartment = new JLabel();
    private final JPanel noticePanel;
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final TableRowSorter<DefaultTableModel> rowSorter;

    private volatile boolean loading;

    public CourseUnitTimetablePanel() {
        super(new BorderLayout());
        setBackground(HoDShellComponents.PANEL_BACKGROUND);

        JPanel context = HoDShellComponents.createContextBanner(
            "Departmental Course-Unit Timetables",
            "DEPT-01 Scope",
            "Department: Computer Science & IT (DEPT-01)",
            "Prof. J. Kamau");
        JPanel body = new JPanel(new BorderLayout());
        body.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        body.setBackground(HoDShellComponents.PANEL_BACKGROUND);

        JPanel topControls = new JPanel(new BorderLayout());
        topControls.setOpaque(false);

        noticePanel = HoDShellComponents.createReadOnlyNotice(
            "Read-only timetable joined from CourseUnit, RoomAllocation and Room. " +
            "Demo department data is for UI testing only.");
        topControls.add(noticePanel, BorderLayout.NORTH);
        topControls.add(createFilterToolbar(), BorderLayout.CENTER);
        body.add(topControls, BorderLayout.NORTH);

        tableModel = createTableModel();
        table = createTable(tableModel);
        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);
        loadDemoData();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(HoDShellComponents.BORDER));
        scrollPane.getViewport().setBackground(Color.WHITE);
        body.add(scrollPane, BorderLayout.CENTER);

        JPanel footerLeft = new JPanel(new BorderLayout());
        footerLeft.setOpaque(false);
        footerLeft.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 8));
        countLabel.setFont(countLabel.getFont().deriveFont(Font.PLAIN, 8f));
        footerDepartment.setText("Home Department: DEPT-01 Computer Science & IT");
        footerDepartment.setFont(footerDepartment.getFont().deriveFont(Font.BOLD, 8f));
        footerLeft.add(countLabel, BorderLayout.WEST);
        footerLeft.add(footerDepartment, BorderLayout.EAST);

        JPanel footerActions = new JPanel();
        footerActions.setOpaque(false);
        footerActions.setBorder(BorderFactory.createEmptyBorder(7, 8, 7, 8));
        printButton.setFont(printButton.getFont().deriveFont(Font.PLAIN, 8f));
        exportButton.setFont(exportButton.getFont().deriveFont(Font.PLAIN, 8f));
        exportButton.setBackground(HoDShellComponents.INSTITUTION_BLUE);
        exportButton.setForeground(Color.WHITE);
        exportButton.setOpaque(true);
        footerActions.add(printButton);
        footerActions.add(exportButton);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setPreferredSize(new Dimension(0, 50));
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, HoDShellComponents.BORDER));
        footer.setBackground(Color.WHITE);
        footer.add(footerLeft, BorderLayout.CENTER);
        footer.add(footerActions, BorderLayout.EAST);
        body.add(footer, BorderLayout.SOUTH);

        add(context, BorderLayout.NORTH);
        add(body, BorderLayout.CENTER);

        applyButton.addActionListener(event -> applyFilters());
        resetButton.addActionListener(event -> resetFilters());
        refreshButton.addActionListener(event -> refreshDemoData());
        printButton.addActionListener(event -> printTable());
        exportButton.addActionListener(event -> exportVisibleData());

        updateCountLabel(tableModel.getRowCount());
    }

    private JPanel createFilterToolbar() {
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setOpaque(false);
        toolbar.setBorder(BorderFactory.createEmptyBorder(8, 0, 7, 0));

        JPanel selects = new JPanel();
        selects.setOpaque(false);
        selects.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        selects.add(createFilterField("Course:", courseFilter));
        courseFilter.setPreferredSize(new Dimension(220, 24));
        selects.add(createFilterField("Scheduled Date:", dateFilter));
        dateFilter.setPreferredSize(new Dimension(155, 24));
        selects.add(createFilterField("Venue:", venueFilter));
        venueFilter.setPreferredSize(new Dimension(220, 24));

        JPanel actions = new JPanel(new BorderLayout());
        actions.setOpaque(false);

        JPanel search = new JPanel(new BorderLayout());
        search.setOpaque(false);
        searchField.setPreferredSize(new Dimension(260, 24));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(HoDShellComponents.BORDER),
            BorderFactory.createEmptyBorder(2, 7, 2, 7)));
        searchField.setText(SEARCH_PLACEHOLDER);
        searchField.setForeground(Color.GRAY);
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent event) {
                if (SEARCH_PLACEHOLDER.equals(searchField.getText())) {
                    searchField.setText("");
                    searchField.setForeground(Color.DARK_GRAY);
                }
            }

            @Override
            public void focusLost(FocusEvent event) {
                if (searchField.getText().trim().isEmpty()) {
                    searchField.setText(SEARCH_PLACEHOLDER);
                    searchField.setForeground(Color.GRAY);
                }
            }
        });
        search.add(searchField, BorderLayout.WEST);

        JPanel buttons = new JPanel();
        buttons.setOpaque(false);
        applyButton.setFont(applyButton.getFont().deriveFont(Font.PLAIN, 8f));
        resetButton.setFont(resetButton.getFont().deriveFont(Font.PLAIN, 8f));
        refreshButton.setFont(refreshButton.getFont().deriveFont(Font.PLAIN, 8f));
        applyButton.setBackground(HoDShellComponents.INSTITUTION_BLUE);
        applyButton.setForeground(Color.WHITE);
        applyButton.setOpaque(true);
        buttons.add(applyButton);
        buttons.add(resetButton);
        buttons.add(refreshButton);

        actions.add(search, BorderLayout.WEST);
        actions.add(buttons, BorderLayout.EAST);

        toolbar.add(selects, BorderLayout.NORTH);
        toolbar.add(actions, BorderLayout.SOUTH);
        return toolbar;
    }

    private JPanel createFilterField(String label, JComboBox<String> combo) {
        JPanel field = new JPanel();
        field.setOpaque(false);
        JLabel name = new JLabel(label + " ");
        name.setFont(name.getFont().deriveFont(Font.PLAIN, 8f));
        field.add(name);
        field.add(combo);
        return field;
    }

    private DefaultTableModel createTableModel() {
        String[] columns = {
            "#", "COURSE CODE", "COURSE UNIT NAME", "CREDIT UNITS",
            "SCHEDULED DATE", "START TIME", "ROOM ID",
            "ALLOCATED VENUE NAME", "CAPACITY"
        };
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Integer.class : String.class;
            }
        };
    }

    private JTable createTable(DefaultTableModel model) {
        JTable result = new JTable(model);
        result.setRowHeight(37);
        result.setFont(result.getFont().deriveFont(Font.PLAIN, 8f));
        result.setShowHorizontalLines(true);
        result.setShowVerticalLines(true);
        result.setGridColor(HoDShellComponents.BORDER);
        result.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = result.getTableHeader();
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(0, 34));
        header.setFont(header.getFont().deriveFont(Font.BOLD, 8f));
        header.setBackground(new Color(242, 244, 247));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        DefaultTableCellRenderer left = new DefaultTableCellRenderer();
        left.setHorizontalAlignment(SwingConstants.LEFT);

        result.getColumnModel().getColumn(0).setPreferredWidth(34);
        result.getColumnModel().getColumn(0).setMaxWidth(50);
        result.getColumnModel().getColumn(1).setPreferredWidth(100);
        result.getColumnModel().getColumn(2).setPreferredWidth(210);
        result.getColumnModel().getColumn(3).setPreferredWidth(75);
        result.getColumnModel().getColumn(4).setPreferredWidth(105);
        result.getColumnModel().getColumn(5).setPreferredWidth(80);
        result.getColumnModel().getColumn(6).setPreferredWidth(70);
        result.getColumnModel().getColumn(7).setPreferredWidth(150);
        result.getColumnModel().getColumn(8).setPreferredWidth(100);

        result.getColumnModel().getColumn(0).setCellRenderer(center);
        result.getColumnModel().getColumn(1).setCellRenderer(createCourseCodeRenderer());
        result.getColumnModel().getColumn(3).setCellRenderer(center);
        result.getColumnModel().getColumn(4).setCellRenderer(center);
        result.getColumnModel().getColumn(5).setCellRenderer(center);
        result.getColumnModel().getColumn(6).setCellRenderer(center);
        result.getColumnModel().getColumn(8).setCellRenderer(createCapacityRenderer());

        result.setDefaultEditor(Object.class, null);
        return result;
    }

    private DefaultTableCellRenderer createCourseCodeRenderer() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(getFont().deriveFont(Font.BOLD, 8f));
                setForeground(HoDShellComponents.INSTITUTION_BLUE);
                return component;
            }
        };
    }

    private DefaultTableCellRenderer createCapacityRenderer() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(getFont().deriveFont(Font.PLAIN, 7f));
                return component;
            }
        };
    }

    private static JButton makeIconButton(String text) {
        JButton button = new JButton(text);
        button.setMargin(new java.awt.Insets(3, 7, 3, 7));
        return button;
    }

    private void loadDemoData() {
        tableModel.setRowCount(0);
        for (TimetableDemoData.TimetableRecord record : TimetableDemoData.records()) {
            tableModel.addRow(new Object[] {
                tableModel.getRowCount() + 1,
                record.getCourseCode(),
                record.getCourseName(),
                record.getCreditUnits(),
                record.getDate(),
                record.getStartTime(),
                record.getRoomId(),
                record.getRoomName(),
                record.getCapacity()
            });
        }
        populateFilterValues();
    }

    private void populateFilterValues() {
        String currentCourse = courseFilter.getSelectedItem() == null
            ? null : courseFilter.getSelectedItem().toString();
        String currentDate = dateFilter.getSelectedItem() == null
            ? null : dateFilter.getSelectedItem().toString();
        String currentVenue = venueFilter.getSelectedItem() == null
            ? null : venueFilter.getSelectedItem().toString();

        courseFilter.removeAllItems();
        dateFilter.removeAllItems();
        venueFilter.removeAllItems();
        courseFilter.addItem("All Department Course Units");
        dateFilter.addItem("All Scheduled Dates");
        venueFilter.addItem("All Department Venues");

        List<String> courses = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        List<String> venues = new ArrayList<>();
        for (TimetableDemoData.TimetableRecord record : TimetableDemoData.records()) {
            if (!courses.contains(record.getCourseCode())) {
                courses.add(record.getCourseCode());
            }
            if (!dates.contains(record.getDate())) {
                dates.add(record.getDate());
            }
            if (!venues.contains(record.getRoomName())) {
                venues.add(record.getRoomName());
            }
        }
        courses.sort(String::compareTo);
        dates.sort(String::compareTo);
        venues.sort(String::compareTo);
        courses.forEach(courseFilter::addItem);
        dates.forEach(dateFilter::addItem);
        venues.forEach(venueFilter::addItem);

        if (currentCourse != null && courses.contains(currentCourse)) {
            courseFilter.setSelectedItem(currentCourse);
        }
        if (currentDate != null && dates.contains(currentDate)) {
            dateFilter.setSelectedItem(currentDate);
        }
        if (currentVenue != null && venues.contains(currentVenue)) {
            venueFilter.setSelectedItem(currentVenue);
        }
    }

    private void applyFilters() {
        if (loading) {
            return;
        }
        loading = true;
        setFilterControlsEnabled(false);
        noticePanel.setEnabled(false);

        SwingWorker<Integer, Void> worker = new SwingWorker<Integer, Void>() {
            @Override
            protected Integer doInBackground() {
                try {
                    Thread.sleep(180);
                } catch (InterruptedException interrupted) {
                    Thread.currentThread().interrupt();
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    int visibleRows = applyCombinedRowFilter();
                    updateCountLabel(visibleRows);
                } catch (Exception error) {
                    JOptionPane.showMessageDialog(
                        CourseUnitTimetablePanel.this,
                        "The timetable filters could not be applied.",
                        "Filter Error",
                        JOptionPane.ERROR_MESSAGE);
                } finally {
                    loading = false;
                    setFilterControlsEnabled(true);
                    noticePanel.setEnabled(true);
                }
            }
        };
        worker.execute();
    }

    private int applyCombinedRowFilter() {
        String course = courseFilter.getSelectedItem().toString();
        String date = dateFilter.getSelectedItem().toString();
        String venue = venueFilter.getSelectedItem().toString();
        String search = searchField.getText().trim();
        if (SEARCH_PLACEHOLDER.equals(search)) {
            search = "";
        }

        List<RowFilter<Object, Object>> filters = new ArrayList<>();
        if (!course.startsWith("All ")) {
            filters.add(RowFilter.regexFilter("^" + java.util.regex.Pattern.quote(course) + "$", 1));
        }
        if (!date.startsWith("All ")) {
            filters.add(RowFilter.regexFilter("^" + java.util.regex.Pattern.quote(date) + "$", 4));
        }
        if (!venue.startsWith("All ")) {
            filters.add(RowFilter.regexFilter("^" + java.util.regex.Pattern.quote(venue) + "$", 7));
        }
        if (!search.isEmpty()) {
            String regex = "(?i).*" + java.util.regex.Pattern.quote(search) + ".*";
            filters.add(RowFilter.regexFilter(regex, 1, 2, 4, 5, 6, 7, 8));
        }

        if (filters.isEmpty()) {
            rowSorter.setRowFilter(null);
        } else if (filters.size() == 1) {
            rowSorter.setRowFilter(filters.get(0));
        } else {
            rowSorter.setRowFilter(RowFilter.andFilter(filters));
        }
        return table.getRowCount();
    }

    private void resetFilters() {
        courseFilter.setSelectedIndex(0);
        dateFilter.setSelectedIndex(0);
        venueFilter.setSelectedIndex(0);
        searchField.setText(SEARCH_PLACEHOLDER);
        searchField.setForeground(Color.GRAY);
        rowSorter.setRowFilter(null);
        updateCountLabel(tableModel.getRowCount());
    }

    private void refreshDemoData() {
        if (loading) {
            return;
        }
        loading = true;
        setFilterControlsEnabled(false);
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException interrupted) {
                    Thread.currentThread().interrupt();
                }
                return null;
            }

            @Override
            protected void done() {
                loadDemoData();
                resetFilters();
                loading = false;
                setFilterControlsEnabled(true);
                JOptionPane.showMessageDialog(
                    CourseUnitTimetablePanel.this,
                    "DEPT-01 demonstration timetable refreshed.",
                    "Refresh",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        };
        worker.execute();
    }

    private void setFilterControlsEnabled(boolean enabled) {
        courseFilter.setEnabled(enabled);
        dateFilter.setEnabled(enabled);
        venueFilter.setEnabled(enabled);
        searchField.setEnabled(enabled);
        applyButton.setEnabled(enabled);
        resetButton.setEnabled(enabled);
        refreshButton.setEnabled(enabled);
    }

    private void updateCountLabel(int visibleRows) {
        countLabel.setText("Showing " + visibleRows + " of " + tableModel.getRowCount()
            + " DEPT-01 department course-unit records");
    }

    private void printTable() {
        try {
            table.print(
                JTable.PrintMode.FIT_WIDTH,
                new MessageFormat("Departmental Course-Unit Timetables - DEPT-01"),
                new MessageFormat("Page {0}"));
        } catch (Exception error) {
            JOptionPane.showMessageDialog(
                this,
                "The timetable could not be sent to the printer.",
                "Print Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportVisibleData() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("dept-01-course-unit-timetable.txt"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File selected = chooser.getSelectedFile();
        if (!selected.getName().toLowerCase().endsWith(".txt")) {
            selected = new File(selected.getAbsolutePath() + ".txt");
        }

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(selected), StandardCharsets.UTF_8))) {
            writer.write("Departmental Course-Unit Timetables - DEPT-01 Computer Science & IT");
            writer.newLine();
            writer.write("Demo data - not from production database");
            writer.newLine();
            writer.newLine();

            for (int column = 0; column < table.getColumnCount(); column++) {
                if (column > 0) {
                    writer.write("\t");
                }
                writer.write(table.getColumnName(column));
            }
            writer.newLine();

            for (int row = 0; row < table.getRowCount(); row++) {
                for (int column = 0; column < table.getColumnCount(); column++) {
                    if (column > 0) {
                        writer.write("\t");
                    }
                    Object value = table.getValueAt(row, column);
                    writer.write(value == null ? "" : value.toString());
                }
                writer.newLine();
            }
            JOptionPane.showMessageDialog(
                this,
                "Visible timetable data exported to:\n" + selected.getAbsolutePath(),
                "Export Complete",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception error) {
            JOptionPane.showMessageDialog(
                this,
                "The timetable could not be exported.",
                "Export Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
