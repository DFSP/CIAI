package pt.unl.fct.ciai.controller;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.MediaTypes;
import pt.unl.fct.ciai.api.ProposalsApi;
import pt.unl.fct.ciai.model.Comment;
import pt.unl.fct.ciai.assembler.CommentResourceAssembler;
import pt.unl.fct.ciai.assembler.ProposalResourceAssembler;
import pt.unl.fct.ciai.assembler.ReviewResourceAssembler;
import pt.unl.fct.ciai.assembler.SectionResourceAssembler;
import pt.unl.fct.ciai.assembler.UserResourceAssembler;
import pt.unl.fct.ciai.exception.BadRequestException;
import pt.unl.fct.ciai.exception.NotFoundException;
import pt.unl.fct.ciai.model.Proposal;
import pt.unl.fct.ciai.model.Review;
import pt.unl.fct.ciai.model.Section;
import pt.unl.fct.ciai.model.User;
import pt.unl.fct.ciai.service.ProposalsService;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping(value = "/proposals", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class ProposalsController implements ProposalsApi {

	private final ProposalsService proposalsService;

	private final ProposalResourceAssembler proposalAssembler;
	private final SectionResourceAssembler sectionAssembler;
	private final ReviewResourceAssembler reviewAssembler;
	private final CommentResourceAssembler commentAssembler;
	private final UserResourceAssembler userAssembler;

	public ProposalsController(ProposalsService proposalsService,
			ProposalResourceAssembler proposalAssembler, SectionResourceAssembler sectionAssembler,
			ReviewResourceAssembler reviewAssembler, CommentResourceAssembler commentAssembler,
			UserResourceAssembler userAssembler) {
		this.proposalsService = proposalsService;
		this.proposalAssembler = proposalAssembler;
		this.sectionAssembler = sectionAssembler;
		this.reviewAssembler = reviewAssembler;
		this.commentAssembler = commentAssembler;
		this.userAssembler = userAssembler;
	}

	@GetMapping
	public ResponseEntity<Resources<Resource<Proposal>>> getProposals(@RequestParam(required = false) String search) {
		Iterable<Proposal> proposals = proposalsService.getProposals(search);
		Resources<Resource<Proposal>> resources = proposalAssembler.toResources(proposals);
		return ResponseEntity.ok(resources);
	}

	@PostMapping
	public ResponseEntity<Resource<Proposal>> addProposal(@RequestBody Proposal proposal) throws URISyntaxException {
		if (proposal.getId() > 0) {
			throw new BadRequestException("A new proposal has to have a non positive id.");
		}
		Proposal newProposal = proposalsService.addProposal(proposal);
		Resource<Proposal> resource = proposalAssembler.toResource(newProposal);
		return ResponseEntity
				.created(new URI(resource.getId().expand().getHref()))
				.body(resource);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Resource<Proposal>> getProposal(@PathVariable("id") long id) {
		Proposal proposal = proposalsService.getProposal(id).orElseThrow(() ->
				new NotFoundException(String.format("Proposal with id %d not found.", id)));
		Resource<Proposal> resource = proposalAssembler.toResource(proposal);
		return ResponseEntity.ok(resource);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateProposal(@PathVariable("id") long id, @RequestBody Proposal newProposal) {  	
		if (newProposal.getId() != id) {
			throw new BadRequestException(String.format("Proposal id %d and path id %d don't match.", newProposal.getId(), id));
		}
		proposalsService.updateProposal(newProposal);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteProposal(@PathVariable("id") long id) {
		proposalsService.deleteProposal(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}/sections")
	public ResponseEntity<Resources<Resource<Section>>> getSections(@PathVariable("id") long id,
																	@RequestParam (value="search") String search) {
		Proposal proposal = proposalsService.getProposal(id).orElseThrow(() ->
				new NotFoundException(String.format("Proposal with id %d not found.", id)));
		Iterable<Section> sections = proposalsService.getSections(id, search);
		Resources<Resource<Section>> resources = sectionAssembler.toResources(sections, proposal);
		return ResponseEntity.ok(resources);
	}

	@PostMapping("/{id}/sections")
	public ResponseEntity<Resource<Section>> addSection(@PathVariable("id") long id, @RequestBody Section section)
			throws URISyntaxException {
		if (section.getId() > 0) {
			throw new BadRequestException("A new section has to have a non positive id.");
		}
		Section newSection = proposalsService.addSection(id, section);
		Resource<Section> resource = sectionAssembler.toResource(newSection);
		return ResponseEntity
				.created(new URI(resource.getId().expand().getHref()))
				.body(resource);
	}

	@GetMapping("/{pid}/sections/{sid}")
	public ResponseEntity<Resource<Section>> getSection(@PathVariable("pid") long pid, @PathVariable("sid") long sid) {
		Section section = proposalsService.getSection(pid, sid).orElseThrow(() ->
				new BadRequestException(String.format("Section id %d does not belong to proposal with id %d", sid, pid)));
		Resource<Section> resource = sectionAssembler.toResource(section);
		return ResponseEntity.ok(resource);
	}

	@PutMapping("/{pid}/sections/{sid}")
	public ResponseEntity<?> updateSection(@PathVariable("pid") long pid, @PathVariable("sid") long sid, @RequestBody Section newSection) {
		if (newSection.getId() != sid) {
			throw new BadRequestException(String.format("Section id %d and path id %d don't match.", newSection.getId(), sid));
		}
		proposalsService.updateSection(pid, newSection);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{pid}/sections/{sid}")
	public ResponseEntity<?> deleteSection(@PathVariable("pid") long pid, @PathVariable("sid") long sid) {
		proposalsService.deleteSection(pid, sid);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}/reviews")
	public ResponseEntity<Resources<Resource<Review>>> getReviews(@PathVariable("id") long id,
																  @RequestParam (value="search") String search) {
		Proposal proposal = proposalsService.getProposal(id).orElseThrow(() ->
				new NotFoundException(String.format("Proposal with id %d not found.", id)));
		Iterable<Review> reviews = proposalsService.getReviews(id, search);
		Resources<Resource<Review>> resources = reviewAssembler.toResources(reviews, proposal);
		return ResponseEntity.ok(resources);
	}

	@PostMapping("/{id}/reviews")
	public ResponseEntity<?> addReview(@PathVariable("id") long id, @RequestBody Review review) throws URISyntaxException {
		if (review.getId() > 0) {
			throw new BadRequestException("A new review has to have a non positive id.");
		}
		Review newReview = proposalsService.addReview(id, review);
		Resource<Review> resource = reviewAssembler.toResource(newReview);
		return ResponseEntity
				.created(new URI(resource.getId().expand().getHref()))
				.body(resource);
	}

	@GetMapping("/{pid}/reviews/{rid}")
	public ResponseEntity<Resource<Review>> getReview(@PathVariable("pid") long pid, @PathVariable("rid") long rid) {
		Review review = proposalsService.getReview(pid, rid).orElseThrow(() ->
				new BadRequestException(String.format("Review id %d does not belong to proposal with id %d", rid, pid)));
		Resource<Review> resource = reviewAssembler.toResource(review);
		return ResponseEntity.ok(resource);
	}

	@PutMapping("/{pid}/reviews/{rid}")
	public ResponseEntity<?> updateReview(@PathVariable("pid") long pid, @PathVariable("rid") long rid, @RequestBody Review newReview) {
		if (newReview.getId() != rid) {
			throw new BadRequestException(String.format("Review id %d and path id %d don't match.", newReview.getId(), rid));
		}
		proposalsService.updateReview(pid, newReview);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{pid}/reviews/{rid}")
	public ResponseEntity<?> deleteReview(@PathVariable("pid") long pid, @PathVariable("rid") long rid) {
		proposalsService.deleteReview(pid, rid);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}/comments")
	public ResponseEntity<Resources<Resource<Comment>>> getComments(@PathVariable("id") long id,
																	@RequestParam (value="search") String search) {
		Proposal proposal = proposalsService.getProposal(id).orElseThrow(() ->
				new NotFoundException(String.format("Proposal with id %d not found.", id)));
		Iterable<Comment> comments = proposalsService.getComments(id, search);
		Resources<Resource<Comment>> resources = commentAssembler.toResources(comments, proposal);
		return ResponseEntity.ok(resources);
	}

	@PostMapping("/{id}/comments")
	public ResponseEntity<Resource<Comment>> addComment(@PathVariable("id") long id, @RequestBody Comment comment)
			throws URISyntaxException {
		if (comment.getId() > 0) {
			throw new BadRequestException("A new comment has to have a non positive id.");
		}
		Comment newComment = proposalsService.addComment(id, comment);
		Resource<Comment> resource = commentAssembler.toResource(newComment);
		return ResponseEntity
				.created(new URI(resource.getId().expand().getHref()))
				.body(resource);
	}

	@GetMapping("/{pid}/comments/{cid}")
	public ResponseEntity<Resource<Comment>> getComment(@PathVariable("pid") long pid, @PathVariable("cid") long cid) {
		Comment comment = proposalsService.getComment(pid, cid).orElseThrow(() ->
				new BadRequestException(String.format("Comment id %d does not belong to proposal with id %d", cid, pid)));
		Resource<Comment> resource = commentAssembler.toResource(comment);
		return ResponseEntity.ok(resource);
	}

	@PutMapping("/{pid}/comments/{cid}")
	public ResponseEntity<?> updateComment(@PathVariable("pid") long pid, @PathVariable("cid") long cid, @RequestBody Comment newComment) {
		if (newComment.getId() != cid) {
			throw new BadRequestException(String.format("Comment id %d and path id %d don't match.", newComment.getId(), cid));
		}
		proposalsService.updateComment(pid, newComment);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{pid}/comments/{cid}")
	public ResponseEntity<?> deleteComment(@PathVariable("pid") long pid, @PathVariable("cid") long cid) {
		proposalsService.deleteComment(pid, cid);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}/biddings")
	public ResponseEntity<Resources<Resource<User>>> getReviewBiddings(@PathVariable("id") long id,
																	 @RequestParam (value="search") String search) {
		Proposal proposal = proposalsService.getProposal(id).orElseThrow(() ->
				new NotFoundException(String.format("Proposal with id %d not found.", id)));
		Iterable<User> biddings = proposalsService.getReviewBiddings(id, search);
		Resources<Resource<User>> resources = userAssembler.toResources(biddings, proposal);
		return ResponseEntity.ok(resources);
	}

	@PostMapping("/{id}/biddings}")
	public ResponseEntity<Resource<User>> addReviewBidding(@PathVariable("id") long id, @RequestBody User user)
			throws URISyntaxException {
		User newUser = proposalsService.addReviewBidding(id, user);
		Resource<User> resource = userAssembler.toResource(newUser);
		return ResponseEntity
				.created(new URI(resource.getId().expand().getHref()))
				.body(resource);
	}

	@GetMapping("/{pid}/biddings/{uid}")
	public ResponseEntity<Resource<User>> getReviewBidding(@PathVariable("pid") long pid, @PathVariable("uid") long uid) {
		User bidding = proposalsService.getReviewBidding(pid, uid).orElseThrow(() ->
				new BadRequestException(String.format("Bidding id %d does not belong to proposal with id %d", uid, pid)));
		Resource<User> resource = userAssembler.toResource(bidding);
		return ResponseEntity.ok(resource);
	}

	@DeleteMapping("/{pid}/biddings/{uid}")
	public ResponseEntity<?> deleteReviewBidding(@PathVariable("pid") long pid, @PathVariable("uid") long uid) {
		proposalsService.deleteReviewBidding(pid, uid);
		return ResponseEntity.noContent().build();
	}

}
