import java.time.LocalDate;

public class TimePeriodUtil {

    public static int getOverlappingTimePeriod(EmployeeAssignment assignment,
                                               EmployeeAssignment otherAssignment) {
        long latestStart = Math.max(assignment.getStart().toEpochDay(),
                otherAssignment.getStart().toEpochDay());
        long earliestEnd = Math.min(assignment.getEnd().toEpochDay(),
                otherAssignment.getEnd().toEpochDay());
        boolean overlapping = latestStart <= earliestEnd;
        if (overlapping) {
            return (int) (earliestEnd - latestStart);
        }

        return 0;
    }
}