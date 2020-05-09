package com.runeterra.world.entity.impl.npc.combat;

import com.runeterra.world.entity.impl.npc.NPC;

public interface SpecialMob {

	public int getAttackAnimation();

	public int getDefenceAnimation();

	public int getDeathAnimation();

	public int getMaxHit();

	public MobCombatHandler executeCombat(NPC mob);
}