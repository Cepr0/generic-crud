![Maven Central](https://img.shields.io/maven-central/v/io.github.cepr0/generic-crud-parent.svg)
![GitHub repo size](https://img.shields.io/github/repo-size/cepr0/generic-crud.svg)
![GitHub](https://img.shields.io/github/license/cepr0/generic-crud.svg)

### Generic-CRUD

**Generic-CRUD** is a small modular library that allows you to simplify the development 
of [Spring](https://spring.io/) web applications by reducing the writing of the boilerplate code for CRUD operations. 
It implements a full set of base operations with SQL (JPA) database to **C**reate, **R**ead, **U**pdate 
and **D**elete your entities.

#### Get started

1. Inherit your **entity** from abstract [JpaEntity](/jpa/src/main/java/io/github/cepr0/crud/model/JpaEntity.java) class:     
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
2. Extend your entity **repository** from the [JpaRepo](/jpa/src/main/java/io/github/cepr0/crud/repo/JpaRepo.java):
```java
public interface ModelRepo extends JpaRepo<Model, Long> {}
```
3. Prepare request and response **DTOs** of your entity - inherit them from [CrudRequest](/base/src/main/java/io/github/cepr0/crud/dto/CrudRequest.java)
and [CrudResponse](/base/src/main/java/io/github/cepr0/crud/dto/CrudResponse.java) interfaces:
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
4. Prepare a **mapper** between the entity and its DTOs based on [CrudMapper](/base/src/main/java/io/github/cepr0/crud/mapper/CrudMapper.java):
```java
@Mapper(config = CrudMapper.class)
public abstract class ModelMapper implements CrudMapper<Model, ModelRequest, ModelResponse> {
}
```
The library uses [MapStruct](http://mapstruct.org/) framework to generate code of the mappers, 
so you should add it dependency to your project.
Note that you should use `CrudMapper.class` to config your mapper.   

5. Prepare a service which will serve your DTOs and entities, extend it from 
[AbstractCrudService](/base/src/main/java/io/github/cepr0/crud/service/AbstractCrudService.java):
```java
@Service
public class ModelService extends AbstractCrudService<Model, Long, ModelRequest, ModelResponse> {
    public ModelService(@NonNull final ModelRepo repo, @NonNull final ModelMapper mapper) {
        super(repo, mapper);
    }
}
```
6. And finally extend your REST controller from [AbstractCrudController](/web/src/main/java/io/github/cepr0/crud/api/AbstractCrudController.java):
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
Then your application is fully setup to perform CRUD operations.
      
### Install 

```xml
<properties>
    <!-- ... -->
    <mapstruct.version>1.3.0.Final</mapstruct.version>
    <generic-crud.version>0.2.0</generic-crud.version>
</properties>   

<dependensies>
    <!-- ... -->
    <dependency>
        <groupId>io.github.cepr0</groupId>
        <artifactId>generic-crud-web</artifactId>
        <version>${generic-crud.version}</version>
    </dependency>
    <dependency>
        <groupId>io.github.cepr0</groupId>
        <artifactId>generic-crud-jpa</artifactId>
        <version>${generic-crud.version}</version>
    </dependency>
    <!-- ... -->
</dependensies>
```

The library is used [MapStruct](http://mapstruct.org) framework, so you should add its dependency as well:
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
Note that the second `path` in the `annotationProcessorPaths` section is necessary 
if you are using [Lombok](https://projectlombok.org/) in your project. 

### Demo Application

You can find a comprehensive example of the library usage in the **[demo](/demo)** module.
