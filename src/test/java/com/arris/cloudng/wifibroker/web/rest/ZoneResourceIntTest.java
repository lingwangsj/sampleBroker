package com.arris.cloudng.wifibroker.web.rest;

import com.arris.cloudng.wifibroker.SampleBrokerApp;

import com.arris.cloudng.wifibroker.domain.Zone;
import com.arris.cloudng.wifibroker.domain.APGroup;
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

    private static final String DEFAULT_SERVICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_DEVICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DEVICE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_NAME = "BBBBBBBBBB";

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
            .serviceId(DEFAULT_SERVICE_ID)
            .deviceId(DEFAULT_DEVICE_ID)
            .serviceName(DEFAULT_SERVICE_NAME)
            .deviceName(DEFAULT_DEVICE_NAME);
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
        assertThat(testZone.getServiceId()).isEqualTo(DEFAULT_SERVICE_ID);
        assertThat(testZone.getDeviceId()).isEqualTo(DEFAULT_DEVICE_ID);
        assertThat(testZone.getServiceName()).isEqualTo(DEFAULT_SERVICE_NAME);
        assertThat(testZone.getDeviceName()).isEqualTo(DEFAULT_DEVICE_NAME);
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
    public void checkServiceIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = zoneRepository.findAll().size();
        // set the field null
        zone.setServiceId(null);

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
    public void checkDeviceIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = zoneRepository.findAll().size();
        // set the field null
        zone.setDeviceId(null);

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
    public void checkServiceNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = zoneRepository.findAll().size();
        // set the field null
        zone.setServiceName(null);

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
    public void checkDeviceNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = zoneRepository.findAll().size();
        // set the field null
        zone.setDeviceName(null);

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
            .andExpect(jsonPath("$.[*].serviceId").value(hasItem(DEFAULT_SERVICE_ID.toString())))
            .andExpect(jsonPath("$.[*].deviceId").value(hasItem(DEFAULT_DEVICE_ID.toString())))
            .andExpect(jsonPath("$.[*].serviceName").value(hasItem(DEFAULT_SERVICE_NAME.toString())))
            .andExpect(jsonPath("$.[*].deviceName").value(hasItem(DEFAULT_DEVICE_NAME.toString())));
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
            .andExpect(jsonPath("$.serviceId").value(DEFAULT_SERVICE_ID.toString()))
            .andExpect(jsonPath("$.deviceId").value(DEFAULT_DEVICE_ID.toString()))
            .andExpect(jsonPath("$.serviceName").value(DEFAULT_SERVICE_NAME.toString()))
            .andExpect(jsonPath("$.deviceName").value(DEFAULT_DEVICE_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllZonesByServiceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get all the zoneList where serviceId equals to DEFAULT_SERVICE_ID
        defaultZoneShouldBeFound("serviceId.equals=" + DEFAULT_SERVICE_ID);

        // Get all the zoneList where serviceId equals to UPDATED_SERVICE_ID
        defaultZoneShouldNotBeFound("serviceId.equals=" + UPDATED_SERVICE_ID);
    }

    @Test
    @Transactional
    public void getAllZonesByServiceIdIsInShouldWork() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get all the zoneList where serviceId in DEFAULT_SERVICE_ID or UPDATED_SERVICE_ID
        defaultZoneShouldBeFound("serviceId.in=" + DEFAULT_SERVICE_ID + "," + UPDATED_SERVICE_ID);

        // Get all the zoneList where serviceId equals to UPDATED_SERVICE_ID
        defaultZoneShouldNotBeFound("serviceId.in=" + UPDATED_SERVICE_ID);
    }

    @Test
    @Transactional
    public void getAllZonesByServiceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get all the zoneList where serviceId is not null
        defaultZoneShouldBeFound("serviceId.specified=true");

        // Get all the zoneList where serviceId is null
        defaultZoneShouldNotBeFound("serviceId.specified=false");
    }

    @Test
    @Transactional
    public void getAllZonesByDeviceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get all the zoneList where deviceId equals to DEFAULT_DEVICE_ID
        defaultZoneShouldBeFound("deviceId.equals=" + DEFAULT_DEVICE_ID);

        // Get all the zoneList where deviceId equals to UPDATED_DEVICE_ID
        defaultZoneShouldNotBeFound("deviceId.equals=" + UPDATED_DEVICE_ID);
    }

    @Test
    @Transactional
    public void getAllZonesByDeviceIdIsInShouldWork() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get all the zoneList where deviceId in DEFAULT_DEVICE_ID or UPDATED_DEVICE_ID
        defaultZoneShouldBeFound("deviceId.in=" + DEFAULT_DEVICE_ID + "," + UPDATED_DEVICE_ID);

        // Get all the zoneList where deviceId equals to UPDATED_DEVICE_ID
        defaultZoneShouldNotBeFound("deviceId.in=" + UPDATED_DEVICE_ID);
    }

    @Test
    @Transactional
    public void getAllZonesByDeviceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get all the zoneList where deviceId is not null
        defaultZoneShouldBeFound("deviceId.specified=true");

        // Get all the zoneList where deviceId is null
        defaultZoneShouldNotBeFound("deviceId.specified=false");
    }

    @Test
    @Transactional
    public void getAllZonesByServiceNameIsEqualToSomething() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get all the zoneList where serviceName equals to DEFAULT_SERVICE_NAME
        defaultZoneShouldBeFound("serviceName.equals=" + DEFAULT_SERVICE_NAME);

        // Get all the zoneList where serviceName equals to UPDATED_SERVICE_NAME
        defaultZoneShouldNotBeFound("serviceName.equals=" + UPDATED_SERVICE_NAME);
    }

    @Test
    @Transactional
    public void getAllZonesByServiceNameIsInShouldWork() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get all the zoneList where serviceName in DEFAULT_SERVICE_NAME or UPDATED_SERVICE_NAME
        defaultZoneShouldBeFound("serviceName.in=" + DEFAULT_SERVICE_NAME + "," + UPDATED_SERVICE_NAME);

        // Get all the zoneList where serviceName equals to UPDATED_SERVICE_NAME
        defaultZoneShouldNotBeFound("serviceName.in=" + UPDATED_SERVICE_NAME);
    }

    @Test
    @Transactional
    public void getAllZonesByServiceNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get all the zoneList where serviceName is not null
        defaultZoneShouldBeFound("serviceName.specified=true");

        // Get all the zoneList where serviceName is null
        defaultZoneShouldNotBeFound("serviceName.specified=false");
    }

    @Test
    @Transactional
    public void getAllZonesByDeviceNameIsEqualToSomething() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get all the zoneList where deviceName equals to DEFAULT_DEVICE_NAME
        defaultZoneShouldBeFound("deviceName.equals=" + DEFAULT_DEVICE_NAME);

        // Get all the zoneList where deviceName equals to UPDATED_DEVICE_NAME
        defaultZoneShouldNotBeFound("deviceName.equals=" + UPDATED_DEVICE_NAME);
    }

    @Test
    @Transactional
    public void getAllZonesByDeviceNameIsInShouldWork() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get all the zoneList where deviceName in DEFAULT_DEVICE_NAME or UPDATED_DEVICE_NAME
        defaultZoneShouldBeFound("deviceName.in=" + DEFAULT_DEVICE_NAME + "," + UPDATED_DEVICE_NAME);

        // Get all the zoneList where deviceName equals to UPDATED_DEVICE_NAME
        defaultZoneShouldNotBeFound("deviceName.in=" + UPDATED_DEVICE_NAME);
    }

    @Test
    @Transactional
    public void getAllZonesByDeviceNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get all the zoneList where deviceName is not null
        defaultZoneShouldBeFound("deviceName.specified=true");

        // Get all the zoneList where deviceName is null
        defaultZoneShouldNotBeFound("deviceName.specified=false");
    }

    @Test
    @Transactional
    public void getAllZonesByApIsEqualToSomething() throws Exception {
        // Initialize the database
        APGroup ap = APGroupResourceIntTest.createEntity(em);
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
            .andExpect(jsonPath("$.[*].serviceId").value(hasItem(DEFAULT_SERVICE_ID.toString())))
            .andExpect(jsonPath("$.[*].deviceId").value(hasItem(DEFAULT_DEVICE_ID.toString())))
            .andExpect(jsonPath("$.[*].serviceName").value(hasItem(DEFAULT_SERVICE_NAME.toString())))
            .andExpect(jsonPath("$.[*].deviceName").value(hasItem(DEFAULT_DEVICE_NAME.toString())));
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
            .serviceId(UPDATED_SERVICE_ID)
            .deviceId(UPDATED_DEVICE_ID)
            .serviceName(UPDATED_SERVICE_NAME)
            .deviceName(UPDATED_DEVICE_NAME);

        restZoneMockMvc.perform(put("/api/zones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedZone)))
            .andExpect(status().isOk());

        // Validate the Zone in the database
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeUpdate);
        Zone testZone = zoneList.get(zoneList.size() - 1);
        assertThat(testZone.getServiceId()).isEqualTo(UPDATED_SERVICE_ID);
        assertThat(testZone.getDeviceId()).isEqualTo(UPDATED_DEVICE_ID);
        assertThat(testZone.getServiceName()).isEqualTo(UPDATED_SERVICE_NAME);
        assertThat(testZone.getDeviceName()).isEqualTo(UPDATED_DEVICE_NAME);
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
