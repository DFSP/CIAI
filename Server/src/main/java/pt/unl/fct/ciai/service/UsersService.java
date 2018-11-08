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

    public Iterable<User> getUsers() { //TODO search
        Iterable<User> users = usersRepository.findAll();
        return users;
    }

    public User addUser(User user) {
        User newUser = usersRepository.save(user);
        return newUser;
    }

    public User getUser(long id) {
        User user = usersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found.", id)));
        return user;
    }

    public void updateUser(long id, User newUser) {
        User oldUser = getUser(id);
        usersRepository.save(newUser);
    }

    public void deleteUser(long id) {
        User user = getUser(id);
        usersRepository.delete(user);
    }

    public Iterable<Proposal> getApproverInProposals(long id) {
        Iterable<Proposal> proposals = usersRepository.getApproveProposals(id);
        return proposals;
    }

    public Proposal addApproverInProposal(long id, Proposal proposal) {
        //TODO approver apenas pode ser um staff da proposal
        User user = getUser(id);
        proposal.setApprover(user);
        user.addApproveProposal(proposal);
        usersRepository.save(user); //TODO verificar se é necessário este save
        Proposal newProposal = proposalsRepository.save(proposal);
        return newProposal;
    }

    public void deleteApproverInProposal(long uid, long pid) {
        User user = getUser(uid);
        Proposal proposal = proposalsRepository.findById(pid)
                .orElseThrow(() -> new NotFoundException(String.format("Proposal with id %d not found.", pid)));
        if (proposal.getApprover().map(User::getId).orElse(-1L) != user.getId()) {
            throw new BadRequestException(String.format("User id %d is not an approver of Proposal with id %d", uid, pid));
        }
        user.removeApproveProposal(proposal);
        usersRepository.save(user); //TODO verificar se é necessário este save
        proposal.setApprover(null);
        proposalsRepository.save(proposal);
    }

}


