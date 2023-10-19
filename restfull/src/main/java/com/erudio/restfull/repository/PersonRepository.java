package com.erudio.restfull.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erudio.restfull.model.Person;

@Repository
public interface PersonRepository  extends JpaRepository<Person, Long>{
  
}
