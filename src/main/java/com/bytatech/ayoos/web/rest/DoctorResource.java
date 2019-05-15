package com.bytatech.ayoos.web.rest;
import com.bytatech.ayoos.domain.Doctor;
import com.bytatech.ayoos.service.DoctorService;
import com.bytatech.ayoos.web.rest.errors.BadRequestAlertException;
import com.bytatech.ayoos.web.rest.util.HeaderUtil;
import com.bytatech.ayoos.web.rest.util.PaginationUtil;
import com.bytatech.ayoos.service.dto.DoctorDTO;
import com.bytatech.ayoos.service.mapper.DoctorMapper;

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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Doctor.
 */
@RestController
@RequestMapping("/api")
public class DoctorResource {

    private final Logger log = LoggerFactory.getLogger(DoctorResource.class);

    private static final String ENTITY_NAME = "doctorDoctor";

    private final DoctorService doctorService;
    
    @Autowired
    private DoctorMapper doctorMapper;
    
    public DoctorResource(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    /**
     * POST  /doctors : Create a new doctor.
     *
     * @param doctorDTO the doctorDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new doctorDTO, or with status 400 (Bad Request) if the doctor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/doctors")
    public ResponseEntity<DoctorDTO> createDoctor(@RequestBody DoctorDTO doctorDTO) throws URISyntaxException {
        log.debug("REST request to save Doctor : {}", doctorDTO);
        if (doctorDTO.getId() != null) {
            throw new BadRequestAlertException("A new doctor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DoctorDTO resultDTO = doctorService.save(doctorDTO);
        
        if (resultDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DoctorDTO result=doctorService.save(resultDTO);
        return ResponseEntity.created(new URI("/api/doctors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /doctors : Updates an existing doctor.
     *
     * @param doctorDTO the doctorDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated doctorDTO,
     * or with status 400 (Bad Request) if the doctorDTO is not valid,
     * or with status 500 (Internal Server Error) if the doctorDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/doctors")
    public ResponseEntity<DoctorDTO> updateDoctor(@RequestBody DoctorDTO doctorDTO) throws URISyntaxException {
        log.debug("REST request to update Doctor : {}", doctorDTO);
        if (doctorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DoctorDTO result = doctorService.save(doctorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, doctorDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /doctors : get all the doctors.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of doctors in body
     */
    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorDTO>> getAllDoctors(Pageable pageable) {
        log.debug("REST request to get a page of Doctors");
        Page<DoctorDTO> page = doctorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/doctors");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /doctors/:id : get the "id" doctor.
     *
     * @param id the id of the doctorDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the doctorDTO, or with status 404 (Not Found)
     */
    @GetMapping("/doctors/{id}")
    public ResponseEntity<DoctorDTO> getDoctor(@PathVariable Long id) {
        log.debug("REST request to get Doctor : {}", id);
        Optional<DoctorDTO> doctorDTO = doctorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(doctorDTO);
    }

    
    @GetMapping("/doctors/{doctorId}")
    public ResponseEntity<DoctorDTO> getDoctorByDoctorId(@PathVariable String doctorId) {
        log.debug("REST request to get Doctor : {}", doctorId);
        DoctorDTO doctorDTO = doctorService.findByDoctorId(doctorId);
        return ResponseUtil.wrapOrNotFound(Optional.of(doctorDTO));
    }
    /**
     * DELETE  /doctors/:id : delete the "id" doctor.
     *
     * @param id the id of the doctorDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/doctors/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        log.debug("REST request to delete Doctor : {}", id);
        doctorService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/doctors?query=:query : search for the doctor corresponding
     * to the query.
     *
     * @param query the query of the doctor search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/doctors")
    public ResponseEntity<List<DoctorDTO>> searchDoctors(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Doctors for query {}", query);
        Page<DoctorDTO> page = doctorService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/doctors");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    @PostMapping("/doctors/toDto")
    public ResponseEntity<List<DoctorDTO>> listToDto(@RequestBody List<Doctor> doctor) {
    	 log.debug("REST request to convert to DTO");
    	List<DoctorDTO> dtos = new ArrayList<>();
    	doctor.forEach(a -> {dtos.add(doctorMapper.toDto(a));});
    	return ResponseEntity.ok().body(dtos);
    }

   @PostMapping("/doctor/modelToDto")
    public ResponseEntity<DoctorDTO> modelToDto(@RequestBody Doctor doctor) {
    	 log.debug("REST request to convert to DTO");
    	return ResponseEntity.ok().body(doctorMapper.toDto(doctor));
    }

}
