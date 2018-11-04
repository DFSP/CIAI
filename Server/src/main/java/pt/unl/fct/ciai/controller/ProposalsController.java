package pt.unl.fct.ciai.controller;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.unl.fct.ciai.model.Comment;
import pt.unl.fct.ciai.repository.CommentsRepository;
import pt.unl.fct.ciai.assembler.CommentResourceAssembler;
import pt.unl.fct.ciai.assemblers.ProposalResourceAssembler;
import pt.unl.fct.ciai.assemblers.ReviewResourceAssembler;
import pt.unl.fct.ciai.assemblers.SectionResourceAssembler;
import pt.unl.fct.ciai.assemblers.UserResourceAssembler;
import pt.unl.fct.ciai.exceptions.BadRequestException;
import pt.unl.fct.ciai.exceptions.NotFoundException;
import pt.unl.fct.ciai.model.Proposal;
import pt.unl.fct.ciai.model.Review;
import pt.unl.fct.ciai.repository.ProposalsRepository;
import pt.unl.fct.ciai.repository.ReviewsRepository;
import pt.unl.fct.ciai.model.Section;
import pt.unl.fct.ciai.model.User;
import pt.unl.fct.ciai.repository.SectionsRepository;
import pt.unl.fct.ciai.repository.UsersRepository;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/proposals", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProposalsController { //Implements proposalControllerApi 
	//TODO apenas com os metodos get, resto fica protected

	private final ProposalsRepository proposalsRepository;
	private final SectionsRepository sectionsRepository;
	private final ReviewsRepository reviewsRepository;
	private final CommentsRepository commentsRepository;
	private final UsersRepository usersRepository;

	private final ProposalResourceAssembler proposalAssembler;
	private final SectionResourceAssembler sectionAssembler;
	private final ReviewResourceAssembler reviewAssembler;
	private final CommentResourceAssembler commentAssembler;
	private final UserResourceAssembler userAssembler;

	public ProposalsController(ProposalsRepository proposalsRepository, SectionsRepository sectionsRepository,
			ReviewsRepository reviewsRepository, CommentsRepository commentsRepository, UsersRepository usersRepository,
			ProposalResourceAssembler proposalAssembler, SectionResourceAssembler sectionAssembler,
			ReviewResourceAssembler reviewAssembler, CommentResourceAssembler commentAssembler,
			UserResourceAssembler userAssembler) {
		this.proposalsRepository = proposalsRepository;
		this.sectionsRepository = sectionsRepository;
		this.reviewsRepository = reviewsRepository;
		this.commentsRepository = commentsRepository;
		this.usersRepository = usersRepository;
		this.proposalAssembler = proposalAssembler;
		this.sectionAssembler = sectionAssembler;
		this.reviewAssembler = reviewAssembler;
		this.commentAssembler = commentAssembler;
		this.userAssembler = userAssembler;
	}

	@GetMapping
	public ResponseEntity<Resources<Resource<Proposal>>> getProposals() {
		// @RequestParam(required = false) String search) { // TODO search mesmo necessário?
		Iterable<Proposal> proposals = proposalsRepository.findAll();
		Resources<Resource<Proposal>> resources = proposalAssembler.toResources(proposals);
		return ResponseEntity.ok(resources);
	}

	@PostMapping
	public ResponseEntity<Resource<Proposal>> addProposal(@RequestBody Proposal proposal) throws URISyntaxException {
		Proposal newProposal = proposalsRepository.save(proposal);
		Resource<Proposal> resource = proposalAssembler.toResource(newProposal);
		return ResponseEntity
				.created(new URI(resource.getId().expand().getHref()))
				.body(resource);	
	}

	@GetMapping("/{id}")
	public Resource<Proposal> getProposal(@PathVariable("id") long id) {
		Proposal proposal = findProposal(id);
		return proposalAssembler.toResource(proposal);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateProposal(@PathVariable("id") long id, @RequestBody Proposal newProposal) {  	
		if (id != newProposal.getId()) {
			throw new BadRequestException(String.format("Body request proposal id %d and path id %d don't match.", newProposal.getId(), id));
		}
		Proposal oldProposal = findProposal(id);
		proposalsRepository.save(newProposal);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteProposal(@PathVariable("id") long id) {
		Proposal proposal = findProposal(id);
		proposalsRepository.delete(proposal);
		return ResponseEntity.noContent().build();
	}

	// TODO
	@GetMapping("/{id}/sections")
	public Resources<Resource<Section>> getSections(@PathVariable("id") long id) {
		// TODO search mesmo necessario? @RequestParam(required = false) String search){
		List<Resource<Section>> sections = 
				findProposal(id).getSections()
				.stream()
				.map(sectionAssembler::toResource)
				.collect(Collectors.toList());
		return new Resources<>(sections,
				linkTo(methodOn(ProposalsController.class).getSections(id)).withSelfRel());	
	}

	@PostMapping("/{id}/sections")
	public ResponseEntity<Resource<Section>> addSection(@PathVariable("id") long id, @RequestBody Section section) throws URISyntaxException {
		Proposal proposal = findProposal(id);
		//		if (proposal.getSections().contains(section)) { //TODO
		//			throw new ConflictException(String.format("Section already associated with proposal id %d.", id));
		//		}
		section.setProposal(proposal);
		proposal.addSection(section);
		proposalsRepository.save(proposal); //TODO verificar se é necessário
		Section newSection = sectionsRepository.save(section);
		Resource<Section> resource = sectionAssembler.toResource(newSection);
		return ResponseEntity
				.created(new URI(resource.getId().expand().getHref()))
				.body(resource);
	}

	@GetMapping("/{pid}/sections/{sid}")
	public Resource<Section> getSection(@PathVariable("pid") long pid, @PathVariable("sid") long sid) {
		Proposal proposal = findProposal(pid);
		Section section = findSection(sid);
		if (!section.getProposal().equals(proposal)) {
			throw new BadRequestException(String.format("Section id %d does not belong to proposal with id %d", sid, pid));
		}
		return sectionAssembler.toResource(section);
	}

	@PutMapping("/{pid}/sections/{sid}")
	public ResponseEntity<?> updateSection(@PathVariable("pid") long pid, @PathVariable("sid") long sid, @RequestBody Section newSection) {
		Proposal proposal = findProposal(pid);
		Section oldSection = findSection(sid);
		if (newSection.getId() != sid) {
			throw new BadRequestException(String.format("Request body section id %d and path paramenter sid %d don't match.", newSection.getId(), sid));
		}
		if (oldSection.getProposal().getId() != proposal.getId()) {
			throw new BadRequestException(String.format("Section id %d does not belong to proposal with id %d", sid, pid));	
		}
		sectionsRepository.save(newSection);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{pid}/sections/{sid}")
	public ResponseEntity<?> deleteSection(@PathVariable("pid") long pid, @PathVariable("sid") long sid) {
		Proposal proposal = findProposal(pid);
		Section section = findSection(sid);
		if (section.getProposal().getId() != proposal.getId()) {
			throw new BadRequestException(String.format("Section id %d does not belong to proposal with id %d", sid, pid));
		}
		sectionsRepository.delete(section);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}/reviews")
	public Resources<Resource<Review>> getReviews(@PathVariable("id") long id) {
		// TODO search mesmo necessario? @RequestParam(required = false) String search){
		List<Resource<Review>> reviews = 
				findProposal(id).getReviews()
				.stream()
				.map(reviewAssembler::toResource)
				.collect(Collectors.toList());
		return new Resources<>(reviews,
				linkTo(methodOn(ProposalsController.class).getReviews(id)).withSelfRel());
	}

	@PostMapping("/{id}/reviews")
	public ResponseEntity<?> addReview(@PathVariable("id") long id, @RequestBody Review review) throws URISyntaxException {
		Proposal proposal = findProposal(id);
		//				if (proposal.getSections().contains(review)) { //TODO
		//					throw new ConflictException(String.format("Review already associated with proposal id %d.", id));
		//				}
		review.setProposal(proposal);
		proposal.addReview(review);
		proposalsRepository.save(proposal); //TODO verificar se é necessário
		Review newReview = reviewsRepository.save(review);
		Resource<Review> resource = reviewAssembler.toResource(newReview);
		return ResponseEntity
				.created(new URI(resource.getId().expand().getHref()))
				.body(resource);
	}

	@GetMapping("/{pid}/reviews/{rid}")
	public Resource<Review> getReview(@PathVariable("pid") long pid, @PathVariable("rid") long rid) {	
		Proposal proposal = findProposal(pid);
		Review review = findReview(rid);
		if (!review.getProposal().equals(proposal)) {
			throw new BadRequestException(String.format("Review id %d does not belong to proposal with id %d", rid, pid));
		}
		return reviewAssembler.toResource(review);
	}

	@PutMapping("/{pid}/reviews/{rid}")
	public ResponseEntity<?> updateReview(@PathVariable("pid") long pid, @PathVariable("rid") long rid, @RequestBody Review newReview) {
		Proposal proposal = findProposal(pid);
		Review oldReview = findReview(rid);
		if (newReview.getId() != rid) {
			throw new BadRequestException(String.format("Request body review id %d and path paramenter rid %d don't match.", newReview.getId(), rid));
		}
		if (oldReview.getProposal().getId() != proposal.getId()) {
			throw new BadRequestException(String.format("Review id %d does not belong to proposal with id %d", rid, pid));	
		}
		reviewsRepository.save(newReview);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{pid}/reviews/{rid}")
	public ResponseEntity<?> deleteReview(@PathVariable("pid") long pid, @PathVariable("rid") long rid) {
		Proposal proposal = findProposal(pid);
		Review review = findReview(rid);
		if (review.getProposal().getId() != proposal.getId()) {
			throw new BadRequestException(String.format("Review id %d does not belong to proposal with id %d", rid, pid));
		}
		reviewsRepository.delete(review);
		return ResponseEntity.noContent().build();
	}

	//TODO
	@GetMapping("/{id}/comments")
	public Resources<Resource<Comment>> getComments(@PathVariable("id") long id) {
		// TODO search mesmo necessario? @RequestParam(required = false) String search){
		List<Resource<Comment>> comments = 
				findProposal(id).getComments()
				.stream()
				.map(commentAssembler::toResource)
				.collect(Collectors.toList());
		return new Resources<>(comments,
				linkTo(methodOn(ProposalsController.class).getComments(id)).withSelfRel());
	}

	@PostMapping("/{id}/comments")
	public ResponseEntity<Resource<Comment>> addComment(@PathVariable("id") long id, @RequestBody Comment comment) throws URISyntaxException {
		Proposal proposal = findProposal(id);
		//				if (proposal.getSections().contains(comment)) { //TODO
		//					throw new ConflictException(String.format("Comment already associated with proposal id %d.", id));
		//				}
		comment.setProposal(proposal);
		proposal.addComment(comment);
		proposalsRepository.save(proposal); //TODO verificar se é necessário
		Comment newComment = commentsRepository.save(comment);
		Resource<Comment> resource = commentAssembler.toResource(newComment);
		return ResponseEntity
				.created(new URI(resource.getId().expand().getHref()))
				.body(resource);
	}

	@GetMapping("/{pid}/comments/{cid}")
	public Resource<Comment> getComment(@PathVariable("pid") long pid, @PathVariable("cid") long cid) {
		Proposal proposal = findProposal(pid);
		Comment comment = findComment(cid);
		if (!comment.getProposal().equals(proposal)) {
			throw new BadRequestException(String.format("Comment id %d does not belong to proposal with id %d", cid, pid));
		}
		return commentAssembler.toResource(comment);
	}

	@PutMapping("/{pid}/comments/{cid}")
	public ResponseEntity<?> updateComment(@PathVariable("pid") long pid, @PathVariable("cid") long cid, @RequestBody Comment newComment) {
		Proposal proposal = findProposal(pid);
		Comment oldComment = findComment(cid);
		if (newComment.getId() != cid) {
			throw new BadRequestException(String.format("Request body comment id %d and path paramenter rid %d don't match.", newComment.getId(), cid));
		}
		if (oldComment.getProposal().getId() != proposal.getId()) {
			throw new BadRequestException(String.format("Comment id %d does not belong to proposal with id %d", cid, pid));	
		}
		commentsRepository.save(newComment);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{pid}/comments/{cid}")
	public ResponseEntity<?> deleteComment(@PathVariable("pid") long pid, @PathVariable("cid") long cid) {
		Proposal proposal = findProposal(pid);
		Comment comment = findComment(cid);
		if (comment.getProposal().getId() != proposal.getId()) {
			throw new BadRequestException(String.format("Comment id %d does not belong to proposal with id %d", cid, pid));
		}
		commentsRepository.delete(comment);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}/biddings")
	public Resources<Resource<User>> getBiddingUsers(@PathVariable("id") long id) {
		// TODO search mesmo necessario? @RequestParam(required = false) String search){
		List<Resource<User>> users = 
				findProposal(id).getBiddings()
				.stream()
				.map(userAssembler::toResource)
				.collect(Collectors.toList());
		return new Resources<>(users,
				linkTo(methodOn(ProposalsController.class).getBiddingUsers(id)).withSelfRel());
	}

	@PostMapping("/{id}/biddings}")
	public ResponseEntity<Resource<User>> addUserForBidding(@PathVariable("id") long id, @RequestBody User user) throws URISyntaxException {
		Proposal proposal = findProposal(id);
		//				if (proposal.getSections().contains(user)) { //TODO
		//					throw new ConflictException(String.format("User already associated with proposal id %d.", id));
		//				}
		//user.addBidding(proposal); TODO
		proposal.addBidding(user);
		proposalsRepository.save(proposal); //TODO verificar se é necessário
		User newUser = usersRepository.save(user);
		Resource<User> resource = userAssembler.toResource(newUser);
		return ResponseEntity
				.created(new URI(resource.getId().expand().getHref()))
				.body(resource);
	}

	@DeleteMapping("/{pid}/biddings/{uid}")
	public ResponseEntity<?> deleteUserForBidding(@PathVariable("pid") long pid, @PathVariable("uid") long uid) {
		Proposal proposal = findProposal(pid);
		User user = findUser(uid);
		//		if (user.getBidding(pid).getId() != proposal.getId()) { //TODO
		//			throw new BadRequestException(String.format("User id %d did not bid on proposal with id %d", uid, pid));
		//		}
		usersRepository.delete(user);
		return ResponseEntity.noContent().build();
	}  

	private Proposal findProposal(long id) {
		return proposalsRepository.findById(id)
				.orElseThrow(() -> new NotFoundException(String.format("Proposal with id %d not found.", id)));
	}

	private Section findSection(long id) {
		return sectionsRepository.findById(id)
				.orElseThrow(() -> new NotFoundException(String.format("Section with id %d not found.", id)));
	}

	private Review findReview(long id) {
		return reviewsRepository.findById(id)
				.orElseThrow(() -> new NotFoundException(String.format("Review with id %d not found.", id)));
	}

	private Comment findComment(long id) {
		return commentsRepository.findById(id)
				.orElseThrow(() -> new NotFoundException(String.format("Comment with id %d not found.", id)));
	}

	private User findUser(long id) {
		return usersRepository.findById(id)
				.orElseThrow(() -> new NotFoundException(String.format("User with id %d not found.", id)));
	}

}
