package pt.unl.fct.ciai.service;

import org.springframework.stereotype.Service;
import pt.unl.fct.ciai.exception.BadRequestException;
import pt.unl.fct.ciai.exception.NotFoundException;
import pt.unl.fct.ciai.model.*;
import pt.unl.fct.ciai.repository.*;
import pt.unl.fct.ciai.utils.Utils;

import java.util.Optional;

@Service
public class ProposalsService {

    private final ProposalsRepository proposalsRepository;
    private final UsersRepository usersRepository;
    private final EmployeesRepository employeesRepository;
    private final SectionsRepository sectionsRepository;
    private final ReviewsRepository reviewsRepository;
    private final CommentsRepository commentsRepository;

    public ProposalsService(ProposalsRepository proposalsRepository, UsersRepository usersRepository,
                            EmployeesRepository employeesRepository, SectionsRepository sectionsRepository,
                            ReviewsRepository reviewsRepository, CommentsRepository commentsRepository) {
        this.proposalsRepository = proposalsRepository;
        this.usersRepository = usersRepository;
        this.employeesRepository = employeesRepository;
        this.sectionsRepository = sectionsRepository;
        this.reviewsRepository = reviewsRepository;
        this.commentsRepository = commentsRepository;
    }

    public Iterable<Proposal> getProposals(String search) {
        //TODO esconder proposals não aprovadas (com uma query)
        return search == null ?
                proposalsRepository.findAll() :
                proposalsRepository.search(search);
    }

    public Proposal addProposal(Proposal proposal) {
        return proposalsRepository.save(proposal);
    }

    public Optional<Proposal> getProposal(long id) {
        //TODO esconder proposals não aprovadas (com uma query)
        return proposalsRepository.findById(id);
    }

    public Proposal updateProposal(Proposal newProposal) {
        Proposal proposal = getProposalIfPresent(newProposal.getId());
        Utils.copyNonNullProperties(newProposal, proposal);
        return proposalsRepository.save(proposal);
    }

    public void deleteProposal(long id) {
        Proposal proposal = getProposalIfPresent(id);
        proposalsRepository.delete(proposal);
    }

    public Iterable<Section> getSections(long id, String search) {
        return search == null ?
                proposalsRepository.getSections(id) :
                proposalsRepository.searchSections(id, search);
    }

    public Section addSection(long pid, Section section) {
        Proposal proposal = getProposalIfPresent(pid);
        section.setProposal(proposal);
        return sectionsRepository.save(section);
      /*  proposal.addSection(newSection);
        proposalsRepository.save(proposal);
        return newSection;*/ //TODO testar se realmente adiciona em todos
    }

    public Optional<Section> getSection(long pid, long sid) {
        getProposalIfPresent(pid);
        return Optional.ofNullable(proposalsRepository.getSection(pid, sid));
    }

    public Section updateSection(long pid, Section newSection) {
        Proposal proposal = getProposalIfPresent(pid);
        Section section = getSectionIfPresent(pid, newSection.getId());
        Utils.copyNonNullProperties(newSection, section);
        return sectionsRepository.save(section);
    }

    public void deleteSection(long pid, long sid) {
        Proposal proposal = getProposalIfPresent(pid);
        Section section = getSectionIfPresent(pid, sid);
        sectionsRepository.delete(section);
    }

    public Iterable<User> getStaff(long id, String search) {
        return search == null ?
                proposalsRepository.getStaff(id) :
                proposalsRepository.searchStaff(id, search);
    }

    public User addStaff(long pid, User user) {
        Proposal proposal = getProposalIfPresent(pid);
        user.addProposal(proposal);
        return usersRepository.save(user);
    }

    public Optional<User> getStaff(long pid, long uid) {
        getProposalIfPresent(pid);
        return Optional.ofNullable(proposalsRepository.getStaff(pid, uid));
    }

    public void removeStaff(long pid, long uid) {
        Proposal proposal = getProposalIfPresent(pid);
        User staff = getStaffIfPresent(pid, uid);
        proposal.removeStaff(staff);
        proposalsRepository.save(proposal); //TODO ver qual destes não é necessário
        staff.removeProposal(proposal);
        usersRepository.save(staff);
    }

    public Iterable<Employee> getMembers(long id, String search) {
        return search == null ?
                proposalsRepository.getMembers(id) :
                proposalsRepository.searchMembers(id, search);
    }

    public Employee addMember(long pid, Employee member) {
        Proposal proposal = getProposalIfPresent(pid);
        member.addProposal(proposal);
        return employeesRepository.save(member);
    }

