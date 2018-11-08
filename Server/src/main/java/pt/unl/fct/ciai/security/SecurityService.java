package pt.unl.fct.ciai.security;

import java.util.Optional;

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

    public boolean isUserOfSystem(User user, long id) {
		Optional<pt.unl.fct.ciai.model.User> u = users.findById(id);
		return u.isPresent() && u.get().getUsername().equals(user.getUsername());
	}

    public boolean isSystemAdmin(User user, long id) {
		Optional<pt.unl.fct.ciai.model.User> u = users.findById(id);
		return u.isPresent() && u.get().getRole().equals("ADMIN");
	}

    public boolean isCompanyAdmin(User user, long id) {
    	/*Optional<Company> company = companies.findById(id);
    	return company.isPresent() && company.get().getAdmin().getUsername().equals(user.getUsername());*/
    	return false; // TODO mudar para query
    }
    
    public boolean isMemberOfMyCompany(long cid, long eid) {
    	Optional<Employee> e = employees.findById(eid);
    	Optional<Company> c = companies.findById(cid);
    	return e.isPresent() && c.isPresent() && e.get().getCompany().equals(c.get());
    }
    
    public boolean isMemberOfProposal(long pid, long uid) {
    	Optional<pt.unl.fct.ciai.model.User> u = users.findById(uid);
    	Optional<Proposal> p = proposals.findById(pid);
    	return u.isPresent() && p.isPresent() && u.get().getProposals().get().contains(p.get()); //TODO
    }
    
    public boolean isCreatorOfProposal(long pid, long uid) {
    	Optional<pt.unl.fct.ciai.model.User> u = users.findById(uid);
    	Optional<Proposal> p = proposals.findById(pid);
    	return u.isPresent() && p.isPresent() && p.get().getProposer().equals(u.get());
    }
    
    public boolean isAuthorOfReview(long rid, long uid) {
    	Optional<pt.unl.fct.ciai.model.User> u = users.findById(uid);
    	Optional<Review> r = reviews.findById(rid);
    	return u.isPresent() && r.isPresent() && r.get().getAuthor().equals(u.get());
    }
    
    public boolean isAuthorOfComment(long cid, long uid) {
    	Optional<pt.unl.fct.ciai.model.User> u = users.findById(uid);
    	Optional<Comment> c = comments.findById(cid);
    	return u.isPresent() && c.isPresent() && c.get().getAuthor().equals(u.get());
    }
}

