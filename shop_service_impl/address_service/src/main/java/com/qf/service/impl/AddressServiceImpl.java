package com.qf.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.dao.IAddressDao;
import com.qf.entity.Address;
import com.qf.service.impl.impl.BaseServiecImpl;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Service
public class AddressServiceImpl extends BaseServiecImpl<Address> implements IAddressService {

    @Autowired
    private IAddressDao addressDao;

    @Override
    public List<Address> getAddressListByUid(Integer uid) {
        return addressDao.getAddressListByUid(uid);
    }

    @Override
    public int addAddress(Address address) {
        return addressDao.addAddress(address);
    }

    @Override
    protected Mapper<Address> getMapper() {
        return addressDao;
    }
}
