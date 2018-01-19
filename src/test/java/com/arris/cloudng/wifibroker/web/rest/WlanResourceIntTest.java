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

    private static final String DEFAULT_NETWORK_ID = "AAAAAAAAAA";
    private static final String UPDATED_NETWORK_ID = "BBBBBBBBBB";

    private static final String DEFAULT_WLAN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_WLAN_NAME = "BBBBBBBBBB";

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
            .networkId(DEFAULT_NETWORK_ID)
            .wlanName(DEFAULT_WLAN_NAME);
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
        assertThat(testWlan.getNetworkId()).isEqualTo(DEFAULT_NETWORK_ID);
        assertThat(testWlan.getWlanName()).isEqualTo(DEFAULT_WLAN_NAME);
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
    public void checkNetworkIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = wlanRepository.findAll().size();
        // set the field null
        wlan.setNetworkId(null);

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
    public void checkWlanNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = wlanRepository.findAll().size();
        // set the field null
        wlan.setWlanName(null);

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
            .andExpect(jsonPath("$.[*].networkId").value(hasItem(DEFAULT_NETWORK_ID.toString())))
            .andExpect(jsonPath("$.[*].wlanName").value(hasItem(DEFAULT_WLAN_NAME.toString())));
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
            .andExpect(jsonPath("$.networkId").value(DEFAULT_NETWORK_ID.toString()))
            .andExpect(jsonPath("$.wlanName").value(DEFAULT_WLAN_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllWlansByNetworkIdIsEqualToSomething() throws Exception {
        // Initialize the database
        wlanRepository.saveAndFlush(wlan);

        // Get all the wlanList where networkId equals to DEFAULT_NETWORK_ID
        defaultWlanShouldBeFound("networkId.equals=" + DEFAULT_NETWORK_ID);

        // Get all the wlanList where networkId equals to UPDATED_NETWORK_ID
        defaultWlanShouldNotBeFound("networkId.equals=" + UPDATED_NETWORK_ID);
    }

    @Test
    @Transactional
    public void getAllWlansByNetworkIdIsInShouldWork() throws Exception {
        // Initialize the database
        wlanRepository.saveAndFlush(wlan);

        // Get all the wlanList where networkId in DEFAULT_NETWORK_ID or UPDATED_NETWORK_ID
        defaultWlanShouldBeFound("networkId.in=" + DEFAULT_NETWORK_ID + "," + UPDATED_NETWORK_ID);

        // Get all the wlanList where networkId equals to UPDATED_NETWORK_ID
        defaultWlanShouldNotBeFound("networkId.in=" + UPDATED_NETWORK_ID);
    }

    @Test
    @Transactional
    public void getAllWlansByNetworkIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        wlanRepository.saveAndFlush(wlan);

        // Get all the wlanList where networkId is not null
        defaultWlanShouldBeFound("networkId.specified=true");

        // Get all the wlanList where networkId is null
        defaultWlanShouldNotBeFound("networkId.specified=false");
    }

    @Test
    @Transactional
    public void getAllWlansByWlanNameIsEqualToSomething() throws Exception {
        // Initialize the database
        wlanRepository.saveAndFlush(wlan);

        // Get all the wlanList where wlanName equals to DEFAULT_WLAN_NAME
        defaultWlanShouldBeFound("wlanName.equals=" + DEFAULT_WLAN_NAME);

        // Get all the wlanList where wlanName equals to UPDATED_WLAN_NAME
        defaultWlanShouldNotBeFound("wlanName.equals=" + UPDATED_WLAN_NAME);
    }

    @Test
    @Transactional
    public void getAllWlansByWlanNameIsInShouldWork() throws Exception {
        // Initialize the database
        wlanRepository.saveAndFlush(wlan);

        // Get all the wlanList where wlanName in DEFAULT_WLAN_NAME or UPDATED_WLAN_NAME
        defaultWlanShouldBeFound("wlanName.in=" + DEFAULT_WLAN_NAME + "," + UPDATED_WLAN_NAME);

        // Get all the wlanList where wlanName equals to UPDATED_WLAN_NAME
        defaultWlanShouldNotBeFound("wlanName.in=" + UPDATED_WLAN_NAME);
    }

    @Test
    @Transactional
    public void getAllWlansByWlanNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        wlanRepository.saveAndFlush(wlan);

        // Get all the wlanList where wlanName is not null
        defaultWlanShouldBeFound("wlanName.specified=true");

        // Get all the wlanList where wlanName is null
        defaultWlanShouldNotBeFound("wlanName.specified=false");
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
            .andExpect(jsonPath("$.[*].networkId").value(hasItem(DEFAULT_NETWORK_ID.toString())))
            .andExpect(jsonPath("$.[*].wlanName").value(hasItem(DEFAULT_WLAN_NAME.toString())));
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
            .networkId(UPDATED_NETWORK_ID)
            .wlanName(UPDATED_WLAN_NAME);

        restWlanMockMvc.perform(put("/api/wlans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWlan)))
            .andExpect(status().isOk());

        // Validate the Wlan in the database
        List<Wlan> wlanList = wlanRepository.findAll();
        assertThat(wlanList).hasSize(databaseSizeBeforeUpdate);
        Wlan testWlan = wlanList.get(wlanList.size() - 1);
        assertThat(testWlan.getNetworkId()).isEqualTo(UPDATED_NETWORK_ID);
        assertThat(testWlan.getWlanName()).isEqualTo(UPDATED_WLAN_NAME);
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
