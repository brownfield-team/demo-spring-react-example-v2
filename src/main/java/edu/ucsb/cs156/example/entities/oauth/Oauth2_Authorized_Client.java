package edu.ucsb.cs156.example.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.AccessLevel;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import edu.ucsb.cs156.example.entities.ClientKey;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.sql.Blob;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@IdClass(ClientKey.class)
@Entity(name = "Oauth2_Authorized_Client")
public class Oauth2_Authorized_Client {
  @Id
  @Column(name = "CLIENT_REGISTRATION_ID", columnDefinition = "VARCHAR(100)", nullable = false)
  private String CLIENT_REGISTRATION_ID;
  @Id
  @Column(name = "PRINCIPAL_NAME", columnDefinition = "VARCHAR(200)", nullable = false)
  private String PRINCIPAL_NAME;
  @Column(name = "ACCESS_TOKEN_TYPE", columnDefinition = "VARCHAR(100)", nullable = false)
  private String ACCESS_TOKEN_TYPE;
  @Column(nullable = false)
  private Blob ACCESS_TOKEN_VALUE;
  @Column(nullable = false)
  private LocalDateTime ACCESS_TOKEN_ISSUED_AT;
  @Column(nullable = false)
  private LocalDateTime ACCESS_TOKEN_EXPIRES_AT;
  @Column(name = "ACCESS_TOKEN_SCOPES", columnDefinition = "VARCHAR(1000) DEFAULT NULL")
  private String ACCESS_TOKEN_SCOPES; 
  @Column(name = "REFRESH_TOKEN_VALUE", columnDefinition = "blob DEFAULT NULL")
  private Blob REFRESH_TOKEN_VALUE;
  @Column(name = "REFRESH_TOKEN_ISSUED_AT", columnDefinition = "timestamp DEFAULT NULL")
  private LocalDateTime REFRESH_TOKEN_ISSUED_AT;
  @Column(name = "CREATED_AT", columnDefinition = "timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL")
  private LocalDateTime CREATED_AT;
}
