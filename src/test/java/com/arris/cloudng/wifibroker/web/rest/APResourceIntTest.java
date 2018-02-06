package com.arris.cloudng.wifibroker.web.rest;

import com.arris.cloudng.wifibroker.SampleBrokerApp;

import com.arris.cloudng.wifibroker.domain.AP;
import com.arris.cloudng.wifibroker.domain.APGroup;
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

    private static final String DEFAULT_SERVICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_NAME = "BBBBBBBBBB";

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
            .serviceId(DEFAULT_SERVICE_ID)
            .serviceName(DEFAULT_SERVICE_NAME);
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
        assertThat(testAP.getServiceId()).isEqualTo(DEFAULT_SERVICE_ID);
        assertThat(testAP.getServiceName()).isEqualTo(DEFAULT_SERVICE_NAME);
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
    public void checkServiceIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPRepository.findAll().size();
        // set the field null
        aP.setServiceId(null);

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
    public void checkServiceNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPRepository.findAll().size();
        // set the field null
        aP.setServiceName(null);

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
            .andExpect(jsonPath("$.[*].serviceId").value(hasItem(DEFAULT_SERVICE_ID.toString())))
            .andExpect(jsonPath("$.[*].serviceName").value(hasItem(DEFAULT_SERVICE_NAME.toString())));
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
            .andExpect(jsonPath("$.serviceId").value(DEFAULT_SERVICE_ID.toString()))
            .andExpect(jsonPath("$.serviceName").value(DEFAULT_SERVICE_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllAPSByServiceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        aPRepository.saveAndFlush(aP);

        // Get all the aPList where serviceId equals to DEFAULT_SERVICE_ID
        defaultAPShouldBeFound("serviceId.equals=" + DEFAULT_SERVICE_ID);

        // Get all the aPList where serviceId equals to UPDATED_SERVICE_ID
        defaultAPShouldNotBeFound("serviceId.equals=" + UPDATED_SERVICE_ID);
    }

    @Test
    @Transactional
    public void getAllAPSByServiceIdIsInShouldWork() throws Exception {
        // Initialize the database
        aPRepository.saveAndFlush(aP);

        // Get all the aPList where serviceId in DEFAULT_SERVICE_ID or UPDATED_SERVICE_ID
        defaultAPShouldBeFound("serviceId.in=" + DEFAULT_SERVICE_ID + "," + UPDATED_SERVICE_ID);

        // Get all the aPList where serviceId equals to UPDATED_SERVICE_ID
        defaultAPShouldNotBeFound("serviceId.in=" + UPDATED_SERVICE_ID);
    }

    @Test
    @Transactional
    public void getAllAPSByServiceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        aPRepository.saveAndFlush(aP);

        // Get all the aPList where serviceId is not null
        defaultAPShouldBeFound("serviceId.specified=true");

        // Get all the aPList where serviceId is null
        defaultAPShouldNotBeFound("serviceId.specified=false");
    }

    @Test
    @Transactional
    public void getAllAPSByServiceNameIsEqualToSomething() throws Exception {
        // Initialize the database
        aPRepository.saveAndFlush(aP);

        // Get all the aPList where serviceName equals to DEFAULT_SERVICE_NAME
        defaultAPShouldBeFound("serviceName.equals=" + DEFAULT_SERVICE_NAME);

        // Get all the aPList where serviceName equals to UPDATED_SERVICE_NAME
        defaultAPShouldNotBeFound("serviceName.equals=" + UPDATED_SERVICE_NAME);
    }

    @Test
    @Transactional
    public void getAllAPSByServiceNameIsInShouldWork() throws Exception {
        // Initialize the database
        aPRepository.saveAndFlush(aP);

        // Get all the aPList where serviceName in DEFAULT_SERVICE_NAME or UPDATED_SERVICE_NAME
        defaultAPShouldBeFound("serviceName.in=" + DEFAULT_SERVICE_NAME + "," + UPDATED_SERVICE_NAME);

        // Get all the aPList where serviceName equals to UPDATED_SERVICE_NAME
        defaultAPShouldNotBeFound("serviceName.in=" + UPDATED_SERVICE_NAME);
    }

    @Test
    @Transactional
    public void getAllAPSByServiceNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        aPRepository.saveAndFlush(aP);

        // Get all the aPList where serviceName is not null
        defaultAPShouldBeFound("serviceName.specified=true");

        // Get all the aPList where serviceName is null
        defaultAPShouldNotBeFound("serviceName.specified=false");
    }

    @Test
    @Transactional
    public void getAllAPSByApgroupIsEqualToSomething() throws Exception {
        // Initialize the database
        APGroup apgroup = APGroupResourceIntTest.createEntity(em);
        em.persist(apgroup);
        em.flush();
        aP.setApgroup(apgroup);
        aPRepository.saveAndFlush(aP);
        Long apgroupId = apgroup.getId();

        // Get all the aPList where apgroup equals to apgroupId
        defaultAPShouldBeFound("apgroupId.equals=" + apgroupId);

        // Get all the aPList where apgroup equals to apgroupId + 1
        defaultAPShouldNotBeFound("apgroupId.equals=" + (apgroupId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultAPShouldBeFound(String filter) throws Exception {
        restAPMockMvc.perform(get("/api/aps?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aP.getId().intValue())))
            .andExpect(jsonPath("$.[*].serviceId").value(hasItem(DEFAULT_SERVICE_ID.toString())))
            .andExpect(jsonPath("$.[*].serviceName").value(hasItem(DEFAULT_SERVICE_NAME.toString())));
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
            .serviceId(UPDATED_SERVICE_ID)
            .serviceName(UPDATED_SERVICE_NAME);

        restAPMockMvc.perform(put("/api/aps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAP)))
            .andExpect(status().isOk());

        // Validate the AP in the database
        List<AP> aPList = aPRepository.findAll();
        assertThat(aPList).hasSize(databaseSizeBeforeUpdate);
        AP testAP = aPList.get(aPList.size() - 1);
        assertThat(testAP.getServiceId()).isEqualTo(UPDATED_SERVICE_ID);
        assertThat(testAP.getServiceName()).isEqualTo(UPDATED_SERVICE_NAME);
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
