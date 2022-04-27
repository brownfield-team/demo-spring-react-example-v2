package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.entities.jobs.Job;
import edu.ucsb.cs156.example.models.ProjectCloningParams;
import edu.ucsb.cs156.example.services.GithubProjectCloningService;
import edu.ucsb.cs156.example.services.jobs.JobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(description = "A service for copying GitHub projects into other repositories")
@RequestMapping("/api/projectcloning")
@RestController
@Slf4j
public class ProjectCloningController extends ApiController {
  @Autowired
  private JobService jobService;

  @Autowired
  private GithubProjectCloningService cloningService;

  @ApiOperation(value = "Copies a GitHub project from one repository into another")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping("/clone")
  public Job cloneBoard(@RequestBody ProjectCloningParams params) {
    return jobService.runAsJob(ctx -> {
      cloningService.cloneProject(params.getFromProjectId(), params.getToRepoId());
    });
  }
}
