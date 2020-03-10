package se.iths.bigjohanna.myservice;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;


@Slf4j
@RequestMapping("/api/v1/users")
@RestController
public class UsersController {

    @Autowired
    private ObjectMapper objectMapper;

    final UsersRepository repository;
    private final UsersModelAssembler assembler;


    //Vi behöver ha en konstruktor med lite injections. Tex UserRepository och en som fixar länkarna
    public UsersController(UsersRepository storage, UsersModelAssembler usersModelAssembler) {
        this.repository = storage;
        this.assembler = usersModelAssembler;
    }


    //här ska vi ha metoder som get, head, post, put, patch om vi vill.
    //till exempel:
    // getAllUsers med GetMapping
    @GetMapping
    public CollectionModel<EntityModel<User>> all() {
        log.info("All persons called");
        return assembler.toCollectionModel(repository.findAll());
    }

    // getUserByID -"- och (value = "/{id}")
    @GetMapping(value = "/{id}")
    public ResponseEntity<EntityModel<User>> one(@PathVariable Integer id) {
        log.info("One person called");
        return repository.findById(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // createUser med: PostMapping och @RequestBody User user
    @PostMapping
    public ResponseEntity<EntityModel<User>> createUser(@RequestBody User user) {

        //409 if conflict, if resource already exists
        if (repository.findById(user.getId()).isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        log.info("Created " + user);
        log.info("Saved to repository " + repository.save(user));

        //länkar med EntityModel
        var entityModel = assembler.toModel(user);

        return new ResponseEntity<>(entityModel, HttpStatus.CREATED);

    }

    @PatchMapping("/{id}")
    ResponseEntity<EntityModel<User>> modifyUser(@RequestBody User incompleteUser, @PathVariable Integer id) throws IOException {

        objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        var u = repository.findById(id);
        if (u.isPresent()) {
            var user = u.get();
            objectMapper.readerForUpdating(user).readValue(objectMapper.writeValueAsString(incompleteUser));
            repository.save(user);
            return new ResponseEntity<>(assembler.toModel(user), HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    // replaceUser med PutMapping("/{id}" changes all fields posted)
    @PutMapping("/{id}")
    ResponseEntity<EntityModel<User>> replaceUser(@RequestBody User newUser, @PathVariable Integer id) {

        if (repository.findById(id).isPresent()) {
            var u = repository.findById(id)
                    .map(user -> {
                        user.setUserName(newUser.getUserName());
                        user.setRealName(newUser.getRealName());
                        user.setCity(newUser.getCity());
                        user.setIncome(newUser.getIncome());
                        user.setInRelationship(newUser.isInRelationship());
                        repository.save(user);
                        log.info("Replaced user with id " + newUser.getId() + " to " + newUser);
                        return user;
                    }).get();
            var entityModel = assembler.toModel(u);
            return new ResponseEntity<>(entityModel, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // deleteUser med DeleteMapping(@Pathvariable long id)
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            log.info("User deleted with id " + id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}