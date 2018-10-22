package pt.unl.fct.ciai.proposal;

import org.springframework.web.bind.annotation.*;
import pt.unl.fct.ciai.exceptions.BadRequestException;
import pt.unl.fct.ciai.exceptions.NotFoundException;
import pt.unl.fct.ciai.review.Review;
import pt.unl.fct.ciai.review.ReviewsRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/proposals")
public class ProposalsController {

    private ProposalsRepository eventProposals;
    private ReviewsRepository reviews;

    public ProposalsController(ProposalsRepository proposals, ReviewsRepository reviews) {
        this.eventProposals = proposals;
        this.reviews = reviews;

    }

    @GetMapping("")
    Iterable<Proposal> getAllProposals(@RequestParam(required = false) String search){
        if(search == null)
            return eventProposals.findAll();
        else
            return eventProposals.searchProposals(search);
    }

    @GetMapping("{id}")
    Proposal getProposalById(@PathVariable long id){
        Optional<Proposal> p1 = eventProposals.findById(id);
        if(p1.isPresent())
            return p1.get();
        else{
            throw new NotFoundException("Proposal with id "+id+" does not exist.");
        }
    }

    @PostMapping("")
    void addProposal(@RequestBody Proposal proposal){
        eventProposals.save(proposal);
    }

    @PutMapping("{id}")
    void updateProposal(@PathVariable long id, @RequestBody Proposal proposal){
        if(proposal.getId() == id) {
            Optional<Proposal> p1 = eventProposals.findById(id);
            if (p1.isPresent())
                eventProposals.save(proposal);
            else
                throw new NotFoundException("Proposal with id "+id+" does not exist.");
        }
        else
            throw new BadRequestException("invalid request");
    }

    @DeleteMapping("{id}")
    void deleteProposal(@PathVariable long id) {
        Optional<Proposal> p1 = eventProposals.findById(id);
        if( p1.isPresent() ) {
            eventProposals.delete(p1.get());
        } else
            throw new NotFoundException("Proposal with id "+id+" does not exist.");
    }

    //TODO: /?search="something"   cria-se um .searchReviews na classe Review?
    @GetMapping("{id}/reviews")
    Iterable<Review> getReviewsByProposalId(@PathVariable long id){//, @RequestParam(required = false) String search){
        //if(search == null) {
            Optional<Proposal> p1 = eventProposals.findById(id);
            if (p1.isPresent())
                return p1.get().getReviews();
            else{
                throw new NotFoundException("Proposal with id "+id+" does not exist.");
            }
        /*}
        else
            return reviews.searchReviews(search);
        */
    }

    @PostMapping("{id}/reviews")
    void addReviewToProposal(@PathVariable long id, @RequestBody Review review){
        Optional<Proposal> p1 = eventProposals.findById(id);
        if (p1.isPresent()) {
            List<Review> aux = p1.get().getReviews();
            aux.add(review);
            p1.get().setReviews(aux);
        }
        else{
            throw new NotFoundException("Proposal with id "+id+" does not exist.");
        }
    }


}
