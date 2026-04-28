package com.bezkoder.spring.jpa.h2.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Schema(description = "Tutorial model representing a learning resource")
@Entity
@Table(name = "tutorials")
public class Tutorial {

  @Schema(description = "Unique identifier of the Tutorial", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Schema(description = "Title of the Tutorial", example = "Spring Boot Tutorial", required = true)
  @Column(name = "title")
  private String title;

  @Schema(description = "Detailed description of the Tutorial", example = "Learn Spring Boot framework from scratch")
  @Column(name = "description")
  private String description;

  @Schema(description = "Publication status of the Tutorial", example = "false")
  @Column(name = "published")
  private boolean published;

  public Tutorial() {

  }

  public Tutorial(String title, String description, boolean published) {
    this.title = title;
    this.description = description;
    this.published = published;
  }

  public long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isPublished() {
    return published;
  }

  public void setPublished(boolean isPublished) {
    this.published = isPublished;
  }

  @Override
  public String toString() {
    return "Tutorial [id=" + id + ", title=" + title + ", desc=" + description + ", published=" + published + "]";
  }

}
