package pt.unl.fct.ciai.contacts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pt.unl.fct.ciai.exceptions.BadRequestException;
import pt.unl.fct.ciai.exceptions.NotFoundException;

@RestController
@RequestMapping(value = "/contacts")
public class ContactsController { //implements ContactsApi {

	@Autowired
	private ContactsRepository contacts;

	@GetMapping
	public Iterable<Contact> getContacts(@RequestParam(value = "search", required = false) String search) {
		return search == null ? contacts.findAll() : contacts.search(search);
	}

	@GetMapping(value = "/{id}")
	public Contact getContact(@PathVariable("id") Long id) {
		return contacts.findById(id).orElseThrow(NotFoundException::new);
	}
	
	@PutMapping(value = "/{id}")
	public void updateContact(@PathVariable("id") Long id, @RequestBody Contact contact) {
		if (!id.equals(contact.getId())) {
			throw new BadRequestException();
		} else {
			contacts.findById(id).orElseThrow(NotFoundException::new);
			contacts.save(contact);
		}
	}
	
	@DeleteMapping(value = "/{id}")
	public void deleteContact(@PathVariable("id") Long id) {
		contacts.deleteById(id);
	}

}
