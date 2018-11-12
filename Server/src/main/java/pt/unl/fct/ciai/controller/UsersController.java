package pt.unl.fct.ciai.controller;

import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.unl.fct.ciai.api.UsersApi;
import pt.unl.fct.ciai.assembler.ProposalResourceAssembler;
import pt.unl.fct.ciai.assembler.UserResourceAssembler;
import pt.unl.fct.ciai.exception.NotFoundException;
import pt.unl.fct.ciai.model.User;
import pt.unl.fct.ciai.service.UsersService;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping(value = "/users", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class UsersController implements UsersApi {

    private final UsersService usersService;

    private final UserResourceAssembler userAssembler;
    private final ProposalResourceAssembler proposalAssembler;

    public UsersController(UsersService usersService,
                           UserResourceAssembler userAssembler, ProposalResourceAssembler proposalAssembler) {
        this.usersService = usersService;
        this.userAssembler = userAssembler;
        this.proposalAssembler = proposalAssembler;
    }

    @GetMapping
    public ResponseEntity<Resources<Resource<User>>> getUsers(@RequestParam(required = false) String search) {
        Iterable<User> users = usersService.getUsers(search);
        Resources<Resource<User>> resources = userAssembler.toResources(users);
        return ResponseEntity.ok(resources);
    }

    @PostMapping
    public ResponseEntity<Resource<User>> addUser(@Valid @RequestBody User user) throws URISyntaxException {
        User newUser = usersService.addUser(user);
        Resource<User> resource = userAssembler.toResource(newUser);
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource<User>> getUser(@PathVariable("id") long id) {
        User user = usersService.getUser(id).orElseThrow(() ->
                new NotFoundException(String.format("User with id %d not found.", id)));
        Resource<User> resource = userAssembler.toResource(user);
        return ResponseEntity.ok(resource);
    }

    @PutMapping("/{id}")
    // @CanModifyUser
    public ResponseEntity<?> updateUser(@PathVariable("id") long id, @RequestBody User user) {
        user.setId(id);
        usersService.updateUser(user);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    // @CanDeleteUser
    public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
        usersService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

/*
	@GetMapping("/{id}/approverInProposals")
	public ResponseEntity<Resources<Resource<Proposal>>> getApproverInProposals(
			@PathVariable("id") long id, @RequestParam(value = "search", required = false) String search) {
		User user = usersService.getUser(id).orElseThrow(() ->
				new NotFoundException(String.format("User with id %d not found.", id)));
		Iterable<Proposal> proposals = usersService.getApproverInProposals(id, search);
		Resources<Resource<Proposal>> resources = proposalAssembler.toResources(proposals); //TODO, user?
		return ResponseEntity.ok(resources);
	}

	@PostMapping("/{id}/approverInProposals")
	// @CanAddApprover
	public ResponseEntity<Resource<Proposal>> addApproverInProposal(@PathVariable("id") long id,
																	@RequestBody Proposal proposal)
			throws URISyntaxException {
		//TODO proposal must exist
		Proposal updatedProposal = usersService.addApproverInProposal(id, proposal);
		Resource<Proposal> resource = proposalAssembler.toResource(updatedProposal);
		return ResponseEntity
				.created(new URI(resource.getId().expand().getHref()))
				.body(resource);
	}

	@GetMapping("/{uid}/approverInProposals/{pid}")
	public ResponseEntity<Resource<Proposal>> getApproverInProposal(@PathVariable("uid") long uid,
																	@PathVariable("pid") long pid) {
		Proposal proposal = usersService.getApproverInProposal(uid, pid).orElseThrow(() ->
				new BadRequestException(String.format("Proposal id %d is not being approved by user id %d", pid, uid)));
		Resource<Proposal> resource = proposalAssembler.toResource(proposal);
		return ResponseEntity.ok(resource);
	}

	@DeleteMapping("/{uid}/approverInProposals/{pid}")
	// @CanDeleteApprover
	public ResponseEntity<?> deleteApproverInProposal(@PathVariable long uid, @PathVariable long pid) {
		usersService.deleteApproverInProposal(uid, pid);
		return ResponseEntity.noContent().build();
	}*/
}
