import java.util.Arrays;
import java.util.List;

public class EmployeeService {

    public List<EmployeeAssignment> parseAssignments(String input) {
        String[] lines = input.split("\n");
        if (lines.length <= 1 && lines[0].isBlank()) {
            return List.of();
        }
        return Arrays.stream(lines)
                .map(EmployeeAssignment::fromString)
                .toList();
    }

    public void findLongestTeamPeriod(List<EmployeeAssignment> assignments) {

    }
}
