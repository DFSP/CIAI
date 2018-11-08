package pt.unl.fct.ciai.service;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import pt.unl.fct.ciai.assembler.ProposalResourceAssembler;
import pt.unl.fct.ciai.assembler.UserResourceAssembler;
import pt.unl.fct.ciai.controller.UsersController;
import pt.unl.fct.ciai.exception.BadRequestException;
import pt.unl.fct.ciai.exception.NotFoundException;
import pt.unl.fct.ciai.model.Proposal;
import pt.unl.fct.ciai.model.User;
import pt.unl.fct.ciai.repository.ProposalsRepository;
import pt.unl.fct.ciai.repository.UsersRepository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final ProposalsRepository proposalsRepository;

    public UsersService(UsersRepository usersRepository, ProposalsRepository proposalsRepository) {
        this.usersRepository = usersRepository;
        this.proposalsRepository = proposalsRepository;
    }

    public Iterable<User> getUsers(String search) {
        return search == null ?
                usersRepository.findAll() :
                usersRepository.search(search);
    }

    public User addUser(User user) {
        return usersRepository.save(user);
    }

    public Optional<User> getUser(long id) {
        return usersRepository.findById(id);
    }

    public void updateUser(long id, User newUser) {
        getUserIfPresent(id);
        usersRepository.save(newUser);
    }

    public void deleteUser(long id) {
        User user = getUserIfPresent(id);
        usersRepository.delete(user);
    }

    public Iterable<Proposal> getApproverInProposals(long id, String search) {
        return search ==  null ?
                usersRepository.getApproveProposals(id) :
                usersRepository.searchApproveProposals(id, search);
    }

    public Proposal addApproverInProposal(long uid, Proposal proposal) {
        //TODO approver apenas pode ser um staff da proposal
        User user = getUserIfPresent(uid);
        proposal.setApprover(user);
        user.addApproveProposal(proposal);
        usersRepository.save(user); //TODO verificar se é necessário este save
        return proposalsRepository.save(proposal);
    }

    public Optional<Proposal> getApproverInProposal(long uid, long pid) {
        return Optional.ofNullable(usersRepository.getApproverInProposal(uid, pid));
    }

    public void deleteApproverInProposal(long uid, long pid) {
        User user = getUserIfPresent(uid);
        Proposal proposal = getProposalIfPresent(uid, pid);
        user.removeApproveProposal(proposal);
        proposal.setApprover(null);
        usersRepository.save(user); //TODO necessario os 2?
        proposalsRepository.save(proposal);
    }

    private User getUserIfPresent(long id) {
        return this.getUser(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found.", id)));
    }

    private Proposal getProposalIfPresent(long uid, long pid) {
        return this.getApproverInProposal(uid, pid)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Proposal with id %d is not being approved by user with id %d.", pid, uid)));
    }

}


