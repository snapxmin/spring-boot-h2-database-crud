package com.bezkoder.spring.jpa.h2.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.bezkoder.spring.jpa.h2.model.Tutorial;
import com.bezkoder.spring.jpa.h2.repository.TutorialRepository;

@Service
public class TutorialService {

    @Autowired
    private TutorialRepository tutorialRepository;

    @Cacheable(value = "tutorials", key = "#id")
    public Optional<Tutorial> getTutorialById(Long id) {
        return tutorialRepository.findById(id);
    }

    @CacheEvict(value = "tutorials", key = "#id")
    public void deleteTutorial(Long id) {
        tutorialRepository.deleteById(id);
    }
}
