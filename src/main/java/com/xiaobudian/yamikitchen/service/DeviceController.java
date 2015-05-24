package com.xiaobudian.yamikitchen.service;

import com.xiaobudian.yamikitchen.common.Result;
import com.xiaobudian.yamikitchen.domain.member.Device;
import com.xiaobudian.yamikitchen.domain.member.IdfaRecord;
import com.xiaobudian.yamikitchen.domain.member.User;
import com.xiaobudian.yamikitchen.repository.member.DeviceRepository;
import com.xiaobudian.yamikitchen.repository.member.IdfaRecordRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;

/**
 * Created by Johnson on 2015/5/13.
 */
@RestController
public class DeviceController {
    private static final String IDFA_HASH_PATTERN = "{0}?key={1}";
    public static final String MAGIC_KEY = "obfuscate";
    @Inject
    private DeviceRepository deviceRepository;
    @Inject
    private IdfaRecordRepository idfaRecordRepository;
    @Inject
    private MemberService memberService;
    @Inject
    private MessageSource messageSource;

    @RequestMapping(value = "/devices", method = RequestMethod.POST)
    @ResponseBody
    public Result addDevice(@RequestBody Device newDevice) {
        Device device = null;
        if (StringUtils.isNotEmpty(newDevice.getUuid())) device = deviceRepository.findByUuid(newDevice.getUuid());
        if (device == null && StringUtils.isNotEmpty(newDevice.getDeviceToken()))
            deviceRepository.findByDeviceToken(newDevice.getDeviceToken());
        if (device != null) {
            device.setUid(newDevice.getUid());
            return Result.successResult(deviceRepository.save(device));
        }
        return Result.successResult(deviceRepository.save(newDevice));
    }

    @RequestMapping(value = "/deviceToken", method = RequestMethod.POST)
    @ResponseBody
    public Result updateDeviceToken(@RequestBody Device newDevice, @AuthenticationPrincipal User user) {
        Device device = deviceRepository.findByUuid(newDevice.getUuid());
        if (device == null) throw new RuntimeException("device.doesnot.exists.error");
        device.setDeviceToken(newDevice.getDeviceToken());
        device.setChannel(newDevice.getChannel());
        if (user != null) device.setUid(user.getId());
        return Result.successResult(deviceRepository.save(device));
    }

    @RequestMapping(value = "/idfa/{idfa}", method = RequestMethod.POST)
    @ResponseBody
    public Result submitIdfaRecord(@PathVariable String idfa, @RequestParam(value = "key") String key, HttpServletRequest request) {
        String signatureString = MessageFormat.format(IDFA_HASH_PATTERN, request.getRequestURL(), MAGIC_KEY);
        if (!StringUtils.equals(DigestUtils.md5Hex(signatureString), key))
            throw new RuntimeException("idfa.invalid.error");
        if (idfaRecordRepository.findByIdfa(idfa) != null)
            return Result.successResult(messageSource.getMessage("idfa.exists.error", null, LocaleContextHolder.getLocale()));
        idfaRecordRepository.save(new IdfaRecord(idfa));
        return Result.successResultWithoutData();
    }
}
