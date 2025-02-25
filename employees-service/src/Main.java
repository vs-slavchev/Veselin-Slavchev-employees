import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        test_whenDataIsCorrect_thenAssignmentIsParsed();
        test_whenEndDateIsNull_thenEndDateIsNow();
        test_whenInputIs4Lines_thenParse4Lines();
        test_whenInputIs0Lines_thenParse0Lines();
        test_whenInputIs1Line_thenParse1Line();
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

    private static void test_whenInputIs4Lines_thenParse4Lines() {
        String input = """
                143, 12, 2013-11-01, 2014-01-05
                218, 10, 2012-05-16, NULL
                143, 10, 2009-01-01, 2011-04-27
                001, 1, 2012-05-22, 2012-05-24
                """;
        List<EmployeeAssignment> assignments = new EmployeeService().parseAssignments(input);

        assert(assignments.size() == 4);
    }

    private static void test_whenInputIs0Lines_thenParse0Lines() {
        String input = "";
        List<EmployeeAssignment> assignments = new EmployeeService().parseAssignments(input);

        assert(assignments.isEmpty());
    }

    private static void test_whenInputIs1Line_thenParse1Line() {
        String input = "143, 12, 2013-11-01, 2014-01-05";
        List<EmployeeAssignment> assignments = new EmployeeService().parseAssignments(input);

        assert(assignments.size() == 1);
    }
}