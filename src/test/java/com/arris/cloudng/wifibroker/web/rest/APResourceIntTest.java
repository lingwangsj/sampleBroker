package com.arris.cloudng.wifibroker.web.rest;

import com.arris.cloudng.wifibroker.SampleBrokerApp;

import com.arris.cloudng.wifibroker.domain.AP;
import com.arris.cloudng.wifibroker.domain.WlanGroup;
import com.arris.cloudng.wifibroker.domain.WlanGroup;
import com.arris.cloudng.wifibroker.domain.Zone;
import com.arris.cloudng.wifibroker.repository.APRepository;
import com.arris.cloudng.wifibroker.service.APService;
import com.arris.cloudng.wifibroker.web.rest.errors.ExceptionTranslator;
import com.arris.cloudng.wifibroker.service.dto.APCriteria;
import com.arris.cloudng.wifibroker.service.APQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.arris.cloudng.wifibroker.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the APResource REST controller.
 *
 * @see APResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SampleBrokerApp.class)
public class APResourceIntTest {

    private static final String DEFAULT_SERIAL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_AP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_AP_NAME = "BBBBBBBBBB";

    @Autowired
    private APRepository aPRepository;

    @Autowired
    private APService aPService;

    @Autowired
    private APQueryService aPQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAPMockMvc;

