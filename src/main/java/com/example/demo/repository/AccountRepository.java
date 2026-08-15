package com.example.demo.repository;

import com.example.demo.entity.JAccount;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<JAccount, String> {

  Optional<JAccount> findByEmail(String email);

  boolean existsByEmail(String email);
}
