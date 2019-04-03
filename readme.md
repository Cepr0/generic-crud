## Generic CRUD

[![](https://jitpack.io/v/Cepr0/generic-crud.svg)](https://jitpack.io/#Cepr0/generic-crud)

**Generic-CRUD** is a handy library that can help exclude writing of boilerplate code for **CRUD** operations 
in Spring web applications. It implements a full set of base operations with a database to create, read, update 
and delete your entities.

### Usage example

1. Inherit your entity from the `IdentifiableEntity` interface or from the `JpaEntity` class:     
```java
@Getter
@Setter
@NoArgsConstructor
public class Model extends JpaEntity<Long> {
    @Id
    @GeneratedValue
    private Long id;
    
    private String name;
}
```
2. Inherit your entity repo from the `JpaRepo`:
```java
public interface ModelRepo extends JpaRepo<Model, Long> {}
```
3. Prepare request and response DTOs of your entity:
```java
@Data
public class ModelRequest implements CrudRequest {
   private String name;
}

@Data
public class ModelResponse implements CrudResponse<Long> {
    private Long id;
    private String name;
}
```
4. Prepare a mapper between the entity and its DTOs:
```java
@Mapper(
    nullValueCheckStrategy = ALWAYS,
    nullValueMappingStrategy = RETURN_DEFAULT,
    nullValuePropertyMappingStrategy = IGNORE
)
public abstract class ModelMapper implements CrudMapper<Model, ModelRequest, ModelResponse> {
}
```
(The library relies on [MapStruct](http://mapstruct.org/) framework to build the mappers.)  

5. Prepare a service which will serve your DTOs and entities:
```java
@Service
public class ModelService extends AbstractCrudService<Model, Long, ModelRequest, ModelResponse> {
    public ModelService(@NonNull final ModelRepo repo, @NonNull final ModelMapper mapper) {
        super(repo, mapper);
    }
}
```
6. And finally prepare a REST controller which will serve the CRUD requests of the entity:
```java
@RestController
@RequestMapping("models")
public class ModelController extends AbstractCrudController<Model, Long, ModelRequest, ModelResponse> {

    public ModelController(@NonNull final ModelService service) {
        super(service);
    }
    
    @PostMapping
    @Override
    public ModelResponse create(@Valid @RequestBody @NonNull final ModelRequest request) {
        return super.create(request);
    }
    
    @PatchMapping("/{id}")
    @Override
    public ResponseEntity<?> update(@PathVariable("id") @NonNull final Long id, @Valid @RequestBody @NonNull final ModelRequest request) {
        return super.update(id, request);
    }
    
    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable("id") @NonNull final Long id) {
        return super.delete(id);
    }
    
    @GetMapping("/{id}")
    @Override
    public ResponseEntity<?> getOne(@PathVariable("id") @NonNull final Long id) {
        return super.getOne(id);
    }
    
    @GetMapping
    @Override
    public Page<ModelResponse> getAll(Pageable pageable) {
        return super.getAll(pageable);
    }
}
``` 
Then your app is fully setup to perform CRUD operations with your entity.
      
## Install 

You can install the library to your project with help of [JitPack](https://jitpack.io/#Cepr0/generic-crud):
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependensies>
    <dependency>
        <groupId>io.github.cepr0</groupId>
        <artifactId>generic-crud-web</artifactId>
        <version>0.0.3-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>io.github.cepr0</groupId>
        <artifactId>generic-crud-jpa</artifactId>
        <version>0.0.3-SNAPSHOT</version>
    </dependency>
</dependensies>
```

## Demo Application

You can find the demo application with the library usage example in the **demo** module.
