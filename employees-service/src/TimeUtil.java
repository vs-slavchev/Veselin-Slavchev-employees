import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class TimeUtil {

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

    public static LocalDate parseDate(String date) {
        DateTimeFormatterBuilder dateTimeFormatterBuilder = new DateTimeFormatterBuilder()
                .append(DateTimeFormatter.ofPattern("[yyyy-MM-dd][MM/dd/yyyy][dd-MM-yyyy][dd.MM.yyyy]"));
        DateTimeFormatter dateTimeFormatter = dateTimeFormatterBuilder.toFormatter();
        return LocalDate.parse(date, dateTimeFormatter);
    }
}