package pt.unl.fct.ciai.controller;

import org.springframework.web.bind.annotation.*;
import pt.unl.fct.ciai.exceptions.ConflictException;
import pt.unl.fct.ciai.model.Comment;
import pt.unl.fct.ciai.repository.CommentsRepository;
import pt.unl.fct.ciai.exceptions.BadRequestException;
import pt.unl.fct.ciai.exceptions.NotFoundException;
import pt.unl.fct.ciai.model.Proposal;
import pt.unl.fct.ciai.model.Review;
import pt.unl.fct.ciai.repository.ProposalsRepository;
import pt.unl.fct.ciai.repository.ReviewsRepository;
import pt.unl.fct.ciai.model.Section;
import pt.unl.fct.ciai.model.User;
import pt.unl.fct.ciai.repository.SectionsRepository;

import java.util.Optional;

@RestController
@RequestMapping("/proposals")
public class ProposalsController {

    private ProposalsRepository proposals;
    private ReviewsRepository reviews;
    private CommentsRepository comments;
    private SectionsRepository sections;

    public ProposalsController(ProposalsRepository proposals, ReviewsRepository reviews,
                               CommentsRepository comments, SectionsRepository sections) {
        this.proposals = proposals;
        this.reviews = reviews;
        this.comments = comments;
        this.sections = sections;
    }

    @GetMapping("")
    Iterable<Proposal> getAllProposals(@RequestParam(required = false) String search){
        if(search == null)
            return proposals.findAll();
        else
            return proposals.searchProposals(search);
    }

    @GetMapping("/{id}")
    Proposal getProposalById(@PathVariable long id){
        Optional<Proposal> p1 = proposals.findById(id);
        if(p1.isPresent())
            return p1.get();
        else{
            throw new NotFoundException(String.format("Proposal with id %d does not exist.", id));
        }
    }

    @PostMapping("")
    void addProposal(@RequestBody Proposal proposal){
        proposals.save(proposal);
    }

    @PutMapping("/{id}")
    void updateProposal(@PathVariable long id, @RequestBody Proposal proposal){
        if(proposal.getId() == id) {
            Optional<Proposal> p1 = proposals.findById(id);
            if (p1.isPresent())
                proposals.save(proposal);
            else
                throw new NotFoundException(String.format("Proposal with id %d does not exist.", id));
        }
        else
            throw new BadRequestException("Invalid request: Body request proposal id and path id don't match.");
    }

    @DeleteMapping("/{id}")
    void deleteProposal(@PathVariable long id) {
        Optional<Proposal> p1 = proposals.findById(id);
        if( p1.isPresent() ) {
            proposals.delete(p1.get());
        } else
            throw new NotFoundException(String.format("Proposal with id %d does not exist.", id));
    }

    // TODO
    @GetMapping("/{id}/sections")
    Iterable<Section> getAllSectionsOfProposal(@PathVariable long id, @RequestParam(required = false) String search){
        Optional<Proposal> p = proposals.findById(id);
        if(p.isPresent()){
            if (search == null)
                return p.get().getSections();
            else
                return sections.searchSections(search); // ----> ver isto
        }
        else throw new NotFoundException(String.format("Proposal with id %d does not exist.", id));
    }

    @PostMapping("/{id}/sections")
    void addSectionOfProposal(@PathVariable long id, @RequestBody Section section){
        Optional<Proposal> p = proposals.findById(id);
        if(p.isPresent()){
            if(!p.get().getSections().contains(section))
                sections.save(section);
            else throw new ConflictException(String.format("Section already associated with proposal id %d.", id));
        }
        else throw new NotFoundException(String.format("Proposal with id %d does not exist.", id));
    }

