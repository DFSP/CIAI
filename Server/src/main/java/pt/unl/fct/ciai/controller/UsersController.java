package pt.unl.fct.ciai.controller;

import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pt.unl.fct.ciai.assemblers.ProposalResourceAssembler;
import pt.unl.fct.ciai.assemblers.UserResourceAssembler;
import pt.unl.fct.ciai.exceptions.BadRequestException;
import pt.unl.fct.ciai.exceptions.NotFoundException;
import pt.unl.fct.ciai.model.Proposal;
import pt.unl.fct.ciai.model.User;
import pt.unl.fct.ciai.repository.ProposalsRepository;
import pt.unl.fct.ciai.repository.UsersRepository;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/users", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class UsersController {

	private final UsersRepository usersRepository;
	private final ProposalsRepository proposalsRepository;

	private final UserResourceAssembler userAssembler;
	private final ProposalResourceAssembler proposalAssembler;

	public UsersController(UsersRepository users, ProposalsRepository proposals,
			UserResourceAssembler userAssembler, ProposalResourceAssembler proposalAssembler) {
		this.usersRepository = users;
		this.proposalsRepository = proposals;
		this.userAssembler = userAssembler;
		this.proposalAssembler = proposalAssembler;
	}

	@GetMapping
	public ResponseEntity<Resources<Resource<User>>> getUsers() {
		// @RequestParam(required = false) String search) { // TODO search mesmo necessário?
		Iterable<User> users = usersRepository.findAll();
		Resources<Resource<User>> resources = userAssembler.toResources(users);
		return ResponseEntity.ok(resources);
	}

	@PostMapping
	public ResponseEntity<Resource<User>> addUser(@RequestBody User user) throws URISyntaxException {
		User newUser = usersRepository.save(user);
		Resource<User> resource = userAssembler.toResource(newUser);
		return ResponseEntity
				.created(new URI(resource.getId().expand().getHref()))
				.body(resource);	
	}

	@GetMapping("/{id}")
	public ResponseEntity<Resource<User>> getUser(@PathVariable long id) {
		User user = findUser(id);
		Resource<User> resource = userAssembler.toResource(user);
		return ResponseEntity.ok(resource);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateUser(@PathVariable long id, @RequestBody User newUser) {
		if (id != newUser.getId()) {
			throw new BadRequestException(String.format("Path id %d does not match user id %d", id, newUser.getId()));
		}
		User oldUser = findUser(id);
		usersRepository.save(newUser);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable long id) {
		User user = findUser(id);
		usersRepository.delete(user);
		return ResponseEntity.noContent().build();
	}

	// Obter a lista de propostas que o User {id} pode aprovar
	@GetMapping("/{id}/approverInProposals")
	public ResponseEntity<Resources<Resource<Proposal>>> getApproverInProposals(@PathVariable long id) {
		// @RequestParam(value = "search", required = false) String search)
		List<Resource<Proposal>> proposals = 
				findUser(id).getProposalsToApprove()
				.stream()
				.map(proposalAssembler::toResource)
				.collect(Collectors.toList());
		Resources<Resource<Proposal>> resources = new Resources<>(proposals,
				linkTo(methodOn(UsersController.class).getApproverInProposals(id)).withSelfRel());
		return ResponseEntity.ok(resources);
	}

	// Add proposta à lista de propostas para o User {id} aprovar
	@PostMapping("/{id}/approverInProposals")
	public ResponseEntity<Resource<Proposal>> addApproverInProposal(@PathVariable long id, @RequestBody Proposal proposal) throws URISyntaxException {
		User user = findUser(id);
		proposal.setApprover(user);
		user.addProposalToApprove(proposal);
		usersRepository.save(user); //TODO verificar se é necessário
		Proposal newEmployee = proposalsRepository.save(proposal);
		Resource<Proposal> resource = proposalAssembler.toResource(newEmployee);
		return ResponseEntity
				.created(new URI(resource.getId().expand().getHref()))
				.body(resource);
	}
	
	// Apaga proposta {pid} à lista de propostas que o User {uid} tem de aprovar
	@DeleteMapping("/{uid}/approverInProposals/{pid}")
	public ResponseEntity<?> deleteApproverInProposal(@PathVariable long uid, @PathVariable long pid) {
		User user = findUser(uid);
		Proposal proposal = findProposal(pid);
		if (proposal.getApprover().map(User::getId).orElse(-1L) != user.getId()) {
			throw new BadRequestException(String.format("User id %d is not an approver of Proposal with id %d", uid, pid));
		}
		user.removeProposalToApprove(proposal);
		usersRepository.save(user); //TODO: (need to save user?)
		proposal.setApprover(null);
		proposalsRepository.save(proposal);
		return ResponseEntity.noContent().build();
	}

	private User findUser(long id) {
		return usersRepository.findById(id)
				.orElseThrow(() -> new NotFoundException(String.format("User with id %d not found.", id)));
	}

	private Proposal findProposal(long id) {
		return proposalsRepository.findById(id)
				.orElseThrow(() -> new NotFoundException(String.format("Proposal with id %d not found.", id)));
	}

}
