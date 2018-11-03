package pt.unl.fct.ciai.controller;

import org.springframework.web.bind.annotation.*;
import pt.unl.fct.ciai.exceptions.BadRequestException;
import pt.unl.fct.ciai.exceptions.ConflictException;
import pt.unl.fct.ciai.exceptions.NotFoundException;
import pt.unl.fct.ciai.model.Proposal;
import pt.unl.fct.ciai.model.User;
import pt.unl.fct.ciai.repository.ProposalsRepository;
import pt.unl.fct.ciai.repository.UsersRepository;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersRepository users;
    private final ProposalsRepository toApprove;

    public UsersController(UsersRepository users, ProposalsRepository toApprove) {
        this.users = users;
        this.toApprove = toApprove;
    }

    @GetMapping("")
    Iterable<User> getAllUsers(@RequestParam(required = false) String search){
        if(search == null)
            return users.findAll();
        else
            return users.searchUsers(search);
    }

    @GetMapping("/{id}")
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

    @PutMapping("/{id}")
    void updateUser(@PathVariable long id, @RequestBody User user){
        if(user.getId() == id) {
            Optional<User> u1 = users.findById(id);
            if (u1.isPresent())
                users.save(user);
            else
                throw new NotFoundException("User with id "+id+" does not exist.");
        }
        else
            throw new BadRequestException("Invalid request: userID on request does not match the UserClass id attribute");
    }

    @DeleteMapping("/{id}")
    void deleteUser(@PathVariable long id) {
        Optional<User> u1 = users.findById(id);
        if( u1.isPresent() ) {
            users.delete(u1.get());
        } else
            throw new NotFoundException("User with id "+id+" does not exist.");
    }

    // Obter a lista de propostas que o User {id} pode aprovar
    @GetMapping("/{id}/approverInProposals")
    Iterable<Proposal> getUserApproverProposals(@PathVariable long id){
        Optional<User> u = users.findById(id);
        if(u.isPresent()){
            return u.get().getProposalsToApprove();
        }
        else throw new NotFoundException("User with id" +id+" not found.");
    }

    // Add proposta à lista de propostas para o User {id} aprovar
    @PostMapping("/{id}/approverInProposals")
    void addUserApproverProposal(@PathVariable long id, @RequestBody Proposal proposal){
        Optional<User> u = users.findById(id);
        if(u.isPresent()){
            if(!u.get().getProposalsToApprove().contains(proposal))
                toApprove.save(proposal);
            else throw new ConflictException("Proposal already to be approved by the user " + id);
        }
        else throw new NotFoundException("User "+id+" not found.");
    }

    // Apaga proposta {pid} à lista de propostas que o User {uid} tem de aprovar
    @DeleteMapping("/{uid}/approverInProposals/{pid}")
    void deleteUserApproverInProposal(@PathVariable long uid, @PathVariable long pid) {
        Optional<User> u = users.findById(uid);
        if(u.isPresent()){
            Optional<Proposal> p = toApprove.findById(pid);
            if(p.isPresent() && u.get().getProposalsToApprove().contains(p.get()))
                toApprove.deleteById(pid);
            else throw new NotFoundException("User "+uid+" does not have proposal "+pid+" to approve.");
        }
        else throw new NotFoundException("User "+uid+" not found.");
    }
}
