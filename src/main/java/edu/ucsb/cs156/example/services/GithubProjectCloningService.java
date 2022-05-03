package edu.ucsb.cs156.example.services;

import edu.ucsb.cs156.example.models.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class GithubProjectCloningService {
  @Autowired
  private GithubService github;

  public void cloneProject(String sourceProjectId, String destinationRepoId, String boardName) {
    Map<String, List<Issue>> contents = github.getProjectColumnsAndIssues(sourceProjectId);
    if(boardName == ""){
      boardName = github.getProjectName(sourceProjectId);
    }
    createAndPopulateProject(destinationRepoId, boardName, contents);
  }

  private void createAndPopulateProject(String repoId, String projectName, Map<String, List<Issue>> columnsAndIssues) {
    String projectId = github.createProject(repoId, projectName);

    columnsAndIssues.forEach((column, issues) -> {
      String columnId = github.addColumnToProject(projectId, column);

      // To preserve order, cards must be added in the reverse order they were read from the source board
      List<Issue> reverseOrderIssues = new ArrayList<>(issues);
      Collections.reverse(reverseOrderIssues);

      reverseOrderIssues
        .stream()
        .map(issue -> github.createIssue(repoId, issue))
        .forEach(issueId -> github.addIssueToProjectColumn(columnId, issueId));
    });

  }
}
