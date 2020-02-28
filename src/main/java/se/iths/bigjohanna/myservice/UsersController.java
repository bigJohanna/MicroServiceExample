package se.iths.bigjohanna.myservice;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

//här ka nvi lägga en @RequestMapping("/api/users")

@Slf4j
@RequestMapping("/api/v1/users")
@RestController
public class UsersController {


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
        log.info("Created " + user);

        var u = repository.save(user);
        log.info("Saved to repository " + u);

        //länkar med EntityModel
    var entityModel = assembler.toModel(u);

   return new ResponseEntity<>(entityModel, HttpStatus.CREATED);

//        return new ResponseEntity<>(assembler.toModel(repository.getOne(u.getId())), ResponseEntity.created(), );
      /*
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(linkTo(UsersController.class).slash(u.getId()).toUri());
        return repository.findById(user.getId())
                .map(assembler::toModel)
                .map(ResponseEntity::created)
                .orElse(ResponseEntity);

       */


//        return new ResponseEntity<User>(u, headers, HttpStatus.CREATED);
    }

    // deleteUser med DeleteMapping(@Pathvariable long id)

    @DeleteMapping("/{id}")
    ResponseEntity<?> deletePerson(@PathVariable Integer id) {
        if (repository.existsById(id)) {
            log.info("User deleted with id " + id);
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    // modifyUser med PatchMapping("/{id}")

    // replaceUser med PutMapping("/{id}")
    @PutMapping("/{id}")
    ResponseEntity<User> replacePerson(@RequestBody User newUser, @PathVariable int id) {
        return repository.findById(id)
                .map(user -> {
                    user.setUserName(newUser.getUserName());
                    user.setRealName(newUser.getRealName());
                    user.setCity(newUser.getCity());
                    user.setIncome(newUser.getIncome());
                    user.setInRelationship(newUser.isInRelationship());
                    repository.save(user);
                    HttpHeaders headers = new HttpHeaders();

                    headers.setLocation(linkTo(UsersController.class).slash(user.getId()).toUri());
                    return new ResponseEntity<>(user, headers, HttpStatus.OK);
                })
                .orElseGet(() ->
                        new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/{id}")
    ResponseEntity<User> modifyPerson(@RequestBody User newUser, @PathVariable int id) {
        return repository.findById(id)
                .map(person -> {
                    if (newUser.getRealName() != null)
                        person.setRealName(newUser.getRealName());

                    repository.save(person);
                    HttpHeaders headers = new HttpHeaders();
                    headers.setLocation(linkTo(UsersController.class).slash(person.getId()).toUri());
                    return new ResponseEntity<>(person, headers, HttpStatus.OK);
                })
                .orElseGet(() ->
                        new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}