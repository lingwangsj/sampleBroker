package com.arris.cloudng.wifibroker.web.rest;

import com.arris.cloudng.wifibroker.SampleBrokerApp;

import com.arris.cloudng.wifibroker.domain.APGroup;
import com.arris.cloudng.wifibroker.domain.WlanGroup;
import com.arris.cloudng.wifibroker.domain.WlanGroup;
import com.arris.cloudng.wifibroker.domain.AP;
import com.arris.cloudng.wifibroker.domain.Zone;
import com.arris.cloudng.wifibroker.repository.APGroupRepository;
import com.arris.cloudng.wifibroker.service.APGroupService;
import com.arris.cloudng.wifibroker.web.rest.errors.ExceptionTranslator;
import com.arris.cloudng.wifibroker.service.dto.APGroupCriteria;
import com.arris.cloudng.wifibroker.service.APGroupQueryService;

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
 * Test class for the APGroupResource REST controller.
 *
 * @see APGroupResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SampleBrokerApp.class)
public class APGroupResourceIntTest {

    private static final String DEFAULT_SERVICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_DEVICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DEVICE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_NAME = "BBBBBBBBBB";

    @Autowired
    private APGroupRepository aPGroupRepository;

    @Autowired
    private APGroupService aPGroupService;

    @Autowired
    private APGroupQueryService aPGroupQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAPGroupMockMvc;

    private APGroup aPGroup;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final APGroupResource aPGroupResource = new APGroupResource(aPGroupService, aPGroupQueryService);
        this.restAPGroupMockMvc = MockMvcBuilders.standaloneSetup(aPGroupResource)
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
    public static APGroup createEntity(EntityManager em) {
        APGroup aPGroup = new APGroup()
            .serviceId(DEFAULT_SERVICE_ID)
            .deviceId(DEFAULT_DEVICE_ID)
            .serviceName(DEFAULT_SERVICE_NAME)
            .deviceName(DEFAULT_DEVICE_NAME);
        return aPGroup;
    }

    @Before
    public void initTest() {
        aPGroup = createEntity(em);
    }

