package com.erudio.restfull.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.stereotype.Service;

import com.erudio.restfull.exceptions.ExceptionResponse.ResourceNotFoundException;
import com.erudio.restfull.mapper.DozerMapper;
import com.erudio.restfull.model.Person;
import com.erudio.restfull.controller.PersonController;
import com.erudio.restfull.data.vo.v1.PersonVO;
import com.erudio.restfull.repository.PersonRepository;

import jakarta.transaction.Transactional;

@Service
public class PersonServices {
	
	private Logger logger = Logger.getLogger(PersonServices.class.getName());
	
	@Autowired
	PersonRepository repository;

	public List<PersonVO> findAll() {

		logger.info("Finding all people!");

		List<PersonVO> persons = DozerMapper.parseListObjects(repository.findAll(), PersonVO.class) ;

		persons
		  .stream()
			.forEach(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));

		return persons;
	}

	public PersonVO findById(Long id) {
		
		logger.info("Finding one person!");
		
		Person entity = repository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
			PersonVO vo = DozerMapper.parseObject(entity, PersonVO.class);
			vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
			return vo;
	}
	
	public PersonVO create(PersonVO person) {

		logger.info("Creating one person!");

		Person entity = DozerMapper.parseObject(person, Person.class);
		
		PersonVO vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);

		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}
	
	public PersonVO update(PersonVO person) {
		
		logger.info("Updating one person!");
		
		Person entity = repository.findById(person.getKey())
			.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());
		
		PersonVO vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);

		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}

	@Transactional
	public PersonVO disablePerson(Long id) {
		
	  logger.info("Disabling one person!");
		
		repository.disablePerson(id);

		var entity = repository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
			var vo = DozerMapper.parseObject(entity, PersonVO.class);
			vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
			return vo;
	}
	
	public void delete(Long id) {
		
		logger.info("Deleting one person!");
		
		Person entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		repository.delete(entity);
	}
}