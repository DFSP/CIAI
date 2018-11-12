package pt.unl.fct.ciai.controller;

import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.unl.fct.ciai.api.ProposalsApi;
import pt.unl.fct.ciai.assembler.*;
import pt.unl.fct.ciai.exception.BadRequestException;
import pt.unl.fct.ciai.exception.NotFoundException;
import pt.unl.fct.ciai.model.*;
import pt.unl.fct.ciai.service.ProposalsService;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping(value = "/proposals", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class ProposalsController implements ProposalsApi {

    private final ProposalsService proposalsService;

    private final ProposalResourceAssembler proposalAssembler;
    private final StaffResourcesAssembler staffAssembler;
    private final MemberResourceAssembler memberAssembler;
    private final SectionResourcesAssembler sectionAssembler;
    private final ReviewResourcesAssembler reviewAssembler;
    private final CommentResourcesAssembler commentAssembler;
    private final UserResourcesAssembler userAssembler;

    public ProposalsController(ProposalsService proposalsService,
                               ProposalResourceAssembler proposalAssembler,
                               StaffResourcesAssembler staffAssembler,
                               MemberResourceAssembler memberAssembler,
                               SectionResourcesAssembler sectionAssembler,
                               ReviewResourcesAssembler reviewAssembler, CommentResourcesAssembler commentAssembler,
                               UserResourcesAssembler userAssembler) {
        this.proposalsService = proposalsService;
        this.proposalAssembler = proposalAssembler;
        this.staffAssembler = staffAssembler;
        this.memberAssembler = memberAssembler;
        this.sectionAssembler = sectionAssembler;
        this.reviewAssembler = reviewAssembler;
        this.commentAssembler = commentAssembler;
        this.userAssembler = userAssembler;
    }

    @GetMapping
    public ResponseEntity<Resources<Resource<Proposal>>> getProposals(
            @RequestParam(value = "searchProposals", required = false) String search) {
        Iterable<Proposal> proposals = proposalsService.getProposals(search);
        Resources<Resource<Proposal>> resources = proposalAssembler.toResources(proposals);
        return ResponseEntity.ok(resources);
    }

    @PostMapping
    public ResponseEntity<Resource<Proposal>> addProposal(@Valid @RequestBody Proposal proposal)
            throws URISyntaxException {
        if (proposal.getId() > 0) {
            throw new BadRequestException(String.format("Expected non negative proposal id, instead got %d", proposal.getId()));
        }
        Proposal newProposal = proposalsService.addProposal(proposal);
        Resource<Proposal> resource = proposalAssembler.toResource(newProposal);
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @GetMapping("/{id}")
    // @CanReadProposal
    public ResponseEntity<Resource<Proposal>> getProposal(@PathVariable("id") long id) {
        Proposal proposal = getProposalIfPresent(id);
        Resource<Proposal> resource = proposalAssembler.toResource(proposal);
        return ResponseEntity.ok(resource);
    }

    @PutMapping("/{id}")
    // @CanModifyProposal
    public ResponseEntity<?> updateProposal(@PathVariable("id") long id, @RequestBody Proposal proposal) {
        if (id != proposal.getId()) {
            throw new BadRequestException(String.format("Path id %d and proposal id %d don't match.", id, proposal.getId()));
        }
        proposalsService.updateProposal(proposal);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    // @CanDeleteProposal
    public ResponseEntity<?> deleteProposal(@PathVariable("id") long id) {
        proposalsService.deleteProposal(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/sections")
    // @CanReadSection
    public ResponseEntity<Resources<Resource<Section>>> getSections(
            @PathVariable("id") long id, @RequestParam(value = "search", required = false) String search) {
        Proposal proposal = getProposalIfPresent(id);
        Iterable<Section> sections = proposalsService.getSections(id, search);
        Resources<Resource<Section>> resources = sectionAssembler.toResources(sections, proposal);
        return ResponseEntity.ok(resources);
    }

    @PostMapping("/{id}/sections")
    // @CanAddSection
    public ResponseEntity<Resource<Section>> addSection(@PathVariable("id") long id, @Valid @RequestBody Section section)
            throws URISyntaxException {
        if (section.getId() > 0) {
            throw new BadRequestException(String.format("Expected non negative section id, instead got %d", section.getId()));
        }
        Section newSection = proposalsService.addSection(id, section);
        Resource<Section> resource = sectionAssembler.toResource(newSection);
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @GetMapping("/{pid}/sections/{sid}")
    // @CanReadOneSection
    public ResponseEntity<Resource<Section>> getSection(@PathVariable("pid") long pid, @PathVariable("sid") long sid) {
        Section section = proposalsService.getSection(pid, sid).orElseThrow(() ->
                new NotFoundException(String.format("Section id %d does not belong to proposal with id %d", sid, pid)));
        Resource<Section> resource = sectionAssembler.toResource(section);
        return ResponseEntity.ok(resource);
    }

    @PutMapping("/{pid}/sections/{sid}")
    // @CanModifySection
    public ResponseEntity<?> updateSection(@PathVariable("pid") long pid, @PathVariable("sid") long sid, @RequestBody Section section) {
        if (sid != section.getId()) {
            throw new BadRequestException(String.format("Path id %d and section id %d don't match.", sid, section.getId()));
        }
        proposalsService.updateSection(pid, section);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{pid}/sections/{sid}")
    // @CanDeleteSection
    public ResponseEntity<?> deleteSection(@PathVariable("pid") long pid, @PathVariable("sid") long sid) {
        proposalsService.deleteSection(pid, sid);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/staff")
    public ResponseEntity<Resources<Resource<User>>> getStaff(
            @PathVariable("id") long id, @RequestParam(value = "search", required = false) String search) {
        Proposal proposal = getProposalIfPresent(id);
        Iterable<User> staff = proposalsService.getStaff(id, search);
        Resources<Resource<User>> resources = staffAssembler.toResources(staff, proposal);
        return ResponseEntity.ok(resources);
    }

    @PostMapping("/{id}/staff")
    // @CanAddStaff
    public ResponseEntity<Resource<User>> addStaff(@PathVariable("id") long id, @RequestBody User staff)
            throws URISyntaxException {
        Proposal proposal = getProposalIfPresent(id);
        User newStaff = proposalsService.addStaff(id, staff);
        Resource<User> resource = staffAssembler.toResource(newStaff, proposal);
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @GetMapping("/{pid}/staff/{uid}")
    public ResponseEntity<Resource<User>> getStaff(@PathVariable("pid") long pid, @PathVariable("uid") long uid) {
        Proposal proposal = getProposalIfPresent(pid);
        User staff = proposalsService.getStaff(pid, uid).orElseThrow(() ->
                new NotFoundException(String.format("Staff id %d does not belong to proposal with id %d", uid, pid)));
        Resource<User> resource = staffAssembler.toResource(staff, proposal);
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{pid}/staff/{uid}")
    /// @CanDeleteStaff
    public ResponseEntity<?> removeStaff(@PathVariable("pid") long pid, @PathVariable("uid") long uid) {
        proposalsService.removeStaff(pid, uid);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<Resources<Resource<Employee>>> getMembers(
            @PathVariable("id") long id, @RequestParam(value = "search", required = false) String search) {
        Proposal proposal = getProposalIfPresent(id);
        Iterable<Employee> members = proposalsService.getMembers(id, search);
        Resources<Resource<Employee>> resources = memberAssembler.toResources(members, proposal);
        return ResponseEntity.ok(resources);
    }

    @PostMapping("/{id}/members")
    // @CanAddMember
    public ResponseEntity<Resource<Employee>> addMember(@PathVariable("id") long id, @RequestBody Employee member)
            throws URISyntaxException {
        Proposal proposal = getProposalIfPresent(id);
        Employee newMember = proposalsService.addMember(id, member);
        Resource<Employee> resource = memberAssembler.toResource(newMember, proposal);
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @GetMapping("/{pid}/members/{mid}")
    public ResponseEntity<Resource<Employee>> getMember(@PathVariable("pid") long pid, @PathVariable("mid") long mid) {
        Proposal proposal = getProposalIfPresent(pid);
        Employee member = proposalsService.getMember(pid, mid).orElseThrow(() ->
                new NotFoundException(String.format("Member id %d does not belong to proposal with id %d", mid, pid)));
        Resource<Employee> resource = memberAssembler.toResource(member, proposal);
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{pid}/members/{mid}")
    // @CanDeleteMember
    public ResponseEntity<?> removeMember(@PathVariable("pid") long pid, @PathVariable("mid") long mid) {
        proposalsService.removeMember(pid, mid);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/reviews")
    // @CanReadReview
    public ResponseEntity<Resources<Resource<Review>>> getReviews(
            @PathVariable("id") long id, @RequestParam(value = "search", required = false) String search) {
        Proposal proposal = getProposalIfPresent(id);
        Iterable<Review> reviews = proposalsService.getReviews(id, search);
        Resources<Resource<Review>> resources = reviewAssembler.toResources(reviews, proposal);
        return ResponseEntity.ok(resources);
    }

    @PostMapping("/{id}/reviews")
    // @CanAddReview
    public ResponseEntity<?> addReview(@PathVariable("id") long id, @Valid @RequestBody Review review)
            throws URISyntaxException {
        if (review.getId() > 0) {
            throw new BadRequestException(String.format("Expected non negative review id, instead got %d", review.getId()));
        }
        Review newReview = proposalsService.addReview(id, review);
        Resource<Review> resource = reviewAssembler.toResource(newReview);
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @GetMapping("/{pid}/reviews/{rid}")
    // @CanReadOneReview
    public ResponseEntity<Resource<Review>> getReview(@PathVariable("pid") long pid, @PathVariable("rid") long rid) {
        Review review = proposalsService.getReview(pid, rid).orElseThrow(() ->
                new NotFoundException(String.format("Review id %d does not belong to proposal with id %d", rid, pid)));
        Resource<Review> resource = reviewAssembler.toResource(review);
        return ResponseEntity.ok(resource);
    }

    @PutMapping("/{pid}/reviews/{rid}")
    // @CanModifyReview
    public ResponseEntity<?> updateReview(
            @PathVariable("pid") long pid, @PathVariable("rid") long rid, @RequestBody Review review) {
        if (rid != review.getId()) {
            throw new BadRequestException(String.format("Path id %d and review id %d don't match.", rid, review.getId()));
        }
        proposalsService.updateReview(pid, review);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{pid}/reviews/{rid}")
    // @CanDeleteReview
    public ResponseEntity<?> deleteReview(@PathVariable("pid") long pid, @PathVariable("rid") long rid) {
        proposalsService.deleteReview(pid, rid);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/comments")
    // @CanReadComment
    public ResponseEntity<Resources<Resource<Comment>>> getComments(
            @PathVariable("id") long id, @RequestParam(value = "search", required = false) String search) {
        Proposal proposal = getProposalIfPresent(id);
        Iterable<Comment> comments = proposalsService.getComments(id, search);
        Resources<Resource<Comment>> resources = commentAssembler.toResources(comments, proposal);
        return ResponseEntity.ok(resources);
    }

    @PostMapping("/{id}/comments")
    // @CanAddComment
    public ResponseEntity<Resource<Comment>> addComment(@PathVariable("id") long id, @Valid @RequestBody Comment comment)
            throws URISyntaxException {
        if (comment.getId() > 0) {
            throw new BadRequestException(String.format("Expected non negative comment id, instead got %d", comment.getId()));
        }
        Comment newComment = proposalsService.addComment(id, comment);
        Resource<Comment> resource = commentAssembler.toResource(newComment);
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @GetMapping("/{pid}/comments/{cid}")
    // @CanReadOneComment
    public ResponseEntity<Resource<Comment>> getComment(@PathVariable("pid") long pid, @PathVariable("cid") long cid) {
        Comment comment = proposalsService.getComment(pid, cid).orElseThrow(() ->
                new NotFoundException(String.format("Comment id %d does not belong to proposal with id %d", cid, pid)));
        Resource<Comment> resource = commentAssembler.toResource(comment);
        return ResponseEntity.ok(resource);
    }

    @PutMapping("/{pid}/comments/{cid}")
    // @CanModifyComment
    public ResponseEntity<?> updateComment(
            @PathVariable("pid") long pid, @PathVariable("cid") long cid, @RequestBody Comment comment) {
        if (cid != comment.getId()) {
            throw new BadRequestException(String.format("Path id %d and comment id %d don't match.", cid, comment.getId()));
        }
        proposalsService.updateComment(pid, comment);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{pid}/comments/{cid}")
    // @CanDeleteComment
    public ResponseEntity<?> deleteComment(@PathVariable("pid") long pid, @PathVariable("cid") long cid) {
        proposalsService.deleteComment(pid, cid);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/biddings")
    public ResponseEntity<Resources<Resource<User>>> getReviewBiddings(
            @PathVariable("id") long id, @RequestParam(value = "search", required = false) String search) {
        Proposal proposal = getProposalIfPresent(id);
        Iterable<User> biddings = proposalsService.getReviewBiddings(id, search);
        Resources<Resource<User>> resources = userAssembler.toResources(biddings, proposal);
        return ResponseEntity.ok(resources);
    }

    @PostMapping("/{id}/biddings")
    // @CanAddBidding
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
                new NotFoundException(String.format("Bidding id %d does not belong to proposal with id %d", uid, pid)));
        Resource<User> resource = userAssembler.toResource(bidding);
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{pid}/biddings/{uid}")
    // @CanDeleteBidding
    public ResponseEntity<?> deleteReviewBidding(@PathVariable("pid") long pid, @PathVariable("uid") long uid) {
        proposalsService.deleteReviewBidding(pid, uid);
        return ResponseEntity.noContent().build();
    }

    private Proposal getProposalIfPresent(long id) {
        return proposalsService.getProposal(id).orElseThrow(() ->
                new NotFoundException(String.format("Proposal with id %d not found.", id)));
    }

}