    public Optional<Employee> getMember(long pid, long mid) {
        getProposalIfPresent(pid);
        return Optional.ofNullable(proposalsRepository.getMember(pid, mid));
    }

    public void removeMember(long pid, long mid) {
        Proposal proposal = getProposalIfPresent(pid);
        Employee member = getMemberIfPresent(pid, mid);
        proposal.removeMember(member);
        proposalsRepository.save(proposal);
        member.removeProposal(proposal);
        employeesRepository.save(member);
    }

    public Iterable<Review> getReviews(long id, String search) {
        return search == null ?
                proposalsRepository.getReviews(id) :
                proposalsRepository.searchReviews(id, search);
    }

    public Review addReview(long pid, Review review) {
        Proposal proposal = getProposalIfPresent(pid);
        review.setProposal(proposal);
        return reviewsRepository.save(review);
    }

    public Optional<Review> getReview(long pid, long rid) {
        return Optional.ofNullable(proposalsRepository.getReview(pid, rid));
    }

    public Review updateReview(long pid, Review newReview) {
        Proposal proposal = getProposalIfPresent(pid);
        Review review = getReviewIfPresent(pid, newReview.getId());
        Utils.copyNonNullProperties(newReview, review);
        return reviewsRepository.save(review);
    }

    public void deleteReview(long pid, long rid) {
        Proposal proposal = getProposalIfPresent(pid);
        Review review = getReviewIfPresent(pid, rid);
        reviewsRepository.delete(review);
    }

    public Iterable<Comment> getComments(long id, String search) {
        return search == null ?
                proposalsRepository.getComments(id) :
                proposalsRepository.searchComments(id, search);
    }

    public Comment addComment(long pid, Comment comment) {
        Proposal proposal = getProposalIfPresent(pid);
        comment.setProposal(proposal);
        return commentsRepository.save(comment);
    }

    public Optional<Comment> getComment(long pid, long cid) {
        return Optional.ofNullable(proposalsRepository.getComment(pid, cid));
    }

    public Comment updateComment(long pid, Comment newComent) {
        Proposal proposal = getProposalIfPresent(pid);
        Comment comment = proposalsRepository.getComment(pid, newComent.getId());
        Utils.copyNonNullProperties(newComent, comment);
        return commentsRepository.save(comment);
    }

    public void deleteComment(long pid, long cid) {
        Proposal proposal = getProposalIfPresent(pid);
        Comment comment = getCommentIfPresent(pid, cid);
        commentsRepository.delete(comment);
    }

    public Iterable<User> getReviewBiddings(long pid, String search) {
        return search == null ?
                proposalsRepository.getReviewBiddings(pid) :
                proposalsRepository.searchReviewBiddings(pid, search);
    }

    public User addReviewBidding(long pid, User user) {
        Proposal proposal = getProposalIfPresent(pid);
        //TODO user tem que existir no sistema
        user.addBidding(proposal);
        return usersRepository.save(user);
    }

    public Optional<User> getReviewBidding(long pid, long uid) {
        return Optional.ofNullable(proposalsRepository.getReviewBidding(pid, uid));
    }

    public void deleteReviewBidding(long pid, long uid) {
        Proposal proposal = getProposalIfPresent(pid);
        User user = getBiddingIfPresent(pid, uid);
        proposal.removeReviewBidding(user);
        proposalsRepository.save(proposal); //TODO ver qual destes não é necessário
        user.removeBidding(proposal);
        usersRepository.save(user);
    }

    private Proposal getProposalIfPresent(long id) {
        return this.getProposal(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Proposal with id %d not found.", id)));
    }

    private User getStaffIfPresent(long pid, long uid) {
        return this.getStaff(pid, uid)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Staff with id %d is not part of proposal with id %d.", uid, pid)));
    }

    private Employee getMemberIfPresent(long pid, long mid) {
        return this.getMember(pid, mid)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Member with id %d is not part of proposal with id %d.", mid, pid)));
    }

    private Section getSectionIfPresent(long pid, long sid) {
        return this.getSection(pid, sid)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Section with id %d doesn't belong to proposal with id %d.", sid, pid)));
    }

    private Review getReviewIfPresent(long pid, long rid) {
        return this.getReview(pid, rid)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Review with id %d doesn't belong to proposal with id %d.", rid, pid)));
    }

    private Comment getCommentIfPresent(long pid, long cid) {
        return this.getComment(pid, cid)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Comment with id %d doesn't belong to proposal with id %d.", cid, pid)));
    }

    private User getBiddingIfPresent(long pid, long uid) {
        return this.getReviewBidding(pid, uid)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Bidding with id %d doesn't belong to proposal with id %d.", uid, pid)));
    }
}
