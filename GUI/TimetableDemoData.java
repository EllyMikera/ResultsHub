import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Temporary in-memory data for visual testing only.
 * This class is intentionally separate from production database/service logic.
 */
public final class TimetableDemoData {
    private static final List<TimetableRecord> RECORDS;

    static {
        List<TimetableRecord> records = new ArrayList<>();
        records.add(new TimetableRecord("COMP 101", "Intro to Computer Science", "3.0",
            "2025-03-10", "08:00", "RM-01", "Lecture Hall 01", "120 Seats"));
        records.add(new TimetableRecord("COMP 102", "Structural Programming", "4.0",
            "2025-03-10", "11:00", "LAB-01", "Computer Lab 01", "45 Workstations"));
        records.add(new TimetableRecord("COMP 202", "Data Structures & Algorithms", "3.0",
            "2025-03-11", "08:00", "RM-02", "Lecture Hall 02", "60 Seats"));
        records.add(new TimetableRecord("COMP 208", "Computer Networks", "3.0",
            "2025-03-12", "14:00", "SC-201", "Science Complex 201", "50 Seats"));
        records.add(new TimetableRecord("COMP 305", "Database Systems II", "3.0",
            "2025-03-13", "09:00", "LAB-02", "Software Eng Lab", "50 Workstations"));
        records.add(new TimetableRecord("COMP 410", "Distributed Computing", "4.0",
            "2025-03-14", "14:00", "RM-01", "Lecture Hall 01", "120 Seats"));
        RECORDS = Collections.unmodifiableList(records);
    }

    private TimetableDemoData() {
    }

    public static List<TimetableRecord> records() {
        return RECORDS;
    }

    public static final class TimetableRecord {
        private final String courseCode;
        private final String courseName;
        private final String creditUnits;
        private final String date;
        private final String startTime;
        private final String roomId;
        private final String roomName;
        private final String capacity;

        TimetableRecord(String courseCode, String courseName, String creditUnits,
                        String date, String startTime, String roomId,
                        String roomName, String capacity) {
            this.courseCode = courseCode;
            this.courseName = courseName;
            this.creditUnits = creditUnits;
            this.date = date;
            this.startTime = startTime;
            this.roomId = roomId;
            this.roomName = roomName;
            this.capacity = capacity;
        }

        public String getCourseCode() { return courseCode; }
        public String getCourseName() { return courseName; }
        public String getCreditUnits() { return creditUnits; }
        public String getDate() { return date; }
        public String getStartTime() { return startTime; }
        public String getRoomId() { return roomId; }
        public String getRoomName() { return roomName; }
        public String getCapacity() { return capacity; }
    }
}
