package com.bytatech.ayoos.service.impl;

import com.bytatech.ayoos.service.DoctorService;
import com.bytatech.ayoos.service.UserRatingService;
import com.bytatech.ayoos.domain.UserRating;
import com.bytatech.ayoos.repository.UserRatingRepository;
import com.bytatech.ayoos.repository.search.UserRatingSearchRepository;
import com.bytatech.ayoos.security.SecurityUtils;
import com.bytatech.ayoos.service.dto.DoctorDTO;
import com.bytatech.ayoos.service.dto.UserRatingDTO;
import com.bytatech.ayoos.service.mapper.UserRatingMapper;
import com.bytatech.ayoos.web.rest.errors.BadRequestAlertException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing UserRating.
 */
@Service
@Transactional
public class UserRatingServiceImpl implements UserRatingService {

    private final Logger log = LoggerFactory.getLogger(UserRatingServiceImpl.class);

    private final UserRatingRepository userRatingRepository;

    private final UserRatingMapper userRatingMapper;

    private final UserRatingSearchRepository userRatingSearchRepository;

    @Autowired
    DoctorService doctorService;
    public UserRatingServiceImpl(UserRatingRepository userRatingRepository, UserRatingMapper userRatingMapper, UserRatingSearchRepository userRatingSearchRepository) {
        this.userRatingRepository = userRatingRepository;
        this.userRatingMapper = userRatingMapper;
        this.userRatingSearchRepository = userRatingSearchRepository;
    }

    /**
     * Save a userRating.
     *
     * @param userRatingDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public UserRatingDTO save(UserRatingDTO userRatingDTO) {
    	
		Integer fiveCount = 0, fourCount = 0, threeCount = 0, twoCount = 0, oneCount = 0;
    	
        log.debug("Request to save UserRating : {}", userRatingDTO);
        UserRating userRating = userRatingMapper.toEntity(userRatingDTO);
        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        userRating.setUserName(currentUserLogin.get());
        userRating.setRatedOn(LocalDate.now());
        userRating = userRatingRepository.save(userRating);
        UserRatingDTO result = userRatingMapper.toDto(userRating);
        userRatingSearchRepository.save(userRating);
        
        
        DoctorDTO doctorDTO = doctorService.findOne(result.getDoctorId()).get();
        
        log.info("..............  "+userRatingDTO.getRating()+"   ..................");
        


			fiveCount = userRatingRepository.getCount(5.0);
			log.debug(">>>>>>>>>>>>>>>>>5>>>>>>>>>>>>>>>>>>" + fiveCount);
	
			fourCount = userRatingRepository.getCount(4.0);
			log.debug(">>>>>>>>>>>>>>>>>4>>>>>>>>>>>>>>>>>>" + fourCount);
		
			threeCount = userRatingRepository.getCount(3.0);
			log.debug(">>>>>>>>>>>>>>>>3>>>>>>>>>>>>>" + threeCount);
		
			twoCount = userRatingRepository.getCount(2.0);
			log.debug(">>>>>>>>>>>>>>2>>>>>>>>>>>>>>>>>" + twoCount);
	
			oneCount = userRatingRepository.getCount(1.0);
			log.debug(">>>>>>>>>>>>>>1>>>>>>>>>>>>>>>>>" + oneCount);
		

		double rating = (5.0 * fiveCount + 4.0 * fourCount + 3.0 * threeCount + 2.0 * twoCount + 1.0 * oneCount)
				/ (fiveCount + fourCount + threeCount + twoCount + oneCount);
		
		BigDecimal ratingDB = new BigDecimal(rating).setScale(3, RoundingMode.HALF_UP);

		log.debug("                  >>>>>>>>" + ratingDB.doubleValue());

		if (doctorDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", "store", "idnull");
		}

		doctorDTO.setTotalRating(ratingDB.doubleValue());

		DoctorDTO dto = doctorService.save(doctorDTO);

		log.debug("**************doctor: ********************" + dto);
        
        return result;
    }

    /**
     * Get all the userRatings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserRatingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserRatings");
        return userRatingRepository.findAll(pageable)
            .map(userRatingMapper::toDto);
    }


    /**
     * Get one userRating by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<UserRatingDTO> findOne(Long id) {
        log.debug("Request to get UserRating : {}", id);
        return userRatingRepository.findById(id)
            .map(userRatingMapper::toDto);
    }

    /**
     * Delete the userRating by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserRating : {}", id);        userRatingRepository.deleteById(id);
        userRatingSearchRepository.deleteById(id);
    }

    /**
     * Search for the userRating corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserRatingDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UserRatings for query {}", query);
        return userRatingSearchRepository.search(queryStringQuery(query), pageable)
            .map(userRatingMapper::toDto);
    }

	/* (non-Javadoc)
	 * @see com.bytatech.ayoos.service.UserRatingService#findByDoctorId(java.lang.Long)
	 */
	@Override
	public List<UserRatingDTO> findByDoctorId(Long doctorId) {
		return userRatingMapper.toDto(userRatingRepository.findByDoctorId(doctorId));
	}
}
