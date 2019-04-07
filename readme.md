## Generic-CRUD

**Generic-CRUD** is a handy small library that can help exclude writing of boilerplate code for **CRUD** operations 
in Spring web applications. It implements a full set of base operations with a database to **C**reate, **R**ead, **U**pdate 
and **D**elete your entities.

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
@Mapper(config = CrudMapper.class)
public abstract class ModelMapper implements CrudMapper<Model, ModelRequest, ModelResponse> {
}
```
Note that you should use 'CrudMapper.class' to config your mapper. The library uses [MapStruct](http://mapstruct.org/) framework, 
so you should add it dependency to your project.  

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
    public ResponseEntity<ModelResponse> create(@Valid @RequestBody @NonNull final ModelRequest request) {
        return super.create(request);
    }
    
    @PatchMapping("/{id}")
    @Override
    public ResponseEntity<ModelResponse> update(@PathVariable("id") @NonNull final Long id, @Valid @RequestBody @NonNull final ModelRequest request) {
        return super.update(id, request);
    }
    
    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity delete(@PathVariable("id") @NonNull final Long id) {
        return super.delete(id);
    }
    
    @GetMapping("/{id}")
    @Override
    public ResponseEntity<ModelResponse> getOne(@PathVariable("id") @NonNull final Long id) {
        return super.getOne(id);
    }
    
    @GetMapping
    @Override
    public ResponseEntity<List<ModelResponse>> getAll() {
        return super.getAll();
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

<properties>
    <!-- ... -->
    <mapstruct.version>1.3.0.Final</mapstruct.version>
    <generic-crud.version>0.0.5-SNAPSHOT</generic-crud.version>
</properties>   

<dependensies>
    <dependency>
        <groupId>com.github.Cepr0.generic-crud-new</groupId>
        <artifactId>generic-crud-web</artifactId>
        <version>${generic-crud.version}</version>
    </dependency>
    <dependency>
        <groupId>com.github.Cepr0.generic-crud-new</groupId>
        <artifactId>generic-crud-jpa</artifactId>
        <version>${generic-crud.version}</version>
    </dependency>
</dependensies>
```

The library is used [MapStruct](http://mapstruct.org) framework, so you must add its dependency to your project as well:
```xml
<dependensies>
    <!-- ... -->
    
    <dependency>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct</artifactId>
        <version>${mapstruct.version}</version>
        <scope>provided</scope>
    </dependency>
    
    <!-- ... -->    
</dependensies>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <configuration>
                <annotationProcessorPaths>
                    <path>
                        <groupId>org.mapstruct</groupId>
                        <artifactId>mapstruct-processor</artifactId>
                        <version>${mapstruct.version}</version>
                    </path>
                    <path>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok</artifactId>
                        <version>${lombok.version}</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
```
Note that the second 'path' section is necessary only if you are using [Lombok](https://projectlombok.org/) in your project. 

## Demo Application

You can find a comprehensive example of the library usage in the **[demo](/demo)** module.
