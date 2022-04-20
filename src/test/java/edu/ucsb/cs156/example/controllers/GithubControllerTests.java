package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.ControllerTestCase;
import edu.ucsb.cs156.example.services.GithubService;
import edu.ucsb.cs156.example.models.SourceRepo;
import edu.ucsb.cs156.example.testconfig.TestConfig;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = GithubController.class)
@AutoConfigureDataJpa
public class GithubControllerTests extends ControllerTestCase {

  @MockBean
  GithubService mockGithubService;

  @Test
  public void users__logged_out() throws Exception {
    mockMvc.perform(get("/api/gh/checkSource").param("org", "ucsb-cs156-w22").param("repo", "HappierCows").param("projNum", "1"))
        .andExpect(status().is(403));
    mockMvc.perform(get("/api/gh/checkDestination").param("org", "ucsb-cs156-w22").param("repo", "HappierCows"))
        .andExpect(status().is(403));
  }

  @WithMockUser(roles = { "USER" })
  @Test
  public void source_user_logged_in_valid_input() throws Exception {
    
    // arrange
    SourceRepo expectedSourceRepo = SourceRepo.builder()
            .org("ucsb-cs156-w22")
            .repo("HappierCows")
            .projectNum(1)
            .projectId("PRO_kwLOG0U47s4A11-W")
            .build();
    when(mockGithubService.projectId("ucsb-cs156-w22", "HappierCows", 1)).thenReturn("PRO_kwLOG0U47s4A11-W");
    String expectedJson = mapper.writeValueAsString(expectedSourceRepo);

    // act
    MvcResult response = mockMvc.perform(get("/api/gh/checkSource").param("org", "ucsb-cs156-w22").param("repo", "HappierCows").param("projNum", "1"))
        .andExpect(status().isOk()).andReturn();

    // assert
    String responseString = response.getResponse().getContentAsString();
    assertEquals(expectedJson, responseString);
  }

  @WithMockUser(roles = { "USER" })
  @Test
  public void source_user_logged_in_invalid_input() throws Exception {
    
    // arrange
    SourceRepo expectedSourceRepo = SourceRepo.builder()
            .org("fakeOrg")
            .repo("fakeRepo")
            .projectNum(1)
            .projectId("")
            .build();
    when(mockGithubService.projectId("fakeOrg", "fakeRepo", 1)).thenReturn("");
    String expectedJson = mapper.writeValueAsString(expectedSourceRepo);

    // act
    MvcResult response = mockMvc.perform(get("/api/gh/checkSource").param("org", "fakeOrg").param("repo", "fakeRepo").param("projNum", "1"))
        .andExpect(status().isOk()).andReturn();

    // assert
    String responseString = response.getResponse().getContentAsString();
    assertEquals(expectedJson, responseString);
  }

  @WithMockUser(roles = { "ADMIN", "USER" })
  @Test
  public void source_users__admin_logged_in_valid_input() throws Exception {
    // arrange
    SourceRepo expectedSourceRepo = SourceRepo.builder()
    .org("ucsb-cs156-w22")
    .repo("HappierCows")
    .projectNum(1)
    .projectId("PRO_kwLOG0U47s4A11-W")
    .build();
    when(mockGithubService.projectId("ucsb-cs156-w22", "HappierCows", 1)).thenReturn("PRO_kwLOG0U47s4A11-W");
    String expectedJson = mapper.writeValueAsString(expectedSourceRepo);

    // act
    MvcResult response = mockMvc.perform(get("/api/gh/checkSource").param("org", "ucsb-cs156-w22").param("repo", "HappierCows").param("projNum", "1"))
    .andExpect(status().isOk()).andReturn();

    // assert
    String responseString = response.getResponse().getContentAsString();
    assertEquals(expectedJson, responseString);
  }

