package com.freedom.ems;

import com.freedom.ems.exception.CustomResponse;
import com.freedom.ems.model.Department;
import org.apache.commons.codec.binary.Base64;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = DEFINED_PORT)
public class IntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private String invalidBasicAuth;
    private String userBasicAuth;
    private String adminBasicAuth;

    @Before
    public void setup() {
        invalidBasicAuth = "Basic " + new String(Base64.encodeBase64("admin:user".getBytes()));
        userBasicAuth = "Basic " + new String(Base64.encodeBase64("user:user".getBytes()));
        adminBasicAuth = "Basic " + new String(Base64.encodeBase64("admin:admin".getBytes()));
    }

    @Test
    public void testNotAuthenticated() {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", invalidBasicAuth);

        Department department = new Department();
        department.setName("Engineering");


        // Create Department #1 - Engineering
        ResponseEntity<Department> department1ResponseEntity = restTemplate
                .exchange("/department", HttpMethod.POST, new HttpEntity<>(department, headers), Department.class);
        Assertions.assertThat(department1ResponseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void testAuthenticatedButNotAuthorized() {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", userBasicAuth);

        Department department = new Department();
        department.setName("Engineering");


        // Create Department #1 - Engineering
        ResponseEntity<Department> department1ResponseEntity = restTemplate
                .exchange("/department", HttpMethod.POST, new HttpEntity<>(department, headers), Department.class);
        Assertions.assertThat(department1ResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void testDepartment() {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", adminBasicAuth);

        Department department = new Department();
        department.setName("Engineering");


        // Create Department #1 - Engineering
        ResponseEntity<Department> department1ResponseEntity = restTemplate
                .exchange("/department", HttpMethod.POST, new HttpEntity<>(department, headers), Department.class);
        Assertions.assertThat(department1ResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);


        // Create Department #2 - HR
        department.setName("HR");
        ResponseEntity<Department> department2ResponseEntity = restTemplate
                .exchange("/department", HttpMethod.POST, new HttpEntity<>(department, headers), Department.class);
        Assertions.assertThat(department2ResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);


        // Get All Departments - Should return Engineering & HR
        ResponseEntity<List<Department>> departments1ResponseEntity = restTemplate
                .exchange("/departments", HttpMethod.GET, new HttpEntity<>(headers),
                        new ParameterizedTypeReference<List<Department>>() {
                        });
        Assertions.assertThat(departments1ResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(departments1ResponseEntity.getBody().size()).isEqualTo(2);
        Assertions.assertThat(departments1ResponseEntity.getBody().get(0).getName()).isEqualTo("Engineering");
        Assertions.assertThat(departments1ResponseEntity.getBody().get(1).getName()).isEqualTo("HR");


        // Delete Department #1 - Engineering
        ResponseEntity<CustomResponse> deleteResponseEntity = restTemplate
                .exchange("/department/1", HttpMethod.DELETE, new HttpEntity<>(headers), CustomResponse.class);
        Assertions.assertThat(deleteResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);


        // Get All Departments - Should return only HR
        ResponseEntity<List<Department>> departments2ResponseEntity = restTemplate
                .exchange("/departments", HttpMethod.GET, new HttpEntity<>(headers),
                        new ParameterizedTypeReference<List<Department>>() {
                        });
        Assertions.assertThat(departments2ResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(departments2ResponseEntity.getBody().size()).isEqualTo(1);
        Assertions.assertThat(departments2ResponseEntity.getBody().get(0).getName()).isEqualTo("HR");
    }
}
