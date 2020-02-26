package se.iths.bigjohanna.myservice;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

//här ka nvi lägga en @RequestMapping("/api/users")

@RequestMapping("/api/v1/users")
@RestController
public class UsersController {


    final UsersRepository repository;
    private final UsersModelAssembler assembler;


    //Vi behöver ha en konstruktor med lite injections. Tex UserRepository och en som fixar länkarna
    public UsersController(UsersRepository storage, UsersModelAssembler usersModelAssembler){
        this.repository = storage;
        this.assembler = usersModelAssembler;
    }


//här ska vi ha metoder som get, head, post, put, patch om vi vill.
    //till exempel:
    // getAllUsers med GetMapping
@GetMapping
public CollectionModel<EntityModel<User>> all() {
    return assembler.toCollectionModel(repository.findAll());
}

    // getUserByID -"- och (value = "/{id}")
    @GetMapping(value = "/{id}")
    public ResponseEntity<EntityModel<User>> one(@PathVariable Integer id) {
        return repository.findById(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    // createUser med PostMapping och @RequestBody User user
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        //log.info("POST create Person " + person);
        var u = repository.save(user);
        //log.info("Saved to repository " + p);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(linkTo(UsersController.class).slash(u.getId()).toUri());
        //headers.add("Location", "/api/persons/" + p.getId());
        return new ResponseEntity<>(u, headers, HttpStatus.CREATED);
    }

    // deleteUser med DeleteMapping(@Pathvariable long id)
    // modifyUser med PatchMapping("/{id}")
    // replaceUser med PutMapping("/{id}")





}
