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
    private final ProposalsRepository proposals;

    public UsersController(UsersRepository users, ProposalsRepository proposals) {
        this.users = users;
        this.proposals = proposals;
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
            throw new NotFoundException(String.format("User with id %d does not exist.", id));
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
                throw new NotFoundException(String.format("User with id %d does not exist.", id));
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
            throw new NotFoundException(String.format("User with id %d does not exist.", id));
    }

    // Obter a lista de propostas que o User {id} pode aprovar
    @GetMapping("/{id}/approverInProposals")
    Iterable<Proposal> getUserApproverProposals(@PathVariable long id){
        Optional<User> u = users.findById(id);
        if(u.isPresent()){
            return u.get().getProposalsToApprove();
        }
        else throw new NotFoundException(String.format("User with id %d does not exist.", id));
    }

    // Add proposta à lista de propostas para o User {id} aprovar
    @PostMapping("/{id}/approverInProposals")
    void addUserApproverProposal(@PathVariable long id, @RequestBody Proposal proposal){
        Optional<User> u = users.findById(id);
        if(u.isPresent()){
            if(!u.get().getProposalsToApprove().contains(proposal))
                u.get().addProposalToApprove(proposal); //TODO: check if this is enough... (need to save user?)
            else throw new ConflictException(String.format("Proposal already approved by the user with id %d", id));
        }
        else throw new NotFoundException(String.format("User with id %d does not exist.", id));
    }

    // Apaga proposta {pid} à lista de propostas que o User {uid} tem de aprovar
    @DeleteMapping("/{uid}/approverInProposals/{pid}")
    void deleteUserApproverInProposal(@PathVariable long uid, @PathVariable long pid) {
        Optional<User> u = users.findById(uid);
        if(u.isPresent()){
            Optional<Proposal> p = proposals.findById(pid);
            if(p.isPresent() && u.get().getProposalsToApprove().contains(p.get()))
                u.get().removeProposalToApprove(p.get()); //TODO: check if this is enough... (need to save user?)
            else throw new NotFoundException(String.format("User with id %d does not have proposal id %d to approve.", uid, pid));
        }
        else throw new NotFoundException(String.format("User with id %d does not exist.", uid));
    }
}
