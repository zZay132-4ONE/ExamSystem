package com.github.tangyi.user.controller;

import com.github.tangyi.common.basic.vo.UserVo;
import com.github.tangyi.common.core.exceptions.ServiceException;
import com.github.tangyi.common.core.model.ResponseBean;
import com.github.tangyi.common.core.utils.ResponseUtil;
import com.github.tangyi.common.core.web.BaseController;
import com.github.tangyi.common.security.utils.SysUtil;
import com.github.tangyi.exam.api.dto.ExaminationDashboardDto;
import com.github.tangyi.exam.api.feign.ExaminationServiceClient;
import com.github.tangyi.user.api.dto.DashboardDto;
import com.github.tangyi.user.service.TenantService;
import com.github.tangyi.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台首页数据展示
 *
 * @author zdz
 * @date 2022/04/16 13:57
 */
@AllArgsConstructor
@Api("后台首页数据展示")
@RestController
@RequestMapping("/v1/dashboard")
public class DashboardController extends BaseController {

    /**
     * 考试服务客户端
     */
    private final ExaminationServiceClient examinationService;

    /**
     * 用户service
     */
    private final UserService userService;

    /**
     * 租户service
     */
    private final TenantService tenantService;

    /**
     * 获取管控台首页数据
     *
     * @return ResponseBean
     */
    @GetMapping
    @ApiOperation(value = "后台首页数据展示", notes = "后台首页数据展示")
    public ResponseBean<DashboardDto> dashboard() {
        String tenantCode = SysUtil.getTenantCode();
        DashboardDto dashboardDto = new DashboardDto();
        UserVo userVo = new UserVo();
        userVo.setTenantCode(tenantCode);
        // 用户数量
        dashboardDto.setOnlineUserNumber(userService.userCount(userVo).toString());
        // 租户数量
		dashboardDto.setTenantCount(tenantService.tenantCount().toString());
        // 考试数量
        ResponseBean<ExaminationDashboardDto> dashboardData = examinationService.findExaminationDashboardData(tenantCode);
        if (!ResponseUtil.isSuccess(dashboardData)) {
            throw new ServiceException("Get examination dashboard data failed: " + dashboardData.getMsg());
        }
        ExaminationDashboardDto examinationDashboardDto = dashboardData.getData();
        if (examinationDashboardDto != null) {
            // 设置主页上显示的当前考试数目
            if (examinationDashboardDto.getExaminationCount() != null) {
                dashboardDto.setExaminationNumber(examinationDashboardDto.getExaminationCount().toString());
            }
            // 设置主页上显示的当前考试人数
            if (examinationDashboardDto.getExamUserCount() != null) {
             dashboardDto.setExamUserNumber(examinationDashboardDto.getExamUserCount().toString());
            }
            // 设置主页上显示的考试记录数目
            if (examinationDashboardDto.getExaminationRecordCount() != null) {
                dashboardDto.setExaminationRecordNumber(examinationDashboardDto.getExaminationRecordCount().toString());
            }
        }
        return new ResponseBean<>(dashboardDto);
    }

    /**
     * 过去一周考试记录数
     *
     * @return ResponseBean
     */
    @GetMapping("examRecordTendency")
	@ApiOperation(value = "过去一周考试记录数", notes = "过去一周考试记录数")
	public ResponseBean<DashboardDto> examRecordTendency(@RequestParam Integer pastDays) {
		DashboardDto dashboardDto = new DashboardDto();
		ResponseBean<ExaminationDashboardDto> examRecordTendencyData =
                examinationService.findExamRecordTendencyData(SysUtil.getTenantCode(), pastDays);
		if (!ResponseUtil.isSuccess(examRecordTendencyData)) {
			throw new ServiceException("Get examination record tendency data failed: " + examRecordTendencyData.getMsg());
        }
		dashboardDto.setExamRecordDate(examRecordTendencyData.getData().getExamRecordDate());
		dashboardDto.setExamRecordData(examRecordTendencyData.getData().getExamRecordData());
		return new ResponseBean<>(dashboardDto);
	}

}
