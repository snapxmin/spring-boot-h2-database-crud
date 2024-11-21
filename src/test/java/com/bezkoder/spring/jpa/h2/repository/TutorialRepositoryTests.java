package com.bezkoder.spring.jpa.h2.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.ContextConfiguration;

import com.bezkoder.spring.jpa.h2.SpringBootJpaH2Application;
import com.bezkoder.spring.jpa.h2.model.Tutorial;

@SpringBootTest
@ContextConfiguration(classes = SpringBootJpaH2Application.class)
@EnableCaching
@DataJpaTest
public class TutorialRepositoryTests {

    @Autowired
    private TutorialRepository tutorialRepository;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    public void setUp() {
        tutorialRepository.deleteAll();
    }

    @Test
    public void testFindById_Cacheable() {
        Tutorial tutorial = new Tutorial("Title", "Description", true);
        tutorial = tutorialRepository.save(tutorial);

        Optional<Tutorial> tutorialFromDb = tutorialRepository.findById(tutorial.getId());
        assertThat(tutorialFromDb).isPresent();

        Optional<Tutorial> tutorialFromCache = tutorialRepository.findById(tutorial.getId());
        assertThat(tutorialFromCache).isPresent();

        assertThat(cacheManager.getCache("tutorials").get(tutorial.getId())).isNotNull();
    }

    @Test
    public void testDeleteById_CacheEvict() {
        Tutorial tutorial = new Tutorial("Title", "Description", true);
        tutorial = tutorialRepository.save(tutorial);

        tutorialRepository.findById(tutorial.getId());
        assertThat(cacheManager.getCache("tutorials").get(tutorial.getId())).isNotNull();

        tutorialRepository.deleteById(tutorial.getId());
        assertThat(cacheManager.getCache("tutorials").get(tutorial.getId())).isNull();
    }

    @Test
    public void testDeleteAll_CacheEvict() {
        Tutorial tutorial1 = new Tutorial("Title1", "Description1", true);
        Tutorial tutorial2 = new Tutorial("Title2", "Description2", true);
        tutorialRepository.save(tutorial1);
        tutorialRepository.save(tutorial2);

        tutorialRepository.findById(tutorial1.getId());
        tutorialRepository.findById(tutorial2.getId());
        assertThat(cacheManager.getCache("tutorials").get(tutorial1.getId())).isNotNull();
        assertThat(cacheManager.getCache("tutorials").get(tutorial2.getId())).isNotNull();

        tutorialRepository.deleteAll();
        assertThat(cacheManager.getCache("tutorials").get(tutorial1.getId())).isNull();
        assertThat(cacheManager.getCache("tutorials").get(tutorial2.getId())).isNull();
    }
}
