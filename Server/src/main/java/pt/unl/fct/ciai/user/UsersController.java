package pt.unl.fct.ciai.user;

import org.springframework.web.bind.annotation.*;
import pt.unl.fct.ciai.exceptions.BadRequestException;
import pt.unl.fct.ciai.exceptions.NotFoundException;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersRepository users;

    public UsersController(UsersRepository users) {
        this.users = users;
    }

    @GetMapping("")
    Iterable<User> getAllUsers(@RequestParam(required = false) String search){
        if(search == null)
            return users.findAll();
        else
            return users.searchUsers(search);
    }

    @GetMapping("{id}")
    User getUserById(@PathVariable long id){
        Optional<User> u1 = users.findById(id);
        if(u1.isPresent())
            return u1.get();
        else{
            throw new NotFoundException("User with id "+id+" does not exist.");
        }
    }

    @PostMapping("")
    void addUser(@RequestBody User user){
        users.save(user);
    }

    @PutMapping("{id}")
    void updateUser(@PathVariable long id, @RequestBody User user){
        if(user.getId() == id) {
            Optional<User> u1 = users.findById(id);
            if (u1.isPresent())
                users.save(user);
            else
                throw new NotFoundException("User with id "+id+" does not exist.");
        }
        else
            throw new BadRequestException("invalid request");
    }

    @DeleteMapping("{id}")
    void deleteUser(@PathVariable long id) {
        Optional<User> u1 = users.findById(id);
        if( u1.isPresent() ) {
            users.delete(u1.get());
        } else
            throw new NotFoundException("User with id "+id+" does not exist.");
    }
}