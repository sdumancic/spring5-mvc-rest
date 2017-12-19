package guru.springfamework.api.v1.mapper;

import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.domain.Customer;
import guru.springfamework.domain.Vendor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VendorMapperTest {

    public static final String VENDORNAME = "Microsoft";

    VendorMapper vendorMapper = VendorMapper.INSTANCE;

    @Test
    public void vendorToVendorDTO() throws Exception {

        //given
        Vendor v1 = new Vendor();
        v1.setName(VENDORNAME);

        //when
        VendorDTO vendorDTO = vendorMapper.vendorToVendorDTO(v1);

        //then
        assertEquals(VENDORNAME, vendorDTO.getName());
    }

}