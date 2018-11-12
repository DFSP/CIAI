package pt.unl.fct.ciai.security;

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
    
    public boolean isPrincipal(User user, long id) {
    	Optional<pt.unl.fct.ciai.model.User> u = users.findById(id);
    	return u.isPresent() && u.get().getUsername().equals(user.getUsername());
    }

    public boolean isCompanyAdmin(User user, long id) {
		pt.unl.fct.ciai.model.User u = users.findByUsername(user.getUsername());
		return u != null && u.getRole() == pt.unl.fct.ciai.model.User.Role.ROLE_COMPANY_ADMIN && 
				companies.existsEmployee(id, u.getId());
	}

	public boolean membersOfSameCompany(User user, long uid) {
		pt.unl.fct.ciai.model.User myself = users.findByUsername(user.getUsername());
		Optional<pt.unl.fct.ciai.model.User> otherUser = users.findById(uid);
		if (myself != null && otherUser.isPresent()) {
			Optional<Employee> amIAnEmployee = employees.findById(myself.getId());
			Optional<Employee> isOtherUserAnEmployee = employees.findById(uid);
			if (amIAnEmployee.isPresent() == isOtherUserAnEmployee.isPresent()) {
				if (!amIAnEmployee.isPresent())
					return true; // both are members of ecma
				else return amIAnEmployee.get().getCompany().equals(isOtherUserAnEmployee.get().getCompany());
			}
		}
		return false;
    }
	
	public boolean isAdminOfAuthorOfProposal(User user, long id) {
		pt.unl.fct.ciai.model.User myself = users.findByUsername(user.getUsername());
		if (myself != null) {
			Optional<Proposal> p = proposals.findById(id);
			if (p.isPresent()) {
				Optional<pt.unl.fct.ciai.model.User> proposer = p.get().getProposer();
				return proposer.isPresent() && membersOfSameCompany(user, proposer.get().getId()) && 
						myself.getRole() == pt.unl.fct.ciai.model.User.Role.ROLE_COMPANY_ADMIN;
			}
		}
		return false;
	}
	
	public boolean isAdminOfAuthorOfReview(User user, long id) {
		pt.unl.fct.ciai.model.User myself = users.findByUsername(user.getUsername());
		if (myself != null) {
			Optional<Review> r = reviews.findById(id);
			if (r.isPresent()) {
				Optional<pt.unl.fct.ciai.model.User> author = r.get().getAuthor();
				return author.isPresent() && membersOfSameCompany(user, author.get().getId()) && 
						myself.getRole() == pt.unl.fct.ciai.model.User.Role.ROLE_COMPANY_ADMIN;
			}
		}
		return false;
	}
	
	public boolean isAdminOfUser(User user, long id) {
		pt.unl.fct.ciai.model.User myself = users.findByUsername(user.getUsername());
		Optional<pt.unl.fct.ciai.model.User> u = users.findById(id);
		return myself != null && u.isPresent() && membersOfSameCompany(user, u.get().getId()) && 
				myself.getRole() == pt.unl.fct.ciai.model.User.Role.ROLE_COMPANY_ADMIN;
	}
    
    public boolean isMemberOrStaffOfProposal(User user, long id) {
    	pt.unl.fct.ciai.model.User myself = users.findByUsername(user.getUsername());
    	return myself != null && 
    			(proposals.getMember(id, myself.getId()) != null || proposals.getStaff(id, myself.getId()) != null);
    }
    
    public boolean isAuthorOfProposal(User user, long id) {
    	pt.unl.fct.ciai.model.User u = users.findByUsername(user.getUsername());
    	Optional<Proposal> p = proposals.findById(id);
    	Optional<pt.unl.fct.ciai.model.User> proposer = Optional.empty();
    	if (p.isPresent())
    		proposer = p.get().getProposer();
    	return u != null && proposer.isPresent() && u.equals(proposer.get());
    }
    
    public boolean isAuthorOfReview(User user, long id) {
    	pt.unl.fct.ciai.model.User u = users.findByUsername(user.getUsername());
    	Optional<Review> r = reviews.findById(id);
    	Optional<pt.unl.fct.ciai.model.User> author = Optional.empty();
    	if (r.isPresent())
    		author = r.get().getAuthor();
    	return u != null && author.isPresent() && u.equals(author.get());
    }
    
    public boolean isAuthorOfComment(User user, long id) {
    	pt.unl.fct.ciai.model.User u = users.findByUsername(user.getUsername());
    	Optional<Comment> c = comments.findById(id);
    	Optional<pt.unl.fct.ciai.model.User> author = Optional.empty();
    	if (c.isPresent())
    		author = c.get().getAuthor();
    	return u != null && author.isPresent() && u.equals(author.get());
    }
    
    public boolean isReviewerOfProposal(User user, long id) {
    	pt.unl.fct.ciai.model.User u = users.findByUsername(user.getUsername());
    	return u != null && proposals.existsReviewer(id, u.getId());
    }
    
    public boolean isProposalApproved(long id) {
    	Optional<Proposal> p = proposals.findById(id);
    	return p.isPresent() && p.get().getState() == Proposal.ProposalState.APPROVED;
    }

	@Bean
	EvaluationContextExtension securityExtension() {
		return new SecurityEvaluationContextExtension();
	}
}

