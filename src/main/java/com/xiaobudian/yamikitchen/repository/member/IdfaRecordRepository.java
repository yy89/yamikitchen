package com.xiaobudian.yamikitchen.repository.member;

import com.xiaobudian.yamikitchen.domain.member.IdfaRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Johnson on 2015/5/13.
 */
public interface IdfaRecordRepository extends JpaRepository<IdfaRecord, Long> {
    public IdfaRecord findByIdfa(String idfa);
}