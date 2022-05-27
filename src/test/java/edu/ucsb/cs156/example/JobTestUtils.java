package edu.ucsb.cs156.example;

import edu.ucsb.cs156.example.entities.jobs.Job;
import edu.ucsb.cs156.example.repositories.jobs.JobsRepository;
import edu.ucsb.cs156.example.services.jobs.JobContext;
import edu.ucsb.cs156.example.services.jobs.JobContextConsumer;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;

public class JobTestUtils {
  public static Answer<Job> runJobAndReturn(Job job) {
    return runJobAndReturn(job, mock(JobsRepository.class));
  }

  public static Answer<Job> runJobAndReturn(Job job, JobsRepository jobsRepository) {
    return invocation -> {
      ((JobContextConsumer) invocation.getArgument(0)).accept(new JobContext(jobsRepository, job));
      return job;
    };
  }
}
