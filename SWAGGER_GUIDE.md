# Swagger/OpenAPI Documentation Guide

This guide explains how to use the Swagger UI documentation for the Tutorial Management API.

## Accessing Swagger UI

Once the application is running, you can access the interactive API documentation at:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Alternative URL**: http://localhost:8080/swagger-ui/index.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs
- **OpenAPI YAML**: http://localhost:8080/v3/api-docs.yaml

## Features

### 1. Interactive API Testing

Swagger UI provides an interactive interface where you can:
- View all available endpoints
- See request/response schemas
- Test API calls directly from the browser
- View example responses

### 2. API Documentation

Each endpoint includes:
- **Operation Summary**: Brief description of what the endpoint does
- **Parameters**: Required and optional parameters with types and examples
- **Request Body**: Schema for POST/PUT requests
- **Responses**: All possible HTTP status codes with descriptions
- **Example Values**: Sample request and response data

## Using Swagger UI

### Step 1: Navigate to Swagger UI

Open your browser and go to: http://localhost:8080/swagger-ui.html

### Step 2: Explore Endpoints

You'll see all endpoints grouped under the "Tutorial" tag:
- `GET /api/tutorials` - Retrieve all tutorials
- `POST /api/tutorials` - Create a new tutorial
- `GET /api/tutorials/{id}` - Get a specific tutorial
- `PUT /api/tutorials/{id}` - Update a tutorial
- `DELETE /api/tutorials/{id}` - Delete a tutorial
- `DELETE /api/tutorials` - Delete all tutorials
- `GET /api/tutorials/published` - Get published tutorials

### Step 3: Test an Endpoint

**Example: Creating a Tutorial**

1. Click on `POST /api/tutorials` to expand it
2. Click the "Try it out" button
3. Edit the request body JSON:
```json
{
  "title": "Spring Boot with Swagger",
  "description": "Learn how to integrate Swagger with Spring Boot",
  "published": false
}
```
4. Click "Execute"
5. View the response below, including:
   - Response code (201 Created)
   - Response body with the created tutorial
   - Response headers

**Example: Getting All Tutorials**

1. Click on `GET /api/tutorials` to expand it
2. Click the "Try it out" button
3. Optionally enter a title to filter (e.g., "Spring")
4. Click "Execute"
5. View the list of tutorials returned

**Example: Updating a Tutorial**

1. Click on `PUT /api/tutorials/{id}` to expand it
2. Click the "Try it out" button
3. Enter the tutorial ID (e.g., 1)
4. Edit the request body:
```json
{
  "title": "Updated Title",
  "description": "Updated Description",
  "published": true
}
```
5. Click "Execute"
6. View the updated tutorial in the response

## API Schema Details

### Tutorial Object Schema

```json
{
  "id": 1,
  "title": "string",
  "description": "string",
  "published": boolean
}
```

**Fields:**
- `id` (long, read-only): Unique identifier, auto-generated
- `title` (string, required): Title of the tutorial
- `description` (string): Detailed description
- `published` (boolean): Publication status (default: false)

## Common Response Codes

| Code | Description | When |
|------|-------------|------|
| 200 | OK | Successful GET/PUT request |
| 201 | Created | Successful POST request |
| 204 | No Content | Successful DELETE or empty result |
| 404 | Not Found | Tutorial with given ID doesn't exist |
| 500 | Internal Server Error | Server error occurred |

## Example Workflow

Here's a complete workflow for testing the API:

### 1. Create Multiple Tutorials

```bash
POST /api/tutorials
{
  "title": "Java Basics",
  "description": "Introduction to Java programming",
  "published": false
}

POST /api/tutorials
{
  "title": "Spring Framework",
  "description": "Learn Spring Framework",
  "published": true
}
```

### 2. Get All Tutorials

```bash
GET /api/tutorials
```

Response:
```json
[
  {
    "id": 1,
    "title": "Java Basics",
    "description": "Introduction to Java programming",
    "published": false
  },
  {
    "id": 2,
    "title": "Spring Framework",
    "description": "Learn Spring Framework",
    "published": true
  }
]
```

