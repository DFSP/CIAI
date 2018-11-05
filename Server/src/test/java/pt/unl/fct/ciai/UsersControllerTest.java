package pt.unl.fct.ciai;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import pt.unl.fct.ciai.assemblers.ProposalResourceAssembler;
import pt.unl.fct.ciai.assemblers.UserResourceAssembler;
import pt.unl.fct.ciai.controller.UsersController;
import pt.unl.fct.ciai.repository.ProposalsRepository;
import pt.unl.fct.ciai.repository.UsersRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UsersController.class, secure = false)
@Import({UserResourceAssembler.class, ProposalResourceAssembler.class})
public class UsersControllerTest {

	@Autowired
	private MockMvc mvc;
	@MockBean
	private UsersRepository usersRepository;
	@MockBean
	private ProposalsRepository proposalsRepository;
	@Autowired
	private UserResourceAssembler userAssembler;
	@Autowired
	private ProposalResourceAssembler proposalAssembler;
	private final ObjectMapper objectMapper;

	private static final String ROOT = "http://localhost";
	
	public UsersControllerTest() {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new Jackson2HalModule());
	}
	
	@Test
	public void testGetUsers() {
		//TODO
	}

	@Test
	public void testAddUser() {
		//TODO
	}

	@Test
	public void testGetUser() {
		//TODO
	}

	@Test
	public void testUpdateUser() {
		//TODO
	}

	@Test
	public void testDeleteUser() {
		//TODO
	}

	@Test
	public void testGetApproverInProposals() {
		//TODO
	}

	@Test
	public void testAddApproverInProposal() {
		//TODO
	}
	
	@Test
	public void testDeleteApproverInProposal() {
		//TODO
	}
	
}
