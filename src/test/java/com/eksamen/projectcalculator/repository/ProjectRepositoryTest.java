package com.eksamen.projectcalculator.repository;

import com.eksamen.projectcalculator.domain.model.Project;
import com.eksamen.projectcalculator.domain.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProjectRepositoryTest {

    // Should_ExpectedBehavior_When_StateUnderTest
    private final ProjectRepository PROJECT_REPOSITORY = new ProjectRepository();
    private final UserRepository USER_REPOSITORY = new UserRepository();


    @Test
    public void Should_ReturnNull_When_RetrievingProjectAfterDelete() {
        // Arrange
        String testEmail = "projectCreateTest@hotmail.dk";

        if (!USER_REPOSITORY.emailExists(testEmail)) {
            USER_REPOSITORY.createUser(testEmail, "test", false);
        }

        User user = USER_REPOSITORY.getUserByEmail(testEmail);
        long id = PROJECT_REPOSITORY.createProject(user.getUserId(), "test project");
        Project project = PROJECT_REPOSITORY.getProjectById(id);

        boolean expected = project != null;

        // Act
        PROJECT_REPOSITORY.deleteProjectById(id);
        Project after = PROJECT_REPOSITORY.getProjectById(id);

        boolean actual = after == null;

        // Assert
        assertEquals(expected, actual);
    }
}
