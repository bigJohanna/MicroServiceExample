package se.iths.bigjohanna.myservice;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UsersModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {


    @Override
    public EntityModel<User> toModel(User entity) {
        return null;
    }

    @Override
    public CollectionModel<EntityModel<User>> toCollectionModel(Iterable<? extends User> entities) {
        return null;
    }
}
