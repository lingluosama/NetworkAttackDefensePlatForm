package com.lingluo.attackdefendplatform.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.lingluo.attackdefendplatform.common.result.Result;
import com.lingluo.attackdefendplatform.converter.TimeConverter;
import com.lingluo.attackdefendplatform.exception.BusinessException;
import com.lingluo.attackdefendplatform.exception.SystemErrorType;
import com.lingluo.attackdefendplatform.model.dto.*;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseMember;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseRecord;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseTemplates;
import com.lingluo.attackdefendplatform.model.form.AttackRecordForm;
import com.lingluo.attackdefendplatform.model.form.AttackTeamForm;
import com.lingluo.attackdefendplatform.model.form.AttackTemplateForm;
import com.lingluo.attackdefendplatform.model.query.AttackRecordQuery;
import com.lingluo.attackdefendplatform.service.*;
import com.lingluo.attackdefendplatform.service.impl.oss.MinioOssService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

import static com.lingluo.attackdefendplatform.converter.TimeConverter.parseLocalDateTime;

@Tag(name="网络攻防流程接口")
@RestController
@RequestMapping("/api/v1/defense")
@RequiredArgsConstructor
public class AttackDefenseController {
    
    private final AttackDefenseRecordService recordService;
    private final AttackDefenseTemplatesService templatesService;
    private final AttackDefenseTargetSystemService targetSystemService;
    private final AttackDefenseAuditService auditService;
    private final MinioOssService minioOssService;
    private final AttackDefenseMemberService memberService;
    
    // 时间格式化器

    //-----------创建接口
    @SaCheckRole("attacker")
    @Operation(description = "创建记录")
    @PostMapping("/record/create")
    public Result<Void> createAttackRecord(
            @Valid AttackRecordForm form
    ){
        
        if(!memberService.inAccreditationDuration()){
            return Result.failed("未进行认证或认证已过期");
        }
        
        AttackDefenseRecord defenseRecord = new AttackDefenseRecord();
        MultipartFile file = form.getFile();
        if(file!=null&&!file.isEmpty()){
            try {
                FileInfo fileInfo = minioOssService.uploadFile(file);
                defenseRecord.setFile(fileInfo.getUrl());
                defenseRecord.setFileName(fileInfo.getName());
            }catch (Exception e){
                return  Result.failed("文件未成功上传:"+e.getMessage());
            }
        }

        defenseRecord.setAttackTeam(form.getAttack_team());
        defenseRecord.setDefendTeam(form.getDefend_team());
        defenseRecord.setSummary(form.getSummary());
        
        defenseRecord.setState(1);
        defenseRecord.setTitle(form.getTitle());
        defenseRecord.setTemplate(form.getTemplate());
        defenseRecord.setSid(form.getSid());
        defenseRecord.setUmpire(form.getUmpire());
        defenseRecord.setCommitTime(LocalDateTime.now());
        
        
        
        boolean saved = recordService.save(defenseRecord);
        if(!saved)return Result.failed("保存失败，请检查字段是否合规");
        return Result.success();
    }

    @SaCheckRole("umpire")
    @Operation(summary = "新建攻防模板")
    @PostMapping(value = "/template/create")
    public Result<Void> createAttackTemplate(
            @Valid AttackTemplateForm form
    ) {
        AttackDefenseTemplates template = new AttackDefenseTemplates();

        template.setTitle(form.getTitle());
        AttackDefenseTemplates one = templatesService.lambdaQuery()
                .select(AttackDefenseTemplates::getId)
                .eq(AttackDefenseTemplates::getTitle, template.getTitle())
                .one();
        if(one!=null){
            return Result.failed("已存在相同标题的模板");
        }

        template.setType(form.getType());
        template.setDescription(form.getDescription());

        //保证状态非空
        template.setInUse(Optional.ofNullable(form.getIn_use()).orElse(false));

        if (form.getContent() != null && !form.getContent().isEmpty()) {
            try {
                FileInfo fileInfo = minioOssService.uploadFile(form.getContent());
                template.setContent(fileInfo.getUrl());
            } catch (Exception e) {
                return Result.failed("模板文件上传失败：" + e.getMessage());
            }
        } 

        template.setCreateTime(LocalDateTime.now());
        template.setUpdateTime(LocalDateTime.now()); 
        template.setInUse(form.getIn_use());
        template.setAttack(form.getAttack());
        template.setUseNum(0); // 新建模板使用次数为0

        boolean saved = templatesService.save(template);

        if (!saved) {
            return Result.failed("未能成功保存模板，请检查字段");
        }

        return Result.success();
    }

