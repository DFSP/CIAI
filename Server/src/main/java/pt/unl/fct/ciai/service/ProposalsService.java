package pt.unl.fct.ciai.service;

import org.springframework.stereotype.Service;
import pt.unl.fct.ciai.exception.BadRequestException;
import pt.unl.fct.ciai.exception.NotFoundException;
import pt.unl.fct.ciai.model.*;
import pt.unl.fct.ciai.repository.*;

import java.util.Optional;

@Service
public class ProposalsService {

    private final ProposalsRepository proposalsRepository;
    private final SectionsRepository sectionsRepository;
    private final ReviewsRepository reviewsRepository;
    private final CommentsRepository commentsRepository;
    private final UsersRepository usersRepository;

    public ProposalsService(ProposalsRepository proposalsRepository, SectionsRepository sectionsRepository,
                            ReviewsRepository reviewsRepository, CommentsRepository commentsRepository,
                            UsersRepository usersRepository) {
        this.proposalsRepository = proposalsRepository;
        this.sectionsRepository = sectionsRepository;
        this.reviewsRepository = reviewsRepository;
        this.commentsRepository = commentsRepository;
        this.usersRepository = usersRepository;
    }

    public Iterable<Proposal> getProposals(String search) {
        //TODO esconder proposals não aprovadas (com uma query)
        return search == null ? proposalsRepository.findAll() : proposalsRepository.search(search);
    }

    public Proposal addProposal(Proposal proposal) {
        return proposalsRepository.save(proposal);
    }

    public Optional<Proposal> getProposal(long id) {
        //TODO esconder proposals não aprovadas (com uma query)
        return proposalsRepository.findById(id);
    }

    public Proposal updateProposal(Proposal proposal) {
        getProposalIfPresent(proposal.getId());
        return proposalsRepository.save(proposal);
    }

    public void deleteProposal(long id) {
        Proposal proposal = getProposalIfPresent(id);
        proposalsRepository.delete(proposal);
    }

    public Iterable<Section> getSections(long id, String search) {
        return search == null ? proposalsRepository.getSections(id) : proposalsRepository.searchSections(id, search);
    }

    public Section addSection(long pid, Section section) {
        Proposal proposal = getProposalIfPresent(pid);
        section.setProposal(proposal);
        proposal.addSection(section);
        proposalsRepository.save(proposal); //TODO verificar se é necessário
        return sectionsRepository.save(section);
    }

    public Optional<Section> getSection(long pid, long sid) {
        getProposalIfPresent(pid);
        return Optional.ofNullable(proposalsRepository.getSection(pid, sid));
    }

    public Section updateSection(long pid, Section section) {
        Proposal proposal = getProposalIfPresent(pid);
        if (!proposalsRepository.existsSection(pid, section.getId())) {
            throw new BadRequestException(String.format(
                    "Section with id %d doesn't belong to proposal with id %d", section.getId(), pid));
        }
        proposal.updateSection(section);
        proposalsRepository.save(proposal);
        return sectionsRepository.save(section);
    }

    public void deleteSection(long pid, long sid) {
        Proposal proposal = getProposalIfPresent(pid);
        Section section = getSectionIfPresent(pid, sid);
        proposal.removeSection(section);
        proposalsRepository.save(proposal); //TODO necessario?
        sectionsRepository.delete(section);
    }

    public Iterable<Review> getReviews(long id, String search) {
        return search == null ? proposalsRepository.getReviews(id) : proposalsRepository.searchReviews(id, search);
    }

    public Review addReview(long pid, Review review) {
        Proposal proposal = getProposalIfPresent(pid);
        review.setProposal(proposal);
        proposal.addReview(review);
        proposalsRepository.save(proposal); //TODO verificar se é necessário
        return reviewsRepository.save(review);
    }

    public Optional<Review> getReview(long pid, long rid) {
        return Optional.ofNullable(proposalsRepository.getReview(pid, rid));
    }

