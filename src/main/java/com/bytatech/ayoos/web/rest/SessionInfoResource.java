package com.bytatech.ayoos.web.rest;

import com.bytatech.ayoos.domain.ReservedSlot;
import com.bytatech.ayoos.domain.Review;
import com.bytatech.ayoos.domain.SessionInfo;
import com.bytatech.ayoos.domain.pojo.Slot;
import com.bytatech.ayoos.service.DoctorService;
import com.bytatech.ayoos.service.SessionInfoService;
import com.bytatech.ayoos.web.rest.errors.BadRequestAlertException;
import com.bytatech.ayoos.web.rest.util.HeaderUtil;
import com.bytatech.ayoos.web.rest.util.PaginationUtil;
import com.bytatech.ayoos.service.dto.DoctorDTO;
import com.bytatech.ayoos.service.dto.ReviewDTO;
import com.bytatech.ayoos.service.dto.SessionInfoDTO;
import com.bytatech.ayoos.service.mapper.DoctorMapper;
import com.bytatech.ayoos.service.mapper.ReviewMapper;
import com.bytatech.ayoos.service.mapper.SessionInfoMapper;

import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing SessionInfo.
 */
@RestController
@RequestMapping("/api")
public class SessionInfoResource {

	private final Logger log = LoggerFactory.getLogger(SessionInfoResource.class);

	private static final String ENTITY_NAME = "doctorSessionInfo";

	private final SessionInfoService sessionInfoService;

	@Autowired
	SessionInfoMapper sessionInfoMapper;
	@Autowired
	DoctorService doctorService;
	@Autowired
	private DoctorMapper doctorMapper;

	public SessionInfoResource(SessionInfoService sessionInfoService) {
		this.sessionInfoService = sessionInfoService;
	}