    @SaCheckRole("umpire")
    @Operation(summary = "新建攻防队伍")
    @PostMapping("/team/create")
    public Result<Void> createAttackTeam(   
            @Valid AttackTeamForm form 
    ) {
        Boolean created = recordService.createTeam(
                form.getAttack(),
                form.getCn_name(),
                form.getEn_name(), 
                form.getLeader(),
                form.getState()
        );

        if (created == null || !created) {
            return Result.failed("创建失败，请检查表单字段");
        }

        return Result.success();
    }

    @SaCheckRole("leader")
    @Operation(summary = "添加攻防队伍成员到队伍中")
    @PostMapping(value = "/team/add", consumes = "multipart/form-data")
    public Result<Void> createAttackTeamMember(
       Integer mid,
       Integer tid
    ) {
        
        try {
            Boolean b = recordService.addMemberToTeam(mid, tid);
            if(b)return Result.success();
            else return Result.failed("插入失败，请重试");
        }catch(BusinessException e){
            return Result.failed(e.getMessage());
        }catch (Exception e){
            throw  e;
        }
    }
    
    @SaCheckRole("leader")
    @Operation(summary = "踢出队伍")
    @DeleteMapping("/team/kick")
    public Result<Void> deleteAttackMember(
            Integer mid,
            Integer tid
    ){
        try {
            Boolean b = recordService.kickMemberFromTeam(mid, tid);
            if(b)return Result.success();
            else return Result.failed("删除失败，请重试");
        }catch(BusinessException e){
            return Result.failed(e.getMessage());
        }catch (Exception e){
            throw  e;   
        }
    }
    
    
    //--------更新接口
    @SaCheckRole("attacker")
    @Operation(summary = "更新攻防审批记录(进行审批)")
    @PutMapping(value = "/record/update", consumes = "multipart/form-data") // 声明接收 multipart/form-data
    public Result<Void> updateAttackRecord(
            @Valid AttackRecordForm form
    ) {

        try {
            Boolean b = recordService.updateRecord(form);
            if(b)return Result.success();
            return  Result.failed("更新失败");
        }catch (BusinessException e){
            return Result.failed(e.getMessage());
        }catch (Exception e){
            throw  e;
        }
    }

    @SaCheckRole("umpire")
    @Operation(summary = "更新攻防模板信息")
    @PutMapping(value = "/template/update", consumes = "multipart/form-data") // 声明接收 multipart/form-data
    public Result<Void> updateAttackTemplate(
            @Valid AttackTemplateForm form
    ) {
        if (form.getId() == null) {
            throw new BusinessException("更新攻防模板失败：模板ID不能为空。");
        }

        AttackDefenseTemplates template = new AttackDefenseTemplates();
        template.setId(form.getId());
        
        //删除旧文件
        AttackDefenseTemplates oldTemplate = templatesService.getById(form.getId());
        if(oldTemplate!=null&&oldTemplate.getContent()!=null){
            minioOssService.deleteFile(oldTemplate.getContent());
        }
        
        // 文件上传
        MultipartFile contentFile = form.getContent();
        if (contentFile != null && !contentFile.isEmpty()) {
            try {
                FileInfo fileInfo = minioOssService.uploadFile(contentFile);
                template.setContent(fileInfo.getUrl()); // 更新文件 URL
            } catch (Exception e) {
                throw new BusinessException("模板文件上传失败：" + e.getMessage(), e);
            }
        }

        // 只设置需要更新的字段
        Optional.ofNullable(form.getTitle()).ifPresent(template::setTitle);
        Optional.ofNullable(form.getType()).ifPresent(template::setType); // 确保 AttackTemplateForm 中有 type 字段
        Optional.ofNullable(form.getDescription()).ifPresent(template::setDescription);
        Optional.ofNullable(form.getAttack()).ifPresent(template::setAttack);
        // 处理 in_use 字段：如果前端提供了，就更新；如果没提供，保持不变
        Optional.ofNullable(form.getIn_use()).ifPresent(template::setInUse);

        template.setUpdateTime(LocalDateTime.now());


        boolean updated = templatesService.updateById(template);

        if (!updated) {
            throw new BusinessException("更新攻防模板失败，模板ID (ID: " + form.getId() + ") 可能不存在或数据未能成功保存。");
        }

        return Result.success(); 
    }
    
    //-------------------------------------------------------------查询接口
    @SaCheckLogin
    @Operation(description = "分页搜索攻防记录")
    @GetMapping("/record/query")
    public Result<AttackRecordPageDTO> searchAttackRecords(
            @Valid AttackRecordQuery query
    ) throws Throwable{
        try {
            LocalDateTime begin_time = parseLocalDateTime(query.getBegin_time());
            LocalDateTime end_time = parseLocalDateTime(query.getEnd_time());
            String loginId = StpUtil.getLoginId().toString();
            AttackRecordPageDTO attackRecordPageDTO = recordService.queryRecord(
                    Integer.valueOf(loginId),
                    query.getOffset(),
                    query.getLimit(),
                    query.getState(),
                    begin_time,
                    end_time,
                    query.getTeam_name(),
                    query.getDesc(),
                    query.getTitle(),
                    query.getTemplate()
            );
            return Result.success(attackRecordPageDTO);
        }catch (DateTimeParseException e){
            throw new BusinessException("时间转换出现错误:"+e.getMessage());
        }
        
    }
    
