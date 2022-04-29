package edu.ucsb.cs156.example.models;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.AccessLevel;


@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class GithubRepository {
    private String org;
    private String repo;
    private String repositoryId;
}