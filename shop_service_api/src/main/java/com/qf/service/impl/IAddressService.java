package com.qf.service.impl;

import com.qf.entity.Address;

import java.util.List;

public interface IAddressService extends IBaseService<Address> {
    List<Address> getAddressListByUid(Integer uid);
    int addAddress(Address address);
}
