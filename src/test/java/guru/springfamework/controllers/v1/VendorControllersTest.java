package guru.springfamework.controllers.v1;

import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.api.v1.model.VendorListDTO;
import guru.springfamework.domain.Vendor;
import guru.springfamework.services.CustomerService;
import guru.springfamework.services.VendorService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static guru.springfamework.controllers.v1.AbstractRestControllerTest.asJsonString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {VendorController.class})
public class VendorControllersTest {

    public static final String NAME = "Sanjin";

    @MockBean //provided by Spring Context instead of initmocks
    VendorService vendorService;

    @Autowired
    MockMvc mockMvc;

    VendorDTO vendorDTO_1;
    VendorDTO vendorDTO_2;

    @Before
    public void setUp() throws Exception{
        vendorDTO_1 = new VendorDTO("Vendor1", VendorController.BASE_URL +"/1");
        vendorDTO_2 = new VendorDTO("Vendor2", VendorController.BASE_URL +"/2");

    }

    @Test
    public void getVendorList() throws Exception{
        VendorListDTO vendorListDTO = new VendorListDTO(Arrays.asList(vendorDTO_1,vendorDTO_2));

        given(vendorService.getAllVendors()).willReturn(vendorListDTO);


        mockMvc.perform(get(VendorController.BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                       ).andExpect(status().isOk())
                        .andExpect(jsonPath("$.vendors", hasSize(2))); // $ = root
    }

    @Test
    public void testGetVendorByID() throws Exception {

        given(vendorService.getVendorById(anyLong())).willReturn(vendorDTO_1);

        mockMvc.perform(get(VendorController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(vendorDTO_1.getName())));
    }

    @Test
    public void createNewVendor() throws Exception{

        given(vendorService.createNewVendor(vendorDTO_1)).willReturn(vendorDTO_1);


        String response = mockMvc.perform(post(VendorController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(vendorDTO_1)))
                .andReturn().getResponse().getContentAsString();

        System.out.println(response);


        mockMvc.perform(post(VendorController.BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(vendorDTO_1)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name",equalTo(vendorDTO_1.getName())))
                    .andExpect(jsonPath("$.vendor_url",equalTo(vendorDTO_1.getVendorUrl())));

    }

    @Test
    public void testUpdateVendor() throws Exception {
        //given
       given(vendorService.saveVendorByDTO(anyLong(), any(VendorDTO.class))).willReturn(vendorDTO_1);

        //when/then
        mockMvc.perform(put(VendorController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(vendorDTO_1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(vendorDTO_1.getName())))
                .andExpect(jsonPath("$.vendor_url", equalTo(vendorDTO_1.getVendorUrl())));
    }

    @Test
    public void testPatchVendor() throws Exception {
        //given
        given(vendorService.patchVendor(anyLong(), any(VendorDTO.class))).willReturn(vendorDTO_1);

        String response = mockMvc.perform(patch(VendorController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(vendorDTO_1)))
                .andReturn().getResponse().getContentAsString();
        System.out.println("---------------------------------");
        System.out.println(response);
        System.out.println("---------------------------------");

        //when/then
        mockMvc.perform(patch(VendorController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(vendorDTO_1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(vendorDTO_1.getName())))
                .andExpect(jsonPath("$.vendor_url", equalTo(vendorDTO_1.getVendorUrl())));
    }

    @Test
    public void testDeleteVendor() throws Exception{
        mockMvc.perform(delete(VendorController.BASE_URL + "/1"))
                .andExpect(status().isOk());

        then(vendorService).should().deleteVendorById(anyLong());
    }

}
