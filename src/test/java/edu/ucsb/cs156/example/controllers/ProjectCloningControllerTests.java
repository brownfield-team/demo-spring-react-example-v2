package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.ControllerTestCase;
import edu.ucsb.cs156.example.JobTestUtils;
import edu.ucsb.cs156.example.entities.jobs.Job;
import edu.ucsb.cs156.example.services.GithubProjectCloningService;
import edu.ucsb.cs156.example.services.jobs.JobService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProjectCloningController.class)
@AutoConfigureDataJpa
public class ProjectCloningControllerTests extends ControllerTestCase {
  @MockBean
  private JobService jobService;

  @MockBean
  private GithubProjectCloningService cloningService;

  @Test
  public void cloneBoard__fails_when_not_logged_in() throws Exception {
    String content = asJsonString(Map.of(
      "fromProjectId", "PRO_dummyid",
      "toRepoId", "R_dummyid"
    ));

    mockMvc.perform(
        post("/api/projectcloning/clone")
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(content)
      )
      .andExpect(status().isForbidden());

    verifyNoInteractions(jobService);
    verifyNoInteractions(cloningService);
  }

  @WithMockUser
  @Test
  public void cloneBoard__fails_when_not_admin() throws Exception {
    String content = asJsonString(Map.of(
      "fromProjectId", "PRO_dummyid",
      "toRepoId", "R_dummyid"
    ));

    mockMvc.perform(
        post("/api/projectcloning/clone")
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)
          .content(content)
      )
      .andExpect(status().isForbidden());

    verifyNoInteractions(jobService);
    verifyNoInteractions(cloningService);
  }

  @WithMockUser(roles = { "ADMIN" })
  @Test
  public void cloneBoard__valid() throws Exception {
    Job job = Job.builder().id(13).build();

    when(jobService.runAsJob(any()))
      .thenAnswer(JobTestUtils.runJobAndReturn(job));

    String content = asJsonString(Map.of(
      "fromProjectId", "PRO_dummyid",
      "toRepoId", "R_dummyid"
    ));

    mockMvc.perform(
      post("/api/projectcloning/clone")
        .with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .content(content)
      )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$").value(job));

    verify(jobService).runAsJob(any());
    verifyNoMoreInteractions(jobService);

    verify(cloningService).cloneProject("PRO_dummyid", "R_dummyid");
    verifyNoMoreInteractions(cloningService);
  }
}
