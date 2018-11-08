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

import pt.unl.fct.ciai.assembler.CommentResourceAssembler;
import pt.unl.fct.ciai.assembler.ProposalResourceAssembler;
import pt.unl.fct.ciai.assembler.ReviewResourceAssembler;
import pt.unl.fct.ciai.assembler.SectionResourceAssembler;
import pt.unl.fct.ciai.assembler.UserResourceAssembler;
import pt.unl.fct.ciai.controller.ProposalsController;
import pt.unl.fct.ciai.repository.CommentsRepository;
import pt.unl.fct.ciai.repository.ProposalsRepository;
import pt.unl.fct.ciai.repository.ReviewsRepository;
import pt.unl.fct.ciai.repository.SectionsRepository;
import pt.unl.fct.ciai.repository.UsersRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ProposalsController.class, secure = false)
@Import({ProposalResourceAssembler.class, SectionResourceAssembler.class,
	ReviewResourceAssembler.class, CommentResourceAssembler.class,
	UserResourceAssembler.class})
public class ProposalsControllerTest {

	@Autowired
	private MockMvc mvc;
	@MockBean
	private ProposalsRepository proposalsRepository;
	@MockBean
	private SectionsRepository sectionsRepository;
	@MockBean
	private ReviewsRepository reviewsRepository;
	@MockBean
	private CommentsRepository commentsRepository;
	@MockBean
	private UsersRepository usersRepository;
	@Autowired
	private ProposalResourceAssembler proposalAssembler;
	@Autowired
	private SectionResourceAssembler sectionAssembler;
	@Autowired
	private ReviewResourceAssembler reviewAssembler;
	@Autowired
	private CommentResourceAssembler commentAssembler;
	@Autowired
	private UserResourceAssembler userAssembler;
	private final ObjectMapper objectMapper;

	private static final String ROOT = "http://localhost";

	public ProposalsControllerTest() {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new Jackson2HalModule());
	}
	
	@Test
	public void testGetProposals() {
		//TODO
	}

	@Test
	public void testAddProposal() {
		//TODO
	}

	@Test
	public void testGetProposal() {
		//TODO
	}

	@Test
	public void testUpdateProposal() {
		//TODO
	}

	@Test
	public void testDeleteProposal() {
		//TODO
	}

	@Test
	public void testGetSections() {
		//TODO
	}

	@Test
	public void testAddSection() {
		//TODO
	}

	@Test
	public void testGetSection() {
		//TODO
	}

	@Test
	public void testUpdateSection() {
		//TODO
	}

	@Test
	public void testDeleteSection() {
		//TODO
	}

	@Test
	public void testGetReviews() {
		//TODO
	}

	@Test
	public void testAddReview() {
		//TODO
	}

	@Test
	public void testGetReview() {
		//TODO
	}

	@Test
	public void testUpdateReview() {
		//TODO
	}

	@Test
	public void testDeleteReview() {
		//TODO
	}

	@Test
	public void testGetComments() {
		//TODO
	}

	@Test
	public void testAddComment() {
		//TODO
	}

	@Test
	public void testGetComment() {
		//TODO
	}

	@Test
	public void testUpdateComment() {
		//TODO
	}

	@Test
	public void testDeleteComment() {
		//TODO
	}

	@Test
	public void testGetBiddingUsers() {
		//TODO
	}

	@Test
	public void testAddUserForBidding() {
		//TODO
	}

	@Test
	public void testDeleteUserForBidding() {
		//TODO
	}  

}
