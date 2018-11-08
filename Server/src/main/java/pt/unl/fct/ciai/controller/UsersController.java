package pt.unl.fct.ciai.controller;

import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import pt.unl.fct.ciai.api.UsersApi;
import pt.unl.fct.ciai.assembler.ProposalResourceAssembler;
import pt.unl.fct.ciai.assembler.UserResourceAssembler;
import pt.unl.fct.ciai.exception.BadRequestException;
import pt.unl.fct.ciai.exception.NotFoundException;
import pt.unl.fct.ciai.model.Proposal;
import pt.unl.fct.ciai.model.User;
import pt.unl.fct.ciai.repository.ProposalsRepository;
import pt.unl.fct.ciai.repository.UsersRepository;
import pt.unl.fct.ciai.service.ProposalsService;
import pt.unl.fct.ciai.service.UsersService;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(value = "/users", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class UsersController implements UsersApi {

	private final UsersService usersService;
	private final ProposalsService proposalsService;

	private final UserResourceAssembler userAssembler;
	private final ProposalResourceAssembler proposalAssembler;

	public UsersController(UsersService usersService, ProposalsService proposalsService,
						   UserResourceAssembler userAssembler, ProposalResourceAssembler proposalAssembler) {
		this.usersService = usersService;
		this.proposalsService = proposalsService;
		this.userAssembler = userAssembler;
		this.proposalAssembler = proposalAssembler;
	}

	@GetMapping
	public ResponseEntity<Resources<Resource<User>>> getUsers() {
		// @RequestParam(required = false) String search) { // TODO search mesmo necessário?
		Iterable<User> users = usersService.getUsers();
		Resources<Resource<User>> resources = userAssembler.toResources(users);
		return ResponseEntity.ok(resources);
	}

	@PostMapping
	public ResponseEntity<Resource<User>> addUser(@RequestBody User user) throws URISyntaxException {
		User newUser = usersService.addUser(user);
		Resource<User> resource = userAssembler.toResource(newUser);
		return ResponseEntity
				.created(new URI(resource.getId().expand().getHref()))
				.body(resource);	
	}

	@GetMapping("/{id}")
	public ResponseEntity<Resource<User>> getUser(@PathVariable long id) {
		User user = usersService.getUser(id);
		Resource<User> resource = userAssembler.toResource(user);
		return ResponseEntity.ok(resource);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateUser(@PathVariable long id, @RequestBody User newUser) {
		if (id != newUser.getId()) {
			throw new BadRequestException(String.format("Path id %d does not match user id %d", id, newUser.getId()));
		}
		usersService.updateUser(id, newUser);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable long id) {
		usersService.deleteUser(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}/approverInProposals")
	public ResponseEntity<Resources<Resource<Proposal>>> getApproverInProposals(@PathVariable long id) {
		// @RequestParam(value = "search", required = false) String search)
		List<Resource<Proposal>> proposals =
				StreamSupport.stream(usersService.getApproverInProposals(id).spliterator(), false)
				.map(proposalAssembler::toResource)
				.collect(Collectors.toList());
		Resources<Resource<Proposal>> resources = new Resources<>(proposals,
				linkTo(methodOn(UsersController.class).getApproverInProposals(id)).withSelfRel());
		return ResponseEntity.ok(resources);
	}

	@PostMapping("/{id}/approverInProposals")
	public ResponseEntity<Resource<Proposal>> addApproverInProposal(@PathVariable long id, @RequestBody Proposal proposal) throws URISyntaxException {
		Proposal newProposal = usersService.addApproverInProposal(id, proposal);
		Resource<Proposal> resource = proposalAssembler.toResource(newProposal);
		return ResponseEntity
				.created(new URI(resource.getId().expand().getHref()))
				.body(resource);
	}

	@DeleteMapping("/{uid}/approverInProposals/{pid}")
	public ResponseEntity<?> deleteApproverInProposal(@PathVariable long uid, @PathVariable long pid) {
		usersService.deleteApproverInProposal(uid, pid);
		return ResponseEntity.noContent().build();
	}

}
