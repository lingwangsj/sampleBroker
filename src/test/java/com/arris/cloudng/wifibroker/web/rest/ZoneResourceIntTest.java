package com.arris.cloudng.wifibroker.web.rest;

import com.arris.cloudng.wifibroker.SampleBrokerApp;

import com.arris.cloudng.wifibroker.domain.Zone;
import com.arris.cloudng.wifibroker.domain.AP;
import com.arris.cloudng.wifibroker.domain.WlanGroup;
import com.arris.cloudng.wifibroker.domain.Wlan;
import com.arris.cloudng.wifibroker.domain.Domain;
import com.arris.cloudng.wifibroker.repository.ZoneRepository;
import com.arris.cloudng.wifibroker.service.ZoneService;
import com.arris.cloudng.wifibroker.web.rest.errors.ExceptionTranslator;
import com.arris.cloudng.wifibroker.service.dto.ZoneCriteria;
import com.arris.cloudng.wifibroker.service.ZoneQueryService;

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
 * Test class for the ZoneResource REST controller.
 *
 * @see ZoneResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SampleBrokerApp.class)
public class ZoneResourceIntTest {

    private static final String DEFAULT_VENUE_ID = "AAAAAAAAAA";
    private static final String UPDATED_VENUE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ZONE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ZONE_NAME = "BBBBBBBBBB";

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private ZoneService zoneService;

    @Autowired
    private ZoneQueryService zoneQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restZoneMockMvc;

    private Zone zone;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ZoneResource zoneResource = new ZoneResource(zoneService, zoneQueryService);
        this.restZoneMockMvc = MockMvcBuilders.standaloneSetup(zoneResource)
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
    public static Zone createEntity(EntityManager em) {
        Zone zone = new Zone()
            .venueId(DEFAULT_VENUE_ID)
            .zoneName(DEFAULT_ZONE_NAME);
        return zone;
    }

    @Before
    public void initTest() {
        zone = createEntity(em);
    }