    @SaCheckLogin
    @Operation(description = "搜索攻防模板")
    @GetMapping("/template/query")
    public Result<AttackTemplatePageDTO> queryTemplate(
            Integer offset,
            Integer limit,
            String type,
            String title,
            Boolean attack
    ){
        try {
            AttackTemplatePageDTO attackTemplatePageDTO = templatesService.queryTemplate(offset, limit, type, title,attack);
            return Result.success(attackTemplatePageDTO);
        }catch (Throwable e){
            throw new BusinessException("查询模板出现错误",e);
        }
    }
    
    
    @SaCheckLogin
    @Operation(description = "搜索队伍")
    @GetMapping("/team/query")
    public Result<AttackTeamPageDTO> queryTeam(
            Integer limit,
            Integer offset,
            Integer type,
            Boolean attack,
            String keyword
    ){
        try {
            AttackTeamPageDTO attackTeamPageDTO = recordService.queryTeamPage(offset, limit, keyword, attack, type);
            return Result.success(attackTeamPageDTO);
        }catch (Exception e){
            throw e;
        }
        
    }
    
    @SaCheckLogin
    @Operation(description = "更新队伍")
    @PutMapping("/team/update")
    public Result<Void> updateTeam(
            Integer id,
            String cn_name,
            String en_name,
            Integer leader,
            Integer state
    ){
        try {
            Boolean updated = recordService.updateTeamInfo(id,cn_name, en_name, leader, state);
            if(updated)return Result.success();
            return Result.failed();
        }catch (BusinessException e){
            return Result.failed(e.getMessage());
        }catch (Exception e){
            throw  e;
        }

    }
    
    
    
    @SaCheckLogin
    @Operation(description = "通过id进行统一查询")
    @GetMapping("/query")
    public Result<Object> queryById(Integer id,String target) {

        //什么都不传就返回自己的信息
        if (id == null || target == null || target.isEmpty()) {
            String loginId = (String) StpUtil.getLoginId();
            MemberAllBelongInfoDTO self = recordService.getMemberById(Integer.valueOf(loginId));
            return Result.success(self);
        }
        try {   
            Object resultObject = null; // 定义一个通用对象来接收查询结果
            switch (target) {
                case "record" -> {
                    resultObject = recordService.getDetailById(id);
                }
                case "team" -> {
                    resultObject = recordService.getTeamById(id);
                }
                case "member" -> {
                    StpUtil.checkRole("admin");//这是用户的全部信息，包含密码等敏感信息，只有管理员允许查看
                    resultObject = recordService.getMemberById(id);
                }
                case "template" -> {
                    resultObject = templatesService.getById(id);
                }
                case "targetSystem" -> {
                    resultObject = targetSystemService.getDetailById(id);
                }
            }
            if (resultObject == null) {
                return Result.failed("未找到ID为 " + id + " 的 " + target + " 对象。");
            }
            return Result.success(resultObject);
        }catch (Exception e){
            throw new BusinessException("通过id进行攻防模块统一查询失败,对象:"+target,e);
        }
    }
    
    
    @SaCheckLogin
    @SaCheckRole("admin")
    @Operation(description = "统一删除接口")
    @DeleteMapping("/delete")
    public Result<Void> delete(Integer id,String target) {
        if (id == null || target == null || target.isEmpty()) {
            return Result.failed("请求不合法，请明确删除目标");
        }
        try {
            Boolean removed=null;
            switch (target) {
                case "record" -> {
                    removed = recordService.removeById(id);
                }
                case "team" ->{
                    removed = recordService.deleteTeam(id);
                }
                case "member" ->{
                    removed = recordService.deleteTeamMember(id);
                }
                case "template" ->{
                    removed = templatesService.deleteTemplate(id);
                }
                case "targetSystem" ->{
                    removed = targetSystemService.deleteSystem(id);
                }
                case "audit"->{
                    removed = auditService.removeById(id);
                }
            }
            if (removed == null|| !removed) {
                return Result.failed("未成功删除为 " + id + " 的 " + target + " 对象。");
            }
            return Result.success();
        }catch (Exception e){
            throw new BusinessException("通过id进行攻防模块统一查询删除失败,目标表:"+target+e.getMessage());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

    }


}
