package org.egov.user.web.controller;

import org.egov.user.domain.service.UserService;
import org.egov.user.persistence.entity.Address;
import org.egov.user.persistence.entity.Role;
import org.egov.user.persistence.entity.SecureUser;
import org.egov.user.persistence.entity.User;
import org.egov.user.persistence.entity.enums.AddressType;
import org.egov.user.persistence.entity.enums.GuardianRelation;
import org.egov.user.web.contract.GetUserByIdRequest;
import org.egov.user.web.contract.GetUserByIdResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Formatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

@RestController
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/details")
    public org.egov.user.web.contract.User getUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SecureUser secureUser = (SecureUser) principal;
        return convertEntityToContract(
                secureUser.getUser()
        );
    }

    @PostMapping("/_get")
    public GetUserByIdResponse get(@RequestBody GetUserByIdRequest request) {
        User userEntity = userService.getUserById(request.getId());
        org.egov.user.web.contract.User userContract = convertEntityToContract(userEntity);
        return new GetUserByIdResponse(userContract);
    }

    private org.egov.user.web.contract.User convertEntityToContract(User userEntity) {
        List<Address> addresses = userEntity.getAddress();
        Optional<Address> permanentAddress = getAddressOfType(addresses, AddressType.PERMANENT);
        Optional<Address> correspondenceAddress = getAddressOfType(addresses, AddressType.CORRESPONDENCE);

        org.egov.user.web.contract.User userContract = org.egov.user.web.contract.User.builder()
                .id(userEntity.getId())
                .userName(userEntity.getUsername())
                .salutation(userEntity.getSalutation())
                .name(userEntity.getName())
                .gender(userEntity.getGender().toString())
                .mobileNumber(userEntity.getMobileNumber())
                .emailId(userEntity.getEmailId())
                .altContactNumber(userEntity.getAltContactNumber())
                .pan(userEntity.getPan())
                .aadhaarNumber(userEntity.getAadhaarNumber())
                .active(userEntity.isActive())
                .dob(userEntity.getDob())
                .pwdExpiryDate(userEntity.getPwdExpiryDate().toDate())
                .locale(userEntity.getLocale())
                .type(userEntity.getType())
                .accountLocked(false)
                .roles(convertEntityToContract(userEntity.getRoles()))
                .createdBy(userEntity.getId())
                .createdDate(userEntity.getCreatedDate())
                .lastModifiedBy(userEntity.getId())
                .lastModifiedDate(userEntity.getLastModifiedDate())
                .build();

        permanentAddress.ifPresent(address -> {
            userContract.setPermanentAddress(convertEntityToContract(address));
            userContract.setPermanentCity(address.getCityTownVillage());
            userContract.setPermanentPinCode(address.getPinCode());
        });

        correspondenceAddress.ifPresent(address -> {
            userContract.setCorrespondenceAddress(convertEntityToContract(address));
            userContract.setCorrespondenceCity(address.getCityTownVillage());
            userContract.setCorrespondencePinCode(address.getPinCode());
        });

        if(isGuardianRelationFatherOrHusband(userEntity.getGuardianRelation())) {
            userContract.setFatherOrHusbandName(userEntity.getGuardian());
        }

        return userContract;
    }

    private boolean isGuardianRelationFatherOrHusband(GuardianRelation guardianRelation) {
        return (guardianRelation.equals(GuardianRelation.Father) || guardianRelation.equals(GuardianRelation.Husband));
    }

    private Optional<Address> getAddressOfType(List<Address> addressList, AddressType addressType) {
        return addressList.stream()
                .filter(address -> address.getType().equals(addressType))
                .findFirst();
    }

    private String convertEntityToContract(Address address) {
        final String ADDRESS_SEPARATOR = "%s, ";
        final String PIN_CODE_FORMATTER = "PIN: %s";

        Formatter formatter = new Formatter();
        if (isNotBlank(address.getHouseNoBldgApt()))
            formatter.format(ADDRESS_SEPARATOR, trim(address.getHouseNoBldgApt()));
        if (isNotBlank(address.getAreaLocalitySector()))
            formatter.format(ADDRESS_SEPARATOR, trim(address.getAreaLocalitySector()));
        if (isNotBlank(address.getStreetRoadLine()))
            formatter.format(ADDRESS_SEPARATOR, trim(address.getStreetRoadLine()));
        if (isNotBlank(address.getLandmark()))
            formatter.format(ADDRESS_SEPARATOR, trim(address.getLandmark()));
        if (isNotBlank(address.getCityTownVillage()))
            formatter.format(ADDRESS_SEPARATOR, trim(address.getCityTownVillage()));
        if (isNotBlank(address.getPostOffice()))
            formatter.format(ADDRESS_SEPARATOR, trim(address.getPostOffice()));
        if (isNotBlank(address.getSubDistrict()))
            formatter.format(ADDRESS_SEPARATOR, trim(address.getSubDistrict()));
        if (isNotBlank(address.getDistrict()))
            formatter.format(ADDRESS_SEPARATOR, trim(address.getDistrict()));
        if (isNotBlank(address.getState()))
            formatter.format(ADDRESS_SEPARATOR, trim(address.getState()));
        if (isNotBlank(address.getCountry()))
            formatter.format(ADDRESS_SEPARATOR, trim(address.getCountry()));
        if (isNotBlank(address.getPinCode()))
            formatter.format(PIN_CODE_FORMATTER, trim(address.getPinCode()));

        return formatter.toString();
    }

    private Set<org.egov.user.web.contract.Role> convertEntityToContract(Set<Role> roleSet) {
        return roleSet.stream().map(
                roleEntity -> {
                    org.egov.user.web.contract.Role roleContract = org.egov.user.web.contract.Role.builder()
                            .id(roleEntity.getId())
                            .name(roleEntity.getName())
                            .description(roleEntity.getDescription())
                            .build();
                    roleContract.setCreatedBy(roleEntity.getCreatedBy().getId());
                    roleContract.setCreatedDate(roleEntity.getCreatedDate());
                    roleContract.setLastModifiedBy(roleEntity.getLastModifiedBy().getId());
                    roleContract.setLastModifiedDate(roleEntity.getLastModifiedDate());
                    return roleContract;
                }
        ).collect(Collectors.toSet());
    }
}
