package com.arris.cloudng.wifibroker.web.rest;

import com.arris.cloudng.wifibroker.SampleBrokerApp;

import com.arris.cloudng.wifibroker.domain.Wlan;
import com.arris.cloudng.wifibroker.domain.Zone;
import com.arris.cloudng.wifibroker.domain.WlanGroup;
import com.arris.cloudng.wifibroker.repository.WlanRepository;
import com.arris.cloudng.wifibroker.service.WlanService;
import com.arris.cloudng.wifibroker.web.rest.errors.ExceptionTranslator;
import com.arris.cloudng.wifibroker.service.dto.WlanCriteria;
import com.arris.cloudng.wifibroker.service.WlanQueryService;

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
 * Test class for the WlanResource REST controller.
 *
 * @see WlanResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SampleBrokerApp.class)
public class WlanResourceIntTest {

    private static final String DEFAULT_SERVICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_DEVICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DEVICE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_NAME = "BBBBBBBBBB";

    @Autowired
    private WlanRepository wlanRepository;

    @Autowired
    private WlanService wlanService;

    @Autowired
    private WlanQueryService wlanQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWlanMockMvc;

    private Wlan wlan;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WlanResource wlanResource = new WlanResource(wlanService, wlanQueryService);
        this.restWlanMockMvc = MockMvcBuilders.standaloneSetup(wlanResource)
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
    public static Wlan createEntity(EntityManager em) {
        Wlan wlan = new Wlan()
            .serviceId(DEFAULT_SERVICE_ID)
            .deviceId(DEFAULT_DEVICE_ID)
            .serviceName(DEFAULT_SERVICE_NAME)
            .deviceName(DEFAULT_DEVICE_NAME);
        return wlan;
    }

    @Before
    public void initTest() {
        wlan = createEntity(em);
    }

