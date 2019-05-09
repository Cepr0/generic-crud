# Generic CRUD

[![Maven Central](https://img.shields.io/maven-central/v/io.github.cepr0/generic-crud-parent.svg)](https://search.maven.org/search?q=g:io.github.cepr0)
[![GitHub repo size](https://img.shields.io/github/repo-size/cepr0/generic-crud.svg)](https://github.com/Cepr0/generic-crud/releases/latest)
[![License](https://img.shields.io/github/license/cepr0/generic-crud.svg)](LICENSE.md)

**Generic CRUD** is a small modular and expandable library that allows you to eliminate the writing of boilerplate code for [CRUD](https://en.wikipedia.org/wiki/Create,_read,_update_and_delete) operations in the development of [Spring](https://spring.io/) applications that work with databases. It implements a full set of base operations to **C**reate, **R**ead, **U**pdate and **D**elete your entities. Currently, it works with **JPA databases** and **MongoDB** but you can expand it to work with other databases.  

- [Quick start](#quick-start)
- [Demo](#demo)
- [Requirements](#requirements)
- [Installation](#installation)
- [Features](#features)
  - [Entities](#entities)
  - [DTOs](#dtos)
  - [Repositories](#repositories)
  - [Mappers](#mappers)
  - [Services](#services)
    - [Pre-processing](#pre-processing)
    - [Post-processing](#post-processing)
  - [Controllers](#controllers)
    - [Validation groups](#validation-groups)
    - [Page serializer](#page-serializer)
  - [Expandability](#expandability)
- [Structure](#structure)

## Quick start

Assume that you work with JPA database.

1. Inherit your **entity** abstract [JpaEntity](/jpa/src/main/java/io/github/cepr0/crud/model/JpaEntity.java) class:     
```java
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Model extends JpaEntity<Integer> {
    @Id
    @GeneratedValue
    private Integer id;
    
    private String name;
}
```
2. Extend your entity **repository** from [JpaRepo](/jpa/src/main/java/io/github/cepr0/crud/repo/JpaRepo.java):
```java
public interface ModelRepo extends JpaRepo<Model, Integer> {}
```
3. Prepare request and response **DTOs** of your entity - inherit them from [CrudRequest](/base/src/main/java/io/github/cepr0/crud/dto/CrudRequest.java)
and [CrudResponse](/base/src/main/java/io/github/cepr0/crud/dto/CrudResponse.java) interfaces:
```java
@Data
public class ModelRequest implements CrudRequest {
   private String name;
}

@Data
public class ModelResponse implements CrudResponse<Integer> {
    private Integer id;
    private String name;
}
```
4. Prepare a **mapper** between the entity and its DTOs based on [CrudMapper](/base/src/main/java/io/github/cepr0/crud/mapper/CrudMapper.java):
```java
@Mapper(config = CrudMapper.class)
public abstract class ModelMapper implements CrudMapper<Model, ModelRequest, ModelResponse> {
}
```
(The library uses [MapStruct](http://mapstruct.org/) framework to generate code of the mappers, so you have to add its dependency to your project. Note that you should use `CrudMapper.class` to config your mapper. )  

5. Prepare a service which will serve your DTOs and entities, extending it from 
[AbstractCrudService](/base/src/main/java/io/github/cepr0/crud/service/AbstractCrudService.java):
```java
@Service
public class ModelService extends AbstractCrudService<Model, Integer, ModelRequest, ModelResponse> {
    public ModelService(ModelRepo repo, ModelMapper mapper) {
        super(repo, mapper);
    }
}
```
6. And finally extend your REST controller from [AbstractCrudController](/web/src/main/java/io/github/cepr0/crud/api/AbstractCrudController.java):
```java
@RestController
@RequestMapping("models")
public class ModelController extends AbstractCrudController<Model, Integer, ModelRequest, ModelResponse> {

    public ModelController(ModelService service) {
        super(service);
    }
    
    @PostMapping
    @Override
    public ResponseEntity<ModelResponse> create(@Valid @RequestBody ModelRequest request) {
        return super.create(request);
    }
    
    @PatchMapping("/{id}")
    @Override
    public ResponseEntity<ModelResponse> update(@PathVariable("id") Integer id, @Valid @RequestBody ModelRequest request) {
        return super.update(id, request);
    }
    
    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity delete(@PathVariable("id") Integer id) {
        return super.delete(id);
    }
    
    @GetMapping("/{id}")
    @Override
    public ResponseEntity<ModelResponse> getOne(@PathVariable("id") Integer id) {
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

If you need to work with MongoDB you should extend your entities (documents) from [IdentifiableEntity](/model/src/main/java/io/github/cepr0/crud/model/IdentifiableEntity.java), and your repositories from [MongoRepo](/mongo/src/main/java/io/github/cepr0/crud/repo/MongoRepo.java):

```java
@Data
@Document
public class Model implements IdentifiableEntity<String> {
   @Id private String id;
   // other stuff
}

public interface ModelRepo extends MongoRepo<Model, String> {
}
```

Other steps (from 3 to 6) are the same.

## Demo

А comprehensive example of using the library with JPA database you can find in the **[demo](/demo)** module.

## Requirements

The library works with Java 8+, Spring Framework 4.3+ (Spring Boot 1.5+) and MapStruct 1.3+.

## Installation 

Depending on the type of your database, add `io.github.cepr0:generic-crud-jpa` or `io.github.cepr0:generic-crud-mongo` dependency to your project. Additionally you can add `io.github.cepr0:generic-crud-web` if you have a web layer in your application and if you want to use `AbstractCrudCtroller` (and other features) from this module:

```xml
<properties>
    <!-- ... -->
    <generic-crud.version>0.3.1</generic-crud.version>
</properties>   

<dependensies>
    <!-- ... -->
  
    <!-- For JPA databases -->
    <dependency>
        <groupId>io.github.cepr0</groupId>
        <artifactId>generic-crud-jpa</artifactId>
        <version>${generic-crud.version}</version>
    </dependency>
  
    <!-- For MongoDB -->
    <dependency>
        <groupId>io.github.cepr0</groupId>
        <artifactId>generic-crud-mongo</artifactId>
        <version>${generic-crud.version}</version>
    </dependency>

    <dependency>
        <groupId>io.github.cepr0</groupId>
        <artifactId>generic-crud-web</artifactId>
        <version>${generic-crud.version}</version>
    </dependency>
  
    <!-- ... -->
</dependensies>
```

The library uses [MapStruct](http://mapstruct.org) framework, so add its dependency and [configuration](http://mapstruct.org/documentation/dev/reference/html/#setup):
```xml
<properties>
    <!-- ... -->     
    <mapstruct.version>1.3.0.Final</mapstruct.version>
</properties>   

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
only if you are using [Lombok](https://projectlombok.org/) in your project. 

## Features

Main components of the library and the common data flow are shown on the following diagram:

![Generic CRUD main components and data flow](diagram.png)

It assumes that the incoming `CrudRequest`s (data transfer objects – DTOs) go through the `CrudController`, transform to the related entities (T) in the `CrudService` with `CrudMapper` , and are processed in the `CrudRepo`. Then the processed entities return to the `CrudService`, where they are converted with `CrudMapper` to `CrudResponse` DTOs and return to the `CrudController`.

### Entities

In order to work with CRUD operations, your entities should implement `IdentifiableEntity` marker interface:

```java
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Model implements IdentifiableEntity<Integer> {
   @Id
   @GeneratedValue
   private Integer id;   
   // other stuff
}

@Data
@Document
public class Model implements IdentifiableEntity<String> {
   @Id private String id;
   // other stuff
}
```

In JPA environment, instead of `IdentifiableEntity`, you can extend the entities from the convenient **`JpaEntity`** class. It inherits `IdentifiableEntity` and also overrides `eguals()`, `hashCode()` and `toString()` methods. Implementation of those methods is based on the entity <u>identifier</u> only, `eguals()` and `hashCode()` methods behave consistently across all entity state transitions (see details [here](https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/)).

### DTOs

The library assumes that all incoming requests and outgoing responses are data transfer objects (not entities). All request DTOs should implement `CrudRequest` marker interface, and all response DTOs – `CrudResponses` interface. Unlike requests, the responses must implement the identifier getter, for example:

```java
@Data
public class ModelRequest implements CrudRequest {
   private String name;
}

@Data
public class ModelResponse implements CrudResponse<Integer> {
   private Integer id;
   private String name;
}
```

### Repositories

If you work with JPA database your repositories should inherit `JpaRepo` interface, which, in turn, inherits the base interface `CrudRepo` and standard `JpaRepository` repository. If you work with MongoDB you should extend your repositories from `MongoRepo`, which extends `MongoRepository`. 
Therefore all your repositories have the functionality of both `CrudRepo` and `JpaRepository` / `MongoRepository`. 

There are the following main methods of `JpaRepo`:

- `create()` – creates a new entity
- `update()` – updates one entity by its `id`
- `del()` – delete one entity by its `id`
- `getById()` – read one entity by its `id`
- `getAll()` – read all entities

and two auxiliary methods:

- `getToUpdateById()`
- `getToDeleteById()`

which are used in the `update()` and `delete()` methods to read entities from the database before they are updated or deleted respectively. 

You can override the 'read' methods in your repository to provide a custom functionality. Here is, for example, an implementation of 'soft delete' feature:

```java
public interface ModelRepo extends JpaRepo<Model, Integer> {

   // Overriding the 'delete' method of JpaRepository
   @Override
   default void delete(Model model) {
      model.setDeleted(true);
   }

   @Query("select m from Model m where m.id = ?1 and m.deleted = false")
   @Override
   Optional<Model> getToDeleteById(Integer id);

   @Query("select m from Model m where m.id = ?1 and m.deleted = false")
   @Override
   Optional<Model> getToUpdateById(Integer id);

   @Query("select m from Model m where m.id = ?1 and m.deleted = false")
   @Override
   Optional<User> getById(Integer id);

   @Query("select m from Model m where m.deleted = false")
   @Override
   Page<User> getAll(Pageable pageable);
}
```

This example assumed that `Model` entity has the boolean property `deleted`.

### Mappers

Mappers are used to convert DTOs to entities and vice versa. This is done automatically thanks to [MapStruct](http://mapstruct.org/) framework. All mappers must be abstract classes or interfaces, have MapStruct `@Mapper` annotation (with default configuration from `CrudMapper`), and implement/extend `CrudMapper` interface:

```java
@Mapper(config = CrudMapper.class)
public abstract class ModelMapper implements CrudMapper<Model, ModelRequest, ModelResponse> {
}
```

Then MapStruct will be able to generate `ModelMapperImpl` as Spring bean for you.

`CrudMapper` has three methods for DTO/entity transformation:

- `T toCreate(CrudRequest request)` is used in **create** operations to convert `CrudRequest` into the entity `T`;
- `T toUpdate(CrudRequest request, @MappingTarget T target)` is used in **update** operations to update the target entity `T` with data of `CrudRequest` DTO;
- `CrudResponse toResponse(T entity)` is used to convert entities `T` to response `CrudResponse` DTOs.

You can override these methods in your mappers to make custom changes, for example:

```java
@Mapping(target = "modelId", source = "id")
@Override
public abstract ModelResponse toResponse(Model model);
```

More info of how to customize the mapping you can find in the MapStruct [documentation](mapstruct.org/documentation/dev/reference/html).

Note that you have to use the default configuration `@Mapper(config = CrudMapper.class)` to have the mappers generated properly. The configuration in the `CrudMapper` is the following:

```java
@MapperConfig(
    nullValueMappingStrategy = RETURN_DEFAULT,
    nullValueCheckStrategy = ALWAYS,
    nullValuePropertyMappingStrategy = IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = "spring"
)
```

This configuration assumes that:

- the target bean type is always instantiated and returned regardless of whether the source is `null` or not;
- the source property values are always checked for `null`;
- if a source bean property equals `null` the target bean property will be ignored and retain its existing value;
- the mapper implementation will have Spring `@Component` annotation; 

If you have complex entity that has an association with the second one, you can configure your mapper (with `uses` parameter) to use the repository of the second entity, to provide the mapping from its identifier to its reference (with `getOne` method of repository), and the mapper of the second entity, to provide the mapping from the entity to its response DTO. For example:

```java
@Entity
public class Person implements IdentifiableEntity<Integer> {
    // ...
    
    @OneToMany(mappedBy = "person")
    private Set<Car> cars;
}

@Entity
public class Car implements IdentifiableEntity<Integer> {
    // ...
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Person person;
}

@Data
public class PersonResponse implements CrudResponse<Integer> {
    private Integer id;
    private String name;
    @JsonIgnoreProperties("person") private Set<CarResponse> cars;
}

@Data
public class CarResponse implements CrudResponse<Integer> {
    private Integer id;
    private String name;
    @JsonIgnoreProperties("cars") private PersonDto person;
}

@Data
public class PersonDto {
    private Integer id;
    private String name;
}

@Mapper(config = CrudMapper.class, uses = {CarRepo.class, CarMapper.class})
public abstract class PersonMapper implements CrudMapper<Person, PersonRequest, PersonResponse> {
   public abstract PersonDto toPersonDto(Person person);
}

@Mapper(config = CrudMapper.class, uses = {PersonRepo.class, PersonMapper.class})
public abstract class CarMapper implements CrudMapper<Car, CarRequest, CarResponse> {
}
```

### Services

If you want your service to perform CRUD operations you have to simply inherit it from `AbstractCrudService`:

```java
@Service
public class ModelService extends AbstractCrudService<Model, Integer, ModelRequest, ModelResponse> {
    public ModelService(ModelRepo repo, ModelMapper mapper) {
       super(repo, mapper);
    }
}
```

#### Pre-processing

If you need to perform some pre-processing of your DTOs and entities in 'create' and 'update' operations you can override 'callback' methods `onCreate(CrudRequest request, T entity)` and `onUpdate(CrudRequest request, T entity)` of `AbstractCrudService`, for example:

```java
@Override
protected void onCreate(ModelRequest request, Model model) {
   model.setCreatedAt(Instant.now());
   model.setUpdatedAt(Instant.now());
   if (request.getFoo() > 5) {
      model.setBar("set five");
   }
}

@Override
protected void onUpdate(ModelRequest request, Model model) {
   user.setUpdatedAt(Instant.now());
   if (model.getBar() == null && request.getFoo() > 5) {
      model.setBar("set five");
   }
}
```

Note that these methods are called in the `AbstractCrudService` just after mapping and before performing a corresponding operation - create or update, so in the 'entity' parameter you will have already updated data from `CrudRequest` DTO. 

#### Post-processing

If you need some post-processing of your entities you can use another feature – entity events. Events are published by  `AbstractCrudService` after the entities are created, updated or deleted. All published events contain corresponding entities.  Events can be handled like any other application events, for example using `@EventListener` or `@TransactionalEventListener` annotations. To publish the events the service invokes its factory callback methods: `onUpdateEvent(T entity)`, `onUpdateEvent(T entity)` and `onDeleteEvent(T entity)` right before performing the corresponding operation. If such a method returns a non-null event then the service publishes it (by default these methods return `null`):

```java
@Override
protected UpdateModelEvent onUpdateEvent(Model model) {
   return new UpdateModelEvent(model);
}

@Async
@TransactionalEventListener
public void handleUpdateModelEvent(UpdateModelEvent event) {
   Model model = event.getEntity();
   log.info("Model: {}", model);
}
```

You should create such events by extending the [EntityEvent](/model/src/main/java/io/github/cepr0/crud/event/EntityEvent.java) class:

```java
public class UpdateModelEvent extends EntityEvent<Model> {
   public UpdatePersonEvent(Model model) {
      super(model);
   }
}
```

### Controllers

The library provides `AbstractCrudController` class – a simple abstract implementation of REST controller that support CRUD operations, and which you can use in your application:

```java
@RestController
@RequestMapping("models")
public class ModelController extends AbstractCrudController<Model, Integer, ModelRequest, ModelResponse> {

    public ModelController(ModelService service) {
        super(service);
    }
    
    @PostMapping
    @Override
    public ResponseEntity<ModelResponse> create(@RequestBody ModelRequest request) {
        return super.create(request);
    }
    
    @GetMapping("/{id}")
    @Override
    public ResponseEntity<ModelResponse> getOne(@PathVariable("id") Integer id) {
        return super.getOne(id);
    }
    
    @GetMapping
    @Override
    public ResponseEntity<List<ModelResponse>> getAll() {
        return super.getAll();
    }
}
```

`AbstractCrudController` returns the following data and HTTP statuses:

| Operation | Returned data                                                | HTTP status                    |
| --------- | ------------------------------------------------------------ | ------------------------------ |
| Create    | DTO of created object                                        | 201 Created                    |
| Update    | DTO of updated object / Empty body if object is not found by its ID | 200 OK / 404 Not Found         |
| Delete    | Empty body                                                   | 204 No Content / 404 Not Found |
| Get one   | DTO of found object / Empty body if object is not found by its ID | 200 OK / 404 Not Found         |
| Get all   | Page or List with DTOs of objects                            | 200 OK                         |

While using this abstract controller you shouldn't forget to provide your 'mapping' annotations as well as other annotations such as `@RequestBody`, `@PathVariable` and so on.

Instead of using `AbstractCrudController` you can use your own controller from scratch but don't forget to inject your implementation of `CrudService` to it:

```java
@RestController
@RequestMapping("models")
public class ModelController {

    private final ModelService service;
    
    public ModelController(ModelService service) {
        this.service = service;
    }
    
    @PostMapping
    @Override
    public ResponseEntity<ModelResponse> create(@RequestBody ModelRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }
}
```

#### Validation groups

To separate the validation of your incoming DTOs in two groups - for create and update operations, you can use `OnCreate` and `OnUpdate` interfaces provided by the library:

```java
@Data
public class ModelRequest implements CrudRequest {
    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @NotNull(groups = OnCreate.class)
    private Integer age;
}

@RestController
@RequestMapping("models")
public class ModelController extends AbstractCrudController<Model, Integer, ModelRequest, ModelResponse> {

    public ModelController(ModelService service) {
        super(service);
    }
    
    @PostMapping
    @Override
    public ResponseEntity<ModelResponse> create(@Validated(OnCreate.class) @RequestBody ModelRequest request) {
        return super.create(request);
    }
  
    @PatchMapping("/{id}")
    @Override
    public ResponseEntity<ModelResponse> update(@PathVariable("id") Integer id, @Validated(OnUpdate.class) @RequestBody ModelRequest request) {
        return super.update(id, request);
    }
}
```

#### Page serializer

Method `getAll(Pageable pageable)` of `AbstractCrudController` return the [Page](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/domain/Page.html) object with content of outbound DTOs. The library provide the customizable [CrudPageSerializer](/web/src/main/java/io/github/cepr0/crud/api/CrudPageSerializer.java) which you can use in your applications to render a `Page` object:

```json
{
  "models": [
    {
      "id": 2,
      "name": "model2"
    },
    {
      "id": 1,
      "name": "model1"
    },
  ],
  "page": {
    "number": 1,
    "size": 20,
    "total": 1,
    "first": false,
    "last": false
  },
  "elements": {
    "total": 2,
    "exposed": 2
  },
  "sort": [
    {
      "property": "id",
      "direction": "DESC"
    }
  ]
}
```

To use this serializer you can, for example, extend it with your own one and add [JsonComponent](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/jackson/JsonComponent.html) annotation:

```java
@JsonComponent
public class CustomPageSerializer extends CrudPageSerializer {
}
```

You can customized a name of every field of that view. To customize the field names of `page`, `elements` and `sort` sections you can just set your value to the corresponding protected property of [CrudPageSerializer](/web/src/main/java/io/github/cepr0/crud/api/CrudPageSerializer.java). To change the name of the 'content' property you can use annotation `@ContentAlias` with your response DTO, or replace the value of `contentAliasMode` , the protected property of [CrudPageSerializer](/web/src/main/java/io/github/cepr0/crud/api/CrudPageSerializer.java), to change the behavior of naming the 'content' property: 

```java
@Data
@ContentAlias("my_models")
public class ModelResponse implements CrudResponse<Integer> {   
   private Integer id;
   private String name;
}

@JsonComponent
public class CustomPageSerializer extends CrudPageSerializer {
   public CustomPageSerializer() {
      elementsExposed = "on_page";
      contentAliasMode = ContentAliasMode.SNAKE_CASE;
   }
}
```

The first option in provided example above replaces the 'content' field to `my_models`:

```
{
  "my_models" [...],
  "page": ...
  ...
}
```

The second option replaces the 'content' field to 'snake case' variant of the response DTO class name and changes the `elements.exposed` name to  `elements.on_page`:

```
{
  "model_responses" [...],
  "page": ...,
  "elements": {
    "total": 2,
    "on_page": 2
  },
  ... 
}
```

Available options of `ContentAliasMode`:

- `FIRST_WORD` – the plural form of the first word of the "content" class is used (e.g. `ModelResponse` -> `models`). This is a default value of `contentAliasMode` property of `CrudPageSerializer`;
- `SNAKE_CASE` – a "snake case" of the "content" class in the plural form is used (e.g. `ModelResponse` -> `model_reponses`);
- `CAMEL_CASE` – a "camel case" of the "content" class in the plural form is used (e.g. `ModelResponse` -> `modelReponses`);
- `DEFAULT_NAME` – the value of `defaultContentName` of `CrudPageSerializer` is used (`content` by default).

The `@ContentAlias` has the higher priority than the `ContentAliasMode`.

### Expandability

Currently, the library support JPA databases and MongoDB, but you can expand it by implementing the [CrudRepo](/base/src/main/java/io/github/cepr0/crud/repo/CrudRepo.java) interface for another database type. The new module will work with other modules of the library without their modifications.

## Structure

The library is separated into the following modules: 

- generic-crud-model
- generic-crud-base
- generic-crud-jpa
- generic-crud-mongo
- generic-crud-web

**Model** module contains base classes such as `IdentifiableEntity` and `EntityEvent` and **doesn't have any dependencies**. 
You can freely include it in your 'model' module without worrying about unnecessary dependencies 
(if you have for example a multi-module project, where your model classes locate in the dedicated module).

**Base** module contains interfaces of base elements like `CrudRepo`, `CrudMapper` and `CrudService`, 
and the abstract implementation of the last one – `AbstractCrudService`. The module depends on **model** module, as well as on external non-transitive dependencies: 

- `org.springframework:spring-tx`
- `org.springframework:spring-context`
- `org.springframework.data:spring-data-commons`
- `org.mapstruct:mapstruct`

**JPA** module contains `JpaRepo` – the 'implementation' of `CrudRepo` that extends `JpaRepository`, so that you can work with any JPA-supported database. The module depends on **base** module and external non-transitive dependency:

- `org.springframework.data:spring-data-jpa`

You can use this module in the applications where the `spring-data-jpa` and all external dependencies of **base** module are present (for example in Spring-Boot application with `spring-boot-starter-data-jpa` starter).

**Mongo** module contains `MongRepo` – the 'implementation' of `CrudRepo` that extends `MongoRepository`. The module depends on **base** module and external non-transitive dependency:

- `org.springframework.data:spring-data-mongodb`

You can use this module in the applications where the `spring-data-mongodb` and all external dependencies of **base** module are present.

**Web** module contains an abstract implementation of REST controller – `AbstractCrudController` (and other related classes). 
This module depends on **base** module and external dependencies:

- `org.atteo:evo-inflector`
- `org.springframework:spring-web`
- `org.springframework.data:spring-data-commons` 
- `com.fasterxml.jackson.core:jackson-databind`

All external dependencies, except `evo-inflector`, are non-transitive. You can use the **web** module in the applications where those external dependencies (and all external dependencies of **base** module) are present (for example in the Spring-Boot application with `spring-boot-starter-web` and `spring-boot-starter-data-jpa` starters).

