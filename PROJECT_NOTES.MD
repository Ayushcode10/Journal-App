# 📓 Journal App – Deep Technical Notes

This document explains **every concept, class, and line of code** in the Journal App project, so I can understand *exactly* how it works and upgrade it confidently.

---

## 1️⃣ Project Overview
- **Goal**: A multi-user journaling application built with **Spring Boot** and **MongoDB**.
- **Main Features**:
    - Create, Read, Update, Delete journal entries
    - Multiple users with their own entries
    - REST API endpoints
- **Core Technologies**:
    - Java 17+
    - Spring Boot
    - Spring Data MongoDB
    - Maven
    - RESTful API

---

## 2️⃣ Core Spring Boot Concepts in Use

### **@SpringBootApplication**
- Combines:
    - `@Configuration` → Marks this class as a Spring config source.
    - `@EnableAutoConfiguration` → Tells Spring Boot to configure beans automatically.
    - `@ComponentScan` → Finds my `@Component`, `@Service`, `@Controller` classes.

### **@RestController**
- Special type of controller that:
    - Automatically serializes return values to JSON
    - No need to add `@ResponseBody` to every method

### **Dependency Injection (@Autowired)**
- Lets Spring create and inject objects automatically
- Removes the need to use `new` manually for dependencies

### **@RequestMapping / @GetMapping / @PostMapping / @DeleteMapping**
- Maps HTTP requests (GET, POST, DELETE) to specific controller methods

---

## 3️⃣ API Endpoints

| Method | Endpoint                          | Description                  | Parameters         | Example Response |
|--------|-----------------------------------|------------------------------|--------------------|------------------|
| GET    | `/journal/{username}`             | Get all journal entries      | `username`         | JSON List        |
| POST   | `/journal/{username}`             | Create a new entry           | `username`, Body   | Created Entry    |
| PUT    | `/journal/{username}/{entryId}`   | Update an existing entry     | `username`, `id`   | Updated Entry    |
| DELETE | `/journal/{username}/{entryId}`   | Delete an entry by ID        | `username`, `id`   | No Content       |

---

## 4️⃣ Code Walkthrough

### **Controller Layer**
Handles HTTP requests and responses. Example:

```java
@DeleteMapping("id/{userName}/{myId}")
public ResponseEntity<?> deleteEntryById(@PathVariable ObjectId myId, @PathVariable String userName){
    try{
        journalEntryService.DeleteById(myId, userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    catch (Exception e){
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
```

### Explanation:

- **@DeleteMapping** → Binds DELETE HTTP requests to this method.

- **@PathVariable** → Extracts values from the URL path.

- **ResponseEntity** → Wraps the HTTP status + optional body.

- **HttpStatus.NO_CONTENT** → Means successful deletion (204).

- **Try/Catch ensures bad input** → BAD_REQUEST (400).

### Service Layer
Contains business logic. Example:

```java
public void DeleteById(ObjectId id, String userName){
    User user = userService.findByUserName(userName);
    user.getJournalEntries().removeIf(x -> x.getId().equals(id));
    journalEntryRepo.deleteById(id);
    userService.saveEntry(user);
}

```
### Explanation:

1. Get the user by username.

2. Remove matching journal entry from user's list.

3. Delete from ```journalEntryRepo``` (MongoDB).

4. Save updated user back.

## 5️⃣ Common Spring Boot/MongoDB Concepts
### Optional<T>
- A container object which may or may not contain a value.

- Avoids NullPointerException.

- Example:

```java
Optional<JournalEntry> entry = journalEntryRepo.findById(id);
if(entry.isPresent()) {
    // use entry.get()
}
```

### MongoRepository
- Interface that gives me ready-made CRUD methods:

``findAll()``, ``findById()``, ``deleteById()``, etc.

## 6️⃣ Application Flow (High Level)

1. Client sends HTTP request (e.g., DELETE /journal/ayush/123).

2. Controller maps request → calls Service.

3. Service applies business rules → calls Repository.

4. Repository talks to MongoDB.

5. Response goes back through Service → Controller → Client.

## 7️⃣ Future Upgrades To Document
- Authentication & Authorization (Spring Security)

- Exception Handling with @ControllerAdvice

- DTOs & Validation

- Pagination

- Unit & Integration Tests