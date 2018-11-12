package pt.unl.fct.ciai.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pt.unl.fct.ciai.model.*;

public interface ProposalsRepository extends CrudRepository<Proposal, Long> {

    // Own queries

    @Query("SELECT DISTINCT p "
            + "FROM Proposal p JOIN p.staff s JOIN p.members m "
            + "WHERE p.state = 'APPROVED' "
            + "OR s.username = ?#{principal.username} "
            + "OR m.username = ?#{principal.username} "
            + "OR p.proposer.username = ?#{principal.username} "
            + "OR 1=?#{hasRole('ROLE_SYS_ADMIN') ? 1 : 0}")
    Iterable<Proposal> getPublicProposals();

    @Query("SELECT DISTINCT p "
            + "FROM Proposal p JOIN p.staff s JOIN p.members m "
            + "WHERE (p.state = 'APPROVED' "
            + "OR s.username = ?#{principal.username} "
            + "OR m.username = ?#{principal.username} "
            + "OR p.proposer.username = ?#{principal.username} "
            + "OR 1=?#{hasRole('ROLE_SYS_ADMIN') ? 1 : 0})"
            + "AND "
            + "(p.id LIKE CONCAT('%',:search,'%') "
            + "OR p.title LIKE CONCAT('%',:search,'%') "
            + "OR p.description LIKE CONCAT('%',:search,'%') "
            + "OR p.state LIKE CONCAT('%',:search,'%') "
            + "OR p.creationDate LIKE CONCAT('%',:search,'%'))")
    Iterable<Proposal> searchPublicProposals(@Param(value = "search") String search);

    @Query("SELECT p "
            + "FROM Proposal p JOIN p.staff s JOIN p.members m "
            + "WHERE p.id = :id "
            + "AND "
            + "(p.state = 'APPROVED' "
            + "OR s.username = ?#{principal.username} "
            + "OR m.username = ?#{principal.username} "
            + "OR p.proposer.username = ?#{principal.username} "
            + "OR 1=?#{hasRole('ROLE_SYS_ADMIN') ? 1 : 0})")
    Proposal getPublicProposal(@Param(value = "id") long id);

    // Section queries

    @Query("SELECT s "
            + "FROM Proposal p JOIN p.sections s "
            + "WHERE p.id = :pid"
    )
    Iterable<Section> getSections(@Param(value = "pid") long pid);

    @Query("SELECT s "
            + "FROM Proposal p JOIN p.sections s "
            + "WHERE p.id = :pid "
            + "AND "
            + "(s.title LIKE CONCAT('%',:search,'%') "
            + "OR s.description LIKE CONCAT('%',:search,'%') "
            + "OR s.id LIKE CONCAT('%',:search,'%') "
            + "OR s.goals LIKE CONCAT('%',:search,'%') "
            + "OR s.material LIKE CONCAT('%',:search,'%') "
            + "OR s.workPlan LIKE CONCAT('%',:search,'%') "
            + "OR s.budget LIKE CONCAT('%',:search,'%'))"
    )
    Iterable<Section> searchSections(@Param(value = "pid") long pid, @Param(value = "search") String search);

    @Query("SELECT s "
            + "FROM Proposal p JOIN p.sections s "
            + "WHERE p.id = :pid AND s.id = :sid"
    )
    Section getSection(@Param(value = "pid") long pid, @Param(value = "sid") long sid);

    @Query("SELECT CASE WHEN s IS NOT NULL THEN TRUE ELSE FALSE END " +
            "FROM Proposal p JOIN p.sections s " +
            "WHERE p.id = :pid AND s.id = :sid")
    boolean existsSection(@Param(value = "pid") long pid, @Param(value = "sid") long sid);

    // Staff queries

    @Query("SELECT s "
            + "FROM Proposal p JOIN p.staff s "
            + "WHERE p.id = :pid"
    )
    Iterable<User> getStaff(@Param(value = "pid") long pid);

