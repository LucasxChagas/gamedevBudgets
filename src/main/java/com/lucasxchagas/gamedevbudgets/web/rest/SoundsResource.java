package com.lucasxchagas.gamedevbudgets.web.rest;

import com.lucasxchagas.gamedevbudgets.repository.SoundsRepository;
import com.lucasxchagas.gamedevbudgets.service.SoundsService;
import com.lucasxchagas.gamedevbudgets.service.dto.SoundsDTO;
import com.lucasxchagas.gamedevbudgets.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.lucasxchagas.gamedevbudgets.domain.Sounds}.
 */
@RestController
@RequestMapping("/api")
public class SoundsResource {

    private final Logger log = LoggerFactory.getLogger(SoundsResource.class);

    private static final String ENTITY_NAME = "sounds";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SoundsService soundsService;

    private final SoundsRepository soundsRepository;

    public SoundsResource(SoundsService soundsService, SoundsRepository soundsRepository) {
        this.soundsService = soundsService;
        this.soundsRepository = soundsRepository;
    }

    /**
     * {@code POST  /sounds} : Create a new sounds.
     *
     * @param soundsDTO the soundsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new soundsDTO, or with status {@code 400 (Bad Request)} if the sounds has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sounds")
    public ResponseEntity<SoundsDTO> createSounds(@Valid @RequestBody SoundsDTO soundsDTO) throws URISyntaxException {
        log.debug("REST request to save Sounds : {}", soundsDTO);
        if (soundsDTO.getId() != null) {
            throw new BadRequestAlertException("A new sounds cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SoundsDTO result = soundsService.save(soundsDTO);
        return ResponseEntity
            .created(new URI("/api/sounds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sounds/:id} : Updates an existing sounds.
     *
     * @param id the id of the soundsDTO to save.
     * @param soundsDTO the soundsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated soundsDTO,
     * or with status {@code 400 (Bad Request)} if the soundsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the soundsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sounds/{id}")
    public ResponseEntity<SoundsDTO> updateSounds(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SoundsDTO soundsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Sounds : {}, {}", id, soundsDTO);
        if (soundsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, soundsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!soundsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SoundsDTO result = soundsService.update(soundsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, soundsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sounds/:id} : Partial updates given fields of an existing sounds, field will ignore if it is null
     *
     * @param id the id of the soundsDTO to save.
     * @param soundsDTO the soundsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated soundsDTO,
     * or with status {@code 400 (Bad Request)} if the soundsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the soundsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the soundsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sounds/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SoundsDTO> partialUpdateSounds(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SoundsDTO soundsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Sounds partially : {}, {}", id, soundsDTO);
        if (soundsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, soundsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!soundsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SoundsDTO> result = soundsService.partialUpdate(soundsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, soundsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sounds} : get all the sounds.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sounds in body.
     */
    @GetMapping("/sounds")
    public List<SoundsDTO> getAllSounds(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Sounds");
        return soundsService.findAll();
    }

    /**
     * {@code GET  /sounds/:id} : get the "id" sounds.
     *
     * @param id the id of the soundsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the soundsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sounds/{id}")
    public ResponseEntity<SoundsDTO> getSounds(@PathVariable Long id) {
        log.debug("REST request to get Sounds : {}", id);
        Optional<SoundsDTO> soundsDTO = soundsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(soundsDTO);
    }

    /**
     * {@code DELETE  /sounds/:id} : delete the "id" sounds.
     *
     * @param id the id of the soundsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sounds/{id}")
    public ResponseEntity<Void> deleteSounds(@PathVariable Long id) {
        log.debug("REST request to delete Sounds : {}", id);
        soundsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
