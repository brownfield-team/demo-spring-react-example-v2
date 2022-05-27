package edu.ucsb.cs156.example.repositories;

import edu.ucsb.cs156.example.entities.Oauth2_Authorized_Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Oauth2_Authorized_ClientRepository extends CrudRepository<Oauth2_Authorized_Client, Long> {
}