    @Query("SELECT s "
            + "FROM Proposal p JOIN p.staff s "
            + "WHERE p.id = :pid "
            + "AND "
            + "(s.id LIKE CONCAT('%',:search,'%')"
            + "OR s.firstName LIKE CONCAT('%',:search,'%') "
            + "OR s.lastName LIKE CONCAT('%',:search,'%') "
            + "OR s.username LIKE CONCAT('%',:search,'%') "
            + "OR s.email LIKE CONCAT('%',:search,'%') "
            + "OR s.role LIKE CONCAT('%',:search,'%'))"
    )
    Iterable<User> searchStaff(@Param(value = "pid") long pid, @Param(value = "search") String search);

    @Query("SELECT u "
            + "FROM Proposal p JOIN p.staff u "
            + "WHERE p.id = :pid AND u.id = :uid"
    )
    User getStaff(@Param(value = "pid") long pid, @Param(value = "uid") long uid);


    @Query("SELECT CASE WHEN s IS NOT NULL THEN TRUE ELSE FALSE END " +
            "FROM Proposal p JOIN p.staff s " +
            "WHERE p.id = :pid AND s.id = :sid")
    boolean existsStaff(@Param(value = "pid") long pid, @Param(value = "sid") long sid);

    // Member queries

    @Query("SELECT m "
            + "FROM Proposal p JOIN p.members m "
            + "WHERE p.id = :pid"
    )
    Iterable<Employee> getMembers(@Param(value = "pid") long pid);

    @Query("SELECT e "
            + "FROM Proposal p JOIN p.members e "
            + "WHERE p.id = :pid "
            + "AND "
            + "(e.id LIKE CONCAT('%',:search,'%') "
            + "OR e.city LIKE CONCAT('%',:search,'%') "
            + "OR e.address LIKE CONCAT('%',:search,'%') "
            + "OR e.zipCode LIKE CONCAT('%',:search,'%') "
            + "OR e.cellPhone LIKE CONCAT('%',:search,'%') "
            + "OR e.homePhone LIKE CONCAT('%',:search,'%') "
            + "OR e.gender LIKE CONCAT('%',:search,'%') "
            + "OR e.salary LIKE CONCAT('%',:search,'%') "
            + "OR e.birthday LIKE CONCAT('%',:search,'%'))"
    )

    Iterable<Employee> searchMembers(@Param(value = "pid") long pid, @Param(value = "search") String search);

    @Query("SELECT e "
            + "FROM Proposal p JOIN p.members e "
            + "WHERE p.id = :pid AND e.id = :eid"
    )
    Employee getMember(@Param(value = "pid") long pid, @Param(value = "eid") long eid);

    @Query("SELECT CASE WHEN m IS NOT NULL THEN TRUE ELSE FALSE END " +
            "FROM Proposal p JOIN p.members m " +
            "WHERE p.id = :pid AND m.id = :mid")
    boolean existsMember(@Param(value = "pid") long pid, @Param(value = "mid") long mid);

    // Review queries

    @Query("SELECT r "
            + "FROM Proposal p JOIN p.reviews r "
            + "WHERE p.id = :pid"
    )
    Iterable<Review> getReviews(@Param(value = "pid") long pid);

    @Query("SELECT r "
            + "FROM Proposal p JOIN p.reviews r "
            + "WHERE p.id = :pid "
            + "AND "
            + "(r.id LIKE CONCAT('%',:search,'%') "
            + "OR r.title LIKE CONCAT('%',:search,'%') "
            + "OR r.text LIKE CONCAT('%',:search,'%') "
            + "OR r.summary LIKE CONCAT('%',:search,'%') "
            + "OR r.classification LIKE CONCAT('%',:search,'%') "
            + "OR r.creationDate LIKE CONCAT('%',:search,'%'))"
    )
    Iterable<Review> searchReviews(@Param(value = "pid") long pid, @Param(value = "search") String search);