	/**
	 * POST /session-infos : Create a new sessionInfo.
	 *
	 * @param sessionInfoDTO
	 *            the sessionInfoDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new sessionInfoDTO, or with status 400 (Bad Request) if the
	 *         sessionInfo has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/session-infos")
	public ResponseEntity<SessionInfoDTO> createSessionInfo(@Valid @RequestBody SessionInfoDTO sessionInfoDTO)
			throws URISyntaxException {
		log.debug("REST request to save SessionInfo : {}", sessionInfoDTO);
		if (sessionInfoDTO.getId() != null) {
			throw new BadRequestAlertException("A new sessionInfo cannot already have an ID", ENTITY_NAME, "idexists");
		}
		SessionInfoDTO resultDTO = sessionInfoService.save(sessionInfoDTO);
		if (resultDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		SessionInfoDTO result = sessionInfoService.save(resultDTO);
		return ResponseEntity.created(new URI("/api/session-infos/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /session-infos : Updates an existing sessionInfo.
	 *
	 * @param sessionInfoDTO
	 *            the sessionInfoDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         sessionInfoDTO, or with status 400 (Bad Request) if the
	 *         sessionInfoDTO is not valid, or with status 500 (Internal Server
	 *         Error) if the sessionInfoDTO couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/session-infos")
	public ResponseEntity<SessionInfoDTO> updateSessionInfo(@Valid @RequestBody SessionInfoDTO sessionInfoDTO)
			throws URISyntaxException {
		log.debug("REST request to update SessionInfo : {}", sessionInfoDTO);
		if (sessionInfoDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		SessionInfoDTO result = sessionInfoService.save(sessionInfoDTO);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sessionInfoDTO.getId().toString()))
				.body(result);
	}

	/**
	 * GET /session-infos : get all the sessionInfos.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         sessionInfos in body
	 */
	@GetMapping("/session-infos")
	public ResponseEntity<List<SessionInfoDTO>> getAllSessionInfos(Pageable pageable) {
		log.debug("REST request to get a page of SessionInfos");
		Page<SessionInfoDTO> page = sessionInfoService.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/session-infos");
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

	/**
	 * GET /session-infos/:id : get the "id" sessionInfo.
	 *
	 * @param id
	 *            the id of the sessionInfoDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         sessionInfoDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/session-infos/{id}")
	public ResponseEntity<SessionInfoDTO> getSessionInfo(@PathVariable Long id) {
		log.debug("REST request to get SessionInfo : {}", id);
		Optional<SessionInfoDTO> sessionInfoDTO = sessionInfoService.findOne(id);
		return ResponseUtil.wrapOrNotFound(sessionInfoDTO);
	}

	/**
	 * DELETE /session-infos/:id : delete the "id" sessionInfo.
	 *
	 * @param id
	 *            the id of the sessionInfoDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/session-infos/{id}")
	public ResponseEntity<Void> deleteSessionInfo(@PathVariable Long id) {
		log.debug("REST request to delete SessionInfo : {}", id);
		sessionInfoService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/session-infos?query=:query : search for the sessionInfo
	 * corresponding to the query.
	 *
	 * @param query
	 *            the query of the sessionInfo search
	 * @param pageable
	 *            the pagination information
	 * @return the result of the search
	 */
	@GetMapping("/_search/session-infos")
	public ResponseEntity<List<SessionInfoDTO>> searchSessionInfos(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search for a page of SessionInfos for query {}", query);
		Page<SessionInfoDTO> page = sessionInfoService.search(query, pageable);
		HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page,
				"/api/_search/session-infos");
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

	@PostMapping("/createSessionInfo")
	public List<SessionInfoDTO> setSessionToMonth(@RequestBody List<SessionInfoDTO> sessionList,
			@RequestParam List<Integer> monthList) {

		Date currentdate = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(currentdate);// we can give different date as our prefference
		List<SessionInfoDTO> sessionDTO = new ArrayList<SessionInfoDTO>();
		for (Integer monthReff : monthList) {

			for (int i = 0; monthReff == (c.get(Calendar.MONTH)); i++) {

				int weekRef = c.get(Calendar.DAY_OF_WEEK);

				for (SessionInfoDTO sDTO : sessionList) {

					if (weekRef == sDTO.getWeekDay()) {
						SessionInfo s = new SessionInfo();
						s.setSessionName(sDTO.getSessionName());
						s.setDate(c.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
						s.setWeekDay(weekRef);
						s.setFromTime(sDTO.getFromTime());
						s.setToTime(sDTO.getToTime());
						s.setInterval(sDTO.getInterval());
						DoctorDTO doctorDTO = doctorService.findOne(sDTO.getDoctorId()).get();

						s.setDoctor(doctorMapper.toEntity(doctorDTO));

						if (s.getId() != null) {

							throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
						}
						SessionInfoDTO Sessiondto = sessionInfoMapper.toDto(s);
						SessionInfoDTO dto=	sessionInfoService.save(Sessiondto);
						sessionDTO.add(dto);
					}
				}

				c.add(Calendar.DAY_OF_MONTH, +1);

			}
		}
		return sessionDTO;
	}

	
	
	
	
	
	
	/*@GetMapping("/slots/{date}")
	public List<Slot> createSlots(@PathVariable LocalDate date) {

		List<SessionInfoDTO> sessionList = sessionInfoService.findByDate(date);

		List<Slot> slots = new ArrayList<Slot>();

		Double startTime = 0.0;
		Double endTime = 0.0;

		for (SessionInfoDTO sessionDTO : sessionList) {

			for (int i = 0; startTime <= sessionDTO.getToTime(); i++) {

				Slot s = new Slot();

				if (i == 0) {
					s.setStarTime(sessionDTO.getFromTime());
					// System.out.println("if starttime
					// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>cl"+s.getStarTime());
				} else {
					// endTime = s.getToTime();
					s.setStarTime(endTime);
					// System.out.println("else
					// endtime>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+s.getStarTime());
				}

				s.setToTime(s.getStarTime() + sessionDTO.getInterval());
				// System.out.println("totime
				// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+s.getToTime());
				s.setDate(sessionDTO.getDate());
				s.setId(i + 1);

				slots.add(s);

				startTime = s.getStarTime();
				endTime = s.getToTime();
			}

		}

		return slots;

	}*/

}
