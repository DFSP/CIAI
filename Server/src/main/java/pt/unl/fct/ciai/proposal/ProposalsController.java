package pt.unl.fct.ciai.proposal;

import org.springframework.web.bind.annotation.*;
import pt.unl.fct.ciai.comment.Comment;
import pt.unl.fct.ciai.comment.CommentsRepository;
import pt.unl.fct.ciai.exceptions.BadRequestException;
import pt.unl.fct.ciai.exceptions.NotFoundException;
import pt.unl.fct.ciai.review.Review;
import pt.unl.fct.ciai.review.ReviewsRepository;

import java.util.Optional;

@RestController
@RequestMapping("/proposals")
public class ProposalsController {

    private ProposalsRepository proposals;
    private ReviewsRepository reviews;
    private CommentsRepository comments;

    public ProposalsController(ProposalsRepository proposals, ReviewsRepository reviews,
                               CommentsRepository comments) {
        this.proposals = proposals;
        this.reviews = reviews;
        this.comments = comments;
    }

    @GetMapping("")
    Iterable<Proposal> getAllProposals(@RequestParam(required = false) String search){
        if(search == null)
            return proposals.findAll();
        else
            return proposals.searchProposals(search);
    }

    @GetMapping("{id}")
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

    @PutMapping("{id}")
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

    @DeleteMapping("{id}")
    void deleteProposal(@PathVariable long id) {
        Optional<Proposal> p1 = proposals.findById(id);
        if( p1.isPresent() ) {
            proposals.delete(p1.get());
        } else
            throw new NotFoundException("Proposal with id "+id+" does not exist.");
    }

    @GetMapping("{id}/reviews")
    Iterable<Review> getAllReviewsOfProposal(@RequestParam(required = false) String search){
        if(search == null)
            return reviews.findAll();
        else
            return reviews.searchReviews(search);
    }

    @PostMapping("{id}/reviews")
    void addReview(@RequestBody Review review){
        reviews.save(review);
    }

    @GetMapping("{id}/comments")
    Iterable<Comment> getAllCommentsOfProposal(@RequestParam(required = false) String search){
        if(search == null)
            return comments.findAll();
        else
            return comments.searchComments(search);
    }

    @PostMapping("{id}/comments")
    void addComment(@RequestBody Comment comment){
        comments.save(comment);
    }

}