    @Test
    @Transactional
    public void createWlan() throws Exception {
        int databaseSizeBeforeCreate = wlanRepository.findAll().size();

        // Create the Wlan
        restWlanMockMvc.perform(post("/api/wlans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wlan)))
            .andExpect(status().isCreated());

        // Validate the Wlan in the database
        List<Wlan> wlanList = wlanRepository.findAll();
        assertThat(wlanList).hasSize(databaseSizeBeforeCreate + 1);
        Wlan testWlan = wlanList.get(wlanList.size() - 1);
        assertThat(testWlan.getServiceId()).isEqualTo(DEFAULT_SERVICE_ID);
        assertThat(testWlan.getDeviceId()).isEqualTo(DEFAULT_DEVICE_ID);
        assertThat(testWlan.getServiceName()).isEqualTo(DEFAULT_SERVICE_NAME);
        assertThat(testWlan.getDeviceName()).isEqualTo(DEFAULT_DEVICE_NAME);
    }

    @Test
    @Transactional
    public void createWlanWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = wlanRepository.findAll().size();

        // Create the Wlan with an existing ID
        wlan.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWlanMockMvc.perform(post("/api/wlans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wlan)))
            .andExpect(status().isBadRequest());

        // Validate the Wlan in the database
        List<Wlan> wlanList = wlanRepository.findAll();
        assertThat(wlanList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkServiceIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = wlanRepository.findAll().size();
        // set the field null
        wlan.setServiceId(null);

        // Create the Wlan, which fails.

        restWlanMockMvc.perform(post("/api/wlans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wlan)))
            .andExpect(status().isBadRequest());

        List<Wlan> wlanList = wlanRepository.findAll();
        assertThat(wlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDeviceIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = wlanRepository.findAll().size();
        // set the field null
        wlan.setDeviceId(null);

        // Create the Wlan, which fails.

        restWlanMockMvc.perform(post("/api/wlans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wlan)))
            .andExpect(status().isBadRequest());

        List<Wlan> wlanList = wlanRepository.findAll();
        assertThat(wlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkServiceNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = wlanRepository.findAll().size();
        // set the field null
        wlan.setServiceName(null);

        // Create the Wlan, which fails.

        restWlanMockMvc.perform(post("/api/wlans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wlan)))
            .andExpect(status().isBadRequest());

        List<Wlan> wlanList = wlanRepository.findAll();
        assertThat(wlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDeviceNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = wlanRepository.findAll().size();
        // set the field null
        wlan.setDeviceName(null);

        // Create the Wlan, which fails.

        restWlanMockMvc.perform(post("/api/wlans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wlan)))
            .andExpect(status().isBadRequest());

        List<Wlan> wlanList = wlanRepository.findAll();
        assertThat(wlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWlans() throws Exception {
        // Initialize the database
        wlanRepository.saveAndFlush(wlan);

        // Get all the wlanList
        restWlanMockMvc.perform(get("/api/wlans?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wlan.getId().intValue())))
            .andExpect(jsonPath("$.[*].serviceId").value(hasItem(DEFAULT_SERVICE_ID.toString())))
            .andExpect(jsonPath("$.[*].deviceId").value(hasItem(DEFAULT_DEVICE_ID.toString())))
            .andExpect(jsonPath("$.[*].serviceName").value(hasItem(DEFAULT_SERVICE_NAME.toString())))
            .andExpect(jsonPath("$.[*].deviceName").value(hasItem(DEFAULT_DEVICE_NAME.toString())));
    }

    @Test
    @Transactional
    public void getWlan() throws Exception {
        // Initialize the database
        wlanRepository.saveAndFlush(wlan);

        // Get the wlan
        restWlanMockMvc.perform(get("/api/wlans/{id}", wlan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(wlan.getId().intValue()))
            .andExpect(jsonPath("$.serviceId").value(DEFAULT_SERVICE_ID.toString()))
            .andExpect(jsonPath("$.deviceId").value(DEFAULT_DEVICE_ID.toString()))
            .andExpect(jsonPath("$.serviceName").value(DEFAULT_SERVICE_NAME.toString()))
            .andExpect(jsonPath("$.deviceName").value(DEFAULT_DEVICE_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllWlansByServiceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        wlanRepository.saveAndFlush(wlan);

        // Get all the wlanList where serviceId equals to DEFAULT_SERVICE_ID
        defaultWlanShouldBeFound("serviceId.equals=" + DEFAULT_SERVICE_ID);

        // Get all the wlanList where serviceId equals to UPDATED_SERVICE_ID
        defaultWlanShouldNotBeFound("serviceId.equals=" + UPDATED_SERVICE_ID);
    }

    @Test
    @Transactional
    public void getAllWlansByServiceIdIsInShouldWork() throws Exception {
        // Initialize the database
        wlanRepository.saveAndFlush(wlan);

        // Get all the wlanList where serviceId in DEFAULT_SERVICE_ID or UPDATED_SERVICE_ID
        defaultWlanShouldBeFound("serviceId.in=" + DEFAULT_SERVICE_ID + "," + UPDATED_SERVICE_ID);

        // Get all the wlanList where serviceId equals to UPDATED_SERVICE_ID
        defaultWlanShouldNotBeFound("serviceId.in=" + UPDATED_SERVICE_ID);
    }

    @Test
    @Transactional
    public void getAllWlansByServiceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        wlanRepository.saveAndFlush(wlan);

        // Get all the wlanList where serviceId is not null
        defaultWlanShouldBeFound("serviceId.specified=true");

        // Get all the wlanList where serviceId is null
        defaultWlanShouldNotBeFound("serviceId.specified=false");
    }

    @Test
    @Transactional
    public void getAllWlansByDeviceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        wlanRepository.saveAndFlush(wlan);

        // Get all the wlanList where deviceId equals to DEFAULT_DEVICE_ID
        defaultWlanShouldBeFound("deviceId.equals=" + DEFAULT_DEVICE_ID);

        // Get all the wlanList where deviceId equals to UPDATED_DEVICE_ID
        defaultWlanShouldNotBeFound("deviceId.equals=" + UPDATED_DEVICE_ID);
    }

    @Test
    @Transactional
    public void getAllWlansByDeviceIdIsInShouldWork() throws Exception {
        // Initialize the database
        wlanRepository.saveAndFlush(wlan);

        // Get all the wlanList where deviceId in DEFAULT_DEVICE_ID or UPDATED_DEVICE_ID
        defaultWlanShouldBeFound("deviceId.in=" + DEFAULT_DEVICE_ID + "," + UPDATED_DEVICE_ID);

        // Get all the wlanList where deviceId equals to UPDATED_DEVICE_ID
        defaultWlanShouldNotBeFound("deviceId.in=" + UPDATED_DEVICE_ID);
    }

    @Test
    @Transactional
    public void getAllWlansByDeviceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        wlanRepository.saveAndFlush(wlan);

        // Get all the wlanList where deviceId is not null
        defaultWlanShouldBeFound("deviceId.specified=true");

        // Get all the wlanList where deviceId is null
        defaultWlanShouldNotBeFound("deviceId.specified=false");
    }

    @Test
    @Transactional
    public void getAllWlansByServiceNameIsEqualToSomething() throws Exception {
        // Initialize the database
        wlanRepository.saveAndFlush(wlan);

        // Get all the wlanList where serviceName equals to DEFAULT_SERVICE_NAME
        defaultWlanShouldBeFound("serviceName.equals=" + DEFAULT_SERVICE_NAME);

        // Get all the wlanList where serviceName equals to UPDATED_SERVICE_NAME
        defaultWlanShouldNotBeFound("serviceName.equals=" + UPDATED_SERVICE_NAME);
    }

    @Test
    @Transactional
    public void getAllWlansByServiceNameIsInShouldWork() throws Exception {
        // Initialize the database
        wlanRepository.saveAndFlush(wlan);

        // Get all the wlanList where serviceName in DEFAULT_SERVICE_NAME or UPDATED_SERVICE_NAME
        defaultWlanShouldBeFound("serviceName.in=" + DEFAULT_SERVICE_NAME + "," + UPDATED_SERVICE_NAME);

        // Get all the wlanList where serviceName equals to UPDATED_SERVICE_NAME
        defaultWlanShouldNotBeFound("serviceName.in=" + UPDATED_SERVICE_NAME);
    }

    @Test
    @Transactional
    public void getAllWlansByServiceNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        wlanRepository.saveAndFlush(wlan);

        // Get all the wlanList where serviceName is not null
        defaultWlanShouldBeFound("serviceName.specified=true");

        // Get all the wlanList where serviceName is null
        defaultWlanShouldNotBeFound("serviceName.specified=false");
    }

    @Test
    @Transactional
    public void getAllWlansByDeviceNameIsEqualToSomething() throws Exception {
        // Initialize the database
        wlanRepository.saveAndFlush(wlan);

        // Get all the wlanList where deviceName equals to DEFAULT_DEVICE_NAME
        defaultWlanShouldBeFound("deviceName.equals=" + DEFAULT_DEVICE_NAME);

        // Get all the wlanList where deviceName equals to UPDATED_DEVICE_NAME
        defaultWlanShouldNotBeFound("deviceName.equals=" + UPDATED_DEVICE_NAME);
    }

    @Test
    @Transactional
    public void getAllWlansByDeviceNameIsInShouldWork() throws Exception {
        // Initialize the database
        wlanRepository.saveAndFlush(wlan);

        // Get all the wlanList where deviceName in DEFAULT_DEVICE_NAME or UPDATED_DEVICE_NAME
        defaultWlanShouldBeFound("deviceName.in=" + DEFAULT_DEVICE_NAME + "," + UPDATED_DEVICE_NAME);

        // Get all the wlanList where deviceName equals to UPDATED_DEVICE_NAME
        defaultWlanShouldNotBeFound("deviceName.in=" + UPDATED_DEVICE_NAME);
    }

    @Test
    @Transactional
    public void getAllWlansByDeviceNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        wlanRepository.saveAndFlush(wlan);

        // Get all the wlanList where deviceName is not null
        defaultWlanShouldBeFound("deviceName.specified=true");

        // Get all the wlanList where deviceName is null
        defaultWlanShouldNotBeFound("deviceName.specified=false");
    }

    @Test
    @Transactional
    public void getAllWlansByZoneIsEqualToSomething() throws Exception {
        // Initialize the database
        Zone zone = ZoneResourceIntTest.createEntity(em);
        em.persist(zone);
        em.flush();
        wlan.setZone(zone);
        wlanRepository.saveAndFlush(wlan);
        Long zoneId = zone.getId();

        // Get all the wlanList where zone equals to zoneId
        defaultWlanShouldBeFound("zoneId.equals=" + zoneId);

        // Get all the wlanList where zone equals to zoneId + 1
        defaultWlanShouldNotBeFound("zoneId.equals=" + (zoneId + 1));
    }


    @Test
    @Transactional
    public void getAllWlansByGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        WlanGroup group = WlanGroupResourceIntTest.createEntity(em);
        em.persist(group);
        em.flush();
        wlan.addGroup(group);
        wlanRepository.saveAndFlush(wlan);
        Long groupId = group.getId();

        // Get all the wlanList where group equals to groupId
        defaultWlanShouldBeFound("groupId.equals=" + groupId);

        // Get all the wlanList where group equals to groupId + 1
        defaultWlanShouldNotBeFound("groupId.equals=" + (groupId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultWlanShouldBeFound(String filter) throws Exception {
        restWlanMockMvc.perform(get("/api/wlans?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wlan.getId().intValue())))
            .andExpect(jsonPath("$.[*].serviceId").value(hasItem(DEFAULT_SERVICE_ID.toString())))
            .andExpect(jsonPath("$.[*].deviceId").value(hasItem(DEFAULT_DEVICE_ID.toString())))
            .andExpect(jsonPath("$.[*].serviceName").value(hasItem(DEFAULT_SERVICE_NAME.toString())))
            .andExpect(jsonPath("$.[*].deviceName").value(hasItem(DEFAULT_DEVICE_NAME.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultWlanShouldNotBeFound(String filter) throws Exception {
        restWlanMockMvc.perform(get("/api/wlans?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingWlan() throws Exception {
        // Get the wlan
        restWlanMockMvc.perform(get("/api/wlans/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWlan() throws Exception {
        // Initialize the database
        wlanService.save(wlan);

        int databaseSizeBeforeUpdate = wlanRepository.findAll().size();

        // Update the wlan
        Wlan updatedWlan = wlanRepository.findOne(wlan.getId());
        // Disconnect from session so that the updates on updatedWlan are not directly saved in db
        em.detach(updatedWlan);
        updatedWlan
            .serviceId(UPDATED_SERVICE_ID)
            .deviceId(UPDATED_DEVICE_ID)
            .serviceName(UPDATED_SERVICE_NAME)
            .deviceName(UPDATED_DEVICE_NAME);

        restWlanMockMvc.perform(put("/api/wlans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWlan)))
            .andExpect(status().isOk());

        // Validate the Wlan in the database
        List<Wlan> wlanList = wlanRepository.findAll();
        assertThat(wlanList).hasSize(databaseSizeBeforeUpdate);
        Wlan testWlan = wlanList.get(wlanList.size() - 1);
        assertThat(testWlan.getServiceId()).isEqualTo(UPDATED_SERVICE_ID);
        assertThat(testWlan.getDeviceId()).isEqualTo(UPDATED_DEVICE_ID);
        assertThat(testWlan.getServiceName()).isEqualTo(UPDATED_SERVICE_NAME);
        assertThat(testWlan.getDeviceName()).isEqualTo(UPDATED_DEVICE_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingWlan() throws Exception {
        int databaseSizeBeforeUpdate = wlanRepository.findAll().size();

        // Create the Wlan

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWlanMockMvc.perform(put("/api/wlans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wlan)))
            .andExpect(status().isCreated());

        // Validate the Wlan in the database
        List<Wlan> wlanList = wlanRepository.findAll();
        assertThat(wlanList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWlan() throws Exception {
        // Initialize the database
        wlanService.save(wlan);

        int databaseSizeBeforeDelete = wlanRepository.findAll().size();

        // Get the wlan
        restWlanMockMvc.perform(delete("/api/wlans/{id}", wlan.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Wlan> wlanList = wlanRepository.findAll();
        assertThat(wlanList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Wlan.class);
        Wlan wlan1 = new Wlan();
        wlan1.setId(1L);
        Wlan wlan2 = new Wlan();
        wlan2.setId(wlan1.getId());
        assertThat(wlan1).isEqualTo(wlan2);
        wlan2.setId(2L);
        assertThat(wlan1).isNotEqualTo(wlan2);
        wlan1.setId(null);
        assertThat(wlan1).isNotEqualTo(wlan2);
    }
}
