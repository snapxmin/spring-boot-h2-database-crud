package com.bezkoder.spring.jpa.h2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import com.bezkoder.spring.jpa.h2.model.Tutorial;

public interface TutorialRepository extends JpaRepository<Tutorial, Long> {
  List<Tutorial> findByPublished(boolean published);

  List<Tutorial> findByTitleContainingIgnoreCase(String title);

  @Cacheable(value = "tutorials", key = "#id")
  Optional<Tutorial> findById(Long id);

  @CacheEvict(value = "tutorials", key = "#id")
  void deleteById(Long id);

  @CacheEvict(value = "tutorials", allEntries = true)
  void deleteAll();
}
