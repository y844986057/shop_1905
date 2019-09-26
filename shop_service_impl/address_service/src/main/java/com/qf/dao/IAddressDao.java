package com.qf.dao;

import com.qf.entity.Address;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface IAddressDao extends Mapper<Address> {
    List<Address> getAddressListByUid(Integer uid);
    int addAddress(Address address);
}