    public Review updateReview(long pid, Review review) {
        Proposal proposal = getProposalIfPresent(pid);
        if (!proposalsRepository.existsReview(pid, review.getId())) {
            throw new BadRequestException(String.format(
                    "Review with id %d doesn't belong to proposal with id %d", review.getId(), pid));
        }
        proposal.updateReview(review);
        proposalsRepository.save(proposal);
        return reviewsRepository.save(review);
    }

    public void deleteReview(long pid, long rid) {
        Proposal proposal = getProposalIfPresent(pid);
        Review review = getReviewIfPresent(pid, rid);
        proposal.removeReview(review);
        proposalsRepository.save(proposal); //TODO necessario?
        reviewsRepository.delete(review);
    }

    public Iterable<Comment> getComments(long id, String search) {
        return search == null ? proposalsRepository.getComments(id) : proposalsRepository.searchComments(id, search);
    }

    public Comment addComment(long pid, Comment comment) {
        Proposal proposal = getProposalIfPresent(pid);
        comment.setProposal(proposal);
        proposal.addComment(comment);
        proposalsRepository.save(proposal); //TODO verificar se é necessário
        return commentsRepository.save(comment);
    }

    public Optional<Comment> getComment(long pid, long cid) {
        return Optional.ofNullable(proposalsRepository.getComment(pid, cid));
    }

    public Comment updateComment(long pid, Comment comment) {
        Proposal proposal = getProposalIfPresent(pid);
        if (!proposalsRepository.existsComment(pid, comment.getId())) {
            throw new BadRequestException(String.format(
                    "Comment with id %d doesn't belong to proposal with id %d", comment.getId(), pid));
        }
        proposal.updateComment(comment);
        proposalsRepository.save(proposal);
        return commentsRepository.save(comment);
    }

    public void deleteComment(long pid, long cid) {
        Proposal proposal = getProposalIfPresent(pid);
        Comment comment = getCommentIfPresent(pid, cid);
        proposal.removeComment(comment);
        proposalsRepository.save(proposal); //TODO necessario?
        commentsRepository.delete(comment);
    }

    public Iterable<User> getReviewBiddings(long pid, String search) {
        return search == null ? proposalsRepository.getReviewBiddings(pid) : proposalsRepository.searchReviewBiddings(pid, search);
    }

    public User addReviewBidding(long pid, User user) {
        Proposal proposal = getProposalIfPresent(pid);
        //TODO user tem que existir no sistema
        user.addBidding(proposal);
        proposal.addReviewBidding(user);
        proposalsRepository.save(proposal); //TODO verificar se é necessário
        return usersRepository.save(user);
    }

    public Optional<User> getReviewBidding(long pid, long uid) {
        return Optional.ofNullable(proposalsRepository.getReviewBidding(pid, uid));
    }

    public void deleteReviewBidding(long pid, long uid) {
        Proposal proposal = getProposalIfPresent(pid);
        User user = getBiddingIfPresent(pid, uid);
        proposal.removeReviewBidding(user);
        proposalsRepository.save(proposal);
        user.removeBidding(proposal);
        usersRepository.save(user);
    }

    private Proposal getProposalIfPresent(long id) {
        return this.getProposal(id)
                .orElseThrow(() -> new NotFoundException(String.format("Proposal with id %d not found.", id)));
    }

    private Section getSectionIfPresent(long pid, long sid) {
        return getSection(pid, sid)
                .orElseThrow(() -> new BadRequestException(
                        String.format("Section with id %d doesn't belong to proposal with id %d.", sid, pid)));
    }

    private Review getReviewIfPresent(long pid, long rid) {
        return getReview(pid, rid)
                .orElseThrow(() -> new BadRequestException(
                        String.format("Review with id %d doesn't belong to proposal with id %d.", rid, pid)));
    }

    private Comment getCommentIfPresent(long pid, long cid) {
        return getComment(pid, cid)
                .orElseThrow(() -> new BadRequestException(
                        String.format("Comment with id %d doesn't belong to proposal with id %d.", cid, pid)));
    }

    private User getBiddingIfPresent(long pid, long uid) {
        return this.getReviewBidding(pid, uid)
                .orElseThrow(() -> new BadRequestException(
                        String.format("Bidding with id %d doesn't belong to proposal with id %d.", uid, pid)));
    }
}
