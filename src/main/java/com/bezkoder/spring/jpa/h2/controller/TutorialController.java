package com.bezkoder.spring.jpa.h2.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.spring.jpa.h2.model.Tutorial;
import com.bezkoder.spring.jpa.h2.repository.TutorialRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Tutorial", description = "Tutorial management APIs")
@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class TutorialController {

  private final TutorialRepository tutorialRepository;

  public TutorialController(TutorialRepository tutorialRepository) {
    this.tutorialRepository = tutorialRepository;
  }

  private <T> ResponseEntity<T> handleException(Exception e) {
    return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private ResponseEntity<HttpStatus> handleDeleteException(Exception e) {
    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private <T> ResponseEntity<List<T>> handleEmptyList(List<T> list) {
    if (list.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @Operation(
      summary = "Retrieve all Tutorials",
      description = "Get all Tutorial objects. You can optionally filter by title.",
      tags = { "tutorials", "get" })
  @ApiResponses({
      @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Tutorial.class), mediaType = "application/json") }),
      @ApiResponse(responseCode = "204", description = "No tutorials found", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
  @GetMapping("/tutorials")
  public ResponseEntity<List<Tutorial>> getAllTutorials(@RequestParam(required = false) String title) {
    try {
      List<Tutorial> tutorials = (title == null) 
          ? tutorialRepository.findAll()
          : tutorialRepository.findByTitleContainingIgnoreCase(title);

      return handleEmptyList(tutorials);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  @Operation(
      summary = "Retrieve a Tutorial by Id",
      description = "Get a Tutorial object by specifying its id.",
      tags = { "tutorials", "get" })
  @ApiResponses({
      @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Tutorial.class), mediaType = "application/json") }),
      @ApiResponse(responseCode = "404", description = "Tutorial not found", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
  @GetMapping("/tutorials/{id}")
  public ResponseEntity<Tutorial> getTutorialById(@PathVariable("id") long id) {
    try {
      return tutorialRepository.findById(id)
          .map(tutorial -> new ResponseEntity<>(tutorial, HttpStatus.OK))
          .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    } catch (Exception e) {
      return handleException(e);
    }
  }

  @Operation(
      summary = "Create a new Tutorial",
      description = "Create a new Tutorial object. The tutorial will be created as unpublished by default.",
      tags = { "tutorials", "post" })
  @ApiResponses({
      @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema(implementation = Tutorial.class), mediaType = "application/json") }),
      @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
  @PostMapping("/tutorials")
  public ResponseEntity<Tutorial> createTutorial(@RequestBody Tutorial tutorial) {
    try {
      Tutorial newTutorial = new Tutorial(tutorial.getTitle(), tutorial.getDescription(), false);
      Tutorial savedTutorial = tutorialRepository.save(newTutorial);
      return new ResponseEntity<>(savedTutorial, HttpStatus.CREATED);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  @Operation(
      summary = "Update a Tutorial by Id",
      description = "Update a Tutorial object by specifying its id.",
      tags = { "tutorials", "put" })
  @ApiResponses({
      @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Tutorial.class), mediaType = "application/json") }),
      @ApiResponse(responseCode = "404", description = "Tutorial not found", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
  @PutMapping("/tutorials/{id}")
  public ResponseEntity<Tutorial> updateTutorial(@PathVariable("id") long id, @RequestBody Tutorial tutorial) {
    try {
      return tutorialRepository.findById(id)
          .map(existingTutorial -> {
            existingTutorial.setTitle(tutorial.getTitle());
            existingTutorial.setDescription(tutorial.getDescription());
            existingTutorial.setPublished(tutorial.isPublished());
            return new ResponseEntity<>(tutorialRepository.save(existingTutorial), HttpStatus.OK);
          })
          .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    } catch (Exception e) {
      return handleException(e);
    }
  }

  @Operation(
      summary = "Delete a Tutorial by Id",
      description = "Delete a Tutorial object by specifying its id.",
      tags = { "tutorials", "delete" })
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Tutorial deleted successfully"),
      @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
  @DeleteMapping("/tutorials/{id}")
  public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") long id) {
    try {
      tutorialRepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return handleDeleteException(e);
    }
  }

  @Operation(
      summary = "Delete all Tutorials",
      description = "Delete all Tutorial objects from the database.",
      tags = { "tutorials", "delete" })
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "All tutorials deleted successfully"),
      @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
  @DeleteMapping("/tutorials")
  public ResponseEntity<HttpStatus> deleteAllTutorials() {
    try {
      tutorialRepository.deleteAll();
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return handleDeleteException(e);
    }
  }

  @Operation(
      summary = "Retrieve all Published Tutorials",
      description = "Get all Tutorial objects that are marked as published.",
      tags = { "tutorials", "get" })
  @ApiResponses({
      @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Tutorial.class), mediaType = "application/json") }),
      @ApiResponse(responseCode = "204", description = "No published tutorials found", content = { @Content(schema = @Schema()) }),
      @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
  @GetMapping("/tutorials/published")
  public ResponseEntity<List<Tutorial>> findByPublished() {
    try {
      List<Tutorial> tutorials = tutorialRepository.findByPublished(true);
      return handleEmptyList(tutorials);
    } catch (Exception e) {
      return handleException(e);
    }
  }

}
