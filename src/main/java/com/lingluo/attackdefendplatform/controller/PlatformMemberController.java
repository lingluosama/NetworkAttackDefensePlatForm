package com.lingluo.attackdefendplatform.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.lingluo.attackdefendplatform.common.result.Result;
import com.lingluo.attackdefendplatform.exception.BusinessException;
import com.lingluo.attackdefendplatform.model.dto.AuthorizedDTO;
import com.lingluo.attackdefendplatform.model.dto.MemberBelongInfoDTO;
import com.lingluo.attackdefendplatform.model.dto.MemberListInfoDTO;
import com.lingluo.attackdefendplatform.model.form.AttackTeamMemberForm;
import com.lingluo.attackdefendplatform.model.query.SystemMemberQuery;
import com.lingluo.attackdefendplatform.service.AttackDefenseMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name="用户功能接口")
@RestController
@RequestMapping("/api/v1/defense/member")
@RequiredArgsConstructor
public class PlatformMemberController {
    private final AttackDefenseMemberService memberService;
    
    @Operation(summary = "注册接口")
    @PostMapping("/register")
    public Result<AuthorizedDTO> memberRegister(
            @Valid AttackTeamMemberForm form
    ){
        try {
            AuthorizedDTO token = memberService.register(form);
            return Result.success(token);
            
        }catch (BusinessException e){
            return Result.failed(e.getMessage());
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Operation(summary = "登录接口")
    @PostMapping("/login")
    public Result<AuthorizedDTO> memberLogin(
         String certificate,
         String password,
         String type
    ){
        try {
            AuthorizedDTO authorizedDTO = memberService.login(certificate, password, type);
            return Result.success(authorizedDTO);
        }catch (BusinessException e){
            return Result.failed(e.getMessage());
        }catch (Exception e){
            throw e;
        }
        
    }
    
    @Operation(summary ="平台用户非敏感信息接口" )
    @GetMapping("/info")
    public Result<MemberBelongInfoDTO> getMemberInfo(
            Integer id
    ){
        MemberBelongInfoDTO memberSimpleInfoById = memberService.getMemberSimpleInfoById(id);
        return Result.success(memberSimpleInfoById);
    }
    
    @SaCheckRole
    @Operation(summary = "用户列表")
    @GetMapping("/query")
    public Result<MemberListInfoDTO> queryMemberInfo(
            @Valid SystemMemberQuery query
    ){  
        try {
            MemberListInfoDTO dto = memberService.querySystemMember(
                    query.getOffset(),
                    query.getLimit(),
                    query.getKeyword(),
                    query.getDepartment(),
                    query.getOffice(),
                    query.getRole(),
                    query.getState()
            );
            return Result.success(dto);
        }catch (BusinessException e){
            return Result.failed(e.getMessage());
        }catch (Exception e){
            throw e;
        }
    }
    
    @SaCheckLogin
    @Operation(summary = "更新用户信息")
    @PutMapping("/update")
    public Result<Void> updateMember(
            @Valid AttackTeamMemberForm form
    ){
        if(Integer.parseInt(StpUtil.getLoginId().toString())!=form.getId()&&!StpUtil.getRoleList().contains("admin")){
            return Result.failed("权限不足");
        }
            
        try {
            Boolean b = memberService.updateMemberInfo(form);
            if(b)return Result.success();
            else return Result.failed("数据更新失败，未能同步到行,请重试");
        }catch (BusinessException e){
            return Result.failed(e.getMessage());
        }catch (Exception e){
            throw e;
        }
    }
    
    @SaCheckLogin
    @Operation(summary = "上传承诺书")
    @PutMapping("/accreditation")
    public Result<Void> updateAccreditation(
            MultipartFile file
    ){
        int uid = Integer.parseInt(StpUtil.getLoginId().toString());
        
        try {
            Boolean b = memberService.updateAccreditation(uid, file);
            if(b)return Result.success();
            else return Result.failed();
        }catch (BusinessException e){
            return Result.failed(e.getMessage());
        }catch (Exception e){
            throw e;
        }
    }
    
    @SaCheckRole("umpire")
    @Operation(summary = "审核用户承诺书")
    @PostMapping("/accreditation/trial")
    public Result<Void> trialAccreditation(
            Integer mid,
            Boolean pass,
            Integer expiration,
            String comment
    ){
        try {
            Boolean b = memberService.trialAccreditation(mid, expiration, pass,comment);
            if(b)return Result.success();
            else return Result.failed();
        }catch (BusinessException e){
            return Result.failed(e.getMessage());
        }catch (Exception e){
            throw e;
        }
    }
    
    
}
