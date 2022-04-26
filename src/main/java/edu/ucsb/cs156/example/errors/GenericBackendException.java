package edu.ucsb.cs156.example.errors;

import lombok.Getter;

import java.util.stream.Collectors;

public class GenericBackendException extends RuntimeException {
  public GenericBackendException(String message) {
    super(message);
  }
}
