import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        test_whenDataIsCorrect_thenAssignmentIsParsed();
        test_whenEndDateIsNull_thenEndDateIsNow();

    }

    public static void test_whenDataIsCorrect_thenAssignmentIsParsed() {
        String input = "143, 12, 2013-11-01, 2014-01-05";

        EmployeeAssignment assignment = EmployeeAssignment.fromString(input);

        assert("143".equals(assignment.getEmployeeId()));
        assert("12".equals(assignment.getProjectId()));
        assert(LocalDate.parse("2013-11-01").equals(assignment.getStart()));
        assert(LocalDate.parse("2014-01-05").equals(assignment.getEnd()));
    }

    public static void test_whenEndDateIsNull_thenEndDateIsNow() {
        String input = "218, 10, 2012-05-16, NULL";

        EmployeeAssignment assignment = EmployeeAssignment.fromString(input);

        assert(LocalDate.now().equals(assignment.getEnd()));
    }
}