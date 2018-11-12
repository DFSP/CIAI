package pt.unl.fct.ciai.service;

import org.springframework.stereotype.Service;
import pt.unl.fct.ciai.utils.Utils;
import pt.unl.fct.ciai.exception.NotFoundException;
import pt.unl.fct.ciai.model.User;
import pt.unl.fct.ciai.repository.ProposalsRepository;
import pt.unl.fct.ciai.repository.UsersRepository;

import java.util.Optional;

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

    public void updateUser(User newUser) {
        User user = getUserIfPresent(newUser.getId());
        Utils.copyNonNullProperties(newUser, user);
        usersRepository.save(user);
    }

    public void deleteUser(long id) {
        User user = getUserIfPresent(id);
        usersRepository.delete(user);
    }

    private User getUserIfPresent(long id) {
        return this.getUser(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found.", id)));
    }

}