    @Query("SELECT r "
            + "FROM Proposal p JOIN p.reviews r "
            + "WHERE p.id = :pid AND r.id = :rid"
    )
    Review getReview(@Param(value = "pid") long pid, @Param(value = "rid") long rid);

    @Query("SELECT CASE WHEN r IS NOT NULL THEN TRUE ELSE FALSE END " +
            "FROM Proposal p JOIN p.reviews r " +
            "WHERE p.id = :pid AND r.id = :rid")
    boolean existsReview(@Param(value = "pid") long pid, @Param(value = "rid") long rid);


    // Comment queries

    @Query("SELECT c "
            + "FROM Proposal p JOIN p.comments c "
            + "WHERE p.id = :pid"
    )
    Iterable<Comment> getComments(@Param(value = "pid") long pid);

    @Query("SELECT c "
            + "FROM Proposal p JOIN p.comments c "
            + "WHERE p.id = :pid "
            + "AND "
            + "(c.id LIKE CONCAT('%',:search,'%') "
            + "OR c.title LIKE CONCAT('%',:search,'%') "
            + "OR c.text LIKE CONCAT('%',:search,'%') "
            + "OR c.creationDate LIKE CONCAT('%',:search,'%'))"
    )
    Iterable<Comment> searchComments(@Param(value = "pid") long pid, @Param(value = "search") String search);

    @Query("SELECT c "
            + "FROM Proposal p JOIN p.comments c "
            + "WHERE p.id = :pid AND c.id = :cid"
    )
    Comment getComment(@Param(value = "pid") long pid, @Param(value = "cid") long cid);

    @Query("SELECT CASE WHEN c IS NOT NULL THEN TRUE ELSE FALSE END " +
            "FROM Proposal p JOIN p.comments c " +
            "WHERE p.id = :pid AND c.id = :cid")
    boolean existsComment(@Param(value = "pid") long pid, @Param(value = "cid") long cid);


    // Biddings queries  

    @Query("SELECT u "
            + "FROM Proposal p JOIN p.reviewBiddings u "
            + "WHERE p.id = :pid"
    )
    Iterable<User> getReviewBiddings(@Param(value = "pid") long pid);

    @Query("SELECT u "
            + "FROM Proposal p JOIN p.reviewBiddings u "
            + "WHERE p.id = :pid "
            + "AND "
            + "(u.id LIKE CONCAT('%',:search,'%') "
            + "OR u.firstName LIKE CONCAT('%',:search,'%') "
            + "OR u.lastName LIKE CONCAT('%',:search,'%') "
            + "OR u.username LIKE CONCAT('%',:search,'%') "
            + "OR u.email LIKE CONCAT('%',:search,'%') "
            + "OR u.role LIKE CONCAT('%',:search,'%'))"
    )
    Iterable<User> searchReviewBiddings(@Param(value = "pid") long pid, @Param(value = "search") String search);

    @Query("SELECT u "
            + "FROM Proposal p JOIN p.reviewBiddings u "
            + "WHERE p.id = :pid AND u.id = :uid"
    )
    User getReviewBidding(@Param(value = "pid") long pid, @Param(value = "uid") long uid);


    // Proposer queries

    @Query("SELECT u "
            + "FROM Proposal p JOIN p.proposer u "
            + "WHERE p.id = :pid"
    )
    User getProposer(@Param(value = "pid") long pid);

    @Query("SELECT CASE WHEN u IS NOT NULL THEN TRUE ELSE FALSE END "
            + "FROM Proposal p "
            + "WHERE p.id = :pid AND p.proposer.id = :uid")
    boolean existsProposer(@Param(value = "pid") long pid, @Param(value = "uid") long uid);

    // Reviewer queries

    @Query("SELECT CASE WHEN r IS NOT NULL THEN TRUE ELSE FALSE END "
            + "FROM Proposal p JOIN p.reviewers r "
            + "WHERE p.id = :pid AND r.id = :uid")
    boolean existsReviewer(@Param(value = "pid") long pid, @Param(value = "uid") long uid);

}
