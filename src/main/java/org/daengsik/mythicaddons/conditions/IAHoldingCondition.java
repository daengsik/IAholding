package org.daengsik.mythicaddons.conditions;

import dev.lone.itemsadder.api.CustomStack;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.conditions.IEntityCondition;
import io.lumine.mythic.core.skills.SkillCondition;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * IAHoldingCondition - ItemsAdder 아이템을 손에 들고 있는지 확인하는 MythicMobs 커스텀 조건
 
 * 이 클래스는 MythicMobs의 SkillCondition을 상속하고 IEntityCondition 인터페이스를 구현하여
 * 플레이어가 특정 ItemsAdder 커스텀 아이템을 메인 핸드에 들고 있는지 확인합니다.

 * - ItemsAdder API 사용
 * - 네임스페이스 ID 기반 정확한 매칭 (대소문자 무시)
 * - 플레이어만 대상으로 제한
 * - 메인 핸드 아이템만 체크

 * 
 * @author daengsik
 * @version 1.0-SNAPSHOT
 * @since MythicMobs 5.9.5, ItemsAdder 4.0.14
 */
public class IAHoldingCondition extends SkillCondition implements IEntityCondition {
    
    /**
     * 확인할 ItemsAdder 아이템의 네임스페이스 ID
     * 예: "custom:myitem", "iasurvival:rice"
     */
    private final String requiredIaId;
    
    /**
     * IAHoldingCondition 생성자
     * 
     * MythicMobs 5.9.5의 표준 생성자 시그니처: (String conditionName, MythicLineConfig config)
     * 
     * @param line 조건 이름 (예: "iaholding")
     * @param config MythicMobs에서 전달하는 설정 객체
     *               지원하는 파라미터: "i", "id", "item"
     *               예: iaholding{i=custom:myitem}
     */
    public IAHoldingCondition(String line, MythicLineConfig config) {
        super(line);
        
        // 설정에서 아이템 ID 파싱: iaholding{i=custom:myitem}
        // 여러 파라미터명을 지원하여 사용자 편의성 향상
        this.requiredIaId = config.getString(new String[]{"i", "id", "item"}, "").trim();
    }
    
    /**
     * 조건 확인 메서드
     * 
     * 플레이어가 지정된 ItemsAdder 아이템을 메인 핸드에 들고 있는지 확인합니다.
     * 
     * 확인 과정:
     * 1. 설정된 아이템 ID가 있는지 확인
     * 2. 엔티티가 플레이어인지 확인
     * 3. 플레이어가 메인 핸드에 아이템을 들고 있는지 확인
     * 4. 해당 아이템이 ItemsAdder 커스텀 아이템인지 확인
     * 5. 아이템의 네임스페이스 ID가 일치하는지 확인
     * 
     * @param entity 확인할 엔티티 (플레이어만 처리됨)
     * @return true: 조건 만족 (플레이어가 지정된 IA 아이템을 들고 있음)
     *         false: 조건 불만족 (플레이어가 아니거나, 아이템이 없거나, ID가 다름)
     */
    @Override
    public boolean check(AbstractEntity entity) {
        // 설정된 아이템 ID가 없으면 조건 실패
        if (requiredIaId.isEmpty()) {
            return false;
        }
        
        // 플레이어만 처리 (다른 엔티티는 아이템을 들 수 없음)
        if (!entity.isPlayer()) {
            return false;
        }
        
        // 플레이어 객체 가져오기
        Player player = (Player) entity.getBukkitEntity();
        
        // 메인 핸드의 아이템 가져오기
        ItemStack item = player.getInventory().getItemInMainHand();
        
        // 플레이어가 아이템을 들고 있는지 확인 (공기 블록이 아닌지)
        if (item == null || item.getType().isAir()) {
            return false;
        }
        
        // ItemsAdder API를 사용하여 커스텀 아이템인지 확인
        CustomStack customStack = CustomStack.byItemStack(item);
        if (customStack == null) {
            return false; // ItemsAdder 아이템이 아님
        }
        
        // ItemsAdder 아이템의 네임스페이스 ID 가져오기 (예: "custom:myitem")
        String heldId = customStack.getNamespacedID();
        
        // 대소문자 무시하고 ID 비교
        return requiredIaId.equalsIgnoreCase(heldId);
    }
}
