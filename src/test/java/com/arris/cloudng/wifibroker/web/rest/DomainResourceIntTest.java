package com.arris.cloudng.wifibroker.web.rest;

import com.arris.cloudng.wifibroker.SampleBrokerApp;

import com.arris.cloudng.wifibroker.domain.Domain;
import com.arris.cloudng.wifibroker.domain.Zone;
import com.arris.cloudng.wifibroker.repository.DomainRepository;
import com.arris.cloudng.wifibroker.service.DomainService;
import com.arris.cloudng.wifibroker.web.rest.errors.ExceptionTranslator;
import com.arris.cloudng.wifibroker.service.dto.DomainCriteria;
import com.arris.cloudng.wifibroker.service.DomainQueryService;

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
 * Test class for the DomainResource REST controller.
 *
 * @see DomainResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SampleBrokerApp.class)
public class DomainResourceIntTest {

    private static final String DEFAULT_SERVICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_DEVICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DEVICE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_NAME = "BBBBBBBBBB";

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private DomainService domainService;

    @Autowired
    private DomainQueryService domainQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDomainMockMvc;

    private Domain domain;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DomainResource domainResource = new DomainResource(domainService, domainQueryService);
        this.restDomainMockMvc = MockMvcBuilders.standaloneSetup(domainResource)
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
    public static Domain createEntity(EntityManager em) {
        Domain domain = new Domain()
            .serviceId(DEFAULT_SERVICE_ID)
            .deviceId(DEFAULT_DEVICE_ID)
            .serviceName(DEFAULT_SERVICE_NAME)
            .deviceName(DEFAULT_DEVICE_NAME);
        return domain;
    }

    @Before
    public void initTest() {
        domain = createEntity(em);
    }

