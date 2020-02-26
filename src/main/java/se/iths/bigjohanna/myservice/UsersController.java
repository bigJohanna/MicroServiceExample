package se.iths.bigjohanna.myservice;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping(value = "/{id}")
    public ResponseEntity<EntityModel<User>> one(@PathVariable int id) {
        return repository.findById(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    // getUserByID -"- och (value = "/{id}")
    // createUser med PostMapping och @RequestBody User user
    // deleteUser med DeleteMapping(@Pathvariable long id)
    // modifyUser med PatchMapping("/{id}")
    // replaceUser med PutMapping("/{id}")





}
