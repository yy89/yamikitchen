package com.xiaobudian.yamikitchen.repository.account;

import com.xiaobudian.yamikitchen.domain.member.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Johnson on 2015/5/17.
 */
public interface BankRepository extends JpaRepository<Bank, Long> {
    public Bank findByBankNameAndBinCode(String bankName,String binCode);
    public Bank findByBankName(String bankName);
    public Bank findByBinCode(String binCode);

}
