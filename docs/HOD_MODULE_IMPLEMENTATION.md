# HoD Module Implementation Handover

## 1. Module Overview

The Head of Department (HoD) module will provide two read-only views of information belonging to the authenticated HoD's department:

- Departmental Course-Unit Timetables — Ngari's screen.
- Departmental Room Allocations — Mash's screen.

This contribution establishes the shared Swing navigation and screen/service starting points. It does not implement database-backed screens.

## 2. Existing Foundation

The relevant project structure is:

```text
config/
  DBConnection.java
GUI/
  HoDPanel.java
  CourseUnitTimetablePanel.java
  RoomAllocationPanel.java
services/
  HoDService.java
database/
  schema.sql
  seed.sql
```

- `HoDPanel` is the module container. It separates its navigation controls from a `CardLayout` content area and switches between the two panels.
- `CourseUnitTimetablePanel` and `RoomAllocationPanel` are separate read-only placeholders. They currently contain no data or database operations.
- `HoDService` is a shared service-layer starting point; no service methods or query contracts have been agreed or implemented.
- `DBConnection.getConnection()` is the existing database connection entry point. Do not add a second connection implementation.

The new folders are included as IntelliJ source roots in `ResultsHub.iml`. The Java sources follow the repository's existing default-package convention; they do not declare packages.

The current checked-in Java sources do not include a main application window or GUI entry point, so mounting `HoDPanel` into the running application remains an integration step.

## 3. Developer Responsibilities

### Ngari

- Implement the Course-Unit Timetable interface.
- Implement its supporting data retrieval/service functionality.
- Connect the screen to the shared HoD module.
- Test the departmental timetable display.

### Mash

- Implement the Room Allocations interface.
- Implement its supporting data retrieval/service functionality.
- Connect the screen to the shared HoD module.
- Test the departmental room allocation display.

### Joint responsibilities

- Agree on shared service interfaces and any data models before implementation.
- Coordinate changes to `HoDService`, `HoDPanel`, and other shared files.
- Maintain consistent UI conventions.
- Integrate and test both screens.
- Resolve integration issues together.

## 4. Screen Requirements

Both screens must be read-only and show only records belonging to the authenticated HoD's department.

| Course-Unit Timetables (Ngari) | Room Allocations (Mash) |
|---|---|
| Course Unit Code | Room ID |
| Course Unit Name | Room Name |
| Date | Course Unit Code |
| Start Time | Course Unit Name |
| Room Name | Date |
| Room Capacity | Start Time |

## 5. Service Layer Expectations

- Put database operations in `services`; GUI classes must not execute SQL directly.
- Reuse `config/DBConnection.java`.
- Use `PreparedStatement` for parameterized queries.
- Manage connections, statements, and result sets so they are closed reliably (for example, with try-with-resources).
- Keep long-running database work off Swing's Event Dispatch Thread so it does not freeze the interface.
- Agree on the shared service interface and data models before adding method signatures. The foundation intentionally does not prescribe them.

## 6. Database Dependencies and Assumptions

`database/schema.sql` and `database/seed.sql` were empty during the initial foundation inspection and remain empty in the current checkout. The database team must establish and confirm the schema before implementation of queries.

The assignment describes these expected relationships, which are **not yet verified against a schema**:

- `CourseUnit.homeDept` references `Department.DeptId`.
- `RoomAllocation.courseUnit` references `CourseUnit.code`.
- `RoomAllocation.roomId` references `Room.roomId`.

Treat these as requirements to confirm with the database team, not as evidence that these tables, columns, or constraints already exist. Do not infer additional relationships, including student registration relationships.

The authenticated HoD's department ID must be supplied by the application's authentication/session context. No such context is present in the current project. Do not hardcode a department ID or invent a new authentication mechanism.

## 7. Implementation Roadmap

1. Review the existing foundation and confirm the database schema and session-context plans.
2. Agree on shared service interfaces and data models.
3. Implement the two screens independently.
4. Connect each screen to the Services layer.
5. Integrate both screens into `HoDPanel`.
6. Test that each view filters by the authenticated HoD's department.
7. Test empty states, error handling, and navigation.
8. Complete joint integration testing.

## 8. Definition of Done

- [ ] Both screens are implemented.
- [ ] Navigation between screens works.
- [ ] Data is retrieved from the database through Services.
- [ ] Department-level filtering uses the authenticated HoD's department context.
- [ ] Both screens are read-only.
- [ ] Empty and error states are handled.
- [ ] The module integrates with ResultsHub.
- [ ] Both developers have reviewed the integrated implementation.

## 9. Collaboration and Git Guidelines

Coordinate edits to shared files—especially `HoDService` and `HoDPanel`—before integrating each developer's work. Keep screen-specific implementation in the corresponding panel and its agreed service logic; avoid creating competing shared service classes or overwriting another developer's changes.

Follow the group leader's instructions for branch naming, pull-request targets, and merging. This document does not prescribe a team-wide Git workflow or assume which branch is the integration target.
