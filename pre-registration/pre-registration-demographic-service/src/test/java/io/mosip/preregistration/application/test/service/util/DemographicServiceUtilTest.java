package io.mosip.preregistration.application.test.service.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.mosip.kernel.core.exception.ParseException;
import io.mosip.preregistration.application.dto.CreateDemographicDTO;
import io.mosip.preregistration.application.entity.DemographicEntity;
import io.mosip.preregistration.application.exception.MissingRequestParameterException;
import io.mosip.preregistration.application.exception.OperationNotAllowedException;
import io.mosip.preregistration.application.exception.system.JsonParseException;
import io.mosip.preregistration.application.service.util.DemographicServiceUtil;
import io.mosip.preregistration.core.exception.InvalidRequestParameterException;

/**
 * Test class to test the PreRegistration Service util methods
 * 
 * @author Ravi C Balaji
 * @since 1.0.0
 * 
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DemographicServiceUtilTest {

	@Autowired
	private DemographicServiceUtil demographicServiceUtil;

	private CreateDemographicDTO saveDemographicRequest = null;
	private CreateDemographicDTO updateDemographicRequest = null;
	private DemographicEntity demographicEntity = null;
	private String requestId = null;
	private JSONObject jsonObject;
	private JSONParser parser = null;

	@Before
	public void setUp() throws Exception {
		requestId = "mosip.preregistration";
		parser = new JSONParser();

		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("pre-registration.json").getFile());
		jsonObject = (JSONObject) parser.parse(new FileReader(file));

		saveDemographicRequest = new CreateDemographicDTO();
		saveDemographicRequest.setPreRegistrationId(null);
		saveDemographicRequest.setStatusCode("Pending_Appointment");
		saveDemographicRequest.setLangCode("ENG");
		saveDemographicRequest.setCreatedBy("9900806086");
		saveDemographicRequest.setCreatedDateTime(new Date());
		saveDemographicRequest.setUpdatedBy(null);
		saveDemographicRequest.setUpdatedDateTime(new Date());
		saveDemographicRequest.setDemographicDetails(jsonObject);

		updateDemographicRequest = new CreateDemographicDTO();
		updateDemographicRequest.setPreRegistrationId("35760478648170");
		updateDemographicRequest.setStatusCode("Pending_Appointment");
		updateDemographicRequest.setLangCode("ENG");
		updateDemographicRequest.setCreatedBy("9900806086");
		updateDemographicRequest.setCreatedDateTime(new Date());
		updateDemographicRequest.setUpdatedBy("9900806086");
		updateDemographicRequest.setUpdatedDateTime(new Date());
		updateDemographicRequest.setDemographicDetails(jsonObject);

		demographicEntity = new DemographicEntity();
		demographicEntity.setPreRegistrationId("35760478648170");
		demographicEntity.setApplicantDetailJson(Mockito.anyString().getBytes());
	}

	@Test(expected = InvalidRequestParameterException.class)
	public void prepareDemographicEntityFailureTest1() {
		saveDemographicRequest.setCreatedBy(null);
		Mockito.when(demographicServiceUtil.prepareDemographicEntity(saveDemographicRequest, requestId, "save"))
				.thenThrow(InvalidRequestParameterException.class);
	}

	@Test(expected = MissingRequestParameterException.class)
	public void prepareDemographicEntityFailureTest2() {
		String type = null;
		Mockito.when(demographicServiceUtil.prepareDemographicEntity(saveDemographicRequest, requestId, type))
				.thenThrow(MissingRequestParameterException.class);
	}

	@Test(expected = JsonParseException.class)
	public void setterForCreateDTOFailureTest() {
		Mockito.when(demographicServiceUtil.setterForCreateDTO(demographicEntity)).thenThrow(JsonParseException.class);
	}

	@Test
	public void isNullFailureTest() {
		assertThat(demographicServiceUtil.isNull(Mockito.anyCollection()), is(true));
	}

	@Test(expected = OperationNotAllowedException.class)
	public void checkStatusForDeletionFailureTest() {
		Mockito.when(demographicServiceUtil.checkStatusForDeletion(Mockito.anyString()))
				.thenThrow(OperationNotAllowedException.class);
	}
	
	@Test(expected = ParseException.class)
	public void dateSetterTest1(){
		Map<String, String> dateMap = new HashMap<>();
		dateMap.put("FromDate", "2018-10-17");
		String format ="YYYY-MM-DD";
		Mockito.when(demographicServiceUtil.dateSetter(dateMap,format)).thenThrow(ParseException.class);
	}
}