    @Test
    @Transactional
    public void createAPGroup() throws Exception {
        int databaseSizeBeforeCreate = aPGroupRepository.findAll().size();

        // Create the APGroup
        restAPGroupMockMvc.perform(post("/api/ap-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(aPGroup)))
            .andExpect(status().isCreated());

        // Validate the APGroup in the database
        List<APGroup> aPGroupList = aPGroupRepository.findAll();
        assertThat(aPGroupList).hasSize(databaseSizeBeforeCreate + 1);
        APGroup testAPGroup = aPGroupList.get(aPGroupList.size() - 1);
        assertThat(testAPGroup.getServiceId()).isEqualTo(DEFAULT_SERVICE_ID);
        assertThat(testAPGroup.getDeviceId()).isEqualTo(DEFAULT_DEVICE_ID);
        assertThat(testAPGroup.getServiceName()).isEqualTo(DEFAULT_SERVICE_NAME);
        assertThat(testAPGroup.getDeviceName()).isEqualTo(DEFAULT_DEVICE_NAME);
    }

    @Test
    @Transactional
    public void createAPGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = aPGroupRepository.findAll().size();

        // Create the APGroup with an existing ID
        aPGroup.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAPGroupMockMvc.perform(post("/api/ap-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(aPGroup)))
            .andExpect(status().isBadRequest());

        // Validate the APGroup in the database
        List<APGroup> aPGroupList = aPGroupRepository.findAll();
        assertThat(aPGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkServiceIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPGroupRepository.findAll().size();
        // set the field null
        aPGroup.setServiceId(null);

        // Create the APGroup, which fails.

        restAPGroupMockMvc.perform(post("/api/ap-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(aPGroup)))
            .andExpect(status().isBadRequest());

        List<APGroup> aPGroupList = aPGroupRepository.findAll();
        assertThat(aPGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDeviceIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPGroupRepository.findAll().size();
        // set the field null
        aPGroup.setDeviceId(null);

        // Create the APGroup, which fails.

        restAPGroupMockMvc.perform(post("/api/ap-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(aPGroup)))
            .andExpect(status().isBadRequest());

        List<APGroup> aPGroupList = aPGroupRepository.findAll();
        assertThat(aPGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkServiceNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPGroupRepository.findAll().size();
        // set the field null
        aPGroup.setServiceName(null);

        // Create the APGroup, which fails.

        restAPGroupMockMvc.perform(post("/api/ap-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(aPGroup)))
            .andExpect(status().isBadRequest());

        List<APGroup> aPGroupList = aPGroupRepository.findAll();
        assertThat(aPGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDeviceNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPGroupRepository.findAll().size();
        // set the field null
        aPGroup.setDeviceName(null);

        // Create the APGroup, which fails.

        restAPGroupMockMvc.perform(post("/api/ap-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(aPGroup)))
            .andExpect(status().isBadRequest());

        List<APGroup> aPGroupList = aPGroupRepository.findAll();
        assertThat(aPGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAPGroups() throws Exception {
        // Initialize the database
        aPGroupRepository.saveAndFlush(aPGroup);

        // Get all the aPGroupList
        restAPGroupMockMvc.perform(get("/api/ap-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aPGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].serviceId").value(hasItem(DEFAULT_SERVICE_ID.toString())))
            .andExpect(jsonPath("$.[*].deviceId").value(hasItem(DEFAULT_DEVICE_ID.toString())))
            .andExpect(jsonPath("$.[*].serviceName").value(hasItem(DEFAULT_SERVICE_NAME.toString())))
            .andExpect(jsonPath("$.[*].deviceName").value(hasItem(DEFAULT_DEVICE_NAME.toString())));
    }

    @Test
    @Transactional
    public void getAPGroup() throws Exception {
        // Initialize the database
        aPGroupRepository.saveAndFlush(aPGroup);

        // Get the aPGroup
        restAPGroupMockMvc.perform(get("/api/ap-groups/{id}", aPGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(aPGroup.getId().intValue()))
            .andExpect(jsonPath("$.serviceId").value(DEFAULT_SERVICE_ID.toString()))
            .andExpect(jsonPath("$.deviceId").value(DEFAULT_DEVICE_ID.toString()))
            .andExpect(jsonPath("$.serviceName").value(DEFAULT_SERVICE_NAME.toString()))
            .andExpect(jsonPath("$.deviceName").value(DEFAULT_DEVICE_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllAPGroupsByServiceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        aPGroupRepository.saveAndFlush(aPGroup);

        // Get all the aPGroupList where serviceId equals to DEFAULT_SERVICE_ID
        defaultAPGroupShouldBeFound("serviceId.equals=" + DEFAULT_SERVICE_ID);

        // Get all the aPGroupList where serviceId equals to UPDATED_SERVICE_ID
        defaultAPGroupShouldNotBeFound("serviceId.equals=" + UPDATED_SERVICE_ID);
    }

    @Test
    @Transactional
    public void getAllAPGroupsByServiceIdIsInShouldWork() throws Exception {
        // Initialize the database
        aPGroupRepository.saveAndFlush(aPGroup);

        // Get all the aPGroupList where serviceId in DEFAULT_SERVICE_ID or UPDATED_SERVICE_ID
        defaultAPGroupShouldBeFound("serviceId.in=" + DEFAULT_SERVICE_ID + "," + UPDATED_SERVICE_ID);

        // Get all the aPGroupList where serviceId equals to UPDATED_SERVICE_ID
        defaultAPGroupShouldNotBeFound("serviceId.in=" + UPDATED_SERVICE_ID);
    }

    @Test
    @Transactional
    public void getAllAPGroupsByServiceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        aPGroupRepository.saveAndFlush(aPGroup);

        // Get all the aPGroupList where serviceId is not null
        defaultAPGroupShouldBeFound("serviceId.specified=true");

        // Get all the aPGroupList where serviceId is null
        defaultAPGroupShouldNotBeFound("serviceId.specified=false");
    }

    @Test
    @Transactional
    public void getAllAPGroupsByDeviceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        aPGroupRepository.saveAndFlush(aPGroup);

        // Get all the aPGroupList where deviceId equals to DEFAULT_DEVICE_ID
        defaultAPGroupShouldBeFound("deviceId.equals=" + DEFAULT_DEVICE_ID);

        // Get all the aPGroupList where deviceId equals to UPDATED_DEVICE_ID
        defaultAPGroupShouldNotBeFound("deviceId.equals=" + UPDATED_DEVICE_ID);
    }

    @Test
    @Transactional
    public void getAllAPGroupsByDeviceIdIsInShouldWork() throws Exception {
        // Initialize the database
        aPGroupRepository.saveAndFlush(aPGroup);

        // Get all the aPGroupList where deviceId in DEFAULT_DEVICE_ID or UPDATED_DEVICE_ID
        defaultAPGroupShouldBeFound("deviceId.in=" + DEFAULT_DEVICE_ID + "," + UPDATED_DEVICE_ID);

        // Get all the aPGroupList where deviceId equals to UPDATED_DEVICE_ID
        defaultAPGroupShouldNotBeFound("deviceId.in=" + UPDATED_DEVICE_ID);
    }

    @Test
    @Transactional
    public void getAllAPGroupsByDeviceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        aPGroupRepository.saveAndFlush(aPGroup);

        // Get all the aPGroupList where deviceId is not null
        defaultAPGroupShouldBeFound("deviceId.specified=true");

        // Get all the aPGroupList where deviceId is null
        defaultAPGroupShouldNotBeFound("deviceId.specified=false");
    }

    @Test
    @Transactional
    public void getAllAPGroupsByServiceNameIsEqualToSomething() throws Exception {
        // Initialize the database
        aPGroupRepository.saveAndFlush(aPGroup);

        // Get all the aPGroupList where serviceName equals to DEFAULT_SERVICE_NAME
        defaultAPGroupShouldBeFound("serviceName.equals=" + DEFAULT_SERVICE_NAME);

        // Get all the aPGroupList where serviceName equals to UPDATED_SERVICE_NAME
        defaultAPGroupShouldNotBeFound("serviceName.equals=" + UPDATED_SERVICE_NAME);
    }

    @Test
    @Transactional
    public void getAllAPGroupsByServiceNameIsInShouldWork() throws Exception {
        // Initialize the database
        aPGroupRepository.saveAndFlush(aPGroup);

        // Get all the aPGroupList where serviceName in DEFAULT_SERVICE_NAME or UPDATED_SERVICE_NAME
        defaultAPGroupShouldBeFound("serviceName.in=" + DEFAULT_SERVICE_NAME + "," + UPDATED_SERVICE_NAME);

        // Get all the aPGroupList where serviceName equals to UPDATED_SERVICE_NAME
        defaultAPGroupShouldNotBeFound("serviceName.in=" + UPDATED_SERVICE_NAME);
    }

    @Test
    @Transactional
    public void getAllAPGroupsByServiceNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        aPGroupRepository.saveAndFlush(aPGroup);

        // Get all the aPGroupList where serviceName is not null
        defaultAPGroupShouldBeFound("serviceName.specified=true");

        // Get all the aPGroupList where serviceName is null
        defaultAPGroupShouldNotBeFound("serviceName.specified=false");
    }

    @Test
    @Transactional
    public void getAllAPGroupsByDeviceNameIsEqualToSomething() throws Exception {
        // Initialize the database
        aPGroupRepository.saveAndFlush(aPGroup);

        // Get all the aPGroupList where deviceName equals to DEFAULT_DEVICE_NAME
        defaultAPGroupShouldBeFound("deviceName.equals=" + DEFAULT_DEVICE_NAME);

        // Get all the aPGroupList where deviceName equals to UPDATED_DEVICE_NAME
        defaultAPGroupShouldNotBeFound("deviceName.equals=" + UPDATED_DEVICE_NAME);
    }

    @Test
    @Transactional
    public void getAllAPGroupsByDeviceNameIsInShouldWork() throws Exception {
        // Initialize the database
        aPGroupRepository.saveAndFlush(aPGroup);

        // Get all the aPGroupList where deviceName in DEFAULT_DEVICE_NAME or UPDATED_DEVICE_NAME
        defaultAPGroupShouldBeFound("deviceName.in=" + DEFAULT_DEVICE_NAME + "," + UPDATED_DEVICE_NAME);

        // Get all the aPGroupList where deviceName equals to UPDATED_DEVICE_NAME
        defaultAPGroupShouldNotBeFound("deviceName.in=" + UPDATED_DEVICE_NAME);
    }

    @Test
    @Transactional
    public void getAllAPGroupsByDeviceNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        aPGroupRepository.saveAndFlush(aPGroup);

        // Get all the aPGroupList where deviceName is not null
        defaultAPGroupShouldBeFound("deviceName.specified=true");

        // Get all the aPGroupList where deviceName is null
        defaultAPGroupShouldNotBeFound("deviceName.specified=false");
    }

    @Test
    @Transactional
    public void getAllAPGroupsByWg24IsEqualToSomething() throws Exception {
        // Initialize the database
        WlanGroup wg24 = WlanGroupResourceIntTest.createEntity(em);
        em.persist(wg24);
        em.flush();
        aPGroup.setWg24(wg24);
        aPGroupRepository.saveAndFlush(aPGroup);
        Long wg24Id = wg24.getId();

        // Get all the aPGroupList where wg24 equals to wg24Id
        defaultAPGroupShouldBeFound("wg24Id.equals=" + wg24Id);

        // Get all the aPGroupList where wg24 equals to wg24Id + 1
        defaultAPGroupShouldNotBeFound("wg24Id.equals=" + (wg24Id + 1));
    }


    @Test
    @Transactional
    public void getAllAPGroupsByWg50IsEqualToSomething() throws Exception {
        // Initialize the database
        WlanGroup wg50 = WlanGroupResourceIntTest.createEntity(em);
        em.persist(wg50);
        em.flush();
        aPGroup.setWg50(wg50);
        aPGroupRepository.saveAndFlush(aPGroup);
        Long wg50Id = wg50.getId();

        // Get all the aPGroupList where wg50 equals to wg50Id
        defaultAPGroupShouldBeFound("wg50Id.equals=" + wg50Id);

        // Get all the aPGroupList where wg50 equals to wg50Id + 1
        defaultAPGroupShouldNotBeFound("wg50Id.equals=" + (wg50Id + 1));
    }


    @Test
    @Transactional
    public void getAllAPGroupsByApIsEqualToSomething() throws Exception {
        // Initialize the database
        AP ap = APResourceIntTest.createEntity(em);
        em.persist(ap);
        em.flush();
        aPGroup.addAp(ap);
        aPGroupRepository.saveAndFlush(aPGroup);
        Long apId = ap.getId();

        // Get all the aPGroupList where ap equals to apId
        defaultAPGroupShouldBeFound("apId.equals=" + apId);

        // Get all the aPGroupList where ap equals to apId + 1
        defaultAPGroupShouldNotBeFound("apId.equals=" + (apId + 1));
    }


    @Test
    @Transactional
    public void getAllAPGroupsByZoneIsEqualToSomething() throws Exception {
        // Initialize the database
        Zone zone = ZoneResourceIntTest.createEntity(em);
        em.persist(zone);
        em.flush();
        aPGroup.setZone(zone);
        aPGroupRepository.saveAndFlush(aPGroup);
        Long zoneId = zone.getId();

        // Get all the aPGroupList where zone equals to zoneId
        defaultAPGroupShouldBeFound("zoneId.equals=" + zoneId);

        // Get all the aPGroupList where zone equals to zoneId + 1
        defaultAPGroupShouldNotBeFound("zoneId.equals=" + (zoneId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultAPGroupShouldBeFound(String filter) throws Exception {
        restAPGroupMockMvc.perform(get("/api/ap-groups?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aPGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].serviceId").value(hasItem(DEFAULT_SERVICE_ID.toString())))
            .andExpect(jsonPath("$.[*].deviceId").value(hasItem(DEFAULT_DEVICE_ID.toString())))
            .andExpect(jsonPath("$.[*].serviceName").value(hasItem(DEFAULT_SERVICE_NAME.toString())))
            .andExpect(jsonPath("$.[*].deviceName").value(hasItem(DEFAULT_DEVICE_NAME.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultAPGroupShouldNotBeFound(String filter) throws Exception {
        restAPGroupMockMvc.perform(get("/api/ap-groups?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingAPGroup() throws Exception {
        // Get the aPGroup
        restAPGroupMockMvc.perform(get("/api/ap-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAPGroup() throws Exception {
        // Initialize the database
        aPGroupService.save(aPGroup);

        int databaseSizeBeforeUpdate = aPGroupRepository.findAll().size();

        // Update the aPGroup
        APGroup updatedAPGroup = aPGroupRepository.findOne(aPGroup.getId());
        // Disconnect from session so that the updates on updatedAPGroup are not directly saved in db
        em.detach(updatedAPGroup);
        updatedAPGroup
            .serviceId(UPDATED_SERVICE_ID)
            .deviceId(UPDATED_DEVICE_ID)
            .serviceName(UPDATED_SERVICE_NAME)
            .deviceName(UPDATED_DEVICE_NAME);

        restAPGroupMockMvc.perform(put("/api/ap-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAPGroup)))
            .andExpect(status().isOk());

        // Validate the APGroup in the database
        List<APGroup> aPGroupList = aPGroupRepository.findAll();
        assertThat(aPGroupList).hasSize(databaseSizeBeforeUpdate);
        APGroup testAPGroup = aPGroupList.get(aPGroupList.size() - 1);
        assertThat(testAPGroup.getServiceId()).isEqualTo(UPDATED_SERVICE_ID);
        assertThat(testAPGroup.getDeviceId()).isEqualTo(UPDATED_DEVICE_ID);
        assertThat(testAPGroup.getServiceName()).isEqualTo(UPDATED_SERVICE_NAME);
        assertThat(testAPGroup.getDeviceName()).isEqualTo(UPDATED_DEVICE_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingAPGroup() throws Exception {
        int databaseSizeBeforeUpdate = aPGroupRepository.findAll().size();

        // Create the APGroup

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAPGroupMockMvc.perform(put("/api/ap-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(aPGroup)))
            .andExpect(status().isCreated());

        // Validate the APGroup in the database
        List<APGroup> aPGroupList = aPGroupRepository.findAll();
        assertThat(aPGroupList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAPGroup() throws Exception {
        // Initialize the database
        aPGroupService.save(aPGroup);

        int databaseSizeBeforeDelete = aPGroupRepository.findAll().size();

        // Get the aPGroup
        restAPGroupMockMvc.perform(delete("/api/ap-groups/{id}", aPGroup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<APGroup> aPGroupList = aPGroupRepository.findAll();
        assertThat(aPGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(APGroup.class);
        APGroup aPGroup1 = new APGroup();
        aPGroup1.setId(1L);
        APGroup aPGroup2 = new APGroup();
        aPGroup2.setId(aPGroup1.getId());
        assertThat(aPGroup1).isEqualTo(aPGroup2);
        aPGroup2.setId(2L);
        assertThat(aPGroup1).isNotEqualTo(aPGroup2);
        aPGroup1.setId(null);
        assertThat(aPGroup1).isNotEqualTo(aPGroup2);
    }
}
