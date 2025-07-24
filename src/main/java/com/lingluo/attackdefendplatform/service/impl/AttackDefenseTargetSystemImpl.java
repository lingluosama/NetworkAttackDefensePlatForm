package com.lingluo.attackdefendplatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingluo.attackdefendplatform.mapper.AttackDefenseTargetSystemMapper;
import com.lingluo.attackdefendplatform.model.dto.TargetSystemResponseDTO;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseRecord;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseTargetSystem;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseTeam;
import com.lingluo.attackdefendplatform.model.query.TargetSystemQuery;
import com.lingluo.attackdefendplatform.service.AttackDefenseRecordService;
import com.lingluo.attackdefendplatform.service.AttackDefenseTargetSystemService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttackDefenseTargetSystemImpl extends ServiceImpl<AttackDefenseTargetSystemMapper, AttackDefenseTargetSystem> implements AttackDefenseTargetSystemService {

    private final AttackDefenseRecordService recordService;
    @Override
    public TargetSystemResponseDTO querySystem(TargetSystemQuery query) {
        QueryWrapper<AttackDefenseTargetSystem> queryWrapper = new QueryWrapper<>();
        
        Optional.ofNullable(query.getIp()).ifPresent(ip->queryWrapper.eq("ip",ip));
        Optional.ofNullable(query.getStatus()).ifPresent(status->queryWrapper.eq("status",status));
        
        
        //同时匹配名称和部门
        queryWrapper.and(q->{
            q.like("name",query.getKeyword())
                    .or()
                    .like("department",query.getKeyword());
        });

        int pageSize = (query.getLimit() != null && query.getLimit() > 0) ? query.getLimit() : 10;
        int currentPage = query.getOffset() + 1;
        Page<AttackDefenseTargetSystem> page = new Page<>(currentPage, pageSize);

        long count = this.count(queryWrapper);
        Page<AttackDefenseTargetSystem> targetSystemPage = this.page(page, queryWrapper);
        
        TargetSystemResponseDTO responseDTO = new TargetSystemResponseDTO();
        responseDTO.setMount((int) count);
        responseDTO.setList(targetSystemPage.getRecords());


        return responseDTO;
    }

    @Override
    public Boolean deleteSystem(Integer id) {
        LambdaUpdateWrapper<AttackDefenseRecord> updateWrapper = new LambdaUpdateWrapper<>();
        
        //删除记录中对此系统的引用
        updateWrapper.set(AttackDefenseRecord::getSid,null)
                .eq(AttackDefenseRecord::getId,id);
        recordService.update(updateWrapper);
        
        return this.removeById(id);
    }
}
