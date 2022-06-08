package com.lucasxchagas.gamedevbudgets.service;

import com.lucasxchagas.gamedevbudgets.domain.Sounds;
import com.lucasxchagas.gamedevbudgets.repository.SoundsRepository;
import com.lucasxchagas.gamedevbudgets.service.dto.SoundsDTO;
import com.lucasxchagas.gamedevbudgets.service.mapper.SoundsMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Sounds}.
 */
@Service
@Transactional
public class SoundsService {

    private final Logger log = LoggerFactory.getLogger(SoundsService.class);

    private final SoundsRepository soundsRepository;

    private final SoundsMapper soundsMapper;

    public SoundsService(SoundsRepository soundsRepository, SoundsMapper soundsMapper) {
        this.soundsRepository = soundsRepository;
        this.soundsMapper = soundsMapper;
    }

    /**
     * Save a sounds.
     *
     * @param soundsDTO the entity to save.
     * @return the persisted entity.
     */
    public SoundsDTO save(SoundsDTO soundsDTO) {
        log.debug("Request to save Sounds : {}", soundsDTO);
        Sounds sounds = soundsMapper.toEntity(soundsDTO);
        sounds = soundsRepository.save(sounds);
        return soundsMapper.toDto(sounds);
    }

    /**
     * Update a sounds.
     *
     * @param soundsDTO the entity to save.
     * @return the persisted entity.
     */
    public SoundsDTO update(SoundsDTO soundsDTO) {
        log.debug("Request to save Sounds : {}", soundsDTO);
        Sounds sounds = soundsMapper.toEntity(soundsDTO);
        sounds = soundsRepository.save(sounds);
        return soundsMapper.toDto(sounds);
    }

    /**
     * Partially update a sounds.
     *
     * @param soundsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SoundsDTO> partialUpdate(SoundsDTO soundsDTO) {
        log.debug("Request to partially update Sounds : {}", soundsDTO);

        return soundsRepository
            .findById(soundsDTO.getId())
            .map(existingSounds -> {
                soundsMapper.partialUpdate(existingSounds, soundsDTO);

                return existingSounds;
            })
            .map(soundsRepository::save)
            .map(soundsMapper::toDto);
    }

    /**
     * Get all the sounds.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SoundsDTO> findAll() {
        log.debug("Request to get all Sounds");
        return soundsRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(soundsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the sounds with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SoundsDTO> findAllWithEagerRelationships(Pageable pageable) {
        return soundsRepository.findAllWithEagerRelationships(pageable).map(soundsMapper::toDto);
    }

    /**
     * Get one sounds by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SoundsDTO> findOne(Long id) {
        log.debug("Request to get Sounds : {}", id);
        return soundsRepository.findOneWithEagerRelationships(id).map(soundsMapper::toDto);
    }

    /**
     * Delete the sounds by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Sounds : {}", id);
        soundsRepository.deleteById(id);
    }
}
