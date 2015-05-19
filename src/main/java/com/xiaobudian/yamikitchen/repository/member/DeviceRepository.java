package com.xiaobudian.yamikitchen.repository.member;

import com.xiaobudian.yamikitchen.domain.member.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Johnson on 2015/5/13.
 */
public interface DeviceRepository extends JpaRepository<Device, Long> {
    public Device findByUuid(String uuid);

    public List<Device> findByUid(Long uid);

    public Device findByDeviceToken(String deviceToken);

    List<Device> findByUidIn(List<Long> uidList);

    List<Device> findByType(int type);
}