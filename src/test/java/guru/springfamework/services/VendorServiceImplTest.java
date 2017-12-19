package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.CustomerMapper;
import guru.springfamework.api.v1.mapper.VendorMapper;
import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.api.v1.model.VendorListDTO;
import guru.springfamework.domain.Customer;
import guru.springfamework.domain.Vendor;
import guru.springfamework.repositories.CustomerRepository;
import guru.springfamework.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

public class VendorServiceImplTest {

    public static final Long ID = 2L;
    public static final String NAME = "Microsoft";

    VendorService vendorService;

    @Mock
    VendorRepository vendorRepository;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        vendorService = new VendorServiceImpl(VendorMapper.INSTANCE, vendorRepository);
    }

    @Test
    public void getAllVendors() throws Exception {

        //given
        List<Vendor> vendors = Arrays.asList(new Vendor(), new Vendor(), new Vendor());

        given(vendorRepository.findAll()).willReturn(vendors);


        //when
        VendorListDTO vendorListDTOS = vendorService.getAllVendors();

        //then
        then(vendorRepository).should(times(1)).findAll();
        assertThat(vendorListDTOS.getVendors().size(),is(equalTo(3)));

    }

    @Test
    public void getVendorById() throws Exception {

        Vendor v1 = new Vendor();
        v1.setId(100L);
        v1.setName("ABC");


        given(vendorRepository.findById(anyLong())).willReturn(Optional.of(v1));

        VendorDTO vendorDTO = vendorService.getVendorById(100L);

        then(vendorRepository).should(times(1)).findById(anyLong());

        //then
        assertThat(vendorDTO.getName(),is(equalTo("ABC")));

    }

    @Test(expected = ResourceNotFoundException.class)
    public void getVendorByIdNotFound() throws Exception{
        given(vendorRepository.findById(anyLong())).willReturn(Optional.empty());
        VendorDTO vendorDTO = vendorService.getVendorById(1L);
        then(vendorRepository).should(times(1)).findById(anyLong());
    }

    @Test
    public void createNewVendor() throws Exception{
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName("Jim");

        Vendor savedVendor = new Vendor();
        savedVendor.setName(vendorDTO.getName());
        savedVendor.setId(1L);

        given (vendorRepository.save(any(Vendor.class))).willReturn(savedVendor);

        VendorDTO savedDto = vendorService.createNewVendor(vendorDTO);

        //then
        then(vendorRepository).should().save(any(Vendor.class));
        assertEquals(vendorDTO.getName(), savedDto.getName());
        assertEquals("/api/v1/vendors/1", savedDto.getVendorUrl());
        assertThat(savedDto.getVendorUrl(),containsString("1"));
    }


    @Test
    public void saveVendorByDTO() throws Exception{
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName("Jim");
        Vendor savedVendor = new Vendor();
        savedVendor.setName(vendorDTO.getName());
        savedVendor.setId(1L);

        when (vendorRepository.save(any(Vendor.class))).thenReturn(savedVendor);
        VendorDTO savedDTO = vendorService.saveVendorByDTO(1L, vendorDTO);

        assertEquals(vendorDTO.getName(), savedDTO.getName());
        assertEquals("/api/v1/vendors/1", savedDTO.getVendorUrl());
    }

    @Test
    public void deleteVendorById() throws Exception{
        Long id = 1L;
        vendorRepository.deleteById(id);
        verify(vendorRepository,times(1)).deleteById(anyLong());
    }
}