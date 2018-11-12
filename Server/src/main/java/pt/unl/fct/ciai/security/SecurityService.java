package pt.unl.fct.ciai.security;

import java.util.Objects;
import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.query.spi.EvaluationContextExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import pt.unl.fct.ciai.model.Comment;
import pt.unl.fct.ciai.model.Company;
import pt.unl.fct.ciai.model.Employee;
import pt.unl.fct.ciai.model.Proposal;
import pt.unl.fct.ciai.model.Review;
import pt.unl.fct.ciai.repository.CommentsRepository;
import pt.unl.fct.ciai.repository.CompaniesRepository;
import pt.unl.fct.ciai.repository.EmployeesRepository;
import pt.unl.fct.ciai.repository.ProposalsRepository;
import pt.unl.fct.ciai.repository.ReviewsRepository;
import pt.unl.fct.ciai.repository.UsersRepository;

@Service
public class SecurityService {

	private UsersRepository users;
	private EmployeesRepository employees;
	private CompaniesRepository companies;
	private ProposalsRepository proposals;
	private ReviewsRepository reviews;
	private CommentsRepository comments;

    public SecurityService(UsersRepository users, CompaniesRepository companies,
    			EmployeesRepository employees, ProposalsRepository proposals, 
    			ReviewsRepository reviews, CommentsRepository comments) {
        this.users = users;
        this.employees = employees;
        this.companies = companies;
        this.proposals = proposals;
        this.reviews = reviews;
        this.comments = comments;
    }
    
    public boolean isPrincipal(User principal, long id) {
    	Optional<pt.unl.fct.ciai.model.User> user = users.findById(id);
    	return user.isPresent() && user.get().getUsername().equals(principal.getUsername());
    }

    public boolean isCompanyAdmin(User principal, long cid) {
		Employee employee = employees.findByUsername(principal.getUsername());
		return employee != null &&
				employee.getRole() == pt.unl.fct.ciai.model.User.Role.ROLE_COMPANY_ADMIN &&
				companies.existsEmployee(cid, employee.getId());
	}

	public boolean membersOfSameCompany(User principal, long uid) {
		Employee employee = employees.findByUsername(principal.getUsername());
		Employee otherEmployee = employees.findById(uid).orElse(null);
		return employee != null && otherEmployee != null &&
				Objects.equals(employee.getCompany(), otherEmployee.getCompany());
    }
	
	public boolean isAdminOfAuthorOfProposal(User principal, long pid) {
		pt.unl.fct.ciai.model.User user = users.findByUsername(principal.getUsername());
		pt.unl.fct.ciai.model.User proposer = proposals.getProposer(pid);
		return user != null && proposer != null && isAdminOfUser(principal, proposer.getId());
	}

	public boolean isAdminOfAuthorOfReview(User principal, long rid) {
		pt.unl.fct.ciai.model.User user = users.findByUsername(principal.getUsername());
		pt.unl.fct.ciai.model.User author = reviews.findById(rid).map(Review::getAuthor).orElse(null);
		return user != null && author != null && isAdminOfUser(principal, author.getId());
	}
	
	public boolean isAdminOfUser(User principal, long id) {
		pt.unl.fct.ciai.model.User user = users.findByUsername(principal.getUsername());
		return user != null &&
				user.getRole() == pt.unl.fct.ciai.model.User.Role.ROLE_COMPANY_ADMIN &&
				membersOfSameCompany(principal, id);
	}

    public boolean isPartOfProposal(User principal, long pid) {
    	pt.unl.fct.ciai.model.User user = users.findByUsername(principal.getUsername());
    	return user != null &&
				(proposals.existsStaff(pid, user.getId()) ||
				proposals.existsMember(pid, user.getId()) ||
				proposals.existsProposer(pid, user.getId()));
    }
    
    public boolean isAuthorOfProposal(User principal, long id) {
    	pt.unl.fct.ciai.model.User user = users.findByUsername(principal.getUsername());
		pt.unl.fct.ciai.model.User proposer = proposals.findById(id).map(Proposal::getProposer).orElse(null);
    	return Objects.equals(user, proposer);
    }
    
    public boolean isAuthorOfReview(User principal, long id) {
    	pt.unl.fct.ciai.model.User user = users.findByUsername(principal.getUsername());
    	pt.unl.fct.ciai.model.User author = reviews.findById(id).map(Review::getAuthor).orElse(null);
    	return Objects.equals(user, author);
    }
    
    public boolean isAuthorOfComment(User principal, long id) {
    	pt.unl.fct.ciai.model.User user = users.findByUsername(principal.getUsername());
    	pt.unl.fct.ciai.model.User author = comments.findById(id).map(Comment::getAuthor).orElse(null);
    	return Objects.equals(user, author);
    }
    
    public boolean isReviewerOfProposal(User principal, long id) {
    	pt.unl.fct.ciai.model.User user = users.findByUsername(principal.getUsername());
    	return user != null && proposals.existsReviewer(id, user.getId());
    }
    
    public boolean isProposalApproved(long id) {
    	Optional<Proposal> proposal = proposals.findById(id);
    	return proposal.map(Proposal::isApproved).orElse(false);
    }


	@Bean
	EvaluationContextExtension securityExtension() {
		return new SecurityEvaluationContextExtension();
	}

}