    @PutMapping("/{pid}/sections/{sid}")
    void updateSectionOfProposal(@PathVariable long pid, @PathVariable long sid, @RequestBody Section section){
        if(section.getId() == sid) {
            Optional<Proposal> p = proposals.findById(pid);
            if (p.isPresent()){
                if(p.get().getSections().contains(section))
                    sections.save(section);
                else throw new BadRequestException(String.format("Proposal id %d does not have Section id %id associated", pid, sid));
            }
            else throw new NotFoundException(String.format("Proposal with id %d does not exist.", pid));
        }
        else throw new BadRequestException("Invalid request: sectionID on request does not match the Section.id attribute");
    }

    @DeleteMapping("/{pid}/sections/{sid}")
    void deleteSectionOfProposal(@PathVariable long pid, @PathVariable long sid){
        Optional<Proposal> p = proposals.findById(pid);
        if(p.isPresent()) {
            Optional<Section> s = sections.findById(sid);
            if(s.isPresent() && p.get().getSections().contains(s.get())){
                sections.deleteById(sid);
            }
            else throw new BadRequestException(String.format("Proposal id %d does not have Section id %id associated", pid, sid));
        }
        else throw new NotFoundException(String.format("Proposal with id %d does not exist.", pid));
    }

    // TODO
    @GetMapping("/{id}/reviews")
    Iterable<Review> getAllReviewsOfProposal(@PathVariable long id, @RequestParam(required = false) String search){
        Optional<Proposal> p = proposals.findById(id);
        if(p.isPresent()) {
            if (search == null)
                return p.get().getReviews();
            else
                return reviews.searchReviews(search); // ----> ver isto TODO
        }
        else throw new NotFoundException(String.format("Proposal with id %d does not exist.", id));
    }

    @PostMapping("/{id}/reviews")
    void addReview(@PathVariable long id, @RequestBody Review review){
        Optional<Proposal> p = proposals.findById(id);
        if(p.isPresent()){
            if(!p.get().getReviews().contains(review))
                reviews.save(review);
            else throw new ConflictException(String.format("Review already associated with proposal id %d", id));
        }
        else throw new NotFoundException(String.format("Proposal with id %d does not exist.", id));
    }

    @GetMapping("/{pid}/reviews/{rid}")
    Review getOneReviewOfProposal(@PathVariable long pid, @PathVariable long rid){
        Optional<Proposal> p = proposals.findById(pid);
        Optional<Review> r = reviews.findById(rid);
        if(p.isPresent()) {
            if (r.isPresent()) {
                Review review = r.get();
                if (review.getProposal().equals(p.get()))
                    return review;
                else throw new BadRequestException(String.format("Review with id %d does not belong to proposal id %d.", rid, pid));
            } else throw new NotFoundException(String.format("Review with id %d does not exist.", rid));
        } else throw new NotFoundException(String.format("Proposal with id %d does not exist.", pid));
    }

    @PutMapping("/{pid}/reviews/{rid}")
    void updateOneReviewOfProposal(@PathVariable long pid, @PathVariable long rid, @RequestBody Review review){
        if(review.getId() == rid) {
            Optional<Proposal> p = proposals.findById(pid);
            if (p.isPresent()){
                if(p.get().getReviews().contains(review))
                    reviews.save(review);
                else throw new NotFoundException(String.format("Proposal id %d does not have Review id %id associated", pid, rid));
            }
            else throw new NotFoundException(String.format("Proposal with id %d does not exist.", pid));
        }
        else throw new BadRequestException("Invalid request: reviewID on request does not match the Review.id attribute");
    }

    @DeleteMapping("/{pid}/reviews/{rid}")
    void deleteOneReviewOfProposal(@PathVariable long pid, @PathVariable long rid){
        Optional<Proposal> p = proposals.findById(pid);
        if(p.isPresent()) {
            Optional<Review> r = reviews.findById(rid);
            if(r.isPresent() && p.get().getReviews().contains(r.get())){
                reviews.deleteById(rid);
            }
            else throw new NotFoundException(String.format("Proposal id %d does not have Review id %id associated", pid, rid));
        }
        else throw new NotFoundException(String.format("Proposal with id %d does not exist.", pid));
    }

