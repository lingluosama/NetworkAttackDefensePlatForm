package com.lingluo.attackdefendplatform.model.bo;


import com.lingluo.attackdefendplatform.model.entity.AttackDefenseTargetTeam;
import com.lingluo.attackdefendplatform.model.entity.AttackDefenseTeam;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TeamScoreBO implements Comparable<TeamScoreBO> {
    AttackDefenseTeam team;

    Integer submit;

    Double score;

    @Override
    public int compareTo(TeamScoreBO other) {
        // 优先比较得分，使用Double.compare进行安全比较
        int scoreComparison = other.score.compareTo(this.score);
        if (scoreComparison != 0) {
            return scoreComparison;
        }

        // 如果得分相同，则比较提交数
        return other.submit.compareTo(this.submit);
    }


}
