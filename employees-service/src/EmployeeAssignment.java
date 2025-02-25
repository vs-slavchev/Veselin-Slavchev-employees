import java.time.LocalDate;

public class EmployeeAssignment {

    private final String employeeId;
    private final String projectId;
    private final LocalDate start;
    private final LocalDate end;

    private EmployeeAssignment(String employeeId, String projectId,
                               LocalDate start, LocalDate end) {
        this.employeeId = employeeId;
        this.projectId = projectId;
        this.start = start;
        this.end = end;
    }

    public static EmployeeAssignment fromString(String line) {
        String[] parts = line.split(",");
        if (parts.length != 4) {
            throw new InvalidCsvException("Unexpected number of values in: " + line);
        }

        LocalDate startDate = parseDate(parts[2]);
        LocalDate endDate = parseDate(parts[3]);
        if (startDate.isAfter(endDate)) {
            throw new InvalidCsvException("Start date cannot be after end date: " + line);
        }

        return new EmployeeAssignment(
                parts[0].trim(),
                parts[1].trim(),
                startDate,
                endDate);
    }

    private static LocalDate parseDate(String stringDate) {
        if (stringDate == null) {
            throw new InvalidCsvException("Date is null");
        }
        stringDate = stringDate.trim();
        return stringDate.equals("NULL") ? LocalDate.now() : TimeUtil.parseDate(stringDate);
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getProjectId() {
        return projectId;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }
}