    //TODO
    @GetMapping("/{id}/comments")
    Iterable<Comment> getAllCommentsOfProposal(@PathVariable long id, @RequestParam(required = false) String search){
        Optional<Proposal> p = proposals.findById(id);
        if(p.isPresent()) {
            if (search == null)
                return p.get().getComments();
            else
                return comments.searchComments(search); // ----> ver isto TODO
        }
        else throw new NotFoundException(String.format("Proposal with id %d does not exist.", id));
    }

    @PostMapping("/{id}/comments")
    void addComment(@PathVariable long id, @RequestBody Comment comment){
        Optional<Proposal> p = proposals.findById(id);
        if(p.isPresent()){
            if(!p.get().getComments().contains(comment))
                comments.save(comment);
            else throw new ConflictException(String.format("Comment already associated with proposal id %d", id));
        }
        else throw new NotFoundException(String.format("Proposal with id %d does not exist.", id));
    }
    
    @GetMapping("/{pid}/comments/{cid}")
    Comment getOneCommentOfProposal(@PathVariable long pid, @PathVariable long cid){
        Optional<Proposal> p = proposals.findById(pid);
        Optional<Comment> c = comments.findById(cid);
        if(p.isPresent()) {
            if (c.isPresent()) {
            	Comment comment = c.get();
            	if (comment.getProposal().equals(p.get()))
            		return comment;
            	else throw new BadRequestException(String.format("Comment with id %d does not belong to proposal id %d.", cid, pid));
            } else throw new NotFoundException(String.format("Comment with id %d does not exist.", cid));
        } else throw new NotFoundException(String.format("Proposal with id %d does not exist.", pid));
    }

    @PutMapping("/{pid}/comments/{cid}")
    void updateOneCommentOfProposal(@PathVariable long pid, @PathVariable long cid, @RequestBody Comment comment){
        if(comment.getId() == cid) {
            Optional<Proposal> p = proposals.findById(pid);
            if (p.isPresent()){
            	Optional<Comment> c = comments.findById(cid);
            	if (c.isPresent()) {
            		if (c.get().getProposal().equals(p.get()))
            			comments.save(comment);
            		else throw new BadRequestException(String.format("Comment with id %d does not belong to proposal id %d.", cid, pid));
            	} else throw new NotFoundException(String.format("Comment with id %d does not exist.", cid));
            } else throw new NotFoundException(String.format("Proposal with id %d does not exist.", pid));
        } else throw new BadRequestException("Invalid request: commentID on request does not match the Comment.id attribute");
    }
    
    @DeleteMapping("/{pid}/comments/{cid}")
    void deleteOneCommentOfProposal(@PathVariable long pid, @PathVariable long cid){
    	Optional<Proposal> p = proposals.findById(pid);
    	if (p.isPresent()){
    		Optional<Comment> c = comments.findById(cid);
    		if (c.isPresent()) {
    			if (c.get().getProposal().equals(p.get()))
    				comments.deleteById(cid);
    			else throw new BadRequestException(String.format("Comment with id %d does not belong to proposal id %d.", cid, pid));
    		} else throw new NotFoundException(String.format("Comment with id %d does not exist.", cid));
    	} else throw new NotFoundException(String.format("Proposal with id %d does not exist.", pid));
    }
    
    // TODO
    @GetMapping("/{pid}/usersForBiding")
    Iterable<User> getUsersForBidingOfProposal(@PathVariable long pid){
        return null;
    }
    
    // TODO
    @PostMapping("/{pid}/usersForBiding/{uid}")
    void addUserForBiding(@PathVariable long pid, @PathVariable long uid, @RequestBody User user){
        
    }
    
    // TODO
    @DeleteMapping("/{pid}/usersForBiding/{uid}")
    void deleteUserForBiding(@PathVariable long pid, @PathVariable long uid){
        
    }    

}
