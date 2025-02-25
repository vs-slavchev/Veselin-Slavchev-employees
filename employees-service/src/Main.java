import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        test_whenDataIsCorrect_thenAssignmentIsParsed();
        test_whenEndDateIsNull_thenEndDateIsNow();
        test_whenInputIs4Lines_thenParse4Lines();
        test_whenInputIs0Lines_thenParse0Lines();
        test_whenInputIs1Line_thenParse1Line();
        test_whenStartDateIsAfterEndDate_thenThrowException();

        test_whenOverlapIs1Day_thenReturn1Day();
        test_whenOverlapIs1DayReversed_thenReturn1Day();
        test_whenOverlapIs0Days_thenReturn0Days();
        test_whenOverlapIsComplete_thenReturnAllDays();
        test_whenStartsOverlap_thenReturnMinDays();

        test_when1OverlapIs2Days_then1WorkPeriodOf2Days();
        test_when2EqualOverlapsOf2Days_then2WorkPeriodsOf2Days();
        test_whenNoOverlap_thenNoWorkPeriods();
        test_whenLargePeriodContainsSmaller_then1WorkPeriodOf2Days();

        test_timeUtil_whenSameDateInDifferentFormats_thenParseCorrectly();
        test_timeUtil_whenDateIsInvalid_thenThrowException();
    }

    public static void test_whenDataIsCorrect_thenAssignmentIsParsed() {
        String input = "143, 12, 2013-11-01, 2014-01-05";

        EmployeeAssignment assignment = EmployeeAssignment.fromString(input);

        assert ("143".equals(assignment.getEmployeeId()));
        assert ("12".equals(assignment.getProjectId()));
        assert (LocalDate.parse("2013-11-01").equals(assignment.getStart()));
        assert (LocalDate.parse("2014-01-05").equals(assignment.getEnd()));
    }

    public static void test_whenEndDateIsNull_thenEndDateIsNow() {
        String input = "218, 10, 2012-05-16, NULL";

        EmployeeAssignment assignment = EmployeeAssignment.fromString(input);

        assert (LocalDate.now().equals(assignment.getEnd()));
    }

    private static void test_whenInputIs4Lines_thenParse4Lines() {
        String input = """
                143, 12, 2013-11-01, 2014-01-05
                218, 10, 2012-05-16, NULL
                143, 10, 2009-01-01, 2011-04-27
                001, 1, 2012-05-22, 2012-05-24
                """;
        List<EmployeeAssignment> assignments = new EmployeeService().parseAssignments(input);

        assert (assignments.size() == 4);
    }

    private static void test_whenInputIs0Lines_thenParse0Lines() {
        String input = "";
        List<EmployeeAssignment> assignments = new EmployeeService().parseAssignments(input);

        assert (assignments.isEmpty());
    }

    private static void test_whenInputIs1Line_thenParse1Line() {
        String input = "143, 12, 2013-11-01, 2014-01-05";
        List<EmployeeAssignment> assignments = new EmployeeService().parseAssignments(input);

        assert (assignments.size() == 1);
    }

    private static void test_whenStartDateIsAfterEndDate_thenThrowException() {
        String input = "143, 12, 2018-01-01, 2014-01-01";
        try {
            List<EmployeeAssignment> assignments = new EmployeeService().parseAssignments(input);
        } catch (InvalidCsvException e) {
            assert (e.getMessage().equals("Start date cannot be after end date: " + input));
        }
    }

    public static void test_whenOverlapIs1Day_thenReturn1Day() {
        EmployeeAssignment assignment1 = EmployeeAssignment.fromString("143, 12, 2000-01-01, 2000-01-05");
        EmployeeAssignment assignment2 = EmployeeAssignment.fromString("218, 12, 2000-01-04, 2000-01-09");

        int daysWorked = TimeUtil.getOverlappingTimePeriod(assignment1, assignment2);

        assert (daysWorked == 1);
    }

    public static void test_whenOverlapIs1DayReversed_thenReturn1Day() {
        EmployeeAssignment assignment1 = EmployeeAssignment.fromString("143, 12, 2000-01-01, 2000-01-05");
        EmployeeAssignment assignment2 = EmployeeAssignment.fromString("218, 12, 2000-01-04, 2000-01-09");

        int daysWorked = TimeUtil.getOverlappingTimePeriod(assignment2, assignment1);

        assert (daysWorked == 1);
    }

    public static void test_whenOverlapIs0Days_thenReturn0Days() {
        EmployeeAssignment assignment1 = EmployeeAssignment.fromString("143, 12, 2000-01-01, 2000-01-05");
        EmployeeAssignment assignment2 = EmployeeAssignment.fromString("218, 12, 2000-01-05, 2000-01-09");

        int daysWorked = TimeUtil.getOverlappingTimePeriod(assignment1, assignment2);

        assert (daysWorked == 0);
    }

    public static void test_whenOverlapIsComplete_thenReturnAllDays() {
        EmployeeAssignment assignment1 = EmployeeAssignment.fromString("143, 12, 2000-01-01, 2000-01-05");
        EmployeeAssignment assignment2 = EmployeeAssignment.fromString("218, 12, 2000-01-01, 2000-01-05");

        int daysWorked = TimeUtil.getOverlappingTimePeriod(assignment1, assignment2);

        assert (daysWorked == 4);
    }

    public static void test_whenStartsOverlap_thenReturnMinDays() {
        EmployeeAssignment assignment1 = EmployeeAssignment.fromString("143, 12, 2000-01-01, 2000-01-05");
        EmployeeAssignment assignment2 = EmployeeAssignment.fromString("218, 12, 2000-01-01, 2000-01-09");

        int daysWorked = TimeUtil.getOverlappingTimePeriod(assignment1, assignment2);

        assert (daysWorked == 4);
    }

    public static void test_when1OverlapIs2Days_then1WorkPeriodOf2Days() {
        String input = """
                143, 12, 2000-01-01, 2000-01-05
                218, 12, 2000-01-03, 2000-01-09
                """;
        EmployeeService employeeService = new EmployeeService();
        List<EmployeeAssignment> assignments = employeeService.parseAssignments(input);

        List<PairWorkPeriod> longestTeamPeriods = employeeService.findLongestTeamPeriod(assignments);

        assert (longestTeamPeriods.size() == 1);
        assert (longestTeamPeriods.get(0).daysWorked() == 2);
        assert (longestTeamPeriods.get(0).projectId().equals("12"));
    }

    public static void test_when2EqualOverlapsOf2Days_then2WorkPeriodsOf2Days() {
        String input = """
                143, 12, 2000-01-01, 2000-01-05
                218, 12, 2000-01-03, 2000-01-09
                555, 12, 2000-01-07, 2000-01-09
                """;
        EmployeeService employeeService = new EmployeeService();
        List<EmployeeAssignment> assignments = employeeService.parseAssignments(input);

        List<PairWorkPeriod> longestTeamPeriods = employeeService.findLongestTeamPeriod(assignments);

        assert (longestTeamPeriods.size() == 2);
        assert (longestTeamPeriods.get(0).daysWorked() == 2);
    }

    public static void test_whenNoOverlap_thenNoWorkPeriods() {
        String input = """
                143, 12, 2000-01-01, 2000-01-05
                218, 12, 2000-01-06, 2000-01-09
                """;
        EmployeeService employeeService = new EmployeeService();
        List<EmployeeAssignment> assignments = employeeService.parseAssignments(input);

        List<PairWorkPeriod> longestTeamPeriods = employeeService.findLongestTeamPeriod(assignments);

        assert (longestTeamPeriods.isEmpty());
    }

    public static void test_whenLargePeriodContainsSmaller_then1WorkPeriodOf2Days() {
        String input = """
                143, 12, 2000-01-01, 2000-01-10
                218, 12, 2000-01-04, 2000-01-06
                """;
        EmployeeService employeeService = new EmployeeService();
        List<EmployeeAssignment> assignments = employeeService.parseAssignments(input);

        List<PairWorkPeriod> longestTeamPeriods = employeeService.findLongestTeamPeriod(assignments);

        assert (longestTeamPeriods.size() == 1);
        assert (longestTeamPeriods.get(0).daysWorked() == 2);
        assert (longestTeamPeriods.get(0).projectId().equals("12"));
    }

    public static void test_timeUtil_whenSameDateInDifferentFormats_thenParseCorrectly() {
        LocalDate date1 = TimeUtil.parseDate("2025-01-31");
        LocalDate date2 = TimeUtil.parseDate("01/31/2025");
        LocalDate date3 = TimeUtil.parseDate("31-01-2025");
        LocalDate date4 = TimeUtil.parseDate("31.01.2025");

        assert (date1.equals(date2));
        assert (date2.equals(date3));
        assert (date3.equals(date4));
    }

    public static void test_timeUtil_whenDateIsInvalid_thenThrowException() {
        try {
            TimeUtil.parseDate("2025-31-31");
        } catch (DateTimeException e) {
            assert (true);
        }
    }
}