    @Test
    @Transactional
    public void createZone() throws Exception {
        int databaseSizeBeforeCreate = zoneRepository.findAll().size();

        // Create the Zone
        restZoneMockMvc.perform(post("/api/zones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(zone)))
            .andExpect(status().isCreated());

        // Validate the Zone in the database
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeCreate + 1);
        Zone testZone = zoneList.get(zoneList.size() - 1);
        assertThat(testZone.getVenueId()).isEqualTo(DEFAULT_VENUE_ID);
        assertThat(testZone.getZoneName()).isEqualTo(DEFAULT_ZONE_NAME);
    }

    @Test
    @Transactional
    public void createZoneWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = zoneRepository.findAll().size();

        // Create the Zone with an existing ID
        zone.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restZoneMockMvc.perform(post("/api/zones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(zone)))
            .andExpect(status().isBadRequest());

        // Validate the Zone in the database
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkVenueIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = zoneRepository.findAll().size();
        // set the field null
        zone.setVenueId(null);

        // Create the Zone, which fails.

        restZoneMockMvc.perform(post("/api/zones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(zone)))
            .andExpect(status().isBadRequest());

        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkZoneNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = zoneRepository.findAll().size();
        // set the field null
        zone.setZoneName(null);

        // Create the Zone, which fails.

        restZoneMockMvc.perform(post("/api/zones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(zone)))
            .andExpect(status().isBadRequest());

        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllZones() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get all the zoneList
        restZoneMockMvc.perform(get("/api/zones?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(zone.getId().intValue())))
            .andExpect(jsonPath("$.[*].venueId").value(hasItem(DEFAULT_VENUE_ID.toString())))
            .andExpect(jsonPath("$.[*].zoneName").value(hasItem(DEFAULT_ZONE_NAME.toString())));
    }

    @Test
    @Transactional
    public void getZone() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get the zone
        restZoneMockMvc.perform(get("/api/zones/{id}", zone.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(zone.getId().intValue()))
            .andExpect(jsonPath("$.venueId").value(DEFAULT_VENUE_ID.toString()))
            .andExpect(jsonPath("$.zoneName").value(DEFAULT_ZONE_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllZonesByVenueIdIsEqualToSomething() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get all the zoneList where venueId equals to DEFAULT_VENUE_ID
        defaultZoneShouldBeFound("venueId.equals=" + DEFAULT_VENUE_ID);

        // Get all the zoneList where venueId equals to UPDATED_VENUE_ID
        defaultZoneShouldNotBeFound("venueId.equals=" + UPDATED_VENUE_ID);
    }

    @Test
    @Transactional
    public void getAllZonesByVenueIdIsInShouldWork() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get all the zoneList where venueId in DEFAULT_VENUE_ID or UPDATED_VENUE_ID
        defaultZoneShouldBeFound("venueId.in=" + DEFAULT_VENUE_ID + "," + UPDATED_VENUE_ID);

        // Get all the zoneList where venueId equals to UPDATED_VENUE_ID
        defaultZoneShouldNotBeFound("venueId.in=" + UPDATED_VENUE_ID);
    }

    @Test
    @Transactional
    public void getAllZonesByVenueIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get all the zoneList where venueId is not null
        defaultZoneShouldBeFound("venueId.specified=true");

        // Get all the zoneList where venueId is null
        defaultZoneShouldNotBeFound("venueId.specified=false");
    }

    @Test
    @Transactional
    public void getAllZonesByZoneNameIsEqualToSomething() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get all the zoneList where zoneName equals to DEFAULT_ZONE_NAME
        defaultZoneShouldBeFound("zoneName.equals=" + DEFAULT_ZONE_NAME);

        // Get all the zoneList where zoneName equals to UPDATED_ZONE_NAME
        defaultZoneShouldNotBeFound("zoneName.equals=" + UPDATED_ZONE_NAME);
    }

    @Test
    @Transactional
    public void getAllZonesByZoneNameIsInShouldWork() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get all the zoneList where zoneName in DEFAULT_ZONE_NAME or UPDATED_ZONE_NAME
        defaultZoneShouldBeFound("zoneName.in=" + DEFAULT_ZONE_NAME + "," + UPDATED_ZONE_NAME);

        // Get all the zoneList where zoneName equals to UPDATED_ZONE_NAME
        defaultZoneShouldNotBeFound("zoneName.in=" + UPDATED_ZONE_NAME);
    }

    @Test
    @Transactional
    public void getAllZonesByZoneNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get all the zoneList where zoneName is not null
        defaultZoneShouldBeFound("zoneName.specified=true");

        // Get all the zoneList where zoneName is null
        defaultZoneShouldNotBeFound("zoneName.specified=false");
    }

    @Test
    @Transactional
    public void getAllZonesByApIsEqualToSomething() throws Exception {
        // Initialize the database
        AP ap = APResourceIntTest.createEntity(em);
        em.persist(ap);
        em.flush();
        zone.addAp(ap);
        zoneRepository.saveAndFlush(zone);
        Long apId = ap.getId();

        // Get all the zoneList where ap equals to apId
        defaultZoneShouldBeFound("apId.equals=" + apId);

        // Get all the zoneList where ap equals to apId + 1
        defaultZoneShouldNotBeFound("apId.equals=" + (apId + 1));
    }


    @Test
    @Transactional
    public void getAllZonesByWlanGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        WlanGroup wlanGroup = WlanGroupResourceIntTest.createEntity(em);
        em.persist(wlanGroup);
        em.flush();
        zone.addWlanGroup(wlanGroup);
        zoneRepository.saveAndFlush(zone);
        Long wlanGroupId = wlanGroup.getId();

        // Get all the zoneList where wlanGroup equals to wlanGroupId
        defaultZoneShouldBeFound("wlanGroupId.equals=" + wlanGroupId);

        // Get all the zoneList where wlanGroup equals to wlanGroupId + 1
        defaultZoneShouldNotBeFound("wlanGroupId.equals=" + (wlanGroupId + 1));
    }


    @Test
    @Transactional
    public void getAllZonesByWlanIsEqualToSomething() throws Exception {
        // Initialize the database
        Wlan wlan = WlanResourceIntTest.createEntity(em);
        em.persist(wlan);
        em.flush();
        zone.addWlan(wlan);
        zoneRepository.saveAndFlush(zone);
        Long wlanId = wlan.getId();

        // Get all the zoneList where wlan equals to wlanId
        defaultZoneShouldBeFound("wlanId.equals=" + wlanId);

        // Get all the zoneList where wlan equals to wlanId + 1
        defaultZoneShouldNotBeFound("wlanId.equals=" + (wlanId + 1));
    }


    @Test
    @Transactional
    public void getAllZonesByDomainIsEqualToSomething() throws Exception {
        // Initialize the database
        Domain domain = DomainResourceIntTest.createEntity(em);
        em.persist(domain);
        em.flush();
        zone.setDomain(domain);
        zoneRepository.saveAndFlush(zone);
        Long domainId = domain.getId();

        // Get all the zoneList where domain equals to domainId
        defaultZoneShouldBeFound("domainId.equals=" + domainId);

        // Get all the zoneList where domain equals to domainId + 1
        defaultZoneShouldNotBeFound("domainId.equals=" + (domainId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultZoneShouldBeFound(String filter) throws Exception {
        restZoneMockMvc.perform(get("/api/zones?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(zone.getId().intValue())))
            .andExpect(jsonPath("$.[*].venueId").value(hasItem(DEFAULT_VENUE_ID.toString())))
            .andExpect(jsonPath("$.[*].zoneName").value(hasItem(DEFAULT_ZONE_NAME.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultZoneShouldNotBeFound(String filter) throws Exception {
        restZoneMockMvc.perform(get("/api/zones?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingZone() throws Exception {
        // Get the zone
        restZoneMockMvc.perform(get("/api/zones/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateZone() throws Exception {
        // Initialize the database
        zoneService.save(zone);

        int databaseSizeBeforeUpdate = zoneRepository.findAll().size();

        // Update the zone
        Zone updatedZone = zoneRepository.findOne(zone.getId());
        // Disconnect from session so that the updates on updatedZone are not directly saved in db
        em.detach(updatedZone);
        updatedZone
            .venueId(UPDATED_VENUE_ID)
            .zoneName(UPDATED_ZONE_NAME);

        restZoneMockMvc.perform(put("/api/zones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedZone)))
            .andExpect(status().isOk());

        // Validate the Zone in the database
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeUpdate);
        Zone testZone = zoneList.get(zoneList.size() - 1);
        assertThat(testZone.getVenueId()).isEqualTo(UPDATED_VENUE_ID);
        assertThat(testZone.getZoneName()).isEqualTo(UPDATED_ZONE_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingZone() throws Exception {
        int databaseSizeBeforeUpdate = zoneRepository.findAll().size();

        // Create the Zone

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restZoneMockMvc.perform(put("/api/zones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(zone)))
            .andExpect(status().isCreated());

        // Validate the Zone in the database
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteZone() throws Exception {
        // Initialize the database
        zoneService.save(zone);

        int databaseSizeBeforeDelete = zoneRepository.findAll().size();

        // Get the zone
        restZoneMockMvc.perform(delete("/api/zones/{id}", zone.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Zone.class);
        Zone zone1 = new Zone();
        zone1.setId(1L);
        Zone zone2 = new Zone();
        zone2.setId(zone1.getId());
        assertThat(zone1).isEqualTo(zone2);
        zone2.setId(2L);
        assertThat(zone1).isNotEqualTo(zone2);
        zone1.setId(null);
        assertThat(zone1).isNotEqualTo(zone2);
    }
}
