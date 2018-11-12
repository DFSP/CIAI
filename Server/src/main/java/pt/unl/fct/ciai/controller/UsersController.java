package pt.unl.fct.ciai.controller;

import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.unl.fct.ciai.api.UsersApi;
import pt.unl.fct.ciai.assembler.BidProposalResourceAssembler;
import pt.unl.fct.ciai.assembler.ProposalResourceAssembler;
import pt.unl.fct.ciai.assembler.UserProposalResourceAssembler;
import pt.unl.fct.ciai.assembler.UserResourcesAssembler;
import pt.unl.fct.ciai.exception.BadRequestException;
import pt.unl.fct.ciai.exception.NotFoundException;
import pt.unl.fct.ciai.model.Proposal;
import pt.unl.fct.ciai.model.User;
import pt.unl.fct.ciai.security.CanDeleteUser;
import pt.unl.fct.ciai.security.CanModifyUser;
import pt.unl.fct.ciai.security.CanReadOneProposal;
import pt.unl.fct.ciai.security.CanReadUserProposal;
import pt.unl.fct.ciai.service.UsersService;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping(value = "/users", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class UsersController implements UsersApi {

    private final UsersService usersService;

    private final UserResourcesAssembler userAssembler;
    private final BidProposalResourceAssembler bidProposalAssembler;
    private final UserProposalResourceAssembler userProposalAssembler;

    public UsersController(UsersService usersService,
                           UserResourcesAssembler userAssembler, BidProposalResourceAssembler bidProposalAssembler,
                           UserProposalResourceAssembler userProposalAssembler) {
        this.usersService = usersService;
        this.userAssembler = userAssembler;
        this.bidProposalAssembler = bidProposalAssembler;
        this.userProposalAssembler = userProposalAssembler;
    }

    @GetMapping
    @CanReadUserProposal
    public ResponseEntity<Resources<Resource<User>>> getUsers(@RequestParam(required = false) String search) {
        Iterable<User> users = usersService.getUsers(search);
        Resources<Resource<User>> resources = userAssembler.toResources(users);
        return ResponseEntity.ok(resources);
    }

    @PostMapping
    public ResponseEntity<Resource<User>> addUser(@Valid @RequestBody User user) throws URISyntaxException {
        if (user.getId() > 0) {
            throw new BadRequestException(String.format("Expected non negative user id, instead got %d", user.getId()));
        }
        User newUser = usersService.addUser(user);
        Resource<User> resource = userAssembler.toResource(newUser);
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource<User>> getUser(@PathVariable("id") long id) {
        User user = getUserIfPresent(id);
        Resource<User> resource = userAssembler.toResource(user);
        return ResponseEntity.ok(resource);
    }

    @PutMapping("/{id}")
    @CanModifyUser
    public ResponseEntity<?> updateUser(@PathVariable("id") long id, @RequestBody User user) {
        if (id != user.getId()) {
            throw new BadRequestException(String.format("Path id %d and user id %d don't match.", id, user.getId()));
        }
        usersService.updateUser(user);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @CanDeleteUser
    public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
        usersService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/proposals")
    public ResponseEntity<Resources<Resource<Proposal>>> getProposals(
            @PathVariable("id") long id, @RequestParam(value = "search", required = false) String search) {
        User user = getUserIfPresent(id);
        Iterable<Proposal> proposals = usersService.getProposals(id, search);
        Resources<Resource<Proposal>> resources = userProposalAssembler.toResources(proposals, user);
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{uid}/proposals/{pid}")
    @CanReadOneProposal
    public ResponseEntity<Resource<Proposal>> getProposal(
            @PathVariable("uid") long uid, @PathVariable("pid") long pid) {
        User user = getUserIfPresent(uid);
        Proposal proposal = usersService.getProposal(uid, pid).orElseThrow(() ->
                new NotFoundException(String.format("Proposal with id %d doesn't have a member/staff with id %d", pid, uid)));
        Resource<Proposal> resource = userProposalAssembler.toResource(proposal, user);
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/{id}/bids")
    public ResponseEntity<Resources<Resource<Proposal>>> getBids(
            @PathVariable("id") long id, @RequestParam(value = "search", required = false) String search) {
        User user = getUserIfPresent(id);
        Iterable<Proposal> proposals = usersService.getBids(id, search);
        Resources<Resource<Proposal>> resources = bidProposalAssembler.toResources(proposals, user);
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{uid}/bids/{pid}")
    public ResponseEntity<Resource<Proposal>> getBid(
            @PathVariable("uid") long uid, @PathVariable("pid") long pid) {
        User user = getUserIfPresent(uid);
        Proposal proposal = usersService.getBid(uid, pid).orElseThrow(() ->
                new NotFoundException(String.format("Proposal with id %d is not bid by a member/staff with id %d", pid, uid)));
        Resource<Proposal> resource = bidProposalAssembler.toResource(proposal, user);
        return ResponseEntity.ok(resource);
    }

    private User getUserIfPresent(long id) {
        return usersService.getUser(id).orElseThrow(() ->
                new NotFoundException(String.format("User with id %d not found.", id)));
    }

}
