import java.time.LocalDate;

public class EmployeeAssignment {

    private String employeeId;
    private String projectId;
    private LocalDate start;
    private LocalDate end;

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

        return new EmployeeAssignment(
                parts[0].trim(),
                parts[1].trim(),
                parseDate(parts[2]),
                parseDate(parts[3]));
    }

    private static LocalDate parseDate(String stringDate) {
        if (stringDate == null) {
            throw new InvalidCsvException("Date is null");
        }
        stringDate = stringDate.trim();
        return stringDate.equals("NULL") ? LocalDate.now() : LocalDate.parse(stringDate);
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }
}