### 3. Search by Title

```bash
GET /api/tutorials?title=Spring
```

Response:
```json
[
  {
    "id": 2,
    "title": "Spring Framework",
    "description": "Learn Spring Framework",
    "published": true
  }
]
```

### 4. Get Published Tutorials Only

```bash
GET /api/tutorials/published
```

Response:
```json
[
  {
    "id": 2,
    "title": "Spring Framework",
    "description": "Learn Spring Framework",
    "published": true
  }
]
```

### 5. Update a Tutorial

```bash
PUT /api/tutorials/1
{
  "title": "Java Advanced",
  "description": "Advanced Java concepts",
  "published": true
}
```

### 6. Delete a Tutorial

```bash
DELETE /api/tutorials/1
```

Response: 204 No Content

## Advanced Features

### OpenAPI Specification Download

You can download the OpenAPI specification in different formats:

**JSON Format:**
```
http://localhost:8080/v3/api-docs
```

**YAML Format:**
```
http://localhost:8080/v3/api-docs.yaml
```

### Using the Specification

The OpenAPI specification can be used to:
- Generate client SDKs in various languages
- Import into API testing tools (Postman, Insomnia)
- Generate server stubs
- Create documentation in different formats

### Importing into Postman

1. Download the OpenAPI JSON: http://localhost:8080/v3/api-docs
2. Open Postman
3. Click "Import" > "Link"
4. Paste the URL or upload the downloaded JSON
5. Postman will create a collection with all endpoints

## Customization

The Swagger configuration is located in:
```
src/main/java/com/bezkoder/spring/jpa/h2/config/OpenApiConfig.java
```

You can customize:
- API title and description
- Contact information
- License information
- Server URLs
- Security schemes (if authentication is added)

## Comparison with FastAPI

| Feature | Spring Boot + Springdoc | FastAPI |
|---------|-------------------------|---------|
| **Setup** | Requires dependency + config | Built-in, automatic |
| **Annotations** | Extensive annotations needed | Minimal annotations |
| **UI** | Swagger UI + ReDoc support | Swagger UI + ReDoc built-in |
| **Customization** | Configuration class required | Simple config in main file |
| **Type Safety** | Via annotations | Via Python type hints |

Both implementations now provide:
- ✅ Interactive API documentation
- ✅ Swagger UI interface
- ✅ OpenAPI 3.0 specification
- ✅ Try-it-out functionality
- ✅ Schema validation display

## Troubleshooting

### Swagger UI not loading

1. Check that the application is running: http://localhost:8080
2. Verify the dependency is in pom.xml:
   ```xml
   <dependency>
       <groupId>org.springdoc</groupId>
       <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
       <version>2.1.0</version>
   </dependency>
   ```
3. Try alternative URL: http://localhost:8080/swagger-ui/index.html

### API endpoints not showing

1. Ensure controllers are properly annotated with `@RestController`
2. Check that methods have `@Operation` annotations
3. Verify the base package in OpenApiConfig

### CORS Issues

If testing from a different origin, ensure CORS is configured:
```java
@CrossOrigin(origins = "http://localhost:8081")
```

## Best Practices

1. **Add Descriptions**: Always add meaningful descriptions to operations
2. **Use Examples**: Provide example values for better understanding
3. **Document Errors**: Document all possible error responses
4. **Keep Updated**: Update documentation when API changes
5. **Version Your API**: Include version information in the OpenAPI config

## Resources

- **Springdoc Documentation**: https://springdoc.org/
- **OpenAPI Specification**: https://swagger.io/specification/
- **Swagger UI**: https://swagger.io/tools/swagger-ui/
- **Spring Boot Integration**: https://springdoc.org/#getting-started

## Summary

With Swagger/OpenAPI integration:
- ✅ Professional API documentation
- ✅ Interactive testing interface
- ✅ No need for separate API documentation
- ✅ Automatic schema generation
- ✅ Easy client SDK generation
- ✅ Better developer experience

Your Spring Boot application now has the same level of interactive documentation as the FastAPI implementation!
