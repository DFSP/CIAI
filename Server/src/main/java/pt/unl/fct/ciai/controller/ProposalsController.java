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

    @GetMapping("/{id}/sections")
    Iterable<Section> getAllSectionsOfProposal(@PathVariable long id){

        Optional<Proposal> p = proposals.findById(id);
        if(p.isPresent()){
            return p.get().getSections();
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
        else throw new BadRequestException("Invalid request: sectionID on request does not match the SectionClass id attribute");
    }

    @DeleteMapping("/{pid}/sections/{sid}")
    void deleteSectionOfProposal(@PathVariable long pid, @PathVariable long sid){
        Optional<Proposal> p = proposals.findById(pid);
        if(p.isPresent()) {
            Optional<Section> s = sections.findById(sid);
            if(s.isPresent() && p.get().getSections().contains(s)){
                sections.deleteById(sid);
            }
            else throw new NotFoundException("Proposal "+pid+" does not have Section "+sid+" associated.");
        }
        else throw new NotFoundException("Proposal "+pid+" not found.");
    }


    @GetMapping("/{id}/reviews")
    Iterable<Review> getAllReviewsOfProposal(@RequestParam(required = false) String search){
        if(search == null)
            return reviews.findAll();
        else
            return reviews.searchReviews(search);
    }

    @PostMapping("/{id}/reviews")
    void addReview(@RequestBody Review review){
        reviews.save(review);
    }
    
    //TODO
    @GetMapping("/{pid}/reviews/{rid}")
    Review getOneReviewOfProposal(@PathVariable long pid, @PathVariable long rid){
        return null;
    }
    
    //TODO
    @PutMapping("/{pid}/reviews/{rid}")
    void updateOneReviewOfProposal(@PathVariable long pid, @PathVariable long rid, @RequestBody Review review){
        
    }
    
    //TODO
    @DeleteMapping("/{pid}/reviews/{rid}")
    void deleteOneReviewOfProposal(@PathVariable long pid, @PathVariable long rid){
        
    }

    @GetMapping("/{id}/comments")
    Iterable<Comment> getAllCommentsOfProposal(@RequestParam(required = false) String search){
        if(search == null)
            return comments.findAll();
        else
            return comments.searchComments(search);
    }

    @PostMapping("/{id}/comments")
    void addComment(@RequestBody Comment comment){
        comments.save(comment);
    }
    
    // TODO
    @GetMapping("/{pid}/comments/{cid}")
    Comment getOneCommentOfProposal(@PathVariable long pid, @PathVariable long cid){
        return null;
    }
    
    // TODO
    @PutMapping("/{pid}/comments/{cid}")
    void updateOneCommentOfProposal(@PathVariable long pid, @PathVariable long cid){
        
    }
    
    // TODO
    @DeleteMapping("/{pid}/comments/{cid}")
    void deleteOneCommentOfProposal(@PathVariable long pid, @PathVariable long cid){
        
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
