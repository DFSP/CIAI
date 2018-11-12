package pt.unl.fct.ciai.service;

import org.springframework.stereotype.Service;
import pt.unl.fct.ciai.model.Proposal;
import pt.unl.fct.ciai.utils.Utils;
import pt.unl.fct.ciai.exception.NotFoundException;
import pt.unl.fct.ciai.model.User;
import pt.unl.fct.ciai.repository.UsersRepository;

import java.util.Optional;

@Service
public class UsersService {

    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public Iterable<User> getUsers(String search) {
        return search == null ?
                usersRepository.findAll() :
                usersRepository.searchUsers(search);
    }

    public User addUser(User user) {
        return usersRepository.save(user);
    }

    public Optional<User> getUser(long id) {
        return usersRepository.findById(id);
    }

    public void updateUser(User newUser) {
        User user = getUserIfPresent(newUser.getId());
        Utils.copyNonNullProperties(newUser, user);
        usersRepository.save(user);
    }

    public void deleteUser(long id) {
        User user = getUserIfPresent(id);
        usersRepository.delete(user);
    }

    public Iterable<Proposal> getProposals(long id, String search) {
        return search ==  null ?
                usersRepository.getProposals(id) :
                usersRepository.searchProposals(id, search);
    }

    public Optional<Proposal> getProposal(long uid, long pid) {
        return Optional.ofNullable(usersRepository.getProposal(uid, pid));
    }

    public Iterable<Proposal> getBiddings(long id, String search) {
        return search ==  null ?
                usersRepository.getBiddings(id) :
                usersRepository.searchBiddings(id, search);
    }

    public Optional<Proposal> getBidding(long uid, long pid) {
        return Optional.ofNullable(usersRepository.getBidding(uid, pid));
    }

    private User getUserIfPresent(long id) {
        return this.getUser(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found.", id)));
    }

}


