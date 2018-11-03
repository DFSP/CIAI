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
            throw new NotFoundException("Proposal with id "+id+" does not exist.");
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
                throw new NotFoundException("Proposal with id "+id+" does not exist.");
        }
        else
            throw new BadRequestException("invalid request");
    }

    @DeleteMapping("/{id}")
    void deleteProposal(@PathVariable long id) {
        Optional<Proposal> p1 = proposals.findById(id);
        if( p1.isPresent() ) {
            proposals.delete(p1.get());
        } else
            throw new NotFoundException("Proposal with id "+id+" does not exist.");
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
        else throw new NotFoundException("Proposal with id" +id+" not found.");
    }

    @PostMapping("/{id}/sections")
    void addSectionOfProposal(@PathVariable long id, @RequestBody Section section){
        Optional<Proposal> p = proposals.findById(id);
        if(p.isPresent()){
            if(!p.get().getSections().contains(section))
                sections.save(section);
            else throw new ConflictException("Section already associated with proposal " + id);
        }
        else throw new NotFoundException("Proposal "+id+" not found.");
    }

    @PutMapping("/{pid}/sections/{sid}")
    void updateSectionOfProposal(@PathVariable long pid, @PathVariable long sid, @RequestBody Section section){
        if(section.getId() == sid) {
            Optional<Proposal> p = proposals.findById(pid);
            if (p.isPresent()){
                if(p.get().getSections().contains(section))
                    sections.save(section);
                else throw new NotFoundException("Proposal "+pid+" does not have associated Section "+sid);
            }
            else throw new NotFoundException("Proposal "+pid+" not found.");
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
            else throw new NotFoundException("Proposal "+pid+" does not have Section "+sid+" associated.");
        }
        else throw new NotFoundException("Proposal "+pid+" not found.");
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
        else throw new NotFoundException("Proposal "+id+" not found.");
    }

    @PostMapping("/{id}/reviews")
    void addReview(@PathVariable long id, @RequestBody Review review){
        Optional<Proposal> p = proposals.findById(id);
        if(p.isPresent()){
            if(!p.get().getReviews().contains(review))
                reviews.save(review);
            else throw new ConflictException("Review already associated with proposal " + id);
        }
        else throw new NotFoundException("Proposal "+id+" not found.");
    }

    //TODO
    @GetMapping("/{pid}/reviews/{rid}")
    Review getOneReviewOfProposal(@PathVariable long pid, @PathVariable long rid){
        Optional<Proposal> p = proposals.findById(pid);
        if(p.isPresent())
            return reviews.findById(rid).get(); // -----> não está a apanhar da lista da Proposal p, mas sim do repositorio TODO
        else throw new NotFoundException("Proposal "+pid+" not found.");
    }

    @PutMapping("/{pid}/reviews/{rid}")
    void updateOneReviewOfProposal(@PathVariable long pid, @PathVariable long rid, @RequestBody Review review){
        if(review.getId() == rid) {
            Optional<Proposal> p = proposals.findById(pid);
            if (p.isPresent()){
                if(p.get().getReviews().contains(review))
                    reviews.save(review);
                else throw new NotFoundException("Proposal "+pid+" does not have associated Review "+rid);
            }
            else throw new NotFoundException("Proposal "+pid+" not found.");
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
            else throw new NotFoundException("Proposal "+pid+" does not have Review "+rid+" associated.");
        }
        else throw new NotFoundException("Proposal "+pid+" not found.");
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
        else throw new NotFoundException("Proposal "+id+" not found.");
    }

    @PostMapping("/{id}/comments")
    void addComment(@PathVariable long id, @RequestBody Comment comment){
        Optional<Proposal> p = proposals.findById(id);
        if(p.isPresent()){
            if(!p.get().getComments().contains(comment))
                comments.save(comment);
            else throw new ConflictException("Comment already associated with proposal " + id);
        }
        else throw new NotFoundException("Proposal "+id+" not found.");
    }
    
    // TODO
    @GetMapping("/{pid}/comments/{cid}")
    Optional<Comment> getOneCommentOfProposal(@PathVariable long pid, @PathVariable long cid){
        Optional<Proposal> p = proposals.findById(pid);
        if(p.isPresent())
            return comments.findById(cid); // -----> não está a apanhar da lista da Proposal p, mas sim do repositorio TODO
        else throw new NotFoundException("Proposal "+pid+" not found.");
    }

    @PutMapping("/{pid}/comments/{cid}")
    void updateOneCommentOfProposal(@PathVariable long pid, @PathVariable long cid, @RequestBody Comment comment){
        if(comment.getId() == cid) {
            Optional<Proposal> p = proposals.findById(pid);
            if (p.isPresent()){
                if(p.get().getComments().contains(comment))
                    comments.save(comment);
                else throw new NotFoundException("Proposal "+pid+" does not have associated Comment "+cid);
            }
            else throw new NotFoundException("Proposal "+pid+" not found.");
        }
        else throw new BadRequestException("Invalid request: commentID on request does not match the Comment.id attribute");
    }
    
    @DeleteMapping("/{pid}/comments/{cid}")
    void deleteOneCommentOfProposal(@PathVariable long pid, @PathVariable long cid){
        Optional<Proposal> p = proposals.findById(pid);
        if(p.isPresent()) {
            Optional<Comment> c = comments.findById(cid);
            if(c.isPresent() && p.get().getComments().contains(c.get())){
                comments.deleteById(cid);
            }
            else throw new NotFoundException("Proposal "+pid+" does not have Comment "+cid+" associated.");
        }
        else throw new NotFoundException("Proposal "+pid+" not found.");
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