  @WithMockUser(roles = { "ADMIN", "USER" })
  @Test
  public void source_users_admin_logged_in_invalid_input() throws Exception {
    
    // arrange
    SourceRepo expectedSourceRepo = SourceRepo.builder()
            .org("fakeOrg")
            .repo("fakeRepo")
            .projectNum(1)
            .projectId("")
            .build();
    when(mockGithubService.projectId("fakeOrg", "fakeRepo", 1)).thenReturn("");
    String expectedJson = mapper.writeValueAsString(expectedSourceRepo);

    // act
    MvcResult response = mockMvc.perform(get("/api/gh/checkSource").param("org", "fakeOrg").param("repo", "fakeRepo").param("projNum", "1"))
        .andExpect(status().isOk()).andReturn();

    // assert
    String responseString = response.getResponse().getContentAsString();
    assertEquals(expectedJson, responseString);
  }

  @WithMockUser(roles = { "USER" })
  @Test
  public void destination_user_logged_in_valid_input() throws Exception {
    
    // arrange
    SourceRepo expectedDestinationRepo = SourceRepo.builder()
            .org("ucsb-cs156-w22")
            .repo("HappierCows")
            .success(true)
            .repositoryId("PRO_kwLOG0U47s4A11-W")
            .build();
    when(mockGithubService.repositoryId("ucsb-cs156-w22", "HappierCows")).thenReturn("PRO_kwLOG0U47s4A11-W");
    String expectedJson = mapper.writeValueAsString(expectedDestinationRepo);

    // act
    MvcResult response = mockMvc.perform(get("/api/gh/checkDestination").param("org", "ucsb-cs156-w22").param("repo", "HappierCows"))
        .andExpect(status().isOk()).andReturn();

    // assert
    String responseString = response.getResponse().getContentAsString();
    assertEquals(expectedJson, responseString);
  }

  @WithMockUser(roles = { "USER" })
  @Test
  public void destination_user_logged_in_invalid_input() throws Exception {
    
    // arrange
    SourceRepo expectedDestinationRepo = SourceRepo.builder()
            .org("fakeOrg")
            .repo("fakeRepo")
            .success(false)
            .repositoryId("")
            .build();
    when(mockGithubService.repositoryId("fakeOrg", "fakeRepo")).thenReturn("");
    String expectedJson = mapper.writeValueAsString(expectedDestinationRepo);

    // act
    MvcResult response = mockMvc.perform(get("/api/gh/checkDestination").param("org", "fakeOrg").param("repo", "fakeRepo"))
        .andExpect(status().isOk()).andReturn();

    // assert
    String responseString = response.getResponse().getContentAsString();
    assertEquals(expectedJson, responseString);
  }

  @WithMockUser(roles = { "ADMIN", "USER" })
  @Test
  public void destination_users__admin_logged_in_valid_input() throws Exception {
    // arrange
    SourceRepo expectedDestinationRepo = SourceRepo.builder()
    .org("ucsb-cs156-w22")
    .repo("HappierCows")
    .success(true)
    .repositoryId("PRO_kwLOG0U47s4A11-W")
    .build();
    when(mockGithubService.repositoryId("ucsb-cs156-w22", "HappierCows")).thenReturn("PRO_kwLOG0U47s4A11-W");
    String expectedJson = mapper.writeValueAsString(expectedDestinationRepo);

    // act
    MvcResult response = mockMvc.perform(get("/api/gh/checkDestination").param("org", "ucsb-cs156-w22").param("repo", "HappierCows"))
    .andExpect(status().isOk()).andReturn();

    // assert
    String responseString = response.getResponse().getContentAsString();
    assertEquals(expectedJson, responseString);
  }

  @WithMockUser(roles = { "ADMIN", "USER" })
  @Test
  public void destination_users_admin_logged_in_invalid_input() throws Exception {
    
    // arrange
    SourceRepo expectedDestinationRepo = SourceRepo.builder()
            .org("fakeOrg")
            .repo("fakeRepo")
            .success(false)
            .repositoryId("")
            .build();
    when(mockGithubService.repositoryId("fakeOrg", "fakeRepo")).thenReturn("");
    String expectedJson = mapper.writeValueAsString(expectedDestinationRepo);

    // act
    MvcResult response = mockMvc.perform(get("/api/gh/checkDestination").param("org", "fakeOrg").param("repo", "fakeRepo"))
        .andExpect(status().isOk()).andReturn();

    // assert
    String responseString = response.getResponse().getContentAsString();
    assertEquals(expectedJson, responseString);
  }
}

