package com.erudio.restfull.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import com.erudio.restfull.controller.PersonController;
import com.erudio.restfull.data.vo.v1.PersonVO;
import com.erudio.restfull.exceptions.ExceptionResponse.ResourceNotFoundException;
import com.erudio.restfull.mapper.DozerMapper;
import com.erudio.restfull.model.Person;
import com.erudio.restfull.repository.PersonRepository;

import jakarta.transaction.Transactional;

@Service
public class PersonServices {
	
	private Logger logger = Logger.getLogger(PersonServices.class.getName());
	
	@Autowired
	PersonRepository repository;

	@Autowired
	PagedResourcesAssembler<PersonVO> assembler;

	public PagedModel<EntityModel<PersonVO>> findAll(Pageable pageable) {

		logger.info("Finding all people!");

		var personPage = repository.findAll(pageable);

		var personsVosPage = personPage.map(p -> DozerMapper.parseObject(p, PersonVO.class));

		personsVosPage.map(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));
		
		Link link = linkTo(methodOn(PersonController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

		return assembler.toModel(personsVosPage, link);
	}

	public PagedModel<EntityModel<PersonVO>> findPersonsByName(String firstName, Pageable pageable) {

		logger.info("Finding all people!");

		var personPage = repository.findPersonsByName(firstName, pageable);

		var personsVosPage = personPage.map(p -> DozerMapper.parseObject(p, PersonVO.class));

		personsVosPage.map(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));
		
		Link link = linkTo(methodOn(PersonController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

		return assembler.toModel(personsVosPage, link);
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