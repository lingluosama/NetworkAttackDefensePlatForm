package com.lingluo.attackdefendplatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.lingluo.attackdefendplatform.mapper.AttackDefenseTemplatesMapper;
import com.lingluo.attackdefendplatform.model.dto.AttackTemplatePageDTO;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseTemplates;
import com.lingluo.attackdefendplatform.service.AttackDefenseTemplatesService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttackDefenseTemplatesServiceImpl extends ServiceImpl<AttackDefenseTemplatesMapper, AttackDefenseTemplates> implements AttackDefenseTemplatesService {


    @Override
    public AttackTemplatePageDTO queryTemplate(Integer offset, Integer limit, String type, String title) throws Throwable {
        
        
        QueryWrapper<AttackDefenseTemplates> queryWrapper=new QueryWrapper<>();
        
        if(!type.isEmpty()){
            queryWrapper.eq("type",type);
        }
        if(!title.isEmpty()){
            queryWrapper.like("title",title);
        }
        //分页查询
        int pageSize = (limit != null && limit > 0) ? limit : 10;
        int currentPage = offset+1;
        Page<AttackDefenseTemplates> page = new Page<>(currentPage, pageSize);
        Page<AttackDefenseTemplates> templatePage = this.page(page, queryWrapper);
        
        //转列表获取总条目组装DTO
        List<AttackDefenseTemplates> attackDefenseTemplates = templatePage.getRecords().stream().toList();
        long count = this.count(queryWrapper);

        return new AttackTemplatePageDTO((int) count,attackDefenseTemplates);
        
    }
}