    @Test
    @Transactional
    public void createDomain() throws Exception {
        int databaseSizeBeforeCreate = domainRepository.findAll().size();

        // Create the Domain
        restDomainMockMvc.perform(post("/api/domains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(domain)))
            .andExpect(status().isCreated());

        // Validate the Domain in the database
        List<Domain> domainList = domainRepository.findAll();
        assertThat(domainList).hasSize(databaseSizeBeforeCreate + 1);
        Domain testDomain = domainList.get(domainList.size() - 1);
        assertThat(testDomain.getServiceId()).isEqualTo(DEFAULT_SERVICE_ID);
        assertThat(testDomain.getDeviceId()).isEqualTo(DEFAULT_DEVICE_ID);
        assertThat(testDomain.getServiceName()).isEqualTo(DEFAULT_SERVICE_NAME);
        assertThat(testDomain.getDeviceName()).isEqualTo(DEFAULT_DEVICE_NAME);
    }

    @Test
    @Transactional
    public void createDomainWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = domainRepository.findAll().size();

        // Create the Domain with an existing ID
        domain.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDomainMockMvc.perform(post("/api/domains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(domain)))
            .andExpect(status().isBadRequest());

        // Validate the Domain in the database
        List<Domain> domainList = domainRepository.findAll();
        assertThat(domainList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkServiceIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = domainRepository.findAll().size();
        // set the field null
        domain.setServiceId(null);

        // Create the Domain, which fails.

        restDomainMockMvc.perform(post("/api/domains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(domain)))
            .andExpect(status().isBadRequest());

        List<Domain> domainList = domainRepository.findAll();
        assertThat(domainList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDeviceIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = domainRepository.findAll().size();
        // set the field null
        domain.setDeviceId(null);

        // Create the Domain, which fails.

        restDomainMockMvc.perform(post("/api/domains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(domain)))
            .andExpect(status().isBadRequest());

        List<Domain> domainList = domainRepository.findAll();
        assertThat(domainList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkServiceNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = domainRepository.findAll().size();
        // set the field null
        domain.setServiceName(null);

        // Create the Domain, which fails.

        restDomainMockMvc.perform(post("/api/domains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(domain)))
            .andExpect(status().isBadRequest());

        List<Domain> domainList = domainRepository.findAll();
        assertThat(domainList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDeviceNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = domainRepository.findAll().size();
        // set the field null
        domain.setDeviceName(null);

        // Create the Domain, which fails.

        restDomainMockMvc.perform(post("/api/domains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(domain)))
            .andExpect(status().isBadRequest());

        List<Domain> domainList = domainRepository.findAll();
        assertThat(domainList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDomains() throws Exception {
        // Initialize the database
        domainRepository.saveAndFlush(domain);

        // Get all the domainList
        restDomainMockMvc.perform(get("/api/domains?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(domain.getId().intValue())))
            .andExpect(jsonPath("$.[*].serviceId").value(hasItem(DEFAULT_SERVICE_ID.toString())))
            .andExpect(jsonPath("$.[*].deviceId").value(hasItem(DEFAULT_DEVICE_ID.toString())))
            .andExpect(jsonPath("$.[*].serviceName").value(hasItem(DEFAULT_SERVICE_NAME.toString())))
            .andExpect(jsonPath("$.[*].deviceName").value(hasItem(DEFAULT_DEVICE_NAME.toString())));
    }

    @Test
    @Transactional
    public void getDomain() throws Exception {
        // Initialize the database
        domainRepository.saveAndFlush(domain);

        // Get the domain
        restDomainMockMvc.perform(get("/api/domains/{id}", domain.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(domain.getId().intValue()))
            .andExpect(jsonPath("$.serviceId").value(DEFAULT_SERVICE_ID.toString()))
            .andExpect(jsonPath("$.deviceId").value(DEFAULT_DEVICE_ID.toString()))
            .andExpect(jsonPath("$.serviceName").value(DEFAULT_SERVICE_NAME.toString()))
            .andExpect(jsonPath("$.deviceName").value(DEFAULT_DEVICE_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllDomainsByServiceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        domainRepository.saveAndFlush(domain);

        // Get all the domainList where serviceId equals to DEFAULT_SERVICE_ID
        defaultDomainShouldBeFound("serviceId.equals=" + DEFAULT_SERVICE_ID);

        // Get all the domainList where serviceId equals to UPDATED_SERVICE_ID
        defaultDomainShouldNotBeFound("serviceId.equals=" + UPDATED_SERVICE_ID);
    }

    @Test
    @Transactional
    public void getAllDomainsByServiceIdIsInShouldWork() throws Exception {
        // Initialize the database
        domainRepository.saveAndFlush(domain);

        // Get all the domainList where serviceId in DEFAULT_SERVICE_ID or UPDATED_SERVICE_ID
        defaultDomainShouldBeFound("serviceId.in=" + DEFAULT_SERVICE_ID + "," + UPDATED_SERVICE_ID);

        // Get all the domainList where serviceId equals to UPDATED_SERVICE_ID
        defaultDomainShouldNotBeFound("serviceId.in=" + UPDATED_SERVICE_ID);
    }

    @Test
    @Transactional
    public void getAllDomainsByServiceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        domainRepository.saveAndFlush(domain);

        // Get all the domainList where serviceId is not null
        defaultDomainShouldBeFound("serviceId.specified=true");

        // Get all the domainList where serviceId is null
        defaultDomainShouldNotBeFound("serviceId.specified=false");
    }

    @Test
    @Transactional
    public void getAllDomainsByDeviceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        domainRepository.saveAndFlush(domain);

        // Get all the domainList where deviceId equals to DEFAULT_DEVICE_ID
        defaultDomainShouldBeFound("deviceId.equals=" + DEFAULT_DEVICE_ID);

        // Get all the domainList where deviceId equals to UPDATED_DEVICE_ID
        defaultDomainShouldNotBeFound("deviceId.equals=" + UPDATED_DEVICE_ID);
    }

    @Test
    @Transactional
    public void getAllDomainsByDeviceIdIsInShouldWork() throws Exception {
        // Initialize the database
        domainRepository.saveAndFlush(domain);

        // Get all the domainList where deviceId in DEFAULT_DEVICE_ID or UPDATED_DEVICE_ID
        defaultDomainShouldBeFound("deviceId.in=" + DEFAULT_DEVICE_ID + "," + UPDATED_DEVICE_ID);

        // Get all the domainList where deviceId equals to UPDATED_DEVICE_ID
        defaultDomainShouldNotBeFound("deviceId.in=" + UPDATED_DEVICE_ID);
    }

    @Test
    @Transactional
    public void getAllDomainsByDeviceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        domainRepository.saveAndFlush(domain);

        // Get all the domainList where deviceId is not null
        defaultDomainShouldBeFound("deviceId.specified=true");

        // Get all the domainList where deviceId is null
        defaultDomainShouldNotBeFound("deviceId.specified=false");
    }

    @Test
    @Transactional
    public void getAllDomainsByServiceNameIsEqualToSomething() throws Exception {
        // Initialize the database
        domainRepository.saveAndFlush(domain);

        // Get all the domainList where serviceName equals to DEFAULT_SERVICE_NAME
        defaultDomainShouldBeFound("serviceName.equals=" + DEFAULT_SERVICE_NAME);

        // Get all the domainList where serviceName equals to UPDATED_SERVICE_NAME
        defaultDomainShouldNotBeFound("serviceName.equals=" + UPDATED_SERVICE_NAME);
    }

    @Test
    @Transactional
    public void getAllDomainsByServiceNameIsInShouldWork() throws Exception {
        // Initialize the database
        domainRepository.saveAndFlush(domain);

        // Get all the domainList where serviceName in DEFAULT_SERVICE_NAME or UPDATED_SERVICE_NAME
        defaultDomainShouldBeFound("serviceName.in=" + DEFAULT_SERVICE_NAME + "," + UPDATED_SERVICE_NAME);

        // Get all the domainList where serviceName equals to UPDATED_SERVICE_NAME
        defaultDomainShouldNotBeFound("serviceName.in=" + UPDATED_SERVICE_NAME);
    }

    @Test
    @Transactional
    public void getAllDomainsByServiceNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        domainRepository.saveAndFlush(domain);

        // Get all the domainList where serviceName is not null
        defaultDomainShouldBeFound("serviceName.specified=true");

        // Get all the domainList where serviceName is null
        defaultDomainShouldNotBeFound("serviceName.specified=false");
    }

    @Test
    @Transactional
    public void getAllDomainsByDeviceNameIsEqualToSomething() throws Exception {
        // Initialize the database
        domainRepository.saveAndFlush(domain);

        // Get all the domainList where deviceName equals to DEFAULT_DEVICE_NAME
        defaultDomainShouldBeFound("deviceName.equals=" + DEFAULT_DEVICE_NAME);

        // Get all the domainList where deviceName equals to UPDATED_DEVICE_NAME
        defaultDomainShouldNotBeFound("deviceName.equals=" + UPDATED_DEVICE_NAME);
    }

    @Test
    @Transactional
    public void getAllDomainsByDeviceNameIsInShouldWork() throws Exception {
        // Initialize the database
        domainRepository.saveAndFlush(domain);

        // Get all the domainList where deviceName in DEFAULT_DEVICE_NAME or UPDATED_DEVICE_NAME
        defaultDomainShouldBeFound("deviceName.in=" + DEFAULT_DEVICE_NAME + "," + UPDATED_DEVICE_NAME);

        // Get all the domainList where deviceName equals to UPDATED_DEVICE_NAME
        defaultDomainShouldNotBeFound("deviceName.in=" + UPDATED_DEVICE_NAME);
    }

    @Test
    @Transactional
    public void getAllDomainsByDeviceNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        domainRepository.saveAndFlush(domain);

        // Get all the domainList where deviceName is not null
        defaultDomainShouldBeFound("deviceName.specified=true");

        // Get all the domainList where deviceName is null
        defaultDomainShouldNotBeFound("deviceName.specified=false");
    }

    @Test
    @Transactional
    public void getAllDomainsByZoneIsEqualToSomething() throws Exception {
        // Initialize the database
        Zone zone = ZoneResourceIntTest.createEntity(em);
        em.persist(zone);
        em.flush();
        domain.addZone(zone);
        domainRepository.saveAndFlush(domain);
        Long zoneId = zone.getId();

        // Get all the domainList where zone equals to zoneId
        defaultDomainShouldBeFound("zoneId.equals=" + zoneId);

        // Get all the domainList where zone equals to zoneId + 1
        defaultDomainShouldNotBeFound("zoneId.equals=" + (zoneId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultDomainShouldBeFound(String filter) throws Exception {
        restDomainMockMvc.perform(get("/api/domains?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(domain.getId().intValue())))
            .andExpect(jsonPath("$.[*].serviceId").value(hasItem(DEFAULT_SERVICE_ID.toString())))
            .andExpect(jsonPath("$.[*].deviceId").value(hasItem(DEFAULT_DEVICE_ID.toString())))
            .andExpect(jsonPath("$.[*].serviceName").value(hasItem(DEFAULT_SERVICE_NAME.toString())))
            .andExpect(jsonPath("$.[*].deviceName").value(hasItem(DEFAULT_DEVICE_NAME.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultDomainShouldNotBeFound(String filter) throws Exception {
        restDomainMockMvc.perform(get("/api/domains?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingDomain() throws Exception {
        // Get the domain
        restDomainMockMvc.perform(get("/api/domains/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDomain() throws Exception {
        // Initialize the database
        domainService.save(domain);

        int databaseSizeBeforeUpdate = domainRepository.findAll().size();

        // Update the domain
        Domain updatedDomain = domainRepository.findOne(domain.getId());
        // Disconnect from session so that the updates on updatedDomain are not directly saved in db
        em.detach(updatedDomain);
        updatedDomain
            .serviceId(UPDATED_SERVICE_ID)
            .deviceId(UPDATED_DEVICE_ID)
            .serviceName(UPDATED_SERVICE_NAME)
            .deviceName(UPDATED_DEVICE_NAME);

        restDomainMockMvc.perform(put("/api/domains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDomain)))
            .andExpect(status().isOk());

        // Validate the Domain in the database
        List<Domain> domainList = domainRepository.findAll();
        assertThat(domainList).hasSize(databaseSizeBeforeUpdate);
        Domain testDomain = domainList.get(domainList.size() - 1);
        assertThat(testDomain.getServiceId()).isEqualTo(UPDATED_SERVICE_ID);
        assertThat(testDomain.getDeviceId()).isEqualTo(UPDATED_DEVICE_ID);
        assertThat(testDomain.getServiceName()).isEqualTo(UPDATED_SERVICE_NAME);
        assertThat(testDomain.getDeviceName()).isEqualTo(UPDATED_DEVICE_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingDomain() throws Exception {
        int databaseSizeBeforeUpdate = domainRepository.findAll().size();

        // Create the Domain

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDomainMockMvc.perform(put("/api/domains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(domain)))
            .andExpect(status().isCreated());

        // Validate the Domain in the database
        List<Domain> domainList = domainRepository.findAll();
        assertThat(domainList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDomain() throws Exception {
        // Initialize the database
        domainService.save(domain);

        int databaseSizeBeforeDelete = domainRepository.findAll().size();

        // Get the domain
        restDomainMockMvc.perform(delete("/api/domains/{id}", domain.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Domain> domainList = domainRepository.findAll();
        assertThat(domainList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Domain.class);
        Domain domain1 = new Domain();
        domain1.setId(1L);
        Domain domain2 = new Domain();
        domain2.setId(domain1.getId());
        assertThat(domain1).isEqualTo(domain2);
        domain2.setId(2L);
        assertThat(domain1).isNotEqualTo(domain2);
        domain1.setId(null);
        assertThat(domain1).isNotEqualTo(domain2);
    }
}
