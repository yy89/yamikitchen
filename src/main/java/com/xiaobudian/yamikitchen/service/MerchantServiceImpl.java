package com.xiaobudian.yamikitchen.service;

import com.xiaobudian.yamikitchen.domain.Merchant;
import com.xiaobudian.yamikitchen.repository.MerchantRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * Created by hackcoder on 2015/4/24.
 */
@Service("merchantService")
public class MerchantServiceImpl implements MerchantService {
    @Inject
    private MerchantRepository merchantRepository;
    @Override
    public Merchant save(Merchant merchant) {
        return merchantRepository.save(merchant);
    }

    @Override
    public Merchant edit(Merchant merchant) {
        if(merchant!=null){
            Merchant merchantdb = merchantRepository.findOne(merchant.getId());
            if(merchantdb!=null){//数据库中存在修改
                return merchantRepository.save(merchant);
            }
        }
        return null;
    }

    @Override
    public void rest(long id ) {

    }

    @Override
    public void reopen(long id) {

    }
}
