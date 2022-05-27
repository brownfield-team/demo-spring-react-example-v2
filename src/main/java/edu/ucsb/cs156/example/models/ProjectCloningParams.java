package edu.ucsb.cs156.example.models;

import lombok.Data;

@Data
public class ProjectCloningParams {
  private String fromProjectId;
  private String toRepoId;
  private String boardName;
}
