package com.lucasxchagas.gamedevbudgets.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.lucasxchagas.gamedevbudgets.IntegrationTest;
import com.lucasxchagas.gamedevbudgets.domain.Sounds;
import com.lucasxchagas.gamedevbudgets.domain.enumeration.SoundFormats;
import com.lucasxchagas.gamedevbudgets.domain.enumeration.SoundTypes;
import com.lucasxchagas.gamedevbudgets.repository.SoundsRepository;
import com.lucasxchagas.gamedevbudgets.service.dto.SoundsDTO;
import com.lucasxchagas.gamedevbudgets.service.mapper.SoundsMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SoundsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SoundsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final SoundTypes DEFAULT_TYPE = SoundTypes.SoundTrack;
    private static final SoundTypes UPDATED_TYPE = SoundTypes.SFX;

    private static final SoundFormats DEFAULT_FORMAT = SoundFormats.MP3;
    private static final SoundFormats UPDATED_FORMAT = SoundFormats.OGG;

    private static final String ENTITY_API_URL = "/api/sounds";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SoundsRepository soundsRepository;

    @Autowired
    private SoundsMapper soundsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSoundsMockMvc;

    private Sounds sounds;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sounds createEntity(EntityManager em) {
        Sounds sounds = new Sounds().name(DEFAULT_NAME).type(DEFAULT_TYPE).format(DEFAULT_FORMAT);
        return sounds;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sounds createUpdatedEntity(EntityManager em) {
        Sounds sounds = new Sounds().name(UPDATED_NAME).type(UPDATED_TYPE).format(UPDATED_FORMAT);
        return sounds;
    }

    @BeforeEach
    public void initTest() {
        sounds = createEntity(em);
    }

    @Test
    @Transactional
    void createSounds() throws Exception {
        int databaseSizeBeforeCreate = soundsRepository.findAll().size();
        // Create the Sounds
        SoundsDTO soundsDTO = soundsMapper.toDto(sounds);
        restSoundsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(soundsDTO)))
            .andExpect(status().isCreated());

        // Validate the Sounds in the database
        List<Sounds> soundsList = soundsRepository.findAll();
        assertThat(soundsList).hasSize(databaseSizeBeforeCreate + 1);
        Sounds testSounds = soundsList.get(soundsList.size() - 1);
        assertThat(testSounds.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSounds.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testSounds.getFormat()).isEqualTo(DEFAULT_FORMAT);
    }

    @Test
    @Transactional
    void createSoundsWithExistingId() throws Exception {
        // Create the Sounds with an existing ID
        sounds.setId(1L);
        SoundsDTO soundsDTO = soundsMapper.toDto(sounds);

        int databaseSizeBeforeCreate = soundsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSoundsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(soundsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sounds in the database
        List<Sounds> soundsList = soundsRepository.findAll();
        assertThat(soundsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = soundsRepository.findAll().size();
        // set the field null
        sounds.setName(null);

        // Create the Sounds, which fails.
        SoundsDTO soundsDTO = soundsMapper.toDto(sounds);

        restSoundsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(soundsDTO)))
            .andExpect(status().isBadRequest());

        List<Sounds> soundsList = soundsRepository.findAll();
        assertThat(soundsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSounds() throws Exception {
        // Initialize the database
        soundsRepository.saveAndFlush(sounds);

        // Get all the soundsList
        restSoundsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sounds.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].format").value(hasItem(DEFAULT_FORMAT.toString())));
    }

    @Test
    @Transactional
    void getSounds() throws Exception {
        // Initialize the database
        soundsRepository.saveAndFlush(sounds);

        // Get the sounds
        restSoundsMockMvc
            .perform(get(ENTITY_API_URL_ID, sounds.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sounds.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.format").value(DEFAULT_FORMAT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSounds() throws Exception {
        // Get the sounds
        restSoundsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSounds() throws Exception {
        // Initialize the database
        soundsRepository.saveAndFlush(sounds);

        int databaseSizeBeforeUpdate = soundsRepository.findAll().size();

        // Update the sounds
        Sounds updatedSounds = soundsRepository.findById(sounds.getId()).get();
        // Disconnect from session so that the updates on updatedSounds are not directly saved in db
        em.detach(updatedSounds);
        updatedSounds.name(UPDATED_NAME).type(UPDATED_TYPE).format(UPDATED_FORMAT);
        SoundsDTO soundsDTO = soundsMapper.toDto(updatedSounds);

        restSoundsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, soundsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(soundsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Sounds in the database
        List<Sounds> soundsList = soundsRepository.findAll();
        assertThat(soundsList).hasSize(databaseSizeBeforeUpdate);
        Sounds testSounds = soundsList.get(soundsList.size() - 1);
        assertThat(testSounds.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSounds.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSounds.getFormat()).isEqualTo(UPDATED_FORMAT);
    }

    @Test
    @Transactional
    void putNonExistingSounds() throws Exception {
        int databaseSizeBeforeUpdate = soundsRepository.findAll().size();
        sounds.setId(count.incrementAndGet());

        // Create the Sounds
        SoundsDTO soundsDTO = soundsMapper.toDto(sounds);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSoundsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, soundsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(soundsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sounds in the database
        List<Sounds> soundsList = soundsRepository.findAll();
        assertThat(soundsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSounds() throws Exception {
        int databaseSizeBeforeUpdate = soundsRepository.findAll().size();
        sounds.setId(count.incrementAndGet());

        // Create the Sounds
        SoundsDTO soundsDTO = soundsMapper.toDto(sounds);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSoundsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(soundsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sounds in the database
        List<Sounds> soundsList = soundsRepository.findAll();
        assertThat(soundsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSounds() throws Exception {
        int databaseSizeBeforeUpdate = soundsRepository.findAll().size();
        sounds.setId(count.incrementAndGet());

        // Create the Sounds
        SoundsDTO soundsDTO = soundsMapper.toDto(sounds);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSoundsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(soundsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sounds in the database
        List<Sounds> soundsList = soundsRepository.findAll();
        assertThat(soundsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSoundsWithPatch() throws Exception {
        // Initialize the database
        soundsRepository.saveAndFlush(sounds);

        int databaseSizeBeforeUpdate = soundsRepository.findAll().size();

        // Update the sounds using partial update
        Sounds partialUpdatedSounds = new Sounds();
        partialUpdatedSounds.setId(sounds.getId());

        partialUpdatedSounds.name(UPDATED_NAME).type(UPDATED_TYPE).format(UPDATED_FORMAT);

        restSoundsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSounds.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSounds))
            )
            .andExpect(status().isOk());

        // Validate the Sounds in the database
        List<Sounds> soundsList = soundsRepository.findAll();
        assertThat(soundsList).hasSize(databaseSizeBeforeUpdate);
        Sounds testSounds = soundsList.get(soundsList.size() - 1);
        assertThat(testSounds.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSounds.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSounds.getFormat()).isEqualTo(UPDATED_FORMAT);
    }

    @Test
    @Transactional
    void fullUpdateSoundsWithPatch() throws Exception {
        // Initialize the database
        soundsRepository.saveAndFlush(sounds);

        int databaseSizeBeforeUpdate = soundsRepository.findAll().size();

        // Update the sounds using partial update
        Sounds partialUpdatedSounds = new Sounds();
        partialUpdatedSounds.setId(sounds.getId());

        partialUpdatedSounds.name(UPDATED_NAME).type(UPDATED_TYPE).format(UPDATED_FORMAT);

        restSoundsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSounds.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSounds))
            )
            .andExpect(status().isOk());

        // Validate the Sounds in the database
        List<Sounds> soundsList = soundsRepository.findAll();
        assertThat(soundsList).hasSize(databaseSizeBeforeUpdate);
        Sounds testSounds = soundsList.get(soundsList.size() - 1);
        assertThat(testSounds.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSounds.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSounds.getFormat()).isEqualTo(UPDATED_FORMAT);
    }

    @Test
    @Transactional
    void patchNonExistingSounds() throws Exception {
        int databaseSizeBeforeUpdate = soundsRepository.findAll().size();
        sounds.setId(count.incrementAndGet());

        // Create the Sounds
        SoundsDTO soundsDTO = soundsMapper.toDto(sounds);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSoundsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, soundsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(soundsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sounds in the database
        List<Sounds> soundsList = soundsRepository.findAll();
        assertThat(soundsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSounds() throws Exception {
        int databaseSizeBeforeUpdate = soundsRepository.findAll().size();
        sounds.setId(count.incrementAndGet());

        // Create the Sounds
        SoundsDTO soundsDTO = soundsMapper.toDto(sounds);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSoundsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(soundsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sounds in the database
        List<Sounds> soundsList = soundsRepository.findAll();
        assertThat(soundsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSounds() throws Exception {
        int databaseSizeBeforeUpdate = soundsRepository.findAll().size();
        sounds.setId(count.incrementAndGet());

        // Create the Sounds
        SoundsDTO soundsDTO = soundsMapper.toDto(sounds);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSoundsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(soundsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sounds in the database
        List<Sounds> soundsList = soundsRepository.findAll();
        assertThat(soundsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSounds() throws Exception {
        // Initialize the database
        soundsRepository.saveAndFlush(sounds);

        int databaseSizeBeforeDelete = soundsRepository.findAll().size();

        // Delete the sounds
        restSoundsMockMvc
            .perform(delete(ENTITY_API_URL_ID, sounds.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sounds> soundsList = soundsRepository.findAll();
        assertThat(soundsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