    private AP aP;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final APResource aPResource = new APResource(aPService, aPQueryService);
        this.restAPMockMvc = MockMvcBuilders.standaloneSetup(aPResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AP createEntity(EntityManager em) {
        AP aP = new AP()
            .serialNumber(DEFAULT_SERIAL_NUMBER)
            .apName(DEFAULT_AP_NAME);
        return aP;
    }

    @Before
    public void initTest() {
        aP = createEntity(em);
    }

    @Test
    @Transactional
    public void createAP() throws Exception {
        int databaseSizeBeforeCreate = aPRepository.findAll().size();

        // Create the AP
        restAPMockMvc.perform(post("/api/aps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(aP)))
            .andExpect(status().isCreated());

        // Validate the AP in the database
        List<AP> aPList = aPRepository.findAll();
        assertThat(aPList).hasSize(databaseSizeBeforeCreate + 1);
        AP testAP = aPList.get(aPList.size() - 1);
        assertThat(testAP.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
        assertThat(testAP.getApName()).isEqualTo(DEFAULT_AP_NAME);
    }

    @Test
    @Transactional
    public void createAPWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = aPRepository.findAll().size();

        // Create the AP with an existing ID
        aP.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAPMockMvc.perform(post("/api/aps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(aP)))
            .andExpect(status().isBadRequest());

        // Validate the AP in the database
        List<AP> aPList = aPRepository.findAll();
        assertThat(aPList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSerialNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPRepository.findAll().size();
        // set the field null
        aP.setSerialNumber(null);

        // Create the AP, which fails.

        restAPMockMvc.perform(post("/api/aps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(aP)))
            .andExpect(status().isBadRequest());

        List<AP> aPList = aPRepository.findAll();
        assertThat(aPList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkApNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPRepository.findAll().size();
        // set the field null
        aP.setApName(null);

        // Create the AP, which fails.

        restAPMockMvc.perform(post("/api/aps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(aP)))
            .andExpect(status().isBadRequest());

        List<AP> aPList = aPRepository.findAll();
        assertThat(aPList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAPS() throws Exception {
        // Initialize the database
        aPRepository.saveAndFlush(aP);

        // Get all the aPList
        restAPMockMvc.perform(get("/api/aps?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aP.getId().intValue())))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].apName").value(hasItem(DEFAULT_AP_NAME.toString())));
    }

    @Test
    @Transactional
    public void getAP() throws Exception {
        // Initialize the database
        aPRepository.saveAndFlush(aP);

        // Get the aP
        restAPMockMvc.perform(get("/api/aps/{id}", aP.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(aP.getId().intValue()))
            .andExpect(jsonPath("$.serialNumber").value(DEFAULT_SERIAL_NUMBER.toString()))
            .andExpect(jsonPath("$.apName").value(DEFAULT_AP_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllAPSBySerialNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        aPRepository.saveAndFlush(aP);

        // Get all the aPList where serialNumber equals to DEFAULT_SERIAL_NUMBER
        defaultAPShouldBeFound("serialNumber.equals=" + DEFAULT_SERIAL_NUMBER);

        // Get all the aPList where serialNumber equals to UPDATED_SERIAL_NUMBER
        defaultAPShouldNotBeFound("serialNumber.equals=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    public void getAllAPSBySerialNumberIsInShouldWork() throws Exception {
        // Initialize the database
        aPRepository.saveAndFlush(aP);

        // Get all the aPList where serialNumber in DEFAULT_SERIAL_NUMBER or UPDATED_SERIAL_NUMBER
        defaultAPShouldBeFound("serialNumber.in=" + DEFAULT_SERIAL_NUMBER + "," + UPDATED_SERIAL_NUMBER);

        // Get all the aPList where serialNumber equals to UPDATED_SERIAL_NUMBER
        defaultAPShouldNotBeFound("serialNumber.in=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    public void getAllAPSBySerialNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        aPRepository.saveAndFlush(aP);

        // Get all the aPList where serialNumber is not null
        defaultAPShouldBeFound("serialNumber.specified=true");

        // Get all the aPList where serialNumber is null
        defaultAPShouldNotBeFound("serialNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllAPSByApNameIsEqualToSomething() throws Exception {
        // Initialize the database
        aPRepository.saveAndFlush(aP);

        // Get all the aPList where apName equals to DEFAULT_AP_NAME
        defaultAPShouldBeFound("apName.equals=" + DEFAULT_AP_NAME);

        // Get all the aPList where apName equals to UPDATED_AP_NAME
        defaultAPShouldNotBeFound("apName.equals=" + UPDATED_AP_NAME);
    }

    @Test
    @Transactional
    public void getAllAPSByApNameIsInShouldWork() throws Exception {
        // Initialize the database
        aPRepository.saveAndFlush(aP);

        // Get all the aPList where apName in DEFAULT_AP_NAME or UPDATED_AP_NAME
        defaultAPShouldBeFound("apName.in=" + DEFAULT_AP_NAME + "," + UPDATED_AP_NAME);

        // Get all the aPList where apName equals to UPDATED_AP_NAME
        defaultAPShouldNotBeFound("apName.in=" + UPDATED_AP_NAME);
    }

    @Test
    @Transactional
    public void getAllAPSByApNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        aPRepository.saveAndFlush(aP);

        // Get all the aPList where apName is not null
        defaultAPShouldBeFound("apName.specified=true");

        // Get all the aPList where apName is null
        defaultAPShouldNotBeFound("apName.specified=false");
    }

    @Test
    @Transactional
    public void getAllAPSByWg24IsEqualToSomething() throws Exception {
        // Initialize the database
        WlanGroup wg24 = WlanGroupResourceIntTest.createEntity(em);
        em.persist(wg24);
        em.flush();
        aP.setWg24(wg24);
        aPRepository.saveAndFlush(aP);
        Long wg24Id = wg24.getId();

        // Get all the aPList where wg24 equals to wg24Id
        defaultAPShouldBeFound("wg24Id.equals=" + wg24Id);

        // Get all the aPList where wg24 equals to wg24Id + 1
        defaultAPShouldNotBeFound("wg24Id.equals=" + (wg24Id + 1));
    }


    @Test
    @Transactional
    public void getAllAPSByWg50IsEqualToSomething() throws Exception {
        // Initialize the database
        WlanGroup wg50 = WlanGroupResourceIntTest.createEntity(em);
        em.persist(wg50);
        em.flush();
        aP.setWg50(wg50);
        aPRepository.saveAndFlush(aP);
        Long wg50Id = wg50.getId();

        // Get all the aPList where wg50 equals to wg50Id
        defaultAPShouldBeFound("wg50Id.equals=" + wg50Id);

        // Get all the aPList where wg50 equals to wg50Id + 1
        defaultAPShouldNotBeFound("wg50Id.equals=" + (wg50Id + 1));
    }


    @Test
    @Transactional
    public void getAllAPSByZoneIsEqualToSomething() throws Exception {
        // Initialize the database
        Zone zone = ZoneResourceIntTest.createEntity(em);
        em.persist(zone);
        em.flush();
        aP.setZone(zone);
        aPRepository.saveAndFlush(aP);
        Long zoneId = zone.getId();

        // Get all the aPList where zone equals to zoneId
        defaultAPShouldBeFound("zoneId.equals=" + zoneId);

        // Get all the aPList where zone equals to zoneId + 1
        defaultAPShouldNotBeFound("zoneId.equals=" + (zoneId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultAPShouldBeFound(String filter) throws Exception {
        restAPMockMvc.perform(get("/api/aps?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aP.getId().intValue())))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].apName").value(hasItem(DEFAULT_AP_NAME.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultAPShouldNotBeFound(String filter) throws Exception {
        restAPMockMvc.perform(get("/api/aps?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingAP() throws Exception {
        // Get the aP
        restAPMockMvc.perform(get("/api/aps/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAP() throws Exception {
        // Initialize the database
        aPService.save(aP);

        int databaseSizeBeforeUpdate = aPRepository.findAll().size();

        // Update the aP
        AP updatedAP = aPRepository.findOne(aP.getId());
        // Disconnect from session so that the updates on updatedAP are not directly saved in db
        em.detach(updatedAP);
        updatedAP
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .apName(UPDATED_AP_NAME);

        restAPMockMvc.perform(put("/api/aps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAP)))
            .andExpect(status().isOk());

        // Validate the AP in the database
        List<AP> aPList = aPRepository.findAll();
        assertThat(aPList).hasSize(databaseSizeBeforeUpdate);
        AP testAP = aPList.get(aPList.size() - 1);
        assertThat(testAP.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testAP.getApName()).isEqualTo(UPDATED_AP_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingAP() throws Exception {
        int databaseSizeBeforeUpdate = aPRepository.findAll().size();

        // Create the AP

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAPMockMvc.perform(put("/api/aps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(aP)))
            .andExpect(status().isCreated());

        // Validate the AP in the database
        List<AP> aPList = aPRepository.findAll();
        assertThat(aPList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAP() throws Exception {
        // Initialize the database
        aPService.save(aP);

        int databaseSizeBeforeDelete = aPRepository.findAll().size();

        // Get the aP
        restAPMockMvc.perform(delete("/api/aps/{id}", aP.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AP> aPList = aPRepository.findAll();
        assertThat(aPList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AP.class);
        AP aP1 = new AP();
        aP1.setId(1L);
        AP aP2 = new AP();
        aP2.setId(aP1.getId());
        assertThat(aP1).isEqualTo(aP2);
        aP2.setId(2L);
        assertThat(aP1).isNotEqualTo(aP2);
        aP1.setId(null);
        assertThat(aP1).isNotEqualTo(aP2);
    }
}
