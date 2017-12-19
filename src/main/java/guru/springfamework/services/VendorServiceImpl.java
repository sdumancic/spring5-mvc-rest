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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jt on 9/26/17.
 */
@Service
public class VendorServiceImpl implements VendorService {

    private final VendorMapper vendorMapper;
    private final VendorRepository vendorRepository;

    public VendorServiceImpl(VendorMapper vendorMapper, VendorRepository vendorRepository) {
        this.vendorMapper = vendorMapper;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public VendorListDTO getAllVendors() {

        List<VendorDTO> vendorDTOS = vendorRepository.findAll()
                .stream()
                .map(vendor -> {
                    VendorDTO vendorDTO = vendorMapper.vendorToVendorDTO(vendor);
                    vendorDTO .setVendorUrl("/api/v1/vendors/" + vendor.getId() );
                    return vendorDTO;
                    })
                .collect(Collectors.toList());
        return new VendorListDTO(vendorDTOS);
    }

    @Override
    public VendorDTO getVendorById(Long id) {
        return vendorRepository.findById(id)
                .map(vendorMapper::vendorToVendorDTO)
                .map(vendorDTO -> {
                    vendorDTO.setVendorUrl("/api/v1/vendors/" + id);
                    return vendorDTO;
                })
                .orElseThrow(ResourceNotFoundException::new);


    }

    @Override
    public VendorDTO createNewVendor(VendorDTO vendorDTO) {
        Vendor vendor= vendorMapper.vendorDtoToVendor(vendorDTO);
        return saveAndReturnDTO(vendor);

    }

    private VendorDTO saveAndReturnDTO(Vendor vendor) {
        Vendor savedVendor = vendorRepository.save(vendor);
        VendorDTO returnDto= vendorMapper.vendorToVendorDTO(savedVendor);
        returnDto.setVendorUrl("/api/v1/vendors/"+savedVendor.getId());
        return returnDto;
    }

    @Override
    public VendorDTO saveVendorByDTO(Long id, VendorDTO vendorDTO) {
        Vendor vendor = vendorMapper.vendorDtoToVendor(vendorDTO);
        vendor.setId(id);
        return saveAndReturnDTO(vendor);
    }

    @Override
    public VendorDTO patchVendor(Long id, VendorDTO vendorDTO) {

        return vendorRepository.findById(id).map(vendor -> {

            if(vendorDTO.getName() != null){
                vendor.setName(vendorDTO.getName());
            }

            return saveAndReturnDTO(vendor);
        }).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public void deleteVendorById(Long id) {
        vendorRepository.deleteById(id);
    }
}
