package com.erudio.restfull.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erudio.restfull.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{
  
}
