package br.com.erudio.services;

import br.com.erudio.controllers.PersonController;
import br.com.erudio.data.vo.v1.PersonVO;
import br.com.erudio.exceptions.ResourceNotFoundException;
import br.com.erudio.mapper.DozerMapper;
// import br.com.erudio.mapper.custom.PersonMapper;
import br.com.erudio.model.Person;
import br.com.erudio.repositories.PersonRepository;

import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class PersonServices {

    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository repository;

    public List<PersonVO> findAll() {

        logger.info("Finding all People");
        var persons = DozerMapper.parseListObjects(repository.findAll(), PersonVO.class);
        persons.stream().forEach(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getId())).withSelfRel()));
        return persons;
    }
    // public List<PersonVOV2> findAllV2() {

    //     logger.info("Finding all People");
    //     return DozerMapper.parseListObjects(repository.findAll(), PersonVOV2.class);
    // }

    public PersonVO findById(Long id) {

        logger.info("Finding a new Person");
        var entity =  repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        PersonVO vo = DozerMapper.parseObject(entity, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

    
    public PersonVO create(PersonVO person) {
        logger.info("Creating one person!");
		var entity = DozerMapper.parseObject(person, Person.class);
		var vo =  DozerMapper.parseObject(repository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getId())).withSelfRel());
		return vo;
    }
    // public PersonVOV2 createV2(PersonVOV2 person) {
		
	// 	logger.info("Creating one person with V2!");
	// 	var entity = PersonMapper.convertVoTOEntity(person);
	// 	var vo =  PersonMapper.convertEntityToVo(repository.save(entity));
	// 	return vo;
	// }

    public PersonVO update(PersonVO person) {
        logger.info("Update one Person");
        var entity = repository.findById(person.getId()).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getId())).withSelfRel());
        return vo;
    }

    public void delete(Long id) {
        logger.info("Delete one Person");

        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        repository.delete(entity);
    }
}
