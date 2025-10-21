package org.daengsik.mythicaddons;

import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.SkillExecutor;
import io.lumine.mythic.api.skills.conditions.ConditionFactory;
import io.lumine.mythic.bukkit.MythicBukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.daengsik.mythicaddons.conditions.IAHoldingCondition;

/**
 * IAHolding - MythicMobs 애드온 플러그인
 * 
 * 이 플러그인은 MythicMobs의 기존 holding 조건이 ItemsAdder의 커스텀 아이템을 
 * 인식하지 못하는 문제를 해결하기 위해 개발되었습니다.
 * 
 * 기능:
 * - iaholding{i=custom:myitem} 형태의 새로운 조건 제공
 * - ItemsAdder API를 사용하여 커스텀 아이템의 네임스페이스 ID 매칭
 * - 기존 holding 조건과 병렬로 동작 (교체하지 않음)
 * 
 * 사용법:
 * Conditions:
 * - iaholding{i=iasurvival:rice}
 * - iaholding{i=custom:myitem}
 * 
 * @author daengsik
 * @version 1.0-SNAPSHOT
 * @since MythicMobs 5.9.5, ItemsAdder 4.0.14
 */
public final class IAHolding extends JavaPlugin {

    /**
     * 플러그인 활성화 시 실행되는 메서드
     * 
     * 실행 순서:
     * 1. MythicMobs 플러그인 존재 여부 확인 (필수 의존성)
     * 2. ItemsAdder 플러그인 존재 여부 확인 (선택적 의존성)
     * 3. 커스텀 조건 등록
     * 4. 활성화 완료 로그 출력
     */
    @Override
    public void onEnable() {
        // MythicMobs 플러그인 로드 확인 (필수 의존성)
        // MythicMobs가 없으면 이 플러그인은 작동할 수 없으므로 즉시 비활성화
        if (getServer().getPluginManager().getPlugin("MythicMobs") == null) {
            getLogger().severe("MythicMobs를 찾을 수 없습니다! 이 플러그인은 MythicMobs가 필요합니다.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        // ItemsAdder 플러그인 로드 확인 (선택적 의존성)
        // ItemsAdder가 없어도 플러그인은 활성화되지만, iaholding 조건은 항상 false 반환
        if (getServer().getPluginManager().getPlugin("ItemsAdder") == null) {
            getLogger().warning("ItemsAdder를 찾을 수 없습니다! IAHolding 조건은 항상 false를 반환합니다.");
        }
        
        // MythicMobs에 커스텀 조건 등록
        registerConditions();
        
        getLogger().info("IAHolding 플러그인이 성공적으로 활성화되었습니다!");
    }

    /**
     * 플러그인 비활성화 시 실행되는 메서드
     * 
     * 현재는 특별한 정리 작업이 필요하지 않으므로 단순히 로그만 출력
     */
    @Override
    public void onDisable() {
        getLogger().info("IAHolding 플러그인이 비활성화되었습니다.");
    }
    
    /**
     * MythicMobs에 커스텀 조건을 등록하는 메서드
     * 
     * 등록되는 조건:
     * - iaholding: ItemsAdder 아이템을 손에 들고 있는지 확인하는 조건
     * 
     * 등록 과정:
     * 1. MythicBukkit 인스턴스에서 SkillExecutor 가져오기
     * 2. ConditionFactory를 사용하여 "iaholding" 이름으로 조건 등록
     * 3. IAHoldingCondition 클래스와 연결
     * 4. 등록 완료 로그 출력
     */
    private void registerConditions() {
        // MythicMobs의 스킬 시스템에 접근
        SkillExecutor skills = MythicBukkit.inst().getSkills();
        
        // iaholding 조건을 MythicMobs에 등록
        // ConditionFactory는 MythicMobs가 조건을 인스턴스화할 때 사용하는 팩토리 클래스
        skills.registerCondition(new ConditionFactory("iaholding", 
            (MythicLineConfig config) -> new IAHoldingCondition(config)));
        
        getLogger().info("ItemsAdder 아이템용 iaholding 조건이 등록되었습니다.");
    